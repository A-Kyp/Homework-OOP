package actions.recommendation;

import actions.calculators.FavorabilityCalculator;
import actions.calculators.GenrePopularityCalculator;
import actions.indexFinders.UserIndexFinder;
import actions.Sorter;
import actions.commands.Grader;
import actions.commands.SerialsGrader;
import actions.commands.UserCommands;
import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.LinkedHashMap;
import java.util.Map;

public class Recommendation {
    private final RecommBuilder rb = new RecommBuilder();
    private final UserIndexFinder ui = new UserIndexFinder();

    /**
     * @return the result of a standard recommendation
     */
    public String standard(final Input in, final UserCommands comm, final UserInputData u) {
        for (MovieInputData m : in.getMovies()) {
            if (comm.hasSeen(m.getTitle(), u) == 0) {
                return rb.buildRec(m.getTitle(), 's', 1);
            }
        }
        for (SerialInputData serial : in.getSerials()) {
            if (comm.hasSeen(serial.getTitle(), u) == 0) {
                return rb.buildRec(serial.getTitle(), 's', 1);
            }
        }
        return rb.buildRec("", 's', 0);
    }

    /**
     * @return the result of a bestUnseen recommendation
     */
    public String bestUnseen(final Input in, final UserCommands comm, final UserInputData u,
                             final Grader grd) {
        Sorter sorter = new Sorter();
        LinkedHashMap<String, Double> result;

        result = grd.getRateForFilm();

        result.putAll(grd.getRateForSerial());

        result = sorter.sortNAlf(result);

        for (Map.Entry<String, Double> e : result.entrySet()) {
            if (comm.hasSeen(e.getKey(), u) == 0) {
                return rb.buildRec(e.getKey(), 'b', 1);
            }
        }

        /*Check for unrated videos*/
        for (MovieInputData m : in.getMovies()) {
            if (!result.containsKey(m.getTitle()) && comm.hasSeen(m.getTitle(), u) == 0) {
                return rb.buildRec(m.getTitle(), 'b', 1);
            }
        }

        for (SerialInputData m : in.getSerials()) {
            if (!result.containsKey(m.getTitle()) && comm.hasSeen(m.getTitle(), u) == 0) {
                return rb.buildRec(m.getTitle(), 'b', 1);
            }
        }

        return rb.buildRec("", 'b', 0);
    }

    /**
     * @return the result of a popular recommendation
     */
    public String popular(final Input in, final UserCommands comm, final UserInputData u) {
        if (!u.getSubscriptionType().equals("BASIC")) {
            LinkedHashMap<String, Double> result = new LinkedHashMap<>();
            GenrePopularityCalculator gpc = new GenrePopularityCalculator();
            result = gpc.calcPopularity(in, result);

            /*We check for every genre if there are unseen videos*/
            for (Map.Entry<String, Double> e : result.entrySet()) {
                /*Check MOVIES*/
                for (MovieInputData m : in.getMovies()) {
                    if (gpc.hasGenres(m, e.getKey()) && comm.hasSeen(m.getTitle(), u) == 0) {
                        return rb.buildRec(m.getTitle(), 'p', 1);
                    }
                }
                /*Check SERIALS*/
                for (SerialInputData m : in.getSerials()) {
                    if (gpc.hasGenres(m, e.getKey()) && comm.hasSeen(m.getTitle(), u) == 0) {
                        return rb.buildRec(m.getTitle(), 'p', 1);
                    }
                }
            }

            /*
             * If we reached this step, then nothing more can be done....
             * This user has seen it all :P
             * */
        }
        return rb.buildRec("", 'p', 0);
    }

