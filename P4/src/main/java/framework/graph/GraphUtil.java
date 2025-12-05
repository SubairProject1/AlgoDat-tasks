package main.java.framework.graph;

import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.GraphImporter;
import org.jgrapht.io.GraphMLImporter;
import org.jgrapht.io.VertexProvider;

public class GraphUtil {

    public static <VertexT extends Vertex, EdgeT extends Edge> GraphMLImporter<VertexT, EdgeT> createImporter(VertexProvider<VertexT> vertexProvider, EdgeProvider<VertexT, EdgeT> edgeProvider, String edgeWeightAttributeName) {
        GraphMLImporter<VertexT, EdgeT> importer = new GraphMLImporter<VertexT, EdgeT>(vertexProvider, edgeProvider);
        importer.setEdgeWeightAttributeName(edgeWeightAttributeName);
        return importer;
    }

}
