package main.java.exercise;

import main.java.framework.Instance;

public class InstanceImplementation implements Instance {

    private String groupName;           // Art der Instanz (zB "Ins" oder "NN + LS")
    private int number;                 // Nummer des Runs
    private TSPInstance instanz;        // TSP-Instanz
    private String name;                // Name der TSP-Instanz (zB Kro100)
    private int size;                   // Größe der TSP-Instanz
    private int durchlauf;              // Nummer des Durchlaufs

    private int valueSoll;              // Sollwert einer Lösung (zB MST), falls nötig

    private TSPSolution solutionStart;  // Startlösung (zB für LS)

    private int[] order;                // Reihenfolge, in der die Lösung durchsucht werden soll bei Insertion
    private int startCity;              // Startcity bei NN oder Prim


    public InstanceImplementation(String groupName, int number, TSPInstance instanz, int durchlauf,
                                  int valueSoll, TSPSolution solutionStart,
                                  int[] order, int startCity) {
        this.groupName = groupName;
        this.number = number;
        this.instanz = instanz;
        this.name = instanz.getName();
        this.size = instanz.getN();
        this.durchlauf = durchlauf;

        this.valueSoll = valueSoll;

        this.solutionStart = solutionStart;

        this.order = order;
        this.startCity = startCity;
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    public TSPInstance getInstanz() {
        return instanz;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getDurchlauf() {
        return durchlauf;
    }

    public int getValueSoll() {
        return valueSoll;
    }

    public TSPSolution getSolutionStart() {
        return solutionStart;
    }

    public int[] getOrder() {
        return order;
    }

    public int getStartCity() {
        return startCity;
    }
}
