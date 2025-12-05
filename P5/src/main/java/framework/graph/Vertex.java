package main.java.framework.graph;

public abstract class Vertex {

    private int id;

    public Vertex(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
