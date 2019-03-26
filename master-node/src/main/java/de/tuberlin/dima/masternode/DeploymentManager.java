package de.tuberlin.dima.masternode;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import de.tuberlin.dima.common.config.SensorType;
import de.tuberlin.dima.masternode.config.CommandLineParameter;
import de.tuberlin.dima.common.config.EdgeConfig;
import de.tuberlin.dima.common.config.ExperimentConfig;
import de.tuberlin.dima.common.config.SensorConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeploymentManager {
    
    private final ExperimentConfig config;

    public DeploymentManager(ExperimentConfig config) {
        this.config = config;
    }
    
    private void deploy() {

        System.out.println("\n==================================================================");
        System.out.println("STARTING DEPLOYMENT");
        System.out.println("==================================================================");

        // deploy each edge
        for (EdgeConfig edgeConfig : this.config.getEdges()) {
            System.out.println("\nConnect to edge: " + edgeConfig.getHost() + "\n");
            
            try {
                SessionManager sessionManager = new SessionManager(edgeConfig.getUser(), edgeConfig.getHost(), 22, edgeConfig.getPassword());

                sessionManager.sftpMkdir("sensable");

                int commandResult = sessionManager.execCommand("test -e sensable/i2c-stub");
                if (commandResult != 0) {
                    sessionManager.sftpMkdir("sensable/i2c-stub");
                }

                commandResult = sessionManager.execCommand("test -e sensable/i2c-stub/i2c-stub.c");
                if (commandResult != 0) {
                    System.out.print("Copying i2c-stub source and Makefile...");
                    String i2c_stub_c = "i2c-stub/i2c-stub.c";
                    sessionManager.sftpFile("sensable/i2c-stub", new File(i2c_stub_c));
                    String i2c_makefile = "i2c-stub/Makefile";
                    sessionManager.sftpFile("sensable/i2c-stub", new File(i2c_makefile));
                    System.out.print(" Completed!\n\n");
                }

                commandResult = sessionManager.execCommand("test -e sensable/i2c-stub/i2c-stub.ko");
                if (commandResult != 0) {
                    System.out.print("Building i2c-stub from source...");
                    sessionManager.execCommand("sudo apt-get -qq update && sudo apt-get -qq upgrade -y && sudo apt-get -qq autoclean && sudo apt-get -qq autoremove -y");
                    sessionManager.execCommand("sudo apt-get -qq install raspberrypi-kernel-headers -y");
                    sessionManager.execCommand("cd sensable/i2c-stub && make > /dev/null 2>&1");
                    sessionManager.execCommand("cd sensable/i2c-stub && sudo make install > /dev/null 2>&1");
                    System.out.print(" Completed!\n\n");
                }

                System.out.print("Copying data-writer.jar: sensible/data-writer.jar...");
                String dataWriter = "data-writer/target/data-writer-1.0-jar-with-dependencies.jar";
                sessionManager.sftpFile("sensable/", new File(dataWriter), "data-writer.jar");
                System.out.print(" Completed!\n\n");

                System.out.print("Copying data-reader.jar: sensible/data-reader.jar...");
                String dataReader = "data-reader/target/data-reader-1.0-jar-with-dependencies.jar";
                sessionManager.sftpFile("sensable/", new File(dataReader), "data-reader.jar");
                System.out.print(" Completed!\n\n");

                sessionManager.sftpMkdir("sensable/experiments");
                sessionManager.sftpMkdir("sensable/" + this.config.getExperimentFolder());

                System.out.print("Copying config file: sensible/"+ this.config.getExperimentFolder() + "/experiment.config...");
                String configFile = this.config.getExperimentFolder() + "/experiment.config";
                sessionManager.sftpFile("sensable/" + this.config.getExperimentFolder(), new File(configFile));

                sessionManager.sftpMkdir("sensable/" + this.config.getDataFolder());
                System.out.print(" Completed!\n\n");

                for (SensorConfig sensorConfig : edgeConfig.getSensors()) {
                    if (sensorConfig.getSensorType().equals(SensorType.SIMULATED)) {
                        String dataFile = config.getDataFolder() + "/" + sensorConfig.getName() + ".data";
                        System.out.print("Copying data file: sensable/" + dataFile + "...");
                        if (Files.exists(Paths.get(dataFile))) {
                            sessionManager.sftpFile("sensable/" + this.config.getDataFolder(), new File(dataFile));
                            System.out.print(" Completed!\n");
                        }
                        else {
                            throw new RuntimeException("File does not exist!");
                        }
                    }
                }
            }
            catch (JSchException | SftpException | IOException ex) {
                Logger.getLogger(DeploymentManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }

            System.out.println("\n------------------------------------------------------------------");
        }
        System.out.println("\n==================================================================");
        System.out.println("DEPLOYMENT COMPLETED");
        System.out.println("==================================================================\n");

    }

    private void start(List<EdgeConfig> edgesToStart) {
        System.out.println("\n==================================================================");
        System.out.println("STARTING DATA WRITER & DATA READER");
        System.out.println("==================================================================");

        for (EdgeConfig edgeConfig : edgesToStart) {
            System.out.println("\nConnect to edge: " + edgeConfig.getHost() + "\n");

            String addresses = "";
            for (SensorConfig sensorConfig : edgeConfig.getSensors()) {
                if (sensorConfig.getSensorType().equals(SensorType.SIMULATED)) {
                    addresses += sensorConfig.getAddress() + ",";
                }
            }
            addresses = addresses.substring(0, addresses.length() - 1);

            try {
                SessionManager sessionManager = new SessionManager(edgeConfig.getUser(), edgeConfig.getHost(), 22, edgeConfig.getPassword());

                int commandResult = sessionManager.execCommand("sudo modprobe -n --first-time i2c_stub");
                if (commandResult != 0) {
                    System.out.print("Removing i2c-stub kernel module...");
                    sessionManager.execCommand("sudo rmmod i2c_stub");
                    System.out.print(" Completed!\n\n");
                }

                System.out.println("Adding i2c-stub.ko chip_addr=" + addresses);
                sessionManager.execCommand("cd sensable/i2c-stub; sudo insmod i2c-stub.ko chip_addr=" + addresses);
                System.out.print(" Completed!\n\n");

                System.out.print("Executing data writer for experiment: " + this.config.getName() + " on edge: " + edgeConfig.getName() + "... ");
                sessionManager.execCommand("cd sensable; nohup sudo java -jar data-writer.jar -experiment " + this.config.getName() + " -edge " + edgeConfig.getName() + " > /dev/null &");

                System.out.print("Executing data reader for experiment: " + this.config.getName() + " on edge: " + edgeConfig.getName() + "... ");
                sessionManager.execCommand("cd sensable; nohup sudo java -jar data-reader.jar -experiment " + this.config.getName() + " -edge " + edgeConfig.getName() + " > /dev/null &");
            }
            catch (JSchException | IOException ex) {
                Logger.getLogger(DeploymentManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
            System.out.println("\n------------------------------------------------------------------");
        }

        System.out.println("\n==================================================================");
        System.out.println("FINISHED DATA WRITER & DATA READER");
        System.out.println("==================================================================\n");
    }

    private void stop(List<EdgeConfig> edgesToStop) {
        System.out.println("\n==================================================================");
        System.out.println("STOPPING DATA WRITER & DATA READER");
        System.out.println("==================================================================\n");

        for (EdgeConfig edgeConfig : edgesToStop) {
            System.out.println("\nConnect to edge: " + edgeConfig.getHost() + "\n");

            try {
                SessionManager sessionManager = new SessionManager(edgeConfig.getUser(), edgeConfig.getHost(), 22, edgeConfig.getPassword());

                System.out.println("Killing all data-writer processes... ");
                sessionManager.execCommand("for KILLPID in `ps ax | grep 'data-writer' |grep -v 'grep' | awk ' { print $1;}'`; do sudo kill $KILLPID; done");

                System.out.println("Killing all data-reader processes... ");
                sessionManager.execCommand("for KILLPID in `ps ax | grep 'data-reader' |grep -v 'grep' | awk ' { print $1;}'`; do sudo kill $KILLPID; done");

                System.out.println("Removing i2c-stub module... ");
                sessionManager.execCommand("cd sensable/i2c-stub; sudo rmmod i2c_stub");
            }
            catch (JSchException | IOException ex) {
                Logger.getLogger(DeploymentManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
            System.out.println("\n------------------------------------------------------------------");
        }

        System.out.println("\n==================================================================");
        System.out.println("DATA WRITER & DATA READER STOPPED COMPLETED");
        System.out.println("==================================================================\n");
    }

    private void record(List<EdgeConfig> edgesToRecord)
    {
        System.out.println("\n==================================================================");
        System.out.println("STARTING RECORDING LIVE DATA");
        System.out.println("==================================================================");

        for (EdgeConfig edgeConfig : edgesToRecord) {
            System.out.println("\nConnect to edge: " + edgeConfig.getHost() + "\n");

            try {
                SessionManager sessionManager = new SessionManager(edgeConfig.getUser(), edgeConfig.getHost(), 22, edgeConfig.getPassword());
                System.out.print("Recording data for experiment " + this.config.getName() + " on edge: " + edgeConfig.getName() + "...");
                sessionManager.execCommand("cd sensable; nohup sudo java -cp data-reader.jar de.tuberlin.resense.DataRecorder -experiment " + this.config.getName() + " -edge " + edgeConfig.getName() + " > /dev/null &");
            } catch (JSchException | IOException ex) {
                Logger.getLogger(DeploymentManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }

            System.out.println("\n------------------------------------------------------------------");
        }

        System.out.println("\n==================================================================");
        System.out.println("STARTED RECORDING LIVE DATA");
        System.out.println("==================================================================\n");
    }

    private void stopRecording(List<EdgeConfig> edgesToStop) {
        System.out.println("\n==================================================================");
        System.out.println("STOPPING RECORDING LIVE DATA");
        System.out.println("==================================================================\n");

        for (EdgeConfig edgeConfig : edgesToStop) {
            System.out.println("\nConnect to edge: " + edgeConfig.getHost() + "\n");

            try {
                SessionManager sessionManager = new SessionManager(edgeConfig.getUser(), edgeConfig.getHost(), 22, edgeConfig.getPassword());

                System.out.println("Killing all data-reader processes... ");
                sessionManager.execCommand("for KILLPID in `ps ax | grep 'data-reader' |grep -v 'grep' | awk ' { print $1;}'`; do sudo kill $KILLPID; done");
            }
            catch (JSchException | IOException ex) {
                Logger.getLogger(DeploymentManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
            System.out.println("\n------------------------------------------------------------------");
        }

        System.out.println("\n==================================================================");
        System.out.println("STOPPED RECORDING LIVE DATA");
        System.out.println("==================================================================\n");
    }

    private void startEdgesForSensors(List<String> startSensorIds) {
        List<EdgeConfig> edgeConfigs = new ArrayList<EdgeConfig>();
        for (String sensorId : startSensorIds) {
            EdgeConfig edgeConfig = this.config.getEdgeFromSensor(sensorId);
            if (edgeConfig != null) {
                edgeConfigs.add(edgeConfig);
            }
        }
        this.start(edgeConfigs);
    }

    private void stopEdgesForSensors(List<String> stopSensorIds) {
        List<EdgeConfig> edgeConfigs = new ArrayList<EdgeConfig>();
        for (String sensorId : stopSensorIds) {
            EdgeConfig edgeConfig = this.config.getEdgeFromSensor(sensorId);
            if (edgeConfig != null) {
                edgeConfigs.add(edgeConfig);
            }
        }
        this.stop(edgeConfigs);
    }

    private void recordEdgesForSensors(List<String> recordSensorIds) {
        List<EdgeConfig> edgeConfigs = new ArrayList<EdgeConfig>();
        for (String sensorId : recordSensorIds) {
            EdgeConfig edgeConfig = this.config.getEdgeFromSensor(sensorId);
            if (edgeConfig != null) {
                edgeConfigs.add(edgeConfig);
            }
        }

        this.record(edgeConfigs);
    }

    private void stopRecordingEdgesForSensors(List<String> recordSensorIds) {
        List<EdgeConfig> edgeConfigs = new ArrayList<EdgeConfig>();
        for (String sensorId : recordSensorIds) {
            EdgeConfig edgeConfig = this.config.getEdgeFromSensor(sensorId);
            if (edgeConfig != null) {
                edgeConfigs.add(edgeConfig);
            }
        }

        this.stopRecording(edgeConfigs);
    }

    public static void main(String[] args) throws IOException {
        //Parse command line arguments
        CommandLineParameter clp = new CommandLineParameter();
        new JCommander(clp, args);

        //Read the config file
        ObjectMapper mapper = new ObjectMapper();
        
        String experimentFolder = "experiments/" + clp.experiment;
        String dataFolder = experimentFolder + "/data";
        String configFile =  experimentFolder + "/experiment.config";
        
        ExperimentConfig config = mapper.readValue(new File(configFile), ExperimentConfig.class);
        config.setDataFolder(dataFolder);
        config.setExperimentFolder(experimentFolder);
        
        DeploymentManager deploymentManager = new DeploymentManager(config);
        if (clp.deploy) {
            deploymentManager.deploy();
        }
        if (clp.start) {
            deploymentManager.start(config.getEdges());
        }
        if (clp.stop) {
            deploymentManager.stop(config.getEdges());
        }
        if (!clp.startSensors.isEmpty()) {
            deploymentManager.startEdgesForSensors(clp.startSensors);
        }
        if (!clp.stopSensors.isEmpty()) {
            deploymentManager.stopEdgesForSensors(clp.stopSensors);
        }
        if (!clp.recordSensors.isEmpty()) {
            deploymentManager.stopEdgesForSensors(clp.recordSensors);
            deploymentManager.recordEdgesForSensors(clp.recordSensors);
        }
        if (!clp.stopRecordingSensors.isEmpty()) {
            deploymentManager.stopRecordingEdgesForSensors(clp.recordSensors);
        }
        System.exit(1);
    }
    
}
