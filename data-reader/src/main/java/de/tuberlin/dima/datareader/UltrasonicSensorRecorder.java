package de.tuberlin.dima.datareader;

import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UltrasonicSensorRecorder implements Runnable {

    private UltrasonicSensor sensor;
    private WebSocketChannel channel;

    public UltrasonicSensorRecorder(UltrasonicSensor sensor, WebSocketChannel channel)
    {
        this.sensor = sensor;
        this.channel = channel;
    }

    @Override
    public void run() {
        Object[] data;
        try {
            data = sensor.record();
            WebSockets.sendText(Arrays.toString(data), channel, null);
        } catch (Exception ex) {
            Logger.getLogger(UltrasonicSensorRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
