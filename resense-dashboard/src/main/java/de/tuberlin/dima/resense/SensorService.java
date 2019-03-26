package de.tuberlin.dima.resense;

public interface SensorService {
    public abstract void replay(String sensorId, String experimentId);
    public abstract void stopReplay(String sensorId, String experimentId);
    public abstract void record(String sensorId, String experimentId);
    public abstract void stopRecording(String sensorId, String experimentId);
}
