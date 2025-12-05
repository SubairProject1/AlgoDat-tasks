package main.java.exercise;

public class BranchAndBoundResult {

    private int globalLowerBound;
    private int rootUpperBound;

    public BranchAndBoundResult(int globalLowerBound, int rootUpperBound) {
        this.globalLowerBound = globalLowerBound;
        this.rootUpperBound = rootUpperBound;
    }

    public int getGlobalLowerBound() { return this.globalLowerBound; }

    public int getRootUpperBound() { return this.rootUpperBound; }

}
