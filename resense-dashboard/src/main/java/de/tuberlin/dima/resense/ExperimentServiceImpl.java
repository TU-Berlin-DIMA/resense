package de.tuberlin.dima.resense;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;

@Service
public class ExperimentServiceImpl implements ExperimentService {

    @Override
    public void startExperiment(String nameId) {
        this.executeCommand(nameId, "-start");
    }

    @Override
    public void stopExperiment(String nameId) {
        this.executeCommand(nameId, "-stop");
    }

    @Override
    public void deployExperiment(String nameId) {
        this.executeCommand(nameId, "-deploy");
    }

    @Override
    public String getConfiguration(String nameId) throws IOException {
        String inputJson = "experiments" + File.separator +
                nameId + File.separator + "experiment.config";
        byte[] encoded = Files.readAllBytes(Paths.get(inputJson));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private void executeCommand(String nameId, String actionType) {
        List<String> commands = Arrays.asList(
                "java",
                "-jar",
                "master-node/target/master-node-1.0-jar-with-dependencies.jar",
                "-experiment",
                nameId,
                actionType
        );
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        try {
            Process process = processBuilder.start();
        } catch (IOException ioe) {
            // TODO: handle exceptions better
        }
    }
}
