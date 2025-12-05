package main.java.exercise;

import main.java.framework.graph.BasicGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class Graph extends BasicGraph<VertexImplementation, EdgeImplementation> {

    public Graph(SimpleWeightedGraph<VertexImplementation, EdgeImplementation> graph) {
        super(graph);
    }

    public boolean addEdge(int vertexIdStart, int vertexIdEnd) {
        return addEdge(vertexIdStart, vertexIdEnd, 0);
    }

    public boolean addEdge(int vertexIdStart, int vertexIdEnd, int weight) {
        VertexImplementation v1 = this.vertices.get(vertexIdStart);
        VertexImplementation v2 = this.vertices.get(vertexIdEnd);
        if (v1 == null || v2 == null) {
            return false;
        }

        EdgeImplementation e = new EdgeImplementation(v1, v2);
        boolean res = this.graph.addEdge(v1, v2, e);

        if (res) {
            this.edges.put(new Tuple<Integer, Integer>(e.getFrom().getId(), e.getTo().getId()), e);
            this.graph.setEdgeWeight(e, weight);
            return true;
        }

        return false;
    }

    public void setEdgeWeight(int vertexIdStart, int vertexIdEnd, int weight) {
        if (!containsEdge(vertexIdStart, vertexIdEnd)) {
            return;
        }

        VertexImplementation v1 = this.vertices.get(vertexIdStart);
        VertexImplementation v2 = this.vertices.get(vertexIdEnd);
        if (v1 == null || v2 == null) {
            return;
        }

        EdgeImplementation e = this.graph.getEdge(v1, v2);
        this.graph.setEdgeWeight(e, weight);
    }

    public int getEdgeWeight(int vertexIdStart, int vertexIdEnd) {
        EdgeImplementation edgeImpl = super.edges.get(new main.java.framework.graph.BasicGraph.Tuple<Integer, Integer>(vertexIdStart, vertexIdEnd));
        if (edgeImpl == null) {
            edgeImpl = super.edges.get(new main.java.framework.graph.BasicGraph.Tuple<Integer, Integer>(vertexIdEnd, vertexIdStart));
        }

        if (edgeImpl == null) {
            return -1;
        }

        double res = this.graph.getEdgeWeight(edgeImpl);
        return (int)res;
    }


    public int numberOfEdges() {
        return this.graph.edgeSet().size();
    }

    public int[][] getEdges() {
        List<EdgeImplementation> edges = new ArrayList<EdgeImplementation>(this.graph.edgeSet());
        edges = sortEdges(edges);

        int[][] res = new int[edges.size()][2];
        for (int i = 0; i < edges.size(); i++) {
            res[i][0] = edges.get(i).getFrom().getId();
            res[i][1] = edges.get(i).getTo().getId();
        }
        return res;
    }

    private List<EdgeImplementation> sortEdges(List<EdgeImplementation> edges) {
        List<EdgeImplementation> edges2 = new ArrayList();

        for (EdgeImplementation e : edges) {
            VertexImplementation from = (VertexImplementation)e.getFrom();
            VertexImplementation to = (VertexImplementation)e.getTo();

            if (from.getId() > to.getId()) {
                edges2.add(new EdgeImplementation(to, from));
            }
            else {
                edges2.add(new EdgeImplementation(from, to));
            }
        }
        Collections.sort(edges2, (EdgeImplementation e1, EdgeImplementation e2) -> {
            int wert1 = e1.getFrom().getId() * 100000 + e1.getTo().getId();
            int wert2 = e2.getFrom().getId() * 100000 + e2.getTo().getId();
            if (wert1 < wert2) {
                return -1;
            } else if (wert1 > wert2) {
                return 1;
            } else {
                return 0;
            }
        });

        return edges2;
    }

    public void printEdges() {
        List<EdgeImplementation> edges = new ArrayList<EdgeImplementation>(this.graph.edgeSet());
        List<EdgeImplementation> edges2 = sortEdges(edges);

        System.out.println("Edge List:");
        for (EdgeImplementation e : edges2) {
            int weight = this.getEdgeWeight(e.getFrom().getId(), e.getTo().getId());
            String text = "(" + e.getFrom().getId() + ", " + e.getTo().getId() + ")";
            if (weight > 0) {
                text += ", w = " + weight;
            }
            System.out.println(text);
        }
    }

    public int[][] getEdgesOrderedByWeight() {
        List<EdgeImplementation> edges = new ArrayList<EdgeImplementation>(this.graph.edgeSet());
        Collections.sort(edges, (EdgeImplementation edge1, EdgeImplementation edge2) -> {
            double weight1 = this.graph.getEdgeWeight(edge1);
            double weight2 = this.graph.getEdgeWeight(edge2);
            if (weight1 < weight2) {
                return -1;
            } else if (weight1 > weight2) {
                return 1;
            } else {
                return 0;
            }
        });
        int[][] orderedEdges = new int[edges.size()][2];
        for (int i = 0; i < edges.size(); i++) {
            orderedEdges[i][0] = edges.get(i).getFrom().getId();
            orderedEdges[i][1] = edges.get(i).getTo().getId();
        }
        return orderedEdges;
    }

}
