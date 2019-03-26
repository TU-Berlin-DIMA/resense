package de.tuberlin.dima.datareader;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tuberlin.dima.common.config.EdgeConfig;
import de.tuberlin.dima.common.config.ExperimentConfig;
import de.tuberlin.dima.common.config.SensorConfig;
import de.tuberlin.dima.common.config.SensorType;
import de.tuberlin.dima.common.i2c.I2CBus;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataReader {
    
    private static DataReader instance;
    private final Map<String, Sensor> sensors = new HashMap<>();
    
    public Sensor getSensor(String name){
        return this.sensors.get(name);
    }
    
    public static DataReader getInstance(){
        if (DataReader.instance == null) {
            DataReader.instance = new DataReader();
        }
        return DataReader.instance;
    }

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

        EdgeConfig edgeConfig = config.getEdge(clp.edge);
        DataReader dataReader = DataReader.getInstance();
        for (SensorConfig sensorConfig : edgeConfig.getSensors()) {
            I2CBus i2CBus;
            if (sensorConfig.getSensorType().equals(SensorType.REAL)) {
                i2CBus = new I2CBus(1);
            }
            else {
                i2CBus = new I2CBus(3);
            }
            Sensor sensor = new Sensor(i2CBus, sensorConfig.getAddress(), sensorConfig.getType(), sensorConfig.getRegisters());
            dataReader.sensors.put(sensorConfig.getName(), sensor);
        }

        WebSocketServer server = new WebSocketServer();
        server.startServer();

    }
}
