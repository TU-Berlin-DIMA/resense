package de.tuberlin.dima.datareader;

import java.io.IOException;

public interface SensorReadCapabilities {
    abstract Object[] read() throws IOException, InterruptedException;
}
