package main.java.exercise;

import main.java.framework.graph.Vertex;
import org.jgrapht.Graphs;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.AbstractGraph;
import org.jgrapht.alg.vertexcover.GreedyVCImpl;

public class SubGraph {

    private AsSubgraph<VertexImplementation, EdgeImplementation> subGraph;

    public SubGraph(SimpleGraph<VertexImplementation, EdgeImplementation> graph) {
        this.subGraph = new AsSubgraph<>(graph);
    }

    public SubGraph(AsSubgraph<VertexImplementation, EdgeImplementation> subGraph) {
        this.subGraph = new AsSubgraph<>(subGraph);
    }

    public SubGraph clone() {
        return new SubGraph(new AsSubgraph<>(subGraph));
    }

    public int greedyVertexCoverSolutionValue() {
        GreedyVCImpl<VertexImplementation, EdgeImplementation> greedyVC = new GreedyVCImpl<>(this.subGraph);
        return (int)greedyVC.getVertexCover().getWeight();
    }

    public int numberOfVertices() {
        return this.subGraph.vertexSet().size();
    }

    public int numberOfEdges() {
        return this.subGraph.edgeSet().size();
    }

    public VertexImplementation getFirstVertex() {
        for(VertexImplementation vertex : subGraph.vertexSet()) {
            return vertex;
        }
        return null;
    }

    public VertexImplementation getMaxDegreeVertex() {
        VertexImplementation maxDegreeVertex = getFirstVertex();
        int maxDegree = subGraph.degreeOf(maxDegreeVertex);

        for(VertexImplementation vertex : subGraph.vertexSet()) {
            int candidateDegree = subGraph.degreeOf(vertex);
            if(candidateDegree > maxDegree) {
                maxDegreeVertex = vertex;
                maxDegree = candidateDegree;
            }
        }

        return maxDegreeVertex;
    }

    public int degreeOf(VertexImplementation v) {
        return this.subGraph.degreeOf(v);
    }

    public void removeVertex(VertexImplementation v) {
        this.subGraph.removeVertex(v);
    }

    public void removeVertexAndNeighbors(VertexImplementation v) {
        for(EdgeImplementation edges : this.subGraph.edgesOf(v)) {
            this.subGraph.removeVertex(Graphs.getOppositeVertex(this.subGraph, edges, v));
        }
        this.subGraph.removeVertex(v);
    }
}
