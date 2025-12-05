package main.java.exercise;

import main.java.framework.*;

import java.util.Arrays;
import java.util.ArrayList;

public class VerifierImplementation extends Verifier<InstanceImplementation, StudentSolutionImplementation, ResultImplementation> {

    @Override
    public ResultImplementation solveProblemUsingStudentSolution(InstanceImplementation instance, StudentSolutionImplementation studentSolution) {
        Timer timer = new Timer();
        timer.start();
        Graph g = instance.getPreparedGraph();
        boolean[] chosenVertices = new boolean[g.numberOfVertices()];
        Arrays.fill(chosenVertices, Boolean.FALSE);
        TimedSolver solver = new TimedSolver();
        int maxCliqueSizeFound = studentSolution.findMaxClique(g, solver, chosenVertices);
        timer.stop();
        return new ResultImplementation(g.numberOfVertices(), timer.getDuration(), timer.getDuration() - solver.getSolverTime(), solver.getSolverCalls(), maxCliqueSizeFound, chosenVertices);
    }

    @Override
    public Report verifyResult(InstanceImplementation instance, ResultImplementation result) {
        if(result.getMaxCliqueSize() != instance.getMaxCliqueSize()) {
            return new Report(false, "Error in instance " + instance.getNumber() + ": tentative clique number " + result.getMaxCliqueSize() + " but should be " + instance.getMaxCliqueSize() + ".");
        }
        Graph g = instance.getPreparedGraph();
        int numberOfChosenVertices = 0;
        ArrayList<Integer> vertexIds = new ArrayList<Integer>();
        int i = 1;
        for(boolean vertexVar : result.getChosenVertices()) {
            if(vertexVar) {
                numberOfChosenVertices += 1;
                vertexIds.add(i);
            }
            i++;
        }

        for(int j = 0; j < vertexIds.size()-1; j++) {
            for(int k = j+1; k < vertexIds.size(); k++) {
                if(!g.containsEdge(vertexIds.get(j), vertexIds.get(k))) {
                    return new Report(false, "Error in instance " + instance.getNumber() + ": selected non-neighboring vertices " + vertexIds.get(j) + " and " + vertexIds.get(k) + ".");
                }
            }
        }

        if(result.getMaxCliqueSize() != numberOfChosenVertices) {
            return new Report(false, "Error in instance " + instance.getNumber() + ": maxCliqueSize returned " + result.getMaxCliqueSize() + " but number of chosen vertices " + numberOfChosenVertices + ".");
        }

        return new Report(true, "");
    }
}
