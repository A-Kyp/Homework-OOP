package actions.commands;

import actions.MovieIndexFinder;
import actions.SerialIndexFinder;
import actions.UserIndexFinder;
import fileio.ActionInputData;
import fileio.Input;
import fileio.UserInputData;

import java.util.ArrayList;

public class UserCommands {
    public String view(UserInputData user, String title) {

        if(user.getHistory().containsKey(title)) {
            user.getHistory().put(title, user.getHistory().get(title) +1);
        }
        else {
            user.getHistory().put(title, 1);
        }

        return "success -> " + title + " was viewed with total views of " + user.getHistory().get(title);
    }

    private int hasSeen(String title, UserInputData user) {
        if(user.getHistory().containsKey(title)) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public String favorite(UserInputData user , String title) {
        if(hasSeen(title, user) == 1) {
            if(!user.getFavoriteMovies().contains(title)) {
                user.getFavoriteMovies().add(title);
            }
            else {
                return "error -> " + title + " is already in favourite list";
            }
        }
        else{
            return "error -> " + title + " is not seen";
        }
        return "success -> " + title + " was added as favourite";
    }

    MovieIndexFinder mIndex = new MovieIndexFinder();
    UserIndexFinder uIndex = new UserIndexFinder();
    SerialIndexFinder sIndex = new SerialIndexFinder();

    public String grade(Input input, ActionInputData actionInputData, Grader grader, int usrIndex, int filmIndex) {
        if(hasSeen(actionInputData.getTitle(), input.getUsers().get(usrIndex)) == 1) {
            if (grader.getFilmRateOnce().get(filmIndex).get(usrIndex) != null) {
                /*Keep track of the grades of different films*/
                if (grader.getFilmGrades().containsKey(actionInputData.getTitle())) {
                    grader.getFilmGrades().get(actionInputData.getTitle()).add(actionInputData.getGrade());
                } else {
                    ArrayList<Double> arr = new ArrayList<>();
                    arr.add(actionInputData.getGrade());
                    grader.getFilmGrades().put(actionInputData.getTitle(), arr);
                }

                /*Keep track of the user activity*/
                if (grader.getFilmUserActivity().containsKey(actionInputData.getUsername())) {
                    grader.getFilmUserActivity().put(actionInputData.getUsername(),
                            grader.getFilmUserActivity().get(actionInputData.getUsername()) + 1);
                }
                else {
                    grader.getFilmUserActivity().put(actionInputData.getUsername(), 1);
                }

                /*Mark that the user has rated the film*/
                grader.getFilmRateOnce().get(filmIndex).add(usrIndex, Boolean.TRUE);
            }
        }
        else {
            return "error -> " + actionInputData.getTitle() + " is not seen";
        }
        return "success -> " + actionInputData.getTitle()
                + " was rated with " + actionInputData.getGrade()
                + " by " + actionInputData.getUsername();
    }

    public String gradeSerial(ActionInputData a, UserInputData user, Grader grd, int tSeasons) {
        if(hasSeen(a.getTitle(), user) == 0) {
            return "error -> " + a.getTitle() + " is not seen";
        }

        if (grd.addAndGradeSerial(a.getTitle(), a, user, a.getSeasonNumber(), tSeasons) == 0) {
            return "error " + a.getTitle() + " has already been rated";
        }

        /*Keep track of the user activity*/
        if (grd.getFilmUserActivity().containsKey(a.getUsername())) {
            grd.getFilmUserActivity().put(a.getUsername(),
                    grd.getFilmUserActivity().get(a.getUsername()) + 1);
        } else {
            grd.getFilmUserActivity().put(a.getUsername(), 1);
        }
        return "success -> " + a.getTitle()
                + " was rated with " + a.getGrade()
                + " by " + a.getUsername();

    }

    public String execute(Input in, ActionInputData a, UserInputData u, Grader grd) {
        if(a.getType().equals("view")) {
            return view(u, a.getTitle());
        }
        else if(a.getType().compareTo("favorite") == 0) {
            return favorite(u, a.getTitle());
        }
        else if(a.getType().equals("rating")) {
            if(mIndex.getIndex(a.getTitle(), in)  != -1) {
                return grade(in, a, grd, uIndex.getindex(a.getUsername(), in),
                        mIndex.getIndex(a.getTitle(), in));
            }
            else
                return gradeSerial(a, in.getUsers().get(uIndex.getindex(a.getUsername(), in)),
                        grd, in.getSerials().get(sIndex.getIndex(a.getTitle(), in)).getNumberSeason());
        }
        return "error " + a.getTitle() + " has already been rated";
    }
}
