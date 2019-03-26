package de.tuberlin.dima.datareader;

import de.tuberlin.dima.common.config.DataType;
import de.tuberlin.dima.common.i2c.I2CBus;

import java.io.IOException;

public class Sensor implements SensorReadCapabilities {

    private final I2CBus i2cBus;
    private final int address;
    private final DataType[] dataTypes;
    private Integer[] registers;

    public Sensor(I2CBus i2cBus, int address, DataType[] dataTypes, Integer[] registers) {
        this.i2cBus = i2cBus;
        this.address = address;
        this.dataTypes = dataTypes;
        this.registers = registers;
        if (registers == null) {
            this.registers = new Integer[dataTypes.length];
            int register = 0;
            for (int i = 0; i < dataTypes.length; i++) {
                this.registers[i] = register;
                register += dataTypes[i].getLength();
            }
        }
    }

    @Override
    public Object[] read() throws IOException, InterruptedException {
        Object[] results = new Object[dataTypes.length];
        for (int i = 0; i < dataTypes.length; i++) {
            int[] resultArray = i2cBus.get(address, registers[i], dataTypes[i]);
            Object result = dataTypes[i].transform(resultArray);
            results[i] = result;
        }
        return results;
    }

}
