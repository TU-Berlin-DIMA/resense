package de.tuberlin.dima.datareader;

import java.io.IOException;

public interface SensorRecordCapabilities {
    abstract Object[] record() throws IOException, InterruptedException;
}
