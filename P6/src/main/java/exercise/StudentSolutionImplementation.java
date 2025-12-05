package main.java.exercise;

import main.java.framework.StudentInformation;
import main.java.framework.StudentSolution;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class StudentSolutionImplementation implements StudentSolution {
    @Override
    public StudentInformation provideStudentInformation() {
        return new StudentInformation(
                "Subair", // Vorname
                "Kirimow", // Nachname
                "12321260" // Matrikelnummer
        );
    }

    // Implementieren Sie hier Ihre Lösung für Nearest Neighbor
    public void nearestNeighbor(TSPInstance instance, TSPSolution solution, int startCity) {

        // The tour start with startCity
        solution.insertFirst(startCity);

        while (solution.getSolutionLength() < instance.getN()) {
            int nearestCity = -1; // Initialize to an invalid city index
            int minDistance = Integer.MAX_VALUE;

            int lastVisitedCity = solution.get(solution.getSolutionLength() - 1);

            // Find the nearest unvisited city
            for (int i = 0; i < instance.getN(); i++) {
                // Skip visited cities
                if (solution.isVisited(i)) {
                    continue;
                }

                int distance = instance.getDistance(lastVisitedCity, i);

                // Update nearest city if a shorter distance is found
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCity = i;
                }
            }

            solution.insertLast(nearestCity);
        }
    }


    // Implementieren Sie hier Ihre Lösung für Cheapest Insertion
    public void cheapestInsertion(TSPInstance instance, TSPSolution solution, int[] order) {

        // The Tour starts with the first city in order (at least at the beginning)
        solution.insertFirst(order[0]);

        // Insert the cities in the cheapest way, one by one
        for (int i = 1; i < order.length; i++) {

            int chosenCity = order[i];
            int cheapestCost = Integer.MAX_VALUE;
            int cheapestIdx = -1;

            // Now check all the insertion possibilities and the cost difference it would make
            for (int idx = 0; idx < solution.getSolutionLength(); idx++) {
                if(solution.deltaInsert(chosenCity, idx) < cheapestCost) {
                    cheapestCost = solution.deltaInsert(chosenCity, idx);
                    cheapestIdx = idx;
                }
            }

            solution.insert(chosenCity, cheapestIdx);
        }

    }


    // Implementieren Sie hier Ihre Lösung für Local Optimum
    public void localOptimum(TSPInstance instance, TSPSolution solution) {

        int tourLength = solution.getObjective();
        int idx_remove = 0;
        int no_change_count = 0;

        while(no_change_count < instance.getN()) {

             int deletedCity = solution.delete(idx_remove);
             int OneMissingCityTourLength = solution.getObjective();

             int best_idx_insert = -1;

             // Find best insertion for deleted city
             for (int idx_insert = 0; idx_insert < instance.getN(); idx_insert++) {

                 if(OneMissingCityTourLength + solution.deltaInsert(deletedCity, idx_insert) < tourLength) {

                     tourLength = OneMissingCityTourLength + solution.deltaInsert(deletedCity, idx_insert);
                     best_idx_insert = idx_insert;
                 }
             }

            // No improvement, so add the city back to its original place
             if(best_idx_insert == -1) {
                 solution.insert(deletedCity, idx_remove);
                 no_change_count++;

             } else {

                 solution.insert(deletedCity, best_idx_insert);
                 idx_remove--; // If change happened at position i, make it continue search at position i
                 no_change_count = 0;
             }

             // Algorithm will move to the next position unless a change happens
             idx_remove = (idx_remove + 1) % instance.getN();
        }
    }


    // Implementieren Sie hier Ihre Lösung für den Algorithmus von Prim
    public int prim(TSPInstance instance, PriorityQueue q, Graph mst) {

        int vertexAmount = instance.getN();
        int[] minimalCost = new int[vertexAmount];
        boolean[] inMST = new boolean[vertexAmount];

        // first city is gonna be city 0
        inMST[0] = true;
        for (int i = 1; i < vertexAmount; i++) {
            minimalCost[i] = instance.getDistance(0, i);
            q.add(minimalCost[i], i);
        }

        // Create MST
        while(!q.isEmpty()) {

            // Choose closest node to any node from MST
            int minimalCity = q.removeFirst();
            if(minimalCity == -1) break;

            inMST[minimalCity] = true;

            for (int i = 0; i < vertexAmount; i++) {
                if(i != minimalCity) {
                    if(inMST[i] && instance.getDistance(i, minimalCity) == minimalCost[minimalCity]) {

                        // MST might have multiple nodes with same cost, but the city
                        // should be connected to only one node of the current MST
                        if(mst.getNeighbors(minimalCity).length == 0) {
                            mst.addEdge(i, minimalCity, minimalCost[minimalCity]);
                        }

                    } else if(!inMST[i] && instance.getDistance(minimalCity, i) < minimalCost[i]) {

                        // If shorter routes to remaining nodes are found update them
                        minimalCost[i] = instance.getDistance(minimalCity, i);
                        q.decreaseWeight(minimalCost[i], i);
                    }
                }
            }
        }

        // Get the weight of MST
        int totalWeight = 0;
        int[][] edges = mst.getEdges();
        for (int i = 0; i < mst.numberOfEdges(); i++) {
            totalWeight += mst.getEdgeWeight(edges[i][0], edges[i][1]);
        }

        return totalWeight;
    }


    // Implementieren Sie hier Ihre Lösung für die Spanning Tree Heuristik
    public void spanningTreeHeuristic(TSPInstance instance, TSPSolution solution, Graph mst, int startCity) {

        // The tour will start with startCity
        solution.insert(startCity,0);
        int[] neighbours = mst.getNeighbors(startCity);

        int nextFreeIdx = 1;
        int prevCityIdx = 0;
        while(nextFreeIdx < mst.numberOfVertices()) {

            boolean change = false;

            // Check for free neighbour
            for(int neighbour : neighbours) {
                if (!solution.isVisited(neighbour)) {
                    solution.insert(neighbour, nextFreeIdx++);
                    neighbours = mst.getNeighbors(neighbour);
                    change = true;
                    break;
                }
            }

            // If no free neighbour was found, go back to the previous cities until one is found
            if(!change) {
                neighbours = mst.getNeighbors(solution.get(prevCityIdx));
                prevCityIdx--;
            } else {
                prevCityIdx = nextFreeIdx - 1;
            }
        }
    }
}