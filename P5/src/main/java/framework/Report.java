package main.java.framework;

public class Report {

    private boolean correct;
    private String message;

    public Report(boolean correct, String message) {
        this.correct = correct;
        this.message = message;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getMessage() {
        return message;
    }

}