    /**
     * @return the result of a favourite recommendation
     */
    public String favorite(final Input in, final UserCommands comm, final UserInputData u) {
        if (!u.getSubscriptionType().equals("BASIC")) {
            LinkedHashMap<String, Double> result = new LinkedHashMap<>();
            FavorabilityCalculator fc = new FavorabilityCalculator();
            /*Introduce all MOVIES*/
            for (MovieInputData m : in.getMovies()) {
                result.put(m.getTitle(), (double) fc.getFavor(m.getTitle(), in));
            }

            /*Introduce all SERIALS*/
            for (SerialInputData m : in.getSerials()) {
                result.put(m.getTitle(), (double) fc.getFavor(m.getTitle(), in));
            }

            /*Sort non alphabetically*/
            Sorter sorter = new Sorter();
            result = sorter.sortNAlf(result);

            /*Start checking the videos*/
            for (Map.Entry<String, Double> e : result.entrySet()) {
                if (comm.hasSeen(e.getKey(), u) == 0) {
                    return rb.buildRec(e.getKey(), 'f', 1);
                }
            }

            /* So far we can conclude that the user has seen all the videos that
             * have been marked as favourite by at least one user
             * Proceed to check the other videos*/
            for (Map.Entry<String, Double> e : result.entrySet()) {
                if (comm.hasSeen(e.getKey(), u) == 0) {
                    return rb.buildRec(e.getKey(), 'f', 1);
                }
            }

            for (MovieInputData m : in.getMovies()) {
                if (!result.containsKey(m.getTitle()) && comm.hasSeen(m.getTitle(), u) == 0) {
                    return rb.buildRec(m.getTitle(), 'f', 1);
                }
            }

            /*No more videos to see*/
        }
        return rb.buildRec("", 'f', 0);
    }

    /**
     * @return the result of a search recommendation
     */
    public String search(final Input in, final UserCommands comm, final UserInputData u,
                         final ActionInputData a, final Grader grd) {
        if (u.getSubscriptionType().equals("BASIC")) {
            return rb.buildRec("", 'f', 0);
        } else {
            String gen = a.getGenre();
            LinkedHashMap<String, Double> result = new LinkedHashMap<>();
            GenrePopularityCalculator gpc = new GenrePopularityCalculator();

            /*Search through the MOVIES*/
            for (MovieInputData m : in.getMovies()) {
                if (comm.hasSeen(m.getTitle(), u) == 0 && gpc.hasGenres(m, gen)) {
                    result.put(m.getTitle(), grd.videoRate(m.getTitle()));
                }
            }

            /*Search through the SERIALS*/
            for (SerialInputData m : in.getSerials()) {
                if (comm.hasSeen(m.getTitle(), u) == 0 && gpc.hasGenres(m, gen)) {
                    double grade = 0;
                    for (SerialsGrader sg : grd.getSerialsGraders()) {
                        if (sg.getTitle().equals(m.getTitle())) {
                            grade = sg.calculateGrade();
                        }
                    }
                    result.put(m.getTitle(), grade);
                }
            }

            /*Check if there were any results*/
            if (result.size() == 0) {
                return rb.buildRec("", 'h', 0);
            }

            /*Order the results*/
            Sorter sorter = new Sorter();
            result = sorter.sortAscOrder(result);

            /*Print the result*/

            return rb.buildRecSearch(result);
        }
    }

    /**
     * The method that is called whenever a recommendation is to be executed
     * @return the result of the recommendation
     */
    public String execute(final Input in, final ActionInputData a, final UserCommands comm,
                          final Grader grd) {
        switch (a.getType()) {
            case "standard" -> {
                int index = ui.getIndex(a.getUsername(), in);
                return standard(in, comm, in.getUsers().get(index));
            }
            case "best_unseen" -> {
                int index = ui.getIndex(a.getUsername(), in);
                return bestUnseen(in, comm, in.getUsers().get(index), grd);
            }
            case "popular" -> {
                int index = ui.getIndex(a.getUsername(), in);
                return popular(in, comm, in.getUsers().get(index));
            }
            case "favorite", "favourite" -> {
                int index = ui.getIndex(a.getUsername(), in);
                return favorite(in, comm, in.getUsers().get(index));
            }
            case "search" -> {
                int index = ui.getIndex(a.getUsername(), in);
                return search(in, comm, in.getUsers().get(index), a, grd);
            }
            default -> {
                return "";
            }
        }

    }
}
