package actions.queries;

import actions.calculators.ActorAverageCalculator;
import actions.calculators.AwardsCalculator;
import actions.calculators.DurationCalculator;
import actions.calculators.FavorabilityCalculator;
import actions.calculators.ViewsCalculator;
import actions.commands.Grader;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.ShowInput;

import java.util.LinkedHashMap;

public class Query {

    /**
     * @return the result of a user query
     */
    public String numberOfRatings(final int n, final String order, final Grader grd,
                                  final Input in, final ActionInputData a) {
        LinkedHashMap<String, Double> result;
        result = grd.getFilmUserActivity();
        QueryBuilder qb = new QueryBuilder();

        return qb.buildQueryA(result, order, a, in, n, -1, 'a');
    }

    /**
     * @return the result of a MOVIE rate query
     */
    public String ratingMovie(final String order, final Grader grd, final Input in,
                              final ActionInputData a) {
        QueryBuilder qb = new QueryBuilder();

        LinkedHashMap<String, Double> result;
        result = grd.getRateForFilm();

        return qb.buildQuery(result, order, a, in, a.getNumber(), 1, 0);
    }

    /**
     * @return the result of a SERIAL rate query
     */
    public String ratingShow(final String order, final Grader grd, final Input in,
                             final ActionInputData a) {
        LinkedHashMap<String, Double> result;
        result = grd.getRateForSerial();
        QueryBuilder qb = new QueryBuilder();

        return qb.buildQuery(result, order, a, in, a.getNumber(), 1, 1);
    }

    /**
     * @return the result of a MOVIE longest query
     */
    public String longestM(final String order, final Input in, final ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        DurationCalculator dc = new DurationCalculator();
        QueryBuilder qb = new QueryBuilder();

        for (ShowInput show : in.getMovies()) {
            result.put(show.getTitle(), (double) dc.totalDuration(show));
        }

        return qb.buildQuery(result, order, a, in, a.getNumber(), 1, 0);
    }

    /**
     * @return the result of a SERIAL longest query
     */
    public String longestS(final String order, final Input in, final ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        DurationCalculator dc = new DurationCalculator();
        QueryBuilder qb = new QueryBuilder();

        for (ShowInput show : in.getSerials()) {
            result.put(show.getTitle(), (double) dc.totalDuration(show));
        }

        return qb.buildQuery(result, order, a, in, a.getNumber(), 1, 1);
    }

    /**
     * @return the result of a MOVIE favourite query
     */
    public  String favoriteM(final int n, final String order, final Input in,
                             final ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        FavorabilityCalculator fc = new FavorabilityCalculator();
        QueryBuilder qb = new QueryBuilder();

        int favor;

        for (MovieInputData m : in.getMovies()) {
            favor = fc.getFavor(m.getTitle(), in);
            if (favor != 0) {
                result.put(m.getTitle(), (double) favor);
            }
        }

        return qb.buildQuery(result, order, a, in, n, 1, 0);
    }

    /**
     * @return the result of a SERIAL favourite query
     */
    public  String favoriteS(final int n, final String order, final Input in,
                             final ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        FavorabilityCalculator fc = new FavorabilityCalculator();
        QueryBuilder qb = new QueryBuilder();

        int favor;

        for (SerialInputData ser : in.getSerials()) {
            favor = fc.getFavor(ser.getTitle(), in);
            if (favor != 0) {
                result.put(ser.getTitle(), (double) favor);
            }
        }

        return qb.buildQuery(result, order, a, in, n, 1, 1);
    }

    /**
     * @return the result of a MOVIE most views query
     */
    public String mostViewM(final int n, final String order, final Input in,
                            final ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        ViewsCalculator vc = new ViewsCalculator();
        QueryBuilder qb = new QueryBuilder();
        int views;

        for (MovieInputData movie : in.getMovies()) {
            views = vc.getViews(movie.getTitle(), in);
            if (views != 0) {
                result.put(movie.getTitle(), (double) views);
            }
        }
        return qb.buildQuery(result, order, a, in, n, 1, 0);
    }

