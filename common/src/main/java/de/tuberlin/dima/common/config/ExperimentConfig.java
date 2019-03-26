package de.tuberlin.dima.common.config;

import java.util.ArrayList;
import java.util.List;

public class ExperimentConfig {

    private String name;
    private List<EdgeConfig> edges;
    private String experimentFolder;
    private String dataFolder;

    public ExperimentConfig() {
    }

    public String getName() {
        return name;
    }

    public List<EdgeConfig> getEdges() {
        return edges;
    }

    public EdgeConfig getEdge(String name) {
        for (EdgeConfig edgeConfig : edges) {
            if (edgeConfig.getName().equals(name)) {
                return edgeConfig;
            }
        }
        return null;
    }

    public EdgeConfig getEdgeFromSensor(String sensorName) {
        for (EdgeConfig edgeConfig : edges) {
            for (SensorConfig sensorConfig : edgeConfig.getSensors()) {
                if (sensorConfig.getName().equals(sensorName)) {
                    return edgeConfig;
                }
            }
        }
        return null;
    }

    public List<SensorConfig> getSensors()
    {
        List<SensorConfig> sensors = new ArrayList<>();
        for (EdgeConfig edgeConfig : edges) {
            sensors.addAll(edgeConfig.getSensors());
        }

        return sensors;
    }

    public List<EdgeConfig> getEdgesWithRealSensors()
    {
        List<EdgeConfig> realEdges = new ArrayList<>();
        for (EdgeConfig edgeConfig : edges) {
            if (edgeConfig.getSensorsByType(SensorType.REAL).size() > 0) {
                realEdges.add(edgeConfig);
            }
        }

        return realEdges;
    }

    public String getExperimentFolder() {
        return experimentFolder;
    }

    public void setExperimentFolder(String experimentFolder) {
        this.experimentFolder = experimentFolder;
    }

    public String getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(String dataFolder) {
        this.dataFolder = dataFolder;
    }
    
    

}
