package de.tuberlin.dima.resense;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

@Service
public class SensorServiceImpl implements SensorService {

    @Override
    public void replay(String sensorId, String experimentId) {
        this.executeCommand(sensorId, experimentId, "-startSensors");
    }

    @Override
    public void stopReplay(String sensorId, String experimentId) {
        this.executeCommand(sensorId, experimentId, "-stopSensors");
    }

    @Override
    public void record(String sensorId, String experimentId) {
        this.executeCommand(sensorId, experimentId, "-recordSensors");
    }

    @Override
    public void stopRecording(String sensorId, String experimentId) {
        this.executeCommand(sensorId, experimentId, "-stopRecordingSensors");
    }

    private void executeCommand(String sensorId, String experimentId, String actionType) {
        List<String> commands = Arrays.asList(
                "java",
                "-jar",
                "master-node/target/master-node-1.0-jar-with-dependencies.jar",
                "-experiment",
                experimentId,
                actionType,
                sensorId
        );
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        try {
            Process process = processBuilder.start();
        } catch (IOException ioe) {
            // TODO: handle exceptions better
        }
    }
}
