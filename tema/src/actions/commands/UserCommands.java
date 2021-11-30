package actions.commands;

import actions.IndexFinders.MovieIndexFinder;
import actions.IndexFinders.SerialIndexFinder;
import actions.IndexFinders.UserIndexFinder;
import fileio.ActionInputData;
import fileio.Input;
import fileio.UserInputData;

import java.util.HashMap;

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

    public int hasSeen(String title, UserInputData user) {
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

    public String gradeM(Input in, ActionInputData a, Grader grd, int usrIndex, int filmIndex) {
        if(hasSeen(a.getTitle(), in.getUsers().get(usrIndex)) == 1){
            /*Check if the movie was already rated by others users*/
            if(grd.getRatedFilms().containsKey(a.getTitle())) {
                /*Check if the user has already rated*/
                if(grd.getRatedFilms().get(a.getTitle()).containsKey(a.getUsername())) {
                    /*Cheating!!!!! The users has already graded!!!*/
                    return "error -> " + a.getTitle() + " has been already rated";
                }
                else{
                    /*We can add the grade safely*/
                    grd.getRatedFilms().get(a.getTitle()).put(a.getUsername(), a.getGrade());

                    /*Keep track of the user activity*/
                    if (grd.getFilmUserActivity().containsKey(a.getUsername())) {
                        grd.getFilmUserActivity().put(a.getUsername(),
                                grd.getFilmUserActivity().get(a.getUsername()) + 1d);
                    }
                    else {
                        grd.getFilmUserActivity().put(a.getUsername(), 1d);
                    }

                    return "success -> " + a.getTitle()
                            + " was rated with " + a.getGrade()
                            + " by " + a.getUsername();
                }
            }
            else {
                /*We need to add a new entry for movie*/
                grd.getRatedFilms().put(a.getTitle(),new HashMap<String,Double>());
                grd.getRatedFilms().get(a.getTitle()).put(a.getUsername(),a.getGrade());

                /*Keep track of the user activity*/
                if (grd.getFilmUserActivity().containsKey(a.getUsername())) {
                    grd.getFilmUserActivity().put(a.getUsername(),
                            grd.getFilmUserActivity().get(a.getUsername()) + 1d);
                }
                else {
                    grd.getFilmUserActivity().put(a.getUsername(), 1d);
                }

                return "success -> " + a.getTitle()
                        + " was rated with " + a.getGrade()
                        + " by " + a.getUsername();
            }
        }
        else {
            return "error -> " + a.getTitle() + " is not seen";
        }
    }

    public String gradeS(ActionInputData a, UserInputData user, Grader grd, int tSeasons) {
        if(hasSeen(a.getTitle(), user) == 0) {
            return "error -> " + a.getTitle() + " is not seen";
        }

        if (grd.addAndGradeSerial(a.getTitle(), a, user, a.getSeasonNumber(), tSeasons) == 0) {
            return "error " + a.getTitle() + " has already been rated";
        }

        /*Keep track of the user activity*/
        if (grd.getFilmUserActivity().containsKey(a.getUsername())) {
            grd.getFilmUserActivity().put(a.getUsername(),
                    grd.getFilmUserActivity().get(a.getUsername()) + 1d);
        } else {
            grd.getFilmUserActivity().put(a.getUsername(), 1d);
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
                return gradeM(in, a, grd, uIndex.getindex(a.getUsername(), in),
                        mIndex.getIndex(a.getTitle(), in));
            }
            else
                return gradeS(a, in.getUsers().get(uIndex.getindex(a.getUsername(), in)),
                        grd, in.getSerials().get(sIndex.getIndex(a.getTitle(), in)).getNumberSeason());
        }
        return "error " + a.getTitle() + " has already been rated";
    }
}
