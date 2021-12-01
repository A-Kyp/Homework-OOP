package actions.queries;

import actions.indexFinders.ActorIndexFinder;
import actions.indexFinders.MovieIndexFinder;
import actions.indexFinders.SerialIndexFinder;
import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class QueryFilter {

    private final ArrayList<String> filters = new ArrayList();
    private final ArrayList<List<String>> filtersA = new ArrayList();

    /**
     * Initialize the filters list
     */
    public QueryFilter(final ActionInputData a, final int type) {
        if (type > 0) {
            for (List<String> s : a.getFilters()) {
                if (s != null) {
                    filters.add(s.get(0));
                }
            }
        } else {
            for (List<String> s : a.getFilters()) {
                if (s != null) {
                    filtersA.add(s);
                }
            }
        }
    }


    /**
     * Apply filters on MOVIES
     * @return 1 for match filter
     *         0 otherwise
     */
    public int fitFilterM(final Input in, final String title) {
        MovieIndexFinder m = new MovieIndexFinder();
        int index = m.getIndex(title, in);
        MovieInputData movie = in.getMovies().get(index);
        int year = 0;
        int genre = 0;
        String intToString;
        intToString = String.valueOf(movie.getYear());

        if (filters.get(0) == null || filters.get(0).equals(intToString)) {
            year = 1;
        }

        if (filters.get(1) == null) {
            genre = 1;
        } else {
            for (String gen : movie.getGenres()) {
                if (filters.get(1).equals(gen)) {
                    genre = 1;
                    break;
                }
            }
        }

        return year & genre;
    }

    /**
     * Apply filters on SERIALS
     * @return 1 for match filter
     *         0 otherwise
     */
    public int fitFilterS(final Input in, final String title) {
        SerialIndexFinder s = new SerialIndexFinder();
        int index = s.getIndex(title, in);
        SerialInputData serial = in.getSerials().get(index);
        int year = 0;
        int genre = 0;
        String intToString;
        intToString = String.valueOf(serial.getYear());

        if (filters.get(0) == null || filters.get(0).equals(intToString)) {
            year = 1;
        }

        if (filters.get(1) == null) {
            genre = 1;
        } else {
            for (String gen : serial.getGenres()) {
                if (filters.get(1).equals(gen)) {
                    genre = 1;
                    break;
                }
            }
        }

        return year & genre;
    }

    /**
     * Apply filters on Actors
     * @param type custom filter for actors query:
     *             a - average;
     *             w - awards;
     *             d - description;
     *
     * @return 1 for match filter
     *         0 otherwise
     * */
    public int fitFilterAw(final Input in, final String name, final char type) {
        ActorIndexFinder af = new ActorIndexFinder();
        int i = af.getIndex(name, in);
        int all = 0;

        switch (type) {
            case 'a' :
                return 1;

            case 'w' :
                //the List of Awards to search for
                for (String s : filtersA.get(filtersA.size() - 1)) {
                    if (!in.getActors().get(i).getAwards().containsKey(Utils.stringToAwards(s))) {
                        all = -1;
                        break;
                    }
                }

                if (all == 0) {
                    return 1;
                }
                return 0;

            case 'd' :
                StringBuilder str = new StringBuilder();
                str.append(" ");
                str.append(in.getActors().get(i).getCareerDescription().toLowerCase());
                str.append(" ");
                String desc = str.toString();
                desc = desc.replaceAll("\\W", " ");

                for (String s : filtersA.get(filtersA.size() - 1)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" ");
                    sb.append(s.toLowerCase());
                    sb.append(" ");
                    if (!desc.contains(sb.toString())) {
                        all = -1;
                    }
                }
                if (all == -1) {
                    return 0;
                }
                return 1;

            default:
                return 0;
        }
    }
}
