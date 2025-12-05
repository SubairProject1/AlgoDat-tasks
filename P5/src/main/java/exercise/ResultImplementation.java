package main.java.exercise;

import main.java.framework.PersistAs;
import main.java.framework.Result;


public class ResultImplementation implements Result {

    private int tentativePrimalBound;
    private int tentativeDualBound;

    @PersistAs("graph_name")
    private String graphName;

    @PersistAs("edge_probability")
    private int edgeProbabilityInPercent;

    @PersistAs("duration")
    private long duration;

    @PersistAs("node_expansions")
    private int nodeExpansions;

    public ResultImplementation(long duration, String graphName, int edgeProbabilityInPercent, int nodeExpansions, int tentativePrimalBound, int tentativeDualBound) {
        this.duration = duration;
        this.graphName = graphName;
        this.edgeProbabilityInPercent = edgeProbabilityInPercent;
        this.nodeExpansions = nodeExpansions;
        this.tentativePrimalBound = tentativePrimalBound;
        this.tentativeDualBound = tentativeDualBound;
    }

    public int getTentativePrimalBound() {
        return tentativePrimalBound;
    }
    public int getTentativeDualBound() {
        return tentativeDualBound;
    }
    public int getNodeExpansions() { return nodeExpansions; }
}
