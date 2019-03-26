package de.tuberlin.dima.datawriter;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tuberlin.dima.common.config.EdgeConfig;
import de.tuberlin.dima.common.config.ExperimentConfig;
import de.tuberlin.dima.common.config.SensorConfig;
import de.tuberlin.dima.common.i2c.I2CBus;
import java.io.File;
import java.io.IOException;

public class DataWriter {

    public static void main(String[] args) throws InterruptedException, IOException {

        //Parse command line arguments
        CommandLineParameter clp = new CommandLineParameter();
        new JCommander(clp, args);

        //Read the config file
        ObjectMapper mapper = new ObjectMapper();

        String experimentFolder = "experiments/" + clp.experiment;
        String dataFolder = experimentFolder + "/data";
        String configFile = experimentFolder + "/experiment.config";

        ExperimentConfig config = mapper.readValue(new File(configFile), ExperimentConfig.class);
        config.setDataFolder(dataFolder);
        config.setExperimentFolder(experimentFolder);

        I2CBus i2cBus = new I2CBus(3);

        long startTime = System.currentTimeMillis();
        EdgeConfig edgeConfig = config.getEdge(clp.edge);
        for (SensorConfig sensorConfig : edgeConfig.getSensors()) {
            String dataFile = dataFolder + "/" + sensorConfig.getName() + ".data";
            Sensor sensor = new Sensor(i2cBus, sensorConfig.getAddress(), sensorConfig.getType(), startTime, dataFile);
            new Thread(sensor).start();
        }

        //i2cBus.set(0x29, 0x00, new byte[]{0x00, 0x01, 0x02, 0x03});
        //System.out.println(i2cBus.get(0x29, 0x00, DataType.INTEGER)[3]);
    }
}
