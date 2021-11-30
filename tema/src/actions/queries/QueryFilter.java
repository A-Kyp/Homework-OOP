package actions.queries;

import actions.IndexFinders.ActorIndexFinder;
import actions.IndexFinders.MovieIndexFinder;
import actions.IndexFinders.SerialIndexFinder;
import actor.ActorsAwards;
import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QueryFilter {

    ArrayList<String> filters = new ArrayList();
    ArrayList<List<String>> filtersA = new ArrayList<>();

    public QueryFilter(ActionInputData a, int type) {
        if(type > 0) {
            for (List<String> s : a.getFilters()) {
                if (s != null) {
                    filters.add(s.get(0));
                }
            }
        }
        else {
            for (List<String> s : a.getFilters()) {
                if (s != null) {
                    filtersA.add(s);
                }
            }
        }
    }

    public int fitFilterM(Input in, String title) {
        MovieIndexFinder m = new MovieIndexFinder();
        int index = m.getIndex(title, in);
        MovieInputData movie = in.getMovies().get(index);
        int year = 0;
        int genre = 0;
        int nullCount = 0;
        String intToString;
        intToString = String.valueOf(movie.getYear());

        if(filters.get(0) == null || filters.get(0).equals(intToString))
            year = 1;

        if(filters.get(1) == null) {
            genre = 1;
        }
        else {
            for (String gen : movie.getGenres()) {
                if (filters.get(1).equals(gen)) {
                    genre = 1;
                    break;
                }
            }
        }

        return year & genre;
    }

    public int fitFilterS(Input in, String title) {
        SerialIndexFinder s = new SerialIndexFinder();
        int index = s.getIndex(title, in);
        SerialInputData serial = in.getSerials().get(index);
        int year = 0;
        int genre = 0;
        int nullCount = 0;
        String intToString;
        intToString = String.valueOf(serial.getYear());

        if(filters.get(0) == null || filters.get(0).equals(intToString))
            year = 1;

        if(filters.get(1) == null) {
            genre = 1;
        }
        else {
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
     * @param type custom filter for actors query:
     *             a - average
     *             w - awards
     *             d - description
     * */
    public int fitFilterAw(Input in, String name, char type) {
        ActorIndexFinder af = new ActorIndexFinder();
        int index = af.getIndex(name, in);
        int all = 0;

        switch (type) {
            case 'a' :
                return 1;

            case 'w' :
                for(String s : filtersA.get(filtersA.size() - 1)) { //the List of Awards to search for
                        if(!in.getActors().get(index).getAwards().containsKey(Utils.stringToAwards(s))) {
                            all = -1;
                        }
                }

                if(all == 0) {
                    return 1;
                }
                return 0;

            case 'd' :
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" ");
                stringBuilder.append(in.getActors().get(index).getCareerDescription().toLowerCase());
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append(" ");
                String desc = stringBuilder.toString();
                desc = desc.replaceAll("-"," ");
                for(String s : filtersA.get(filtersA.size() - 1)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" ");
                    sb.append(s.toLowerCase());
                    sb.append(" ");
                    if(!desc.contains(sb.toString())) {
                        all = -1;
                    }
                }
                if(all == -1) {
                    return 0;
                }
                return 1;

            default:
                return 0;
        }
    }
}
