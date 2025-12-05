package main.java.exercise;

import main.java.framework.Timer;
import main.java.framework.solver.Solver;

public class TimedSolver {
    private long solverTime;
    private int solverCalls;
    private Solver solver;

    public TimedSolver() {
        solverTime = 0;
        solverCalls = 0;
        solver = new Solver();
    }

    public String solve(String dimacs) {
        Timer timer = new Timer();
        timer.start();
        this.solverCalls += 1;
        String model = this.solver.solve(dimacs);
        timer.stop();
        this.solverTime += timer.getDuration();
        return model;
    }

    public long getSolverTime() { return this.solverTime; }

    public int getSolverCalls() { return this.solverCalls; }
}
