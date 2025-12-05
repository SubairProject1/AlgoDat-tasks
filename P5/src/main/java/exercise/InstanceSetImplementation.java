package main.java.exercise;

import main.java.framework.InstanceSet;
import main.java.framework.graph.GraphUtil;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.*;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.util.Map;


public class InstanceSetImplementation extends InstanceSet<InstanceImplementation, StudentSolutionImplementation, ResultImplementation, VerifierImplementation, SimpleGraph<VertexImplementation, EdgeImplementation>> {

    public InstanceSetImplementation(Path instanceSetPath, Path outputPath) {
        super(instanceSetPath, outputPath, ResultImplementation.class);
    }

    @Override
    protected InstanceImplementation instanceFromCsv(String line) {
        String[] splitLine = line.split(",", 11);
        SimpleGraph<VertexImplementation, EdgeImplementation> graph = this.getAdditionalInput(splitLine[2]);
        return new InstanceImplementation(splitLine[1], Integer.parseInt(splitLine[0]), splitLine[2], graph, Integer.parseInt(splitLine[3]), splitLine[4], splitLine[5], splitLine[6], splitLine[7], Integer.parseInt(splitLine[8]), Integer.parseInt(splitLine[9]), Integer.parseInt(splitLine[10]));
    }

    @Override
    protected StudentSolutionImplementation provideStudentSolution() {
        return new StudentSolutionImplementation();
    }

    @Override
    protected VerifierImplementation provideVerifier() {
        return new VerifierImplementation();
    }

    @Override
    protected SimpleGraph<VertexImplementation, EdgeImplementation> parseAdditionalInput(BufferedReader reader) {
        SimpleGraph<VertexImplementation, EdgeImplementation> graph = new SimpleGraph<VertexImplementation, EdgeImplementation>(EdgeImplementation.class);

        VertexProvider<VertexImplementation> vertexProvider = (String id, Map<String, Attribute> attributes) -> new VertexImplementation(Integer.parseInt(id));
        EdgeProvider<VertexImplementation, EdgeImplementation> edgeProvider = (VertexImplementation from, VertexImplementation to, String label, Map<String, Attribute> attributes) -> new EdgeImplementation(from, to);

        try {
            GraphMLImporter<VertexImplementation, EdgeImplementation> importer = GraphUtil.createImporter(vertexProvider, edgeProvider, "length");
            importer.importGraph(graph, reader);
            return graph;
        } catch(ImportException e) {
            return null;
        }
    }

}
