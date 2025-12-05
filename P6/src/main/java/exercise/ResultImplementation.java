package main.java.exercise;

import main.java.framework.PersistAs;
import main.java.framework.Result;

public class ResultImplementation implements Result {

    private String problemType;

    @PersistAs("duration")
    private long duration;

    @PersistAs("n")
    private int n;

    private TSPInstance instanz;

    @PersistAs("name")
    private String name;

    @PersistAs("x")
    private String x;

    @PersistAs("y")
    private String y;

    @PersistAs("durchlauf")
    private int durchlauf;

    private TSPSolution solutionIst;

    @PersistAs("value")
    private int value;

    @PersistAs("solution")
    private String solution;

    @PersistAs("optimalValue")
    private int optimalValue;

    private Graph mst;


    public ResultImplementation(String problemType, long duration, int n, TSPInstance instanz, int durchlauf,
                                TSPSolution solutionIst, int value, int optimalValue, Graph mst) {
        this.problemType = problemType;
        this.n = n;
        this.duration = duration;

        this.instanz = instanz;
        this.name = instanz.getName();
        double[] xDouble = instanz.getX();
        String x = "";
        for (int i = 0; i < xDouble.length; i++) {
            if (i != 0) {
                x += "|";
            }
            x += xDouble[i];
        }
        this.x = x;
        double[] yDouble = instanz.getY();
        String y = "";
        for (int i = 0; i < yDouble.length; i++) {
            if (i != 0) {
                y += "|";
            }
            y += yDouble[i];
        }
        this.y = y;
        this.durchlauf = durchlauf;
        this.solutionIst = solutionIst;
        this.value = value;

        int[] solutionInt = new int[n];

        if (solutionIst != null) {
            for (int i = 0; i < n; i++) {
                solutionInt[i] = solutionIst.get(i);
            }
            solutionInt = VerifierImplementation.normieren(solutionInt);
        }
        String solution = "";
        for (int i = 0; i < solutionInt.length; i++) {
            if (i != 0) {
                solution += "|";
            }
            solution += solutionInt[i];
        }
        this.solution = solution;

        this.optimalValue = optimalValue;
        this.mst = mst;
    }

    public String getProblemType() {
        return problemType;
    }

    public long getDuration() {
        return duration;
    }

    public int getN() {
        return n;
    }

    public TSPInstance getInstanz() {
        return instanz;
    }

    public String getName() {
        return name;
    }

    public int getDurchlauf() {
        return durchlauf;
    }

    public TSPSolution getSolutionIst() {
        return solutionIst;
    }

    public int getValue() {
        return value;
    }

    public int getOptimalValue() {
        return optimalValue;
    }

    public Graph getMst() {
        return mst;
    }
}
