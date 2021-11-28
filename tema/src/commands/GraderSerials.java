package commands;

import Usr.User;
import fileio.ActionInputData;
import fileio.UserInputData;

import java.util.ArrayList;

public class GraderSerials {
    private ArrayList<Double> seasonsGrades;
    private String username;

    public int addGradeAndCheck(int seasonNumber, Double grade, UserInputData user) {
        if(seasonsGrades.size() < seasonNumber) {
            for (int i = seasonsGrades.size(); i < seasonNumber; ++i) {
                seasonsGrades.add(-1d);
            }
            seasonsGrades.add(grade);
            username = user.getUsername();
            return 1;
        }
        else {
            if(seasonsGrades.get(seasonNumber) == -1) {
                seasonsGrades.set(seasonNumber, grade);
                username = user.getUsername();
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}
