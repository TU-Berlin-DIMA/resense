package de.tuberlin.dima.datareader;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinPullResistance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.tuberlin.dima.common.config.DataType;
import de.tuberlin.dima.common.config.SensorConfig;

public class UltrasonicSensor implements SensorReadCapabilities, SensorRecordCapabilities {

    private static GpioPinDigitalOutput sensorTriggerPin;
    private static GpioPinDigitalInput sensorEchoPin;
    private static final GpioController gpio = GpioFactory.getInstance();
    private double currentDistance = -1.0;
    private DataType dataType = DataType.DOUBLE;
    private SensorConfig ownConfig;
    private String dataDirString;
    private FileWriter fw;
    private BufferedWriter bw;
    private AtomicInteger counter;

    /**
     * The HCSR04 Ultrasonic sensor is connected on the physical pin 16 and 18 which
     * correspond to the GPIO 04 and 05 of the WiringPi library.
     */
    public UltrasonicSensor(SensorConfig config, String dataDirString) {
        this.counter = new AtomicInteger(0);
        this.ownConfig = config;
        this.dataDirString = dataDirString;
        this.instantiateDirAndDataFile();
        // Trigger pin as OUTPUT
        sensorTriggerPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
        // Echo pin as INPUT
        sensorEchoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
    }

    public Double get() {
        try {
            // System.out.print("Distance in centimeters: ");
            currentDistance = getDistance();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return currentDistance;
    }

    @Override
    public Object[] read() throws InterruptedException {
        Object[] results = new Object[1];
        results[0] = this.getDistance();
        return results;
    }

    @Override
    public Object[] record() throws IOException, InterruptedException {
        Object[] results = read();
        this.writeToFile(results);
        return results;
    }

    private void writeToFile(Object[] results) throws IOException {
        this.bw.write(this.counter.getAndIncrement() + "," + Double.toString((double)results[0]));
    }

    private void instantiateDirAndDataFile()
    {
        Path dirPath = Paths.get(this.dataDirString);
        if (Files.notExists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException ioe) {
                Logger.getLogger(UltrasonicSensor.class.getName()).log(Level.SEVERE, null, ioe);
            }
        }

        Path filePath = Paths.get(dirPath.toString() + File.separator + this.ownConfig.getName() + ".data");
        if (Files.notExists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException ioe) {
                Logger.getLogger(UltrasonicSensor.class.getName()).log(Level.SEVERE, null, ioe);
            }
        }

        try {
            this.fw = new FileWriter(filePath.toString(),true);
            this.bw = new BufferedWriter(this.fw);
        } catch (IOException ioe) {
            Logger.getLogger(UltrasonicSensor.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    /**
     * Retrieve the distance measured by the HCSR04 Ultrasonic sensor connected on a
     * Raspberry Pi 3+B
     *
     * @return the distance in centimeters
     * @throws InterruptedException
     */
    private double getDistance() throws InterruptedException {

        double distanceCM = -1;
        try {
            // Thread.sleep(2000);
            sensorTriggerPin.high(); // Make trigger pin HIGH
            Thread.sleep((long) 0.01);// Delay for 10 microseconds
            sensorTriggerPin.low(); // Make trigger pin LOW

            // Wait until the ECHO pin gets HIGH
            while (sensorEchoPin.isLow()) {

            }
            // Store the current time to calculate ECHO pin HIGH time.
            long startTime = System.nanoTime();
            // Wait until the ECHO pin gets LOW
            while (sensorEchoPin.isHigh()) {

            }
            // Store the echo pin HIGH end time to calculate ECHO pin HIGH time.
            long endTime = System.nanoTime();

            distanceCM = ((((endTime - startTime) / 1e3) / 2) / 29.1);
            // Printing out the distance in centimeters
            // System.out.println("Distance: " + distanceCM + " centimeters");

            return distanceCM;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return distanceCM;
    }
}
