package de.tuberlin.dima.datawriter;

import com.beust.jcommander.Parameter;

public class CommandLineParameter {
    
    @Parameter(names = {"-experiment", "-e"}, required = true)
    public String experiment = "";

    @Parameter(names = {"-edge"}, required = true)
    public String edge = "";
    
}
