package de.tuberlin.dima.resense;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@Controller
public class ExperimentController {

    @Autowired
    ExperimentService experimentService;

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/experiment")
    @ResponseBody
    public Experiment getExperiment(@RequestParam(name = "name", required = false, defaultValue = "new-exp") String name,
                                    @RequestParam(name = "datapath", required = true) String dataPath) {
        return new Experiment(counter.incrementAndGet(), name, dataPath);
    }

    // TODO: add id to URI
    @GetMapping("/experiment/start")
    @ResponseStatus(value = HttpStatus.OK)
    public void startExperiment(@RequestParam(name = "name", required = false, defaultValue = "new-exp") String name) {
        experimentService.startExperiment(name);
    }

    @GetMapping("/experiment/stop")
    @ResponseStatus(value = HttpStatus.OK)
    public void stopExperiment(@RequestParam(name = "name", required = false, defaultValue = "new-exp") String name) {
        experimentService.stopExperiment(name);
    }

    @GetMapping("/experiment/deploy")
    @ResponseStatus(value = HttpStatus.OK)
    public void deployExperiment(@RequestParam(name = "name", required = false, defaultValue = "new-exp") String name) {
        experimentService.deployExperiment(name);
    }

    @GetMapping(value = "/experiment/configuration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getConfiguration(@RequestParam(name = "name", required = true, defaultValue = "new-exp") String name) {
        try {
            // TODO: handle JSON conversion
            return experimentService.getConfiguration(name);
        } catch (IOException ioe) {
            // TODO: handle exception
            return "IO Exception";
        }
    }
}