    /**
     * @return the result of a SERIAL most views query
     */
    public String mostViewS(final int n, final String order, final Input in,
                            final ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        ViewsCalculator vc = new ViewsCalculator();
        QueryBuilder qb = new QueryBuilder();
        int views;

        for (SerialInputData movie : in.getSerials()) {
            views = vc.getViews(movie.getTitle(), in);
            if (views != 0) {
                result.put(movie.getTitle(), (double) views);
            }
        }
        return qb.buildQuery(result, order, a, in, n, 1, 2);
    }

    /**
     * @return the result of an actor average query
     */
    public String average(final int n, final String order, final Input in,
                          final ActionInputData a, final Grader grd) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        ActorAverageCalculator ac = new ActorAverageCalculator();
        QueryBuilder qb = new QueryBuilder();
        Double average;
        for (ActorInputData act : in.getActors()) {
            average = ac.getAverage(grd, act);
            if (average != 0d) {
                result.put(act.getName(), average);
            }
        }
        return qb.buildQueryA(result, order, a, in, n, -1, 'a');
    }

    /**
     * @return the result of an actor awards query
     */
    public String awards(final int n, final String order, final Input in,
                         final ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        AwardsCalculator ac = new AwardsCalculator();
        QueryBuilder qb = new QueryBuilder();
        Double aw;

        for (ActorInputData act : in.getActors()) {
            aw = ac.totalAwards(act);
            if (aw != 0d) {
                result.put(act.getName(), aw);
            }
        }
        return qb.buildQueryA(result, order, a, in, n, -1, 'w');
    }

    /**
     * @return the result of an actor filter_description query
     */
    public String description(final int n, final String order, final Input in,
                              final ActionInputData a) {
        double fillNum = 1d;
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        QueryBuilder qb = new QueryBuilder();

        for (ActorInputData act : in.getActors()) {
            result.put(act.getName(), fillNum);
        }

        return qb.buildQueryA(result, order, a, in, n, -1, 'd');
    }

    /**
     * The method that is called whenever a query is to be executed
     * @return the result of the query
     */
    public String execute(final Input in, final ActionInputData a, final Grader grd) {
        if (a.getObjectType().equals("users")) {
            return numberOfRatings(a.getNumber(), a.getSortType(), grd, in, a);
        } else if (a.getObjectType().equals("movies") && a.getCriteria().equals("ratings")) {
            return ratingMovie(a.getSortType(), grd, in, a);
        } else if (a.getObjectType().equals("shows") && a.getCriteria().equals("ratings")) {
            return ratingShow(a.getSortType(), grd, in, a);
        } else if (a.getObjectType().equals("movies") && a.getCriteria().equals("longest")) {
            return longestM(a.getSortType(), in, a);
        } else if (a.getObjectType().equals("shows") && a.getCriteria().equals("longest")) {
            return longestS(a.getSortType(), in, a);
        } else if (a.getObjectType().equals("movies") && (a.getCriteria().equals("favorite")
                                                        || a.getCriteria().equals("favourite"))) {
            return favoriteM(a.getNumber(), a.getSortType(), in, a);
        } else if (a.getObjectType().equals("shows") && (a.getCriteria().equals("favorite")
                || a.getCriteria().equals("favourite"))) {
            return favoriteS(a.getNumber(), a.getSortType(), in, a);
        } else if (a.getObjectType().equals("movies") && a.getCriteria().equals("most_viewed")) {
            return mostViewM(a.getNumber(), a.getSortType(), in, a);
        } else if (a.getObjectType().equals("shows") && a.getCriteria().equals("most_viewed")) {
            return mostViewS(a.getNumber(), a.getSortType(), in, a);
        } else if (a.getObjectType().equals("actors") && a.getCriteria().equals("average")) {
            return average(a.getNumber(), a.getSortType(), in, a, grd);
        } else if (a.getObjectType().equals("actors") && a.getCriteria().equals("awards")) {
            return awards(-1, a.getSortType(), in, a);
        } else if (a.getObjectType().equals("actors")
                    && a.getCriteria().equals("filter_description")) {
            return description(-1, a.getSortType(), in, a);
        }

        return "";
    }
}
