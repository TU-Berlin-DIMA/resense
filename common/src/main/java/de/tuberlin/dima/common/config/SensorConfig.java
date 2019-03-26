package de.tuberlin.dima.common.config;

public class SensorConfig {
    
    private String name;
    private DataType[] type;
    private int address;
    private SensorType sensorType = SensorType.SIMULATED;
    private Integer[] registers;
    
    public SensorConfig() { }

    public String getName() {
        return name;
    }

    public DataType[] getType() {
        return type;
    }

    public int getAddress() {
        return address;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public Integer[] getRegisters() {
        return registers;
    }

    public String getTypeDescription()
    {
        String typeName = type[0].getDescription();
        return typeName.substring(0, 1).toUpperCase() + typeName.substring(1).toLowerCase();
    }
}
