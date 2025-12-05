package main.java.exercise;

import main.java.framework.graph.BasicGraph;
import org.jgrapht.graph.SimpleGraph;

public class Graph extends BasicGraph<VertexImplementation, EdgeImplementation> {

    public Graph(SimpleGraph<VertexImplementation, EdgeImplementation> graph) {
        super(graph);
    }

}