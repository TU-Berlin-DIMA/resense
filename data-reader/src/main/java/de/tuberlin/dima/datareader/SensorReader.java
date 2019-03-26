package de.tuberlin.dima.datareader;

import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import java.io.IOException;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorReader implements Runnable {

    private Sensor sensor;
    private WebSocketChannel channel;

    public SensorReader(Sensor sensor, WebSocketChannel channel) {
        this.sensor = sensor;
        this.channel = channel;
    }

    @Override
    public void run() {
        Object[] data;
        try {
            data = sensor.read();
            WebSockets.sendText(Arrays.toString(data), channel, null);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SensorReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
