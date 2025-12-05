package main.java.framework;

public class StudentInformation {

    private String firstName;
    private String lastName;
    private String matriculationNumber;

    public StudentInformation(String firstName, String lastName, String matriculationNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.matriculationNumber = matriculationNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMatriculationNumber() {
        return matriculationNumber;
    }
}
