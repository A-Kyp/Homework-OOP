package actions.commands;

import actions.indexFinders.MovieIndexFinder;
import actions.indexFinders.SerialIndexFinder;
import actions.indexFinders.UserIndexFinder;
import fileio.ActionInputData;
import fileio.Input;
import fileio.UserInputData;

import java.util.HashMap;

public class UserCommands {
    private final MovieIndexFinder mIndex = new MovieIndexFinder();
    private final UserIndexFinder uIndex = new UserIndexFinder();
    private final SerialIndexFinder sIndex = new SerialIndexFinder();

    /**
     * @return the result of a view command
     */
    private String view(final UserInputData user, final String title) {

        if (user.getHistory().containsKey(title)) {
            user.getHistory().put(title, user.getHistory().get(title) + 1);
        } else {
            user.getHistory().put(title, 1);
        }
        return "success -> " + title + " was viewed with total views of "
                + user.getHistory().get(title);
    }

    /**
     * @return 1 for already seen VIDEO
     *         0 for unseen VIDEO
     */
    public int hasSeen(final String title, final UserInputData user) {
        if (user.getHistory().containsKey(title)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @return the result of a favourite command
     */
    private String favorite(final UserInputData user, final String title) {
        if (hasSeen(title, user) == 1) {
            if (!user.getFavoriteMovies().contains(title)) {
                user.getFavoriteMovies().add(title);
            } else {
                return "error -> " + title + " is already in favourite list";
            }
        } else {
            return "error -> " + title + " is not seen";
        }
        return "success -> " + title + " was added as favourite";
    }

    /**
     * @return the result of a rate MOVIE command
     */
    private String gradeM(final Input in, final ActionInputData a, final Grader grd,
                         final int usrIndex) {
        if (hasSeen(a.getTitle(), in.getUsers().get(usrIndex)) == 1) {
            /*Check if the movie was already rated by others users*/
            if (grd.getRatedFilms().containsKey(a.getTitle())) {
                /*Check if the user has already rated*/
                if (grd.getRatedFilms().get(a.getTitle()).containsKey(a.getUsername())) {
                    /*Cheating!!!!! The users has already graded!!!*/
                    return "error -> " + a.getTitle() + " has been already rated";
                } else {
                    /*We can add the grade safely*/
                    grd.getRatedFilms().get(a.getTitle()).put(a.getUsername(), a.getGrade());

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
            } else {
                /*We need to add a new entry for movie*/
                grd.getRatedFilms().put(a.getTitle(), new HashMap<>());
                grd.getRatedFilms().get(a.getTitle()).put(a.getUsername(), a.getGrade());

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
        } else {
            return "error -> " + a.getTitle() + " is not seen";
        }
    }

    /**
     * @return the result of a rate SERIAL command
     */
    private String gradeS(final ActionInputData a, final UserInputData user, final Grader grd,
                         final int tSeasons) {
        if (hasSeen(a.getTitle(), user) == 0) {
            return "error -> " + a.getTitle() + " is not seen";
        }

        if (grd.addAndGradeSerial(a.getTitle(), a, user, a.getSeasonNumber(), tSeasons) == 0) {
            return "error -> " + a.getTitle() + " has been already rated";
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

    /**
     * The method that is called whenever a user command is to be executed
     * @return the result of the command
     */
    public String execute(final Input in, final ActionInputData a, final UserInputData u,
                          final Grader grd) {
        if (a.getType().equals("view")) {
            return view(u, a.getTitle());
        } else if (a.getType().compareTo("favorite") == 0) {
            return favorite(u, a.getTitle());
        } else if (a.getType().equals("rating")) {
            if (mIndex.getIndex(a.getTitle(), in)  != -1) {
                return gradeM(in, a, grd, uIndex.getIndex(a.getUsername(), in));
            } else {
                return gradeS(a, in.getUsers().get(uIndex.getIndex(a.getUsername(), in)), grd,
                        in.getSerials().get(sIndex.getIndex(a.getTitle(), in)).getNumberSeason());
            }
        }
        return "error " + a.getTitle() + " has been already rated";
    }
}
