package de.tuberlin.dima.resense;

public class Experiment {

    private final long id;
    private final String name;
    private final String dataPath;

    public Experiment(long id, String name, String dataPath) {
        this.id = id;
        this.name = name;
        this.dataPath = dataPath;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDataPath() {
        return dataPath;
    }
}
