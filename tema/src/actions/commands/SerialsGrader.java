package actions.commands;

import java.util.ArrayList;

public final class SerialsGrader {
    private String title;
    private ArrayList<SerialGradesByUser> ratings = new ArrayList<>();

    public SerialsGrader(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public ArrayList<SerialGradesByUser> getRatings() {
        return ratings;
    }

    public void setRatings(final ArrayList<SerialGradesByUser> ratings) {
        this.ratings = ratings;
    }

    /**
     * @return Overall grade for a SERIAL
     *          the average of each user rate for the SERIAL
     */
    public Double calculateGrade() {
        double sum = 0d;
        for (SerialGradesByUser g : ratings) {
            sum += g.average();
        }
        return sum / ratings.size();
    }
}
