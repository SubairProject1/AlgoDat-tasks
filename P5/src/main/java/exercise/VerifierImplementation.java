package main.java.exercise;

import main.java.framework.Report;
import main.java.framework.Timer;
import main.java.framework.Verifier;

import java.util.*;


public class VerifierImplementation extends Verifier<InstanceImplementation, StudentSolutionImplementation, ResultImplementation> {

    @Override
    public ResultImplementation solveProblemUsingStudentSolution(InstanceImplementation instance, StudentSolutionImplementation studentSolution) {
        SubGraph g = instance.getPreparedSubGraph();
        Timer timer = new Timer();
        SubProblemQueue Q;
        BranchAndBoundResult branchAndBoundResult;
        int tentativeOptimum;

        timer.start();
        if(instance.getExecutionMode().contains("root")) {
            Q = new SubProblemQueue(new LinkedList<>());
            SubProblem rootProblem = new SubProblem(studentSolution, instance, g, 0);
            Q.add(rootProblem);
            Q.poll();
            branchAndBoundResult = new BranchAndBoundResult(rootProblem.lowerBound(), rootProblem.upperBound());
        } else if(instance.getExecutionMode().contains("full")) {
            if (instance.getSelectionMethod().contains("fifo")) {
                Q = new SubProblemQueue(new LinkedList<>());
            } else if(instance.getSelectionMethod().contains("lifo")) {
                Queue<SubProblem> lifoQueue = Collections.asLifoQueue(new ArrayDeque<>());
                Q = new SubProblemQueue(lifoQueue);
            } else if(instance.getSelectionMethod().contains("best")) {
                Queue<SubProblem> priorityQueue = new PriorityQueue<>(new SubProblemComparator());
                Q = new SubProblemQueue(priorityQueue);
            } else {
                throw new IllegalArgumentException("Unknown SubProblem selection method provided: " + instance.getSelectionMethod());
            }
            SubProblem rootProblem = new SubProblem(studentSolution, instance, g, 0);
            tentativeOptimum = studentSolution.maximumIndependentSetBranchAndBoundSolver(rootProblem, Q);
            branchAndBoundResult = new BranchAndBoundResult(tentativeOptimum, rootProblem.upperBound());
        } else {
            throw new IllegalArgumentException("Unknown execution mode provided: " + instance.getExecutionMode());
        }
        timer.stop();

        return new ResultImplementation(timer.getDuration(), instance.getGraphName(), instance.getEdgeProbabilityInPercent(), Q.getPollCounter(), branchAndBoundResult.getGlobalLowerBound(), branchAndBoundResult.getRootUpperBound());
    }

    @Override
    public Report verifyResult(InstanceImplementation instance, ResultImplementation result) {
        if (instance.getPrimalBound() != result.getTentativePrimalBound()) {
            return new Report(false, "Error in instance " + instance.getNumber() + "'s best found solution: should to be " + instance.getPrimalBound() + " but was " + result.getTentativePrimalBound() + ".");
        }

        if (instance.getDualBound() != result.getTentativeDualBound()) {
            return new Report(false, "Error in instance " + instance.getNumber() + "'s root upper bound: should to be " + instance.getDualBound() + " but was " + result.getTentativeDualBound() + ".");
        }

        if (instance.getNodeExpansions() > 0 && instance.getNodeExpansions() != result.getNodeExpansions()) {
            return new Report(false, "Error in instance " + instance.getNumber() + "'s number of expanded branch-and-bound nodes (subproblems including root): should to be " + instance.getNodeExpansions() + " but was " + result.getNodeExpansions() + ".");
        }

        return new Report(true, "");
    }
}
