package main.java.exercise;

import main.java.framework.Instance;
import org.jgrapht.graph.SimpleGraph;

public class InstanceImplementation implements Instance {

    private String groupName;
    private int number;
    private SimpleGraph<VertexImplementation, EdgeImplementation> graph;
    private Graph preparedGraph;
    private int maxCliqueSize;

    public InstanceImplementation(String groupName, int number, SimpleGraph<VertexImplementation, EdgeImplementation> graph, int maxCliqueSize) {
        this.groupName = groupName;
        this.number = number;
        this.graph = graph;
        this.preparedGraph = new Graph(graph);
        this.maxCliqueSize = maxCliqueSize;
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    public SimpleGraph<VertexImplementation, EdgeImplementation> getGraph() {
        return this.graph;
    }

    public Graph getPreparedGraph() { return this.preparedGraph; }

    public int getMaxCliqueSize() { return this.maxCliqueSize; }
}
