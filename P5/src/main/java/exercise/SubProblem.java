package main.java.exercise;


public class SubProblem {

    private StudentSolutionImplementation studentSolution;
    private InstanceImplementation instance;
    private SubGraph subGraph;
    private int partialSolutionObjectiveValue;
    private int cachedLowerBound;
    private int cachedUpperBound;

    public SubProblem(StudentSolutionImplementation studentSolution, InstanceImplementation instance, SubGraph g, int partialSolutionObjectiveValue) {
        this.studentSolution = studentSolution;
        this.instance = instance;
        this.subGraph = g;
        this.partialSolutionObjectiveValue = partialSolutionObjectiveValue;
        this.cachedLowerBound = -1;
        this.cachedUpperBound = -1;
    }

    public int lowerBound() {
        if(cachedLowerBound == -1) {
            this.cachedLowerBound = partialSolutionObjectiveValue + studentSolution.vertexCoverBasedHeuristic(subGraph);
        }
        return this.cachedLowerBound;
    }

    public int upperBound() {
        if(cachedUpperBound == -1) {
            if (instance.getDualBoundMethod().contains("trivial")) {
                this.cachedUpperBound = partialSolutionObjectiveValue + studentSolution.trivialUpperBound(subGraph);
            } else if (instance.getDualBoundMethod().contains("hansen")) {
                this.cachedUpperBound = partialSolutionObjectiveValue + studentSolution.hansenUpperBound(subGraph);
            } else {
                throw new IllegalArgumentException("Unknown dual bound method " + instance.getDualBoundMethod());
            }
        }
        return cachedUpperBound;
    }

    public void branch(SubProblemQueue Q) {
        VertexImplementation branchVertex;
        if(instance.getVertexSelectionMethod().contains("lex")) {
            branchVertex = subGraph.getFirstVertex();
        } else if(instance.getVertexSelectionMethod().contains("maxdegree")) {
            branchVertex = subGraph.getMaxDegreeVertex();
        } else {
            throw new IllegalArgumentException("Unknown vertex selection method " + instance.getVertexSelectionMethod());
        }
        int branchVertexDegree = subGraph.degreeOf(branchVertex);

        SubGraph subGraphOne = subGraph.clone();
        subGraphOne.removeVertexAndNeighbors(branchVertex);
        SubProblem branchOne = new SubProblem(studentSolution, instance, subGraphOne, partialSolutionObjectiveValue + 1);

        if(branchVertexDegree > 0) {
            SubGraph subGraphZero = subGraph.clone();
            subGraphZero.removeVertex(branchVertex);
            SubProblem branchZero = new SubProblem(studentSolution, instance, subGraphZero, partialSolutionObjectiveValue);
            Q.add(branchZero);
        }
        Q.add(branchOne);
    }

}
