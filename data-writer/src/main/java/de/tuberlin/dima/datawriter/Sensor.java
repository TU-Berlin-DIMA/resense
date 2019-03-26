package de.tuberlin.dima.datawriter;

import de.tuberlin.dima.common.config.DataType;
import de.tuberlin.dima.common.i2c.I2CBus;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sensor implements Runnable {

    private final I2CBus i2cBus;
    private final int address;
    private final DataType[] dataTypes;
    private int[] registers;
    private long startTime;
    private final String dataFile;

    private BufferedReader reader;
    private String currentLine;

    public Sensor(I2CBus i2cBus, int address, DataType[] dataTypes, long startTime, String dataFile, int[] registers) {
        this.i2cBus = i2cBus;
        this.address = address;
        this.dataTypes = dataTypes;
        this.registers = registers;
        this.startTime = startTime;
        this.dataFile = dataFile;

        
    }

    public Sensor(I2CBus i2cBus, int address, DataType[] dataTypes, long startTime, String dataFile) {
        this(i2cBus, address, dataTypes, startTime, dataFile, null);

        this.registers = new int[dataTypes.length];
        int register = 0;
        for (int i = 0; i < dataTypes.length; i++) {
            this.registers[i] = register;
            register += dataTypes[i].getLength();
        }
    }

    @Override
    public void run() {
        try {
            
            reader = new BufferedReader(new FileReader(dataFile));

            while (true) {
                moveToCurrentLine();
                String[] values = currentLine.split(",");

                for (int i = 0; i < dataTypes.length; i++) {

                    // +1 because we have the timestamp at position 0
                    int[] data = dataTypes[i].transform(values[i + 1]);
                    i2cBus.set(address, registers[i], data);
                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void moveToCurrentLine() throws IOException {
        long readTime = System.currentTimeMillis();

        // get the current line or read it
        if (currentLine == null) {
            currentLine = reader.readLine();
        }

        // check weather the current line is still valid or if we have to read the next line(s)
        while (true) {

            long offset = readTime - this.startTime;
            long timestamp = Long.parseLong(currentLine.split(",")[0]);
            if (timestamp >= offset) {
                break;
            }
            currentLine = reader.readLine();
        }
        
        // we reached the end of the file, restart
        if(currentLine == null){
            startTime = System.currentTimeMillis();
            reader.close();
            reader = new BufferedReader(new FileReader(dataFile));
            currentLine = reader.readLine();
        }
    }

}
