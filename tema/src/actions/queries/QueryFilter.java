package actions.queries;

import actions.MovieIndexFinder;
import actions.SerialIndexFinder;
import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.List;

public class QueryFilter {

    ArrayList<String> filters = new ArrayList();

    public QueryFilter(ActionInputData a) {
        for(List<String> s : a.getFilters()){
            if(s != null) {
                filters.add(s.get(0));
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

        for(String f : filters) {
            if(f != null) {
                intToString = String.valueOf(movie.getYear());
                if (f.equals(intToString)) {
                    year = 1;
                }
                for(String gen : movie.getGenres()) {
                    if (f.equals(gen)) {
                        genre = 1;
                        break;
                    }
                }
            }
            else {
                nullCount ++;
            }
        }
        if(nullCount < 4) {
            return (year & genre);
        }
        else {
            return 1;
        }
    }

    public int fitFilterS(Input in, String title) {
        SerialIndexFinder s = new SerialIndexFinder();
        int index = s.getIndex(title, in);
        SerialInputData serial = in.getSerials().get(index);
        int year = 0;
        int genre = 0;
        int nullCount = 0;
        String intToString;

        for(String f : filters) {
            if(f != null) {
                intToString = String.valueOf(serial.getYear());
                if (f.equals(intToString)) {
                    year = 1;
                }
                for(String gen : serial.getGenres()) {
                    if (f.equals(gen)) {
                        genre = 1;
                        break;
                    }
                }
            }
            else {
                nullCount ++;
            }
        }
        if(nullCount < 4) {
            return (year & genre);
        }
        else {
            return 1;
        }
    }

}
