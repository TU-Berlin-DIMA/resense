package de.tuberlin.dima.common.config;

import java.util.ArrayList;
import java.util.List;

public class EdgeConfig {
    
    private String name;
    private String host;
    private String user;
    private String password;
    private int port;
    private List<SensorConfig> sensors;
    
    public EdgeConfig(){
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    public List<SensorConfig> getSensors() {
        return sensors;
    }

    public List<SensorConfig> getSensorsByType(SensorType type) {
        List<SensorConfig> neededSensors = new ArrayList<SensorConfig>();
        for (SensorConfig sensor : this.sensors) {
            if (sensor.getSensorType().equals(type)) {
                neededSensors.add(sensor);
            }
        }

        return neededSensors;
    }
    
    
}
