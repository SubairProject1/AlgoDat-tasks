package main.java.exercise;

import java.util.Comparator;

public class SubProblemComparator implements Comparator<SubProblem> {
    public int compare(SubProblem s1, SubProblem s2) {
        if (s1.upperBound() > s2.upperBound())
            return -1;
        else if (s1.upperBound() < s2.upperBound())
            return 1;
        return 0;
    }
}
