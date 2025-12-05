package main.java.exercise;

import main.java.framework.StudentInformation;
import main.java.framework.StudentSolution;

import java.util.*;


public class StudentSolutionImplementation implements StudentSolution {
    @Override
    public StudentInformation provideStudentInformation() {
        return new StudentInformation(
                "Subair", // Vorname
                "Kirimow", // Nachname
                "12321260" // Matrikelnummer
        );
    }

    // Implementieren Sie hier Ihre Lösung für triviale obere Schranke
    public int trivialUpperBound(SubGraph g) {
        return g.numberOfVertices();
    }

    // Implementieren Sie hier Ihre Lösung für obere Schranke nach Hansen
    public int hansenUpperBound(SubGraph g) {

        int vertexAmount = g.numberOfVertices();
        int edgeAmount = g.numberOfEdges();

        return (int) (0.5 + Math.sqrt(0.25 + vertexAmount * vertexAmount - vertexAmount - 2 * edgeAmount));
    }

    // Implementieren Sie hier Ihre Lösung basierend auf der gegebenen greedy Vertex Cover Heuristik
    public int vertexCoverBasedHeuristic(SubGraph g) {
        return g.numberOfVertices() - g.greedyVertexCoverSolutionValue();
    }

    // Implementieren Sie hier Ihre Lösung des definierten Branch-and-Bound Algorithmus für das MISP basierend auf der SubProblem Datenstruktur
    public int maximumIndependentSetBranchAndBoundSolver(SubProblem rootProblem, SubProblemQueue Q) {

        int maxLowerBound = rootProblem.lowerBound();
        Q.add(rootProblem);

        while (!Q.isEmpty()) {
            SubProblem instance = Q.poll();

            if (instance.upperBound() > maxLowerBound) {

                instance.branch(Q);

                // go through all the partial instances from the main one
                while (!Q.isEmpty()) {
                    SubProblem partInstance = Q.poll();

                    if (partInstance.lowerBound() > maxLowerBound) {
                        maxLowerBound = partInstance.lowerBound();
                    }

                    // if U' = L' the following code will be skipped
                    // if L' > L is possible in the instance, branch into its partial instances
                    if (partInstance.upperBound() > maxLowerBound) {
                        partInstance.branch(Q);
                    }
                }
            }
        }

        return maxLowerBound;
    }
}
