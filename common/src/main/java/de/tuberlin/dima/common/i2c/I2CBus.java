package de.tuberlin.dima.common.i2c;

import de.tuberlin.dima.common.config.DataType;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class I2CBus {

    private final int busNumber;

    public I2CBus(int address) {
        this.busNumber = address;
    }

    public void set(int address, int register, int[] data) throws IOException, InterruptedException {
        String dataString = joinArray(data);
        String command = "i2cset -f -y " + busNumber + " " + address + " " + register + " " + dataString + "i";
        //System.out.println(command);
        Process p = Runtime.getRuntime().exec(command);
        //p.waitFor();
    }

    public int[] get(int address, int register, DataType type) throws IOException, InterruptedException {

        int length = type.getLength();
        int[] results = new int[length];
        for (int i = 0; i < length; i++) {
            String command = "i2cget -f -y " + busNumber + " " + address + " " + register;
            System.out.println(command);
            Process p = Runtime.getRuntime().exec(command);
            //p.waitFor();
            Scanner scan = new Scanner(new InputStreamReader(p.getInputStream()));
            if (scan.hasNext()) {
                results[i] = hexStringToInt(scan.nextLine());
            }
            register++;
        }
        return results;
    }

    private String joinArray(int[] data) {
        String dataString = "";
        for (int b : data) {
            dataString += b + " ";
        }
        return dataString;
    }

    private int hexStringToInt(String s) {
        s = s.replace("0x", "");
        int result = (int) ((Character.digit(s.charAt(0), 16) << 4)
                + Character.digit(s.charAt(1), 16));
        return result;
    }
}
