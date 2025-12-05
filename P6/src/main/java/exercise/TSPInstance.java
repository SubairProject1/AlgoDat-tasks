package main.java.exercise;

import org.jgrapht.graph.SimpleWeightedGraph;

public class TSPInstance {

    private String name;   // Name der Instanz (zB KroA100)
    private int n;
    private int[][] dist;

    private int optvalue;

    private double[] x;
    private double[] y;

    public TSPInstance(String name, int n, int[][] dist, int optvalue, double[] x, double[] y) {
        this.name = name;
        this.n = n;
        this.dist = dist;
        this.optvalue = optvalue;

        this.x = x;
        this.y = y;
    }

    public Graph createEmptyGraph() {
        SimpleWeightedGraph<VertexImplementation, EdgeImplementation> graph =
                new SimpleWeightedGraph(SimpleWeightedGraph.class);
        for (int i = 0; i < n; i++) {
            VertexImplementation v = new VertexImplementation(i);
            graph.addVertex(v);
        }
        return new Graph(graph);
    }

    public int getDistance(int from, int to) {
        return dist[from][to];
    }

    public String getName() {
        return name;
    }

    public int getN() {
        return n;
    }

    public int getOptvalue() {
        return optvalue;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }
}
