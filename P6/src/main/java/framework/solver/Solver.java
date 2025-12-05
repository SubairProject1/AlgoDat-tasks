package main.java.framework.solver;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class Solver {


    public Solver() {
    }

    public String solve(String dimacs) {
        DimacsReader reader = new EmptyClauseDimacsReader(SolverFactory.newDefault());
        StringWriter modelToStringWriter = new StringWriter();
        PrintWriter modelWriter = new PrintWriter(modelToStringWriter);
        try {
            IProblem problem = reader.parseInstance(new ByteArrayInputStream(dimacs.getBytes(StandardCharsets.UTF_8)));
            if (problem.isSatisfiable()) {
                reader.decode(problem.model(), modelWriter);
                return modelToStringWriter.toString();
            }
        } catch (ParseFormatException e) {
            System.err.println("Parsing error in solver: " + e.getMessage());
            return "";
        } catch (ContradictionException e) {
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return "";
    }
}
