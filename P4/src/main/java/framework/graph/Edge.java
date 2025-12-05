package main.java.framework.graph;

public abstract class Edge {

    private Vertex from;
    private Vertex to;


    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }
}
