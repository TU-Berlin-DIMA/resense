package de.tuberlin.dima.datareader;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tuberlin.dima.common.config.EdgeConfig;
import de.tuberlin.dima.common.config.ExperimentConfig;
import de.tuberlin.dima.common.config.SensorConfig;
import de.tuberlin.dima.common.config.SensorType;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class DataRecorder {

    private static DataRecorder instance;
    private final Map<String, UltrasonicSensor> realSensors = new HashMap<>();

    public UltrasonicSensor getSensor(String name)
    {
        return this.realSensors.get(name);
    }

    public static DataRecorder getInstance()
    {
        if (DataRecorder.instance == null) {
            DataRecorder.instance = new DataRecorder();
        }

        return DataRecorder.instance;
    }

    public static void main(String[] args) throws IOException {
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

        EdgeConfig edgeConfig = config.getEdge(clp.edge);
        DataRecorder dataRecorder = DataRecorder.getInstance();

        for (SensorConfig sensorConfig : edgeConfig.getSensorsByType(SensorType.REAL)) {
            UltrasonicSensor sensor = new UltrasonicSensor(sensorConfig, dataFolder);
            dataRecorder.realSensors.put(sensorConfig.getName(), sensor);
        }

        WebSocketServer server = new WebSocketServer();
        server.startRecordingServer();
    }
}
