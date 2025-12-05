package main.java.exercise;

import main.java.framework.PersistAs;
import main.java.framework.Result;

public class ResultImplementation implements Result {

    private int maxCliqueSize;
    private boolean[] chosenVertices;

    @PersistAs("size")
    private int size;

    @PersistAs("duration")
    private long duration;

    @PersistAs("reduction_time")
    private long reductionTime;

    @PersistAs("solver_calls")
    private int solverCalls;

    public ResultImplementation(int size, long duration, long reductionTime, int solverCalls, int maxCliqueSize, boolean[] chosenVertices) {
        this.size = size;
        this.duration = duration;
        this.reductionTime = reductionTime;
        this.solverCalls = solverCalls;
        this.maxCliqueSize = maxCliqueSize;
        this.chosenVertices = chosenVertices;
    }

    public int getMaxCliqueSize() { return this.maxCliqueSize; }

    public boolean[] getChosenVertices() { return this.chosenVertices; }
}
