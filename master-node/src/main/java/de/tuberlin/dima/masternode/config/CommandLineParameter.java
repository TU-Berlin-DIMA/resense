package de.tuberlin.dima.masternode.config;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandLineParameter {

    @Parameter(names = {"-deploy"})
    public Boolean deploy = false;

    @Parameter(names = {"-start"})
    public Boolean start = false;

    @Parameter(names = {"-stop"})
    public Boolean stop = false;

    @Parameter(names = {"-startSensors"}, listConverter = SensorListConverter.class)
    public List<String> startSensors = new ArrayList<String>();

    @Parameter(names = {"-stopSensors"}, listConverter = SensorListConverter.class)
    public List<String> stopSensors = new ArrayList<String>();

    @Parameter(names = {"-recordingSensors"}, listConverter = SensorListConverter.class)
    public List<String> recordSensors = new ArrayList<String>();

    @Parameter(names = {"-stopRecordingSensors"}, listConverter = SensorListConverter.class)
    public List<String> stopRecordingSensors = new ArrayList<String>();

    @Parameter(names = {"-experiment", "-e"}, required = true)
    public String experiment = "";
    
}
