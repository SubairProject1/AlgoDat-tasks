package main.java.framework.solver;

import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

import java.io.IOException;

public class EmptyClauseDimacsReader extends DimacsReader {

    public EmptyClauseDimacsReader(ISolver solver) {
        super(solver);
    }

    public EmptyClauseDimacsReader(ISolver solver, String format) {
        super(solver, format);
    }

    @Override
    protected boolean handleLine() throws ContradictionException, IOException, ParseFormatException {
        boolean added = false;

        while(!this.scanner.eof()) {
            int lit = this.scanner.nextInt();
            if (lit == 0) {
                // REMOVES CONDITION TO ALWAYS ADD A NEW CLAUSE - THIS CAUSES EMPTY CLAUSES TO BE ADDED
                this.flushConstraint();
                this.literals.clear();
                added = true;
                break;
            }

            this.literals.push(lit);
        }

        return added;
    }
}
