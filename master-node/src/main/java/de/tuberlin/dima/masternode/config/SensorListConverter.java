package de.tuberlin.dima.masternode.config;

import java.util.ArrayList;
import java.util.List;
import com.beust.jcommander.IStringConverter;

public class SensorListConverter implements IStringConverter<List<String>> {
    @Override
    public List<String> convert(String parsedSensorString) {
        String[] sensorIds = parsedSensorString.split(",");
        List<String> idList = new ArrayList<>();
        for (String sensorId : sensorIds) {
            idList.add(sensorId);
        }
        return idList;
    }
}
