package actions.commands;

import java.util.ArrayList;

public class SerialsGrader {
    private String title;
    private ArrayList<SerialGradesByUser> ratings = new ArrayList<>();

    public SerialsGrader(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<SerialGradesByUser> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<SerialGradesByUser> ratings) {
        this.ratings = ratings;
    }

    public Double calculateGrade() {
        double sum = 0d;
        for(SerialGradesByUser g : ratings) {
            sum += g.average();
        }
        return sum/ratings.size();
    }
}
