package main.java.exercise;

import main.java.framework.StudentInformation;
import main.java.framework.StudentSolution;

public class StudentSolutionImplementation implements StudentSolution {
    @Override
    public StudentInformation provideStudentInformation() {
        return new StudentInformation(
                "Subair", // Vorname
                "Kirimow", // Nachname
                "12321260" // Matrikelnummer
        );
    }

    // Implementieren Sie hier Ihre Lösung mit Polynomialzeitreduktion
    public int findMaxClique(Graph g, TimedSolver solver, boolean[] chosenVertices) {

        int maxCliqueNum = 0;
        int left = 1;
        int right = g.numberOfVertices();

        while(left <= right) {
            int k = left + (right - left) / 2;

            StringBuilder dimacs = new StringBuilder();
            StringBuilder clauses = new StringBuilder();
            int clauseSize = 0;

            dimacs.append("p cnf ").append(k * g.numberOfVertices()).append(" ");

            // For each i, there is an i-th vertex in the clique: ⋁(v ∈ V), x(iv)
            for (int i = 1; i <= k; i++) {
                for (int v = 1; v <= g.numberOfVertices(); v++) {
                    int idx = getIndexForVar(i, v, g.numberOfVertices());
                    clauses.append(idx).append(" ");
                }
                clauses.append("0\n");
                clauseSize++;
            }

            // For each i,j, the i-th vertex is different from the j-th vertex: for each v ∈ V, ¬x(iv) ∨ ¬x(jv)
            for (int v = 1; v <= g.numberOfVertices(); v++) {
                for (int i = 1; i <= k; i++) {
                    for (int j = i + 1; j <= k; j++) {
                        int idx_i = getIndexForVar(i, v, g.numberOfVertices());
                        int idx_j = getIndexForVar(j, v, g.numberOfVertices());
                        clauses.append("-").append(idx_i).append(" -").append(idx_j).append(" 0\n");
                        clauseSize++;
                    }
                }
            }

            // For each non-edge (u,v) ∉ E, u,v cannot both belong to the clique: for each i,j, ¬x(iu) ∨ ¬x(jv)
            for (int v = 1; v < g.numberOfVertices(); v++) {
                for (int u = v + 1; u <= g.numberOfVertices(); u++) {
                    if (!g.containsEdge(v, u)) {
                        for (int i = 1; i <= k; i++) {
                            for (int j = 1; j <= k; j++) {
                                int idx_v = getIndexForVar(i, v, g.numberOfVertices());
                                int idx_u = getIndexForVar(j, u, g.numberOfVertices());
                                clauses.append("-").append(idx_v).append(" -").append(idx_u).append(" 0\n");
                                clauseSize++;
                            }
                        }
                    }
                }
            }

            // reduction to SAT finished
            dimacs.append(clauseSize).append("\n").append(clauses);

            // solve SAT
            String result = solver.solve(dimacs.toString());
            if (result.isEmpty()) {

                right = k - 1;

            } else {

                left = k + 1;
                maxCliqueNum = k;

                // reset chosenVertices for the current k
                for (int i = 0; i < chosenVertices.length; i++) {
                    chosenVertices[i] = false;
                }

                // enable all the vertices that are part of the biggest clique (yet)
                String[] literals = result.split(" ");
                for (String literal : literals) {
                    if (literal.equals("0")) break;
                    int var = Integer.parseInt(literal);
                    if (var > 0) {
                        int v = getVertexFromIndex(var, g.numberOfVertices());
                        chosenVertices[v - 1] = true;
                    }
                }
            }
        }

        return maxCliqueNum;
    }

    private int getIndexForVar(int i, int vertexIdx, int graphSize) {
        return graphSize * (i - 1) + vertexIdx;
    }

    private int getVertexFromIndex(int l, int graphSize) {
        return 1 + ((l - 1) % graphSize);
    }
}
