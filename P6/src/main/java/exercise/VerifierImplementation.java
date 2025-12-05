package main.java.exercise;

import main.java.framework.Report;
import main.java.framework.Timer;
import main.java.framework.Verifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class VerifierImplementation extends Verifier<InstanceImplementation, StudentSolutionImplementation, ResultImplementation> {

    Map<String, long[]> durationsNN = new HashMap();
    Map<String, long[]> durationsIns = new HashMap();
    Map<String, long[]> durationsSTH = new HashMap();
    Map<String, TSPSolution[]> solutionsNN = new HashMap();
    Map<String, TSPSolution[]> solutionsIns = new HashMap();
    Map<String, TSPSolution[]> solutionsSTH = new HashMap();

    Map<String, long[]> durationsMst = new HashMap();
    Map<String, Graph[]> msts = new HashMap();

    @Override
    public ResultImplementation solveProblemUsingStudentSolution(InstanceImplementation instance, StudentSolutionImplementation studentSolution) {
        TSPInstance instanz = instance.getInstanz();
        String instanzname = instanz.getName();
        int n = instance.getSize();
        int durchlauf = instance.getDurchlauf();
        String problemType = instance.getGroupName();

        if (problemType.contains("NN")) {
            long duration = 0L;
            TSPSolution solutionIst = new TSPSolution(instanz);

            if (containsDuration(durationsNN, instanzname, durchlauf) & containsSolution(solutionsNN, instanzname, durchlauf)) {
                duration += durationsNN.get(instanzname)[durchlauf];
                solutionIst = solutionsNN.get(instanzname)[durchlauf];
            }
            else {
                Timer timer = new Timer();
                timer.start();
                studentSolution.nearestNeighbor(instanz, solutionIst, instance.getStartCity());
                timer.stop();

                duration += timer.getDuration();
                durationsNN.get(instanzname)[durchlauf] = duration;
                solutionsNN.get(instanzname)[durchlauf] = solutionIst.copy();
            }

            if (problemType.contains("LS")) {
                Timer timer = new Timer();
                timer.start();
                studentSolution.localOptimum(instanz, solutionIst);
                timer.stop();

                duration += timer.getDuration();
            }

            int valueIst = solutionIst.getObjective();
            Graph mst = null;

            return new ResultImplementation(problemType, duration, n, instanz, durchlauf,
                solutionIst, valueIst, instanz.getOptvalue(), mst);
        } else if (problemType.contains("Ins")) {
            long duration = 0L;
            TSPSolution solutionIst = new TSPSolution(instanz);

            if (containsDuration(durationsIns, instanzname, durchlauf) & containsSolution(solutionsIns, instanzname, durchlauf)) {
                duration += durationsIns.get(instanzname)[durchlauf];
                solutionIst = solutionsIns.get(instanzname)[durchlauf];
            }
            else {
                Timer timer = new Timer();
                timer.start();
                studentSolution.cheapestInsertion(instanz, solutionIst, instance.getOrder());
                timer.stop();

                duration += timer.getDuration();
                durationsIns.get(instanzname)[durchlauf] = duration;
                solutionsIns.get(instanzname)[durchlauf] = solutionIst.copy();
            }

            if (problemType.contains("LS")) {
                Timer timer = new Timer();
                timer.start();
                studentSolution.localOptimum(instanz, solutionIst);
                timer.stop();

                duration += timer.getDuration();
            }

            int valueIst = solutionIst.getObjective();
            Graph mst = null;

            return new ResultImplementation(problemType, duration, n, instanz, durchlauf,
                    solutionIst, valueIst, instanz.getOptvalue(), mst);
        } else if (problemType.equals("MST")) {
            long duration = 0L;
            Graph mst = instanz.createEmptyGraph();
            int valueIst = studentSolution.prim(instanz, new PriorityQueue(), mst);
            TSPSolution solutionIst = null;  // brauchen wir hier nicht

            if (containsDuration(durationsMst, instanzname, durchlauf) & containsMst(msts, instanzname, durchlauf)) {
                duration += durationsMst.get(instanzname)[durchlauf];
                mst = msts.get(instanzname)[durchlauf];
            }
            else {
                Timer timer = new Timer();
                timer.start();
                studentSolution.prim(instanz, new PriorityQueue(), mst);
                timer.stop();

                duration += timer.getDuration();
                durationsMst.get(instanzname)[durchlauf] = duration;
                msts.get(instanzname)[durchlauf] = mst;  // TODO: Evtl. kopieren?!
            }

            return new ResultImplementation(problemType, duration, n, instanz, durchlauf,
                    solutionIst, valueIst, instanz.getOptvalue(), mst);
        } else if (problemType.contains("STH")) {
            long duration = 0L;
            TSPSolution solutionIst = new TSPSolution(instanz);
            Graph mst = instanz.createEmptyGraph();

            if (containsDuration(durationsMst, instanzname, durchlauf) & containsMst(msts, instanzname, durchlauf)) {
                duration += durationsMst.get(instanzname)[durchlauf];
                mst = msts.get(instanzname)[durchlauf];
            }
            else {
                Timer timer = new Timer();
                timer.start();
                studentSolution.prim(instanz, new PriorityQueue(), mst);
                timer.stop();

                duration += timer.getDuration();
                durationsMst.get(instanzname)[durchlauf] = duration;
                msts.get(instanzname)[durchlauf] = mst;  // TODO: Evtl. kopieren?!
            }

            if (containsDuration(durationsSTH, instanzname, durchlauf) & containsSolution(solutionsSTH, instanzname, durchlauf)) {
                duration += durationsSTH.get(instanzname)[durchlauf];
                solutionIst = solutionsSTH.get(instanzname)[durchlauf];
            }
            else {
                Timer timer = new Timer();
                timer.start();
                studentSolution.spanningTreeHeuristic(instanz, solutionIst, mst, instance.getStartCity());
                timer.stop();

                duration += timer.getDuration();
                durationsSTH.get(instanzname)[durchlauf] = duration;
                solutionsSTH.get(instanzname)[durchlauf] = solutionIst.copy();
            }

            if (problemType.contains("LS")) {
                Timer timer = new Timer();
                timer.start();
                studentSolution.localOptimum(instanz, solutionIst);
                timer.stop();

                duration += timer.getDuration();
            }

            int valueIst = solutionIst.getObjective();

            return new ResultImplementation(problemType, duration, n, instanz, durchlauf,
                    solutionIst, valueIst, instanz.getOptvalue(), mst);
        } else if (problemType.equals("LS")) {
            long duration = 0L;
            TSPSolution solutionStart = instance.getSolutionStart();
            TSPSolution solutionIst = solutionStart.copy();

            Timer timer = new Timer();
            timer.start();
            studentSolution.localOptimum(instanz, solutionIst);
            timer.stop();

            duration += timer.getDuration();

            int valueIst = solutionIst.getObjective();
            Graph mst = null;

            return new ResultImplementation(problemType, duration, n, instanz, durchlauf,
                    solutionIst, valueIst, instanz.getOptvalue(), mst);
        }
        return null;
    }


    public boolean containsDuration(Map<String, long[]> durations, String key, int durchlauf) {
        if (!durations.containsKey(key)) {
            long[] temp = new long[10];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = -1L;
            }
            durations.put(key, temp);
            return false;
        }
        else {
            long[] temp = durations.get(key);
            if (temp[durchlauf] >= 0L) {
                return true;
            }
            return false;
        }
    }

    public boolean containsSolution(Map<String, TSPSolution[]> solutions, String key, int durchlauf) {
        if (!solutions.containsKey(key)) {
            TSPSolution[] temp = new TSPSolution[10];
            solutions.put(key, temp);
            return false;
        }
        else {
            TSPSolution[] temp = solutions.get(key);
            if (temp[durchlauf] != null) {
                return true;
            }
            return false;
        }
    }

    public boolean containsMst(Map<String, Graph[]> msts, String key, int durchlauf) {
        if (!msts.containsKey(key)) {
            Graph[] temp = new Graph[10];
            msts.put(key, temp);
            return false;
        }
        else {
            Graph[] temp = msts.get(key);
            if (temp[durchlauf] != null) {
                return true;
            }
            return false;
        }
    }


    public boolean checkFeasibleTour(TSPInstance instance, TSPSolution solutionIst) {
        int n = instance.getN();
        for (int city = 0; city < n; city++) {
            boolean containsCity = false;
            for (int i = 0; i < n; i++) {
                if (city == solutionIst.get(i)) {
                    containsCity = true;
                    break;
                }
            }
            if (!containsCity) {
                return false;
            }
        }
        return true;
    }


    public boolean checkStartCity(TSPSolution solutionIst, int startCity) {
        return solutionIst.get(0) == startCity;
    }


    public boolean checkNearestNeighbor(TSPInstance instance, TSPSolution solutionIst) {
        boolean[] visited = new boolean[instance.getN()];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = true;
        }
        visited[solutionIst.get(instance.getN() - 1)] = false;

        for (int i = instance.getN() - 2; i >= 0; i--) {
            int nextCity = solutionIst.get(i+1);
            int currentCity = solutionIst.get(i);
            int bestDist = instance.getDistance(currentCity, nextCity);

            for (int candit = 0; candit < instance.getN(); candit++) {
                if (!visited[candit] && instance.getDistance(currentCity, candit) < bestDist) {
                    return false;
                }
            }

            visited[solutionIst.get(i)] = false;
        }
        return true;
    }


    public boolean checkCheapestInsertion(TSPInstance instance, TSPSolution solutionIst, int[] order) {
        TSPSolution sol = solutionIst.copy();

        for (int stelle = order.length - 1; stelle > 1; stelle--) {
            int city = order[stelle];
            int indexBest = sol.getIndexOfCity(city);

            if (indexBest == -1) {
                return false;
            }

            int deltaBest = -sol.deltaDelete(indexBest);
            sol.delete(indexBest);

            for (int i = 0; i <= sol.getSolutionLength(); i++) {
                if (i == indexBest) {
                    continue;
                }
                if (sol.deltaInsert(city, i) < deltaBest) {
                    return false;
                }
            }
        }

        return true;
    }


    public boolean checkLocalOptimum(TSPInstance instance, TSPSolution solutionIst) {
        TSPSolution sol = solutionIst.copy();

        for (int indexBest = 0; indexBest < instance.getN(); indexBest++) {
            int city = solutionIst.get(indexBest);
            int deltaBest = -sol.deltaDelete(indexBest);
            sol.delete(indexBest);

            for (int i = 0; i <= sol.getSolutionLength(); i++) {
                if (i == indexBest) {
                    continue;
                }
                if (sol.deltaInsert(city, i) < deltaBest) {
                    return false;
                }
            }

            sol.insert(city, indexBest);
        }

        return true;
    }


    public boolean checkSpanningTree(TSPInstance instance, Graph mst) {
        // Anzahl der Kanten = n-1 und alle Knoten erreicht
        if (mst.numberOfEdges() != instance.getN() - 1) {
            return false;
        }

        int[][] edges = mst.getEdges();
        boolean[] visited = new boolean[instance.getN()];

        // Breitensuche
        java.util.LinkedList<Integer> stack = new java.util.LinkedList();
        stack.add(0);

        while (!stack.isEmpty()) {
            int city = stack.pop();
            visited[city] = true;
            for (int i = 0; i < edges.length; i++) {
                int[] edge = edges[i];
                if (edge[0] == city && !visited[edge[1]]) {
                    stack.add(edge[1]);
                }
                if (edge[1] == city && !visited[edge[0]]) {
                    stack.add(edge[0]);
                }
            }
        }

        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                return false;
            }
        }

        return true;
    }


    public boolean checkSpanningTreeObjective(int providedWeight, int expectedWeight) {
        return (providedWeight == expectedWeight);
    }


    public boolean checkSpanningTreeHeuristic(TSPInstance instance, TSPSolution solutionIst, Graph mst, int startCity) {
        // Startknotenbedingung
        int start = solutionIst.get(0);
        if (start != startCity) {
            return false;
        }

        TSPSolution sol = solutionIst.copy();
        for (int t = instance.getN() - 1; t >= 2; t--) {
            int aktuell = sol.get(t);

            // Aktueller Knoten muss Sackgasse sein
            if (deg(mst, sol, aktuell) > 1) {
                return false;
            }

            // Kanten zu Nachbarn mit deg 2 müssen genommen werden, außer, es ist der Startknoten, dann darf die Kante nicht genommen werden
            int nachbar = mst.getNeighbors(aktuell)[0];
            int deg = deg(mst, sol, nachbar);
            if (deg == 2) {
                if (nachbar != start && sol.get(t-1) != nachbar) {
                    return false;
                }
                if (nachbar == start && sol.get(t-1) == nachbar) {
                    return false;
                }
            }

            // Wenn der Nachbar deg > 2 hat, so darf er nicht genommen werden und
            // der Tourvorgängerknoten muss einen Pfad zum Startknoten haben, der über den Nachbarn führt.
            if (deg > 2) {
                if (sol.get(t-1) == nachbar) {
                    return false;
                }

                if (!hasPath(mst, sol, sol.get(t-1), start, nachbar)) {
                    return false;
                }
            }

            sol.delete(t);
        }

        return true;
    }

    private int deg(Graph mst, TSPSolution solution, int city) {
        int[] nachbarn = mst.getNeighbors(city);
        int res = 0;
        for (int i = 0; i < nachbarn.length; i++) {
            if (solution.isVisited(nachbarn[i])) {
                res++;
            }
        }
        return res;
    }

    private boolean hasPath(Graph mst, TSPSolution sol, int from, int to, int mid) {
        // Gibt es einen Pfad von from nach to, der über mid führt?
        boolean[] visited = new boolean[mst.numberOfVertices()];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = !sol.isVisited(i);
        }

        // Pfad from -> mid
        java.util.LinkedList<Integer> stapel = new LinkedList();
        stapel.add(from);
        int current = from;

        while (!stapel.isEmpty()) {
            current = stapel.pop();
            if (current == mid) {
                break;
            }

            visited[current] = true;
            int[] temp = mst.getNeighbors(current);
            for (int nachbar : temp) {
                if (!visited[nachbar]) {
                    stapel.addLast(nachbar);
                }
            }
        }

        // Pfad mid -> to
        stapel = new LinkedList();
        stapel.add(mid);
        current = mid;

        while (!stapel.isEmpty()) {
            current = stapel.pop();
            if (current == to) {
                return true;
            }

            visited[current] = true;
            int[] temp = mst.getNeighbors(current);
            for (int nachbar : temp) {
                if (!visited[nachbar]) {
                    stapel.addLast(nachbar);
                }
            }
        }

        return false;
    }


    public boolean checkEqualityOfTours(TSPInstance instance, TSPSolution solutionIst, TSPSolution solutionSoll) {
        int n = instance.getN();
        int[] solIst = new int[n];
        for (int i = 0; i < n; i++) {
            solIst[i] = solutionIst.get(i);
        }
        int[] solSoll = new int[n];
        for (int i = 0; i < n; i++) {
            solSoll[i] = solutionSoll.get(i);
        }

        // Beide Lösungen normieren (0 ist Startstadt, kleinere Nachbarstadt ist rechter Nachbar)
        solIst = normieren(solIst);
        solSoll = normieren(solSoll);

        for (int i = 0; i < n; i++) {
            if (solIst[i] != solSoll[i]) {
                return false;
            }
        }

        return true;
    }


    public static int[] normieren(int[] x) {
        int n = x.length;
        int[] res = new int[n];

        // Schritt 1: Suche die 0
        int index0 = 0;
        for (int i = 0; i < n; i++) {
            if (x[i] == 0) {
                index0 = i;
                break;
            }
        }

        // Schritt 2: Suche kleinere Richtung und kopiere Elemente
        int left = x[(index0 - 1 + n) % n];
        int right = x[(index0 + 1) % n];

        if (right < left) {
            // gehe nach rechts
            for (int j = 0; j < n; j++) {
                res[j] = x[(j + index0) % n];
            }
        }
        else {
            // gehe nach links
            for (int j = 0; j < n; j++) {
                res[j] = x[(index0 - j + n) % n];
            }
        }

        return res;
    }


    @Override
    public Report verifyResult(InstanceImplementation instance, ResultImplementation result) {
        boolean correct = true;
        String fehlertext = "Error in instance " + instance.getNumber() + " (TSP instance " + instance.getInstanz().getName() + "):\n ";

        if (instance.getGroupName().equals("MST")) {
            if (!checkSpanningTree(instance.getInstanz(), result.getMst())) {
                correct = false;
                fehlertext += " The provided Spanning Tree is not feasible.";
            }
            if (!checkSpanningTreeObjective(result.getValue(), instance.getValueSoll())) {
                correct = false;
                int soll = instance.getValueSoll();
                int ist = result.getValue();
                fehlertext += " The objective of the provided solution is not optimal. Should have an objective of " + soll + " but has " + ist + ". Probably check if you compute the total weight of the spanning tree correctly and/or if you choose the right edges.";
            }

            if (correct) {
                return new Report(true, "");
            } else {
                return new Report(false, fehlertext);
            }
        } else if (instance.getGroupName().equals("LS")) {
            if (!checkFeasibleTour(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not feasible since not all cities are visited.";
            }

            if (!checkLocalOptimum(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not a local optimum with respect to the given insert operator.";
            }

            if (correct) {
                return new Report(true, "");
            } else {
                return new Report(false, fehlertext);
            }
        } else if (instance.getGroupName().equals("NN")) {
            if (!checkFeasibleTour(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not feasible since not all cities are visited.";
            }

            if (!checkNearestNeighbor(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided solution violates the nearest neighbor principle.";
            }

            if (!checkStartCity(result.getSolutionIst(), instance.getStartCity())) {
                correct = false;
                int soll = instance.getStartCity();
                int ist = result.getSolutionIst().get(0);
                fehlertext += " The provided solution should start with city " + soll + " but actually starts with city " + ist + ".";
            }

            if (correct) {
                return new Report(true, "");
            } else {
                return new Report(false, fehlertext);
            }
        } else if (instance.getGroupName().equals("NN+LS")) {
            if (!checkFeasibleTour(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not feasible since not all cities are visited.";
            }

            if (!checkLocalOptimum(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not a local optimum with respect to the given insert operator.";
            }

            if (correct) {
                return new Report(true, "");
            } else {
                return new Report(false, fehlertext);
            }
        } else if (instance.getGroupName().equals("Ins")) {
            if (!checkFeasibleTour(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not feasible since not all cities are visited.";
            }

            if (!checkCheapestInsertion(instance.getInstanz(), result.getSolutionIst(), instance.getOrder())) {
                correct = false;
                fehlertext += " The provided solution could not have been constructed by the cheapest insertion heuristic with the given order. Probably check if you respect the order in which the cities should be inserted and make sure that the cities are inserted at the currently best position.";
            }

            if (correct) {
                return new Report(true, "");
            } else {
                return new Report(false, fehlertext);
            }
        } else if (instance.getGroupName().equals("Ins+LS")) {
            if (!checkFeasibleTour(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not feasible since not all cities are visited.";
            }

            if (!checkLocalOptimum(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not a local optimum with respect to the given insert operator.";
            }

            if (correct) {
                return new Report(true, "");
            } else {
                return new Report(false, fehlertext);
            }
        } else if (instance.getGroupName().equals("STH")) {
            if (!checkFeasibleTour(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not feasible since not all cities are visited.";
            }

            if (!checkSpanningTreeHeuristic(instance.getInstanz(), result.getSolutionIst(), result.getMst(), instance.getStartCity())) {
                correct = false;
                fehlertext += " The provided solution could not have been constructed by the spanning tree heuristic. Probably check if you traverse the spanning tree correctly and if you choose the right start city.";
            }

            if (!checkStartCity(result.getSolutionIst(), instance.getStartCity())) {
                correct = false;
                int soll = instance.getStartCity();
                int ist = result.getSolutionIst().get(0);
                fehlertext += " The provided solution should start with city " + soll + " but actually starts with city " + ist + ".";
            }

            if (correct) {
                return new Report(true, "");
            } else {
                return new Report(false, fehlertext);
            }
        } else if (instance.getGroupName().equals("STH+LS")) {
            if (!checkFeasibleTour(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not feasible since not all cities are visited.";
            }

            if (!checkLocalOptimum(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not a local optimum with respect to the given insert operator.";
            }

            if (!checkLocalOptimum(instance.getInstanz(), result.getSolutionIst())) {
                correct = false;
                fehlertext += " The provided tour is not a local optimum with respect to the given insert operator.";
            }

            if (correct) {
                return new Report(true, "");
            } else {
                return new Report(false, fehlertext);
            }
        } else {
            return null;
        }
    }
}
