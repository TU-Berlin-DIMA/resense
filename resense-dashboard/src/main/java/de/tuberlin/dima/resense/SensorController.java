package de.tuberlin.dima.resense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class SensorController {

    @Autowired
    SensorService sensorService;

    @GetMapping("/sensor/start/{action}")
    @ResponseStatus(value = HttpStatus.OK)
    public void start(@RequestParam(name = "name", required = true)
                              String sensorName,
                      @RequestParam(name = "experiment", required = true)
                              String experimentId,
                      @PathVariable String action) {
        switch (action) {
            case "record":
                sensorService.record(sensorName, experimentId);
                break;
            case "replay":
                sensorService.replay(sensorName, experimentId);
                break;
            default:
                // TODO: invalid verb handling
                break;
        }
    }

    @GetMapping("/sensor/stop/{action}")
    @ResponseStatus(value = HttpStatus.OK)
    public void stop(@RequestParam(name = "name", required = true)
                             String sensorName,
                     @RequestParam(name = "experiment", required = true)
                             String experimentId,
                     @PathVariable String action) {
        switch (action) {
            case "record":
                sensorService.stopRecording(sensorName, experimentId);
                break;
            case "replay":
                sensorService.stopReplay(sensorName, experimentId);
                break;
            default:
                // TODO: invalid verb handling
                break;
        }
    }
}
