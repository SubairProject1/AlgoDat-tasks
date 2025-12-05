package main.java.framework;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class InstanceSet<InstanceT extends Instance, StudentSolutionT extends StudentSolution, ResultT extends Result, VerifierT extends Verifier<? super InstanceT, ? super StudentSolutionT, ResultT>, AdditionalInputT> {

    private Path instanceSetPath;
    private Path outputPath;
    private Class<ResultT> resultClass;
    private Map<String, AdditionalInputT> additionalInputs = new HashMap<String, AdditionalInputT>();

    protected InstanceSet(Path instanceSetPath, Path outputPath, Class<ResultT> resultClass) {
        this.instanceSetPath = instanceSetPath;
        this.outputPath = outputPath;
        this.resultClass = resultClass;
    }

    public int process() {
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(this.instanceSetPath);
        } catch (IOException e) {
            System.err.println("Error while reading instance set");
            return 2;
        }
        StudentSolutionT studentSolution = this.provideStudentSolution();
        StudentInformation studentInformation = studentSolution.provideStudentInformation();
        VerifierT verifier = this.provideVerifier();
        ResultsContainer<ResultT> results = new ResultsContainer<ResultT>(resultClass);
        String line;
        try {
            while((line = reader.readLine()) != null) {
                InstanceT instance = this.instanceFromCsv(line);
                System.out.printf("Running instance %5d - Group: %s\n", instance.getNumber(), instance.getGroupName());
                ResultT result = verifier.solveProblemUsingStudentSolution(instance, studentSolution);
                Report report = verifier.verifyResult(instance, result);
                if (!report.isCorrect()) {
                    System.err.println(report.getMessage());
                    results = null;
                    break;
                } else {
                    results.addResultContainer(new ResultContainer<ResultT>(instance.getGroupName(), instance.getNumber(), result));
                }
            }
        } catch (IOException e) {
            System.err.println("Error while reading instance");
        }
        if (this.outputPath != null && results != null) {
            List<String> csv = new ArrayList<String>();
            csv.add("#" + instanceSetPath.getFileName());
            csv.add("#" + studentInformation.getFirstName());
            csv.add("#" + studentInformation.getLastName());
            csv.add("#" + studentInformation.getMatriculationNumber());
            csv.addAll(results.toCsv());
            this.persistResults(this.outputPath, csv);
        }
        return results != null ? 0 : 1;
    }

    private void persistResults(Path path, List<String> lines) {
        if(!Files.exists(path.getParent())) {
            System.err.println("Output path does not exist");
            return;
        }

        if(Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                System.err.println("Previous solution could not be deleted");
                return;
            }
        }

        try {
            Files.write(path, lines);
        } catch (IOException e) {
            // throw new IOException(Prompt.getFileNotFoundMessage(solFolder + path.getFileName()));
        }

        System.out.println("Speichere Loesung in:\n " + path.toAbsolutePath());
    }

    protected AdditionalInputT getAdditionalInput(String additionalInputName) {
        if (!this.additionalInputs.containsKey(additionalInputName)) {
            Path additionalInputPath = FileSystems.getDefault().getPath("additional-input", additionalInputName);
            BufferedReader additionalInputReader;
            try {
                additionalInputReader = Files.newBufferedReader(additionalInputPath);
                AdditionalInputT additionalInput = this.parseAdditionalInput(additionalInputReader);
                this.additionalInputs.put(additionalInputName, additionalInput);
                return additionalInput;
            } catch (IOException e) {
                System.err.println("Error while reading additional input");
                return null;
            }
        }
        return this.additionalInputs.get(additionalInputName);
    }

    protected abstract InstanceT instanceFromCsv(String line);

    protected abstract StudentSolutionT provideStudentSolution();

    protected abstract VerifierT provideVerifier();

    protected abstract AdditionalInputT parseAdditionalInput(BufferedReader reader);

}
