package de.tuberlin.dima.resense;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tuberlin.dima.common.config.EdgeConfig;
import de.tuberlin.dima.common.config.ExperimentConfig;
import de.tuberlin.dima.common.config.SensorConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        ObjectMapper mapper = new ObjectMapper();
        List<ExperimentConfig> experiments = new ArrayList<ExperimentConfig>();
        List<SensorConfig> allSensors = new ArrayList<SensorConfig>();
        List<EdgeConfig> allRecordingEdges = new ArrayList<EdgeConfig>();

        List<String> existingExperiments = this.getPreconfiguredExperiments("experiments/");
        for (String experimentName : existingExperiments) {
            String experimentFolder = "experiments/" + experimentName;
            String dataFolder = experimentFolder + "/data";
            String configFile =  experimentFolder + "/experiment.config";
            try {
                ExperimentConfig config = mapper.readValue(new File(configFile), ExperimentConfig.class);
                config.setDataFolder(dataFolder);
                config.setExperimentFolder(experimentFolder);
                experiments.add(config);
                allSensors.addAll(config.getSensors());
                allRecordingEdges.addAll(config.getEdgesWithRealSensors());
            } catch (IOException ioe) {

            }
        }

        model.addAttribute("experiments", experiments);
        model.addAttribute("allSensors", allSensors);
        model.addAttribute("allRecordingEdges", allRecordingEdges);

        return "dashboard"; // view
    }

    private List<String> getPreconfiguredExperiments(String pathToExperiments)
    {
        File file = new File(pathToExperiments);
        String[] directories = file.list(new FilenameFilter() {
             @Override
             public boolean accept(File current, String name) {
                 return new File(current, name).isDirectory();
             }
        });
        return Arrays.asList(directories);
    }
}
