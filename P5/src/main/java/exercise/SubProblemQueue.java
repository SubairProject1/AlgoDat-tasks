package main.java.exercise;

import java.util.*;

public class SubProblemQueue {

    private Queue<SubProblem> Q;
    private int pollCounter;

    public SubProblemQueue(Queue<SubProblem> Q) {
        this.Q = Q;
        this.pollCounter = 0;
    }

    public void add(SubProblem p) {
        Q.add(p);
    }

    public SubProblem poll() {
        pollCounter += 1;
        return Q.poll();
    }

    public boolean isEmpty() {
        return Q.isEmpty();
    }

    public int getPollCounter() {
        return pollCounter;
    }
}
