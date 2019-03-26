package de.tuberlin.dima.resense;

import java.io.IOException;

public interface ExperimentService {
    public abstract void startExperiment(String nameId);
    public abstract void stopExperiment(String nameId);
    public abstract void deployExperiment(String nameId);
    public abstract String getConfiguration(String nameId) throws IOException;
}
