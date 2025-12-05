package main.java.exercise;

public class TSPSolution {

    private int solutionLength;  // number of inserted cities
    private int objective;
    private int[] solution;
    private boolean[] visited;
    private TSPInstance instance;

    public TSPSolution(TSPInstance instance) {
        this.solution = new int[instance.getN()];
        for (int i = 0; i < solution.length; i++) {
            this.solution[i] = -1;
        }
        this.visited = new boolean[instance.getN()];
        this.objective = 0;
        this.solutionLength = 0;
        this.instance = instance;
    }

    public int get(int index) {
        return solution[index];
    }

    public int getIndexOfCity(int city) {
        if (!visited[city]) {
            return -1;
        }

        for (int i = 0; i < getSolutionLength(); i++) {
            if (solution[i] == city) {
                return i;
            }
        }

        return -1;
    }

    public int getSolutionLength() {
        return solutionLength;
    }

    public int getObjective() {
        return objective;
    }

    public boolean isVisited(int city) {
        return visited[city];
    }

    /**
     * Inserts city at place index
     */
    public boolean insert(int city, int index) {
        if (visited[city] || index > solutionLength) {
            return false;
        }

        objective += deltaInsert(city, index);

        for (int j = solutionLength; j > index; j--) {
            solution[j] = solution[j-1];
        }
        solution[index] = city;
        visited[city] = true;
        solutionLength++;
        return true;
    }

    public boolean insertLast(int city) {
        return insert(city, solutionLength);
    }

    public boolean insertFirst(int city) {
        return insert(city, 0);
    }


    public int delete(int index) {
        if (index >= solutionLength) {
            return -1;
        }

        objective += deltaDelete(index);
        int city = solution[index];
        visited[city] = false;
        for (int i = index; i < solutionLength - 1; i++) {
            solution[i] = solution[i+1];
        }
        solution[solutionLength - 1] = -1;
        solutionLength--;
        return city;
    }


    public int deleteCity(int city) {
        if (!visited[city]) {
            return -1;
        }
        for (int i = 0; i < solutionLength; i++) {
            if (solution[i] == city) {
                delete(i);
                return i;
            }
        }
        return -1;
    }


    public int deltaDelete(int index) {
        if (index >= solutionLength || solutionLength <= 1) {
            return 0;
        }

        // at least 2 cities in current solution

        int city = solution[index];
        int leftCity, rightCity;

        if (index == 0) {
            leftCity = solution[solutionLength - 1];
            rightCity = solution[index + 1];
        }
        else if (index == solutionLength - 1) {
            leftCity = solution[solutionLength -2];
            rightCity = solution[0];
        }
        else {
            leftCity = solution[index - 1];
            rightCity = solution[index + 1];
        }

        int distA = instance.getDistance(leftCity, city);
        int distB = instance.getDistance(city, rightCity);
        int distC = instance.getDistance(leftCity, rightCity);
        return -distA - distB + distC;
    }


    public int deltaInsert(int city, int index) {
        if (visited[city] || index > solutionLength || solutionLength == 0) {
            return 0;
        }

        int leftCity, rightCity;

        if (index == 0) {
            leftCity = solution[solutionLength - 1];
            rightCity = solution[index];
        }
        else if (index == solutionLength) {
            leftCity = solution[solutionLength -1];
            rightCity = solution[0];
        }
        else {
            leftCity = solution[index - 1];
            rightCity = solution[index];
        }

        int distA = instance.getDistance(leftCity, city);
        int distB = instance.getDistance(city, rightCity);
        int distC = instance.getDistance(leftCity, rightCity);
        return distA + distB - distC;
    }


    public TSPSolution copy() {
        TSPSolution res = new TSPSolution(instance);

        res.solutionLength = this.solutionLength;
        res.objective = this.objective;

        res.solution = new int[this.solution.length];
        for (int i = 0; i < res.solution.length; i++) {
            res.solution[i] = this.solution[i];
        }

        res.visited = new boolean[this.visited.length];
        for (int i = 0; i < res.visited.length; i++) {
            res.visited[i] = this.visited[i];
        }

        return res;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of Cities in current Solution: " + solutionLength + " out of " + instance.getN() + "\n");
        sb.append(solution[0]);
        for (int i = 1; i < solutionLength; i++) {
            sb.append(" - " + solution[i]);
        }
        return sb.toString();
    }

    public void print() {
        System.out.println(this.toString());
    }
}
