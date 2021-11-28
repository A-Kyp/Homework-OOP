package commands;

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
        if(user.getHistory().containsKey(title)) {
//        if(hasSeen(title, user) == 1) {
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

    private int getIndexOfMovie(String title, Input input) {
        for(int i = 0; i < input.getMovies().size(); ++i) {
            if(input.getMovies().get(i).getTitle().equals(title)) {
                return i; //return the index of the film
            }
        }
        return -1; // or -1 if the film does not exist -> it must be a serial
    }

    private int getIndexOfUser(String name, Input input) {
        for(int i = 0; i < input.getUsers().size(); ++i) {
            if(input.getUsers().get(i).getUsername().equals(name)) {
                return i; //return the index of the user
            }
        }
        return -1; // or -1 if the user does not exist
    }

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

    public String gradeSerial(ActionInputData actionInputData, UserInputData user, Grader grd) {
        if(user.getHistory().containsKey(actionInputData.getTitle())) {
            for(GraderSerials s : grd.getSerialGrads()) {
                if(s.addGradeAndCheck(actionInputData.getSeasonNumber(),actionInputData.getGrade(), user) == 0) {
                    return "error " + actionInputData.getTitle() + " has already been rated";
                }
            }

            /*Keep track of the user activity*/
            if (grd.getFilmUserActivity().containsKey(actionInputData.getUsername())) {
                grd.getFilmUserActivity().put(actionInputData.getUsername(),
                        grd.getFilmUserActivity().get(actionInputData.getUsername()) + 1);
            }
            else {
                grd.getFilmUserActivity().put(actionInputData.getUsername(), 1);
            }
            return "success -> " + actionInputData.getTitle()
                    + " was rated with " + actionInputData.getGrade()
                    + " by " + actionInputData.getUsername();
        }
        else {
            return "error -> " + actionInputData.getTitle() + " is not seen";
        }
    }

    public String execute(Input in, ActionInputData action, UserInputData u, Grader grd) {
        if(action.getType().equals("view")) {
            return view(u, action.getTitle());
        }
        else if(action.getType().compareTo("favorite") == 0) {
            return favorite(u, action.getTitle());
        }
        else if(action.getType().equals("rating")) {
            if(getIndexOfMovie(action.getTitle(), in)  != -1) {
                return grade(in, action, grd, getIndexOfUser(action.getUsername(), in),
                        getIndexOfMovie(action.getTitle(), in));
            }
            else return gradeSerial(action, in.getUsers().get(getIndexOfUser(action.getUsername(), in)), grd);
        }
        return "error " + action.getTitle() + " has already been rated";
    }
}
