package main.java.framework;

public abstract class Verifier<InstanceT extends Instance, StudentSolutionT extends StudentSolution, ResultT extends Result> {

    abstract public ResultT solveProblemUsingStudentSolution(InstanceT instance, StudentSolutionT studentSolution);

    abstract public Report verifyResult(InstanceT instance, ResultT result);
}
