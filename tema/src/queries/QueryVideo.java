package queries;

import commands.MovieIndexFinder;
import commands.SerialIndexFinder;
import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.List;

public class QueryVideo {

    ArrayList<String> filters = new ArrayList();

    public QueryVideo(ActionInputData a) {
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

        for(String f : filters) {
            if(f != null) {
                if (f.equals(movie.getYear())) {
                    year = 1;
                }
                if (f.equals(movie.getGenres())) {
                    genre = 1;
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

        for(String f : filters) {
            if(f != null) {
                if (f.equals(serial.getYear())) {
                    year = 1;
                }
                if (f.equals(serial.getGenres())) {
                    genre = 1;
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
