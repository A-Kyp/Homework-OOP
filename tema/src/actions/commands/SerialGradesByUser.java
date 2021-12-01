package actions.commands;

import java.util.ArrayList;

public final class SerialGradesByUser {
    private final ArrayList<Double> seasonsGrades = new ArrayList<>();
    private String username;

    public ArrayList<Double> getSeasonsGrades() {
        return seasonsGrades;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return the average grade given by a single user for the SERIAL
     */
    public Double average() {
        Double sum = 0d;
        for (Double d : seasonsGrades) {
            sum += d;
        }
        return sum / (seasonsGrades.size() - 1);
    }

    /**
     * Initialize a vector with a SERIAL's number of seasons element
     * to be used as storage for the user rate for each season
     * v(i) -> the grade for the i-th season given by a single user
     *
     * @param totalNrSeasons SERIAL total number of seasons
     */
    public void initialize(final int totalNrSeasons) {
        for (int i = 0; i <= totalNrSeasons; ++i) {
            seasonsGrades.add(0d);
        }
    }
}
