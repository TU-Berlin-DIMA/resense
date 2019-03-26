package de.tuberlin.dima.datareader;

import io.undertow.Undertow;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.*;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.websocket;

public class WebSocketServer {

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
    private Map<WebSocketChannel, ScheduledFuture> channels = new HashMap<>();

    public void startServer() {
        Undertow server = Undertow.builder()
            .addHttpListener(8080, "0.0.0.0")
            .setHandler(path()
                .addPrefixPath("/sensors", websocket(new WebSocketConnectionCallback() {

                    @Override
                    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {

                        channel.getReceiveSetter().set(new AbstractReceiveListener() {
                            @Override
                            protected void onClose(WebSocketChannel webSocketChannel, StreamSourceFrameChannel channel) throws IOException {
                                channels.get(webSocketChannel).cancel(true);
                            }
                        });

                        String sensorName = exchange.getRequestParameters().get("name").get(0);
                        long frequency = Long.parseLong(exchange.getRequestParameters().get("frequency").get(0));

                        Sensor sensor = DataReader.getInstance().getSensor(sensorName);
                        ScheduledFuture reader = executorService.scheduleAtFixedRate(new SensorReader(sensor, channel), 0, frequency, TimeUnit.MILLISECONDS);
                        channels.put(channel, reader);
                        channel.resumeReceives();
                    }

                }))
            ).build();
        server.start();
    }

    public void startRecordingServer() {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(path()
                        .addPrefixPath("/sensors", websocket(new WebSocketConnectionCallback() {

                            @Override
                            public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {

                                channel.getReceiveSetter().set(new AbstractReceiveListener() {
                                    @Override
                                    protected void onClose(WebSocketChannel webSocketChannel, StreamSourceFrameChannel channel) throws IOException {
                                        channels.get(webSocketChannel).cancel(true);
                                    }
                                });

                                String sensorName = exchange.getRequestParameters().get("name").get(0);
                                long frequency = Long.parseLong(exchange.getRequestParameters().get("frequency").get(0));

                                UltrasonicSensor sensor = DataRecorder.getInstance().getSensor(sensorName);
                                ScheduledFuture reader = executorService.scheduleAtFixedRate(new UltrasonicSensorRecorder(sensor, channel), 0, frequency, TimeUnit.MILLISECONDS);
                                channels.put(channel, reader);
                                channel.resumeReceives();
                            }

                        }))
                ).build();
        server.start();
    }
}
