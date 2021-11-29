package commands;

import fileio.ActionInputData;
import fileio.UserInputData;

import java.util.ArrayList;

public class SerialGradesByUser {
    private ArrayList<Double> seasonsGrades = new ArrayList<>();
    private String username;

    public ArrayList<Double> getSeasonsGrades() {
        return seasonsGrades;
    }

    public void setSeasonsGrades(ArrayList<Double> seasonsGrades) {
        this.seasonsGrades = seasonsGrades;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double average() {
        Double sum = 0d;
        for(Double d : seasonsGrades) {
            sum += d;
        }
        return sum/seasonsGrades.size();
    }

    public void initialize(int totalNrSeasons) {
        for(int i = 0; i <= totalNrSeasons; ++i) {
            seasonsGrades.add(0d);
        }
    }
}
