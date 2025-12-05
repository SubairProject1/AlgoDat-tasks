package main.java.exercise;

import main.java.framework.Instance;
import org.jgrapht.graph.SimpleGraph;

public class InstanceImplementation implements Instance {

    private String groupName;
    private int number;
    private String graphName;
    private SimpleGraph<VertexImplementation,EdgeImplementation> graph;
    private SubGraph preparedSubGraph;
    private int edgeProbabilityInPercent;
    private String executionMode;
    private String selectionMethod;
    private String vertexSelectionMethod;
    private String dualBoundMethod;
    private int nodeExpansions;

    private int primalBound;
    private int dualBound;

    public InstanceImplementation(String groupName, int number, String graphName, SimpleGraph<VertexImplementation,EdgeImplementation> graph, int edgeProbabilityInPercent, String executionMode, String selectionMethod, String vertexSelectionMethod, String dualBoundMethod, int nodeExpansions, int primalBound, int dualBound) {
        this.groupName = groupName;
        this.number = number;
        this.graph = graph;
        this.graphName = graphName;
        this.preparedSubGraph = new SubGraph(graph);
        this.edgeProbabilityInPercent = edgeProbabilityInPercent;
        this.executionMode = executionMode;
        this.vertexSelectionMethod = vertexSelectionMethod;
        this.selectionMethod = selectionMethod;
        this.dualBoundMethod = dualBoundMethod;
        this.nodeExpansions = nodeExpansions;
        this.primalBound = primalBound;
        this.dualBound = dualBound;
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    public String getGraphName() { return this.graphName; }

    public SimpleGraph<VertexImplementation,EdgeImplementation> getGraph() {
        return this.graph;
    }

    public SubGraph getPreparedSubGraph() { return this.preparedSubGraph; }

    public int getEdgeProbabilityInPercent() { return this.edgeProbabilityInPercent; }

    public String getExecutionMode() { return this.executionMode; }

    public String getSelectionMethod() { return this.selectionMethod; }

    public String getVertexSelectionMethod() { return this.vertexSelectionMethod; }

    public String getDualBoundMethod() { return this.dualBoundMethod; }

    public int getNodeExpansions() { return nodeExpansions; }

    public int getPrimalBound() {
        return primalBound;
    }

    public int getDualBound() { return dualBound; }

}
