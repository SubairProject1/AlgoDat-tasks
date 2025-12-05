package main.java.framework;

import main.java.exercise.InstanceSetImplementation;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Exercise {

    public static void main(String[] args) {

        boolean runTestMode = false;
        if (args.length > 0 && args[0].equals("-test")) {
            runTestMode = true;
        }
        ArrayList<Path> instanceSetPaths = getInstanceSetPaths();
        Collections.sort(instanceSetPaths, new Comparator<Path>() {
            @Override
            public int compare(Path o1, Path o2) {
                if (o1.getFileName().toString().equals("abgabe.csv")) {
                    return -1;
                }
                if (o2.getFileName().toString().equals("abgabe.csv")) {
                    return 1;
                }
                return o1.getFileName().compareTo(o2.getFileName());
            }
        });

        if (!runTestMode) {
            Scanner in = new Scanner(System.in);

            boolean running = true;
            while (running) {
                Path instanceSetPath = requestOption(in, instanceSetPaths);
                if (instanceSetPath == null) {
                    running = false;
                } else {
                    Path outputPath = FileSystems.getDefault().getPath("results", "solution-" + instanceSetPath.getFileName().toString());
                    InstanceSet instanceSet = new InstanceSetImplementation(instanceSetPath, outputPath);
                    instanceSet.process();
                }
            }
            in.close();
            System.out.println("Good bye!");
        } else {
            InstanceSet instanceSet = new InstanceSetImplementation(instanceSetPaths.get(0), null);
            System.exit(instanceSet.process());
        }
    }

    private static ArrayList<Path> getInstanceSetPaths() {
        Path instanceSetsBasePath = FileSystems.getDefault().getPath("instance-sets/");

        ArrayList<Path> instanceSetPaths = new ArrayList<Path>();
        try {
            Files.walk(instanceSetsBasePath).forEach(instanceSetPath -> {
                if (instanceSetPath.getFileName().toString().endsWith(".csv")) {
                    instanceSetPaths.add(instanceSetPath);
                }
            });
            return instanceSetPaths;
        } catch(IOException e) {
            System.err.println("Path not found: " + instanceSetsBasePath);
            return instanceSetPaths;
        }
    }

    private static Path requestOption(Scanner scanner, ArrayList<Path> instanceSetPaths) {
        System.out.println("Select an instance set or exit:");
        for (int i = 0; i < instanceSetPaths.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + instanceSetPaths.get(i).getFileName());
        }
        System.out.println("[0] Exit");
        int option = -1;
        boolean validOption = false;
        String errorMessage = "Invalid input. Enter value between 0 and " + instanceSetPaths.size() + ".";
        while(!validOption) {
            while(!scanner.hasNextInt()) {
                scanner.next();
                System.err.println(errorMessage);
            }
            option = scanner.nextInt();
            validOption = 0 <= option && option <= instanceSetPaths.size();
            if (!validOption) {
                System.err.println(errorMessage);
            }
        }
        if (option == 0) {
            return null;
        } else {
            return instanceSetPaths.get(option - 1);
        }
    }
}
