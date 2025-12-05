package main.java.exercise;

import main.java.framework.InstanceSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class InstanceSetImplementation extends InstanceSet<InstanceImplementation, StudentSolutionImplementation, ResultImplementation, VerifierImplementation, Object> {

    Map<String, TSPInstance> instanzen;  // Instanzen
    int[][] permutationen;

    public InstanceSetImplementation(Path instanceSetPath, Path outputPath) {
        super(instanceSetPath, outputPath, ResultImplementation.class);

        permutationen = readPermutationen();
        instanzen = new HashMap();
    }

    @Override
    protected InstanceImplementation instanceFromCsv(String line) {
        String[] splitLine = line.split(",");
        int number = Integer.parseInt(splitLine[0]);
        String problemType = splitLine[1];
        String instanzname = splitLine[2];
        int durchlauf = Integer.parseInt(splitLine[3]);
        int valueSoll = Integer.parseInt(splitLine[4]);
        String solstring = splitLine[5];

        if (!instanzen.containsKey(instanzname)) {
            instanzen.put(instanzname, readInstance(instanzname, readOptvalue(instanzname)));
        }

        TSPInstance instance = instanzen.get(instanzname);

        if (problemType.equals("NN") || problemType.equals("NN+LS")) {
            TSPSolution solutionStart = null;
            int[] order = null;
            int startCity = getStartCity(durchlauf, instance.getN());

            return new InstanceImplementation(problemType, number, instance, durchlauf, valueSoll, solutionStart, order, startCity);
        } else if (problemType.equals("Ins") || problemType.equals("Ins+LS")) {
            TSPSolution solutionStart = null;
            int[] order = getOrder(permutationen, durchlauf, instance.getN());
            int startCity = -1;

            return new InstanceImplementation(problemType, number, instance, durchlauf, valueSoll, solutionStart, order, startCity);
        } else if (problemType.equals("STH")  || problemType.equals("STH+LS")) {
            TSPSolution solutionStart = null;
            int[] order = null;
            int startCity = getStartCity(durchlauf, instance.getN());

            return new InstanceImplementation(problemType, number, instance, durchlauf, valueSoll, solutionStart, order, startCity);
        } else if (problemType.equals("LS")) {
            TSPSolution solutionStart = getTSPSolution(instance, solstring);
            int[] order = null;
            int startCity = -1;

            return new InstanceImplementation(problemType, number, instance, durchlauf, valueSoll, solutionStart, order, startCity);
        } else if (problemType.equals("MST")) {
            TSPSolution solutionStart = null;
            int[] order = null;
            int startCity = -1;

            return new InstanceImplementation(problemType, number, instance, durchlauf, valueSoll, solutionStart, order, startCity);
        }
        return null;
    }

    @Override
    protected StudentSolutionImplementation provideStudentSolution() {
        return new StudentSolutionImplementation();
    }

    @Override
    protected VerifierImplementation provideVerifier() {
        return new VerifierImplementation();
    }


    public int getStartCity(int durchlauf, int n) {
        int[] permtemp = permutationen[0];
        int i = 0;
        for (int j = 0; j < permtemp.length; j++) {
            if (permtemp[j] < n) {
                if (durchlauf == i) {
                    return permtemp[j];
                }
                i++;
            }
        }
        return -1;
    }

    public static int[] getOrder(int[][] permutationen, int durchlauf, int n) {
        int[] res = new int[n];
        int i = 0;
        int[] permtemp = permutationen[durchlauf];
        for (int j = 0; j < permtemp.length; j++) {
            if (permtemp[j] < n) {
                res[i] = permtemp[j];
                i++;
            }
            if (i >= n) {
                break;
            }
        }
        return res;
    }

    private TSPSolution getTSPSolution(TSPInstance instanz, String solstring) {
        if (solstring.equals("") || solstring.equals("na") || solstring.equals("NA")) {
            return null;
        }

        String[] temp = solstring.split(" ");
        int[] res = new int[temp.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = Integer.parseInt(temp[i]);
        }

        TSPSolution sol = new TSPSolution(instanz);
        for (int i = 0; i < res.length; i++) {
            sol.insertLast(res[i]);
        }

        return sol;
    }


    public int[][] readPermutationen() {
        Path pfad = FileSystems.getDefault().getPath("additional-input", "permutations.txt");
        BufferedReader br;
        try {
            br = Files.newBufferedReader(pfad);
            int anz = Integer.parseInt(br.readLine());
            int[][] res = new int[anz][];

            for (int k = 0; k < anz; k++) {
                String[] splitLine = br.readLine().split(" ");

                res[k] = new int[splitLine.length];
                for (int i = 0; i < res[k].length; i++) {
                    res[k][i] = Integer.parseInt(splitLine[i]);
                }
            }

            return res;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int readOptvalue(String instanzname) {
        Path path = FileSystems.getDefault().getPath("additional-input", instanzname + ".optvalue");
        BufferedReader br;
        try {
            br = Files.newBufferedReader(path);
            return Integer.parseInt(br.readLine());
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public TSPInstance readInstance(String instanzname, int optvalue) {
        Path path = FileSystems.getDefault().getPath("additional-input", instanzname + ".tsp");
        BufferedReader br;
        try {
            int n = -1;
            String name = "";
            String edgeWeightType = "";

            br = Files.newBufferedReader(path);
            String line;

            while (true) {
                line = br.readLine();
                if (line.contains("NAME")) {
                    name = line.split(":")[1].trim();
                    break;
                }
            }

            while (true) {
                line = br.readLine();
                if (line.contains("DIMENSION")) {
                    String temp = line.split(":")[1];
                    n = Integer.parseInt(temp.trim());
                    break;
                }
            }

            while (true) {
                line = br.readLine();
                if (line.contains("EDGE_WEIGHT_TYPE")) {
                    edgeWeightType = line.split(":")[1].trim();
                    break;
                }
            }

            while (true) {
                line = br.readLine();
                if (line.contains("NODE_COORD_SECTION")) {
                    break;
                }
            }

            // Ab hier kommen die nodes
            int[][] dist = new int[n][n];
            double[] xCoords = new double[n];
            double[] yCoords = new double[n];
            int i = 0;

            while (true) {
                line = br.readLine().trim();;
                if (line.contains("EOF")) {
                    break;
                }

                String[] info = line.split(" +");

                xCoords[i] = Double.parseDouble(info[1].trim());
                yCoords[i] = Double.parseDouble(info[2].trim());
                i++;
            }

            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (j == k) {
                        dist[j][k] = 0;
                        continue;
                    }
                    dist[j][k] = InstanceSetImplementation.computeDistance(xCoords, yCoords, j, k, edgeWeightType);
                }
            }

            return new TSPInstance(name, n, dist, optvalue, xCoords, yCoords);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static int computeDistance(double[] x, double[] y, int from, int to, String edgeWeightType) {
        if (edgeWeightType.equals("EUC_2D")) {
            double xd = x[from] - x[to];
            double yd = y[from] - y[to];
            double res = Math.sqrt( xd*xd + yd*yd );
            return (int) (res + 0.5);
        }
        return -1;
    }




    @Override
    protected Integer[] parseAdditionalInput(BufferedReader reader) {
        return null;
    }
}
