package actions.calculators;

import actions.indexFinders.MovieIndexFinder;
import actions.indexFinders.SerialIndexFinder;
import actions.Sorter;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.ShowInput;
import fileio.UserInputData;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GenrePopularityCalculator {

    /**
     * @param m the VIDEO we to check
     * @param gen the genre to check
     * @return true if the VIDEO has the gen genre
     *          false otherwise
     */
    public boolean hasGenres(final ShowInput m, final String gen) {
        for (String g : m.getGenres()) {
            if (g.equals(gen)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param title title of the MOVIE we want
     * @param in Input data
     * @return the MOVIE object that has the given title
     */
    private MovieInputData getMovie(final String title, final Input in) {
        MovieIndexFinder mi = new MovieIndexFinder();
        int index = mi.getIndex(title, in);
        if (index == -1) {
            return null;
        }
        return in.getMovies().get(index);
    }

    /**
     * @param title title of the SERIAL we want
     * @param in Input data
     * @return the SERIAL object that has the given title
     */
    private SerialInputData getSerial(final String title, final Input in) {
            SerialIndexFinder si = new SerialIndexFinder();
            int index = si.getIndex(title, in);
            if (index == -1) {
                return null;
            }
            return in.getSerials().get(index);
    }

    /**
     * @param in input data
     * @param arr initial bank map that will store (Genre - Nr of views for the genre) pairs
     * @return returns the upfated map with calculated values for genre popularity
     */
    public LinkedHashMap<String, Double> calcPopularity(final Input in,
                                                        final LinkedHashMap<String, Double> arr) {
        /*Update for movies*/
        for (UserInputData u : in.getUsers()) { //for every user
            for (Map.Entry<String, Integer> e : u.getHistory().entrySet()) {
                //we update for each movie he has seen
                if (getMovie(e.getKey(), in) != null) {
                    for (String g : Objects.requireNonNull(getMovie(e.getKey(), in)).getGenres()) {
                        //the map that stores popularity for each genre
                        if (arr.containsKey(g)) {
                            //update value
                            arr.put(g, arr.get(g) + Double.valueOf(e.getValue()));
                        } else {
                            arr.put(g, Double.valueOf(e.getValue())); //first genre apparition
                        }
                    }
                }
            }
        }

        /*Update for serials*/
        for (UserInputData u : in.getUsers()) { //for every user
            //we update for each movie he has seen
            for (Map.Entry<String, Integer> e : u.getHistory().entrySet()) {
                if (getSerial(e.getKey().toString(), in) != null) {
                    for (String g : getSerial(e.getKey().toString(), in).getGenres()) {
                        if (arr.containsKey(g)) { //the map that stores popularity for each genre
                            arr.put(g, arr.get(g) + Double.valueOf(e.getValue())); //update value
                        } else {
                            arr.put(g, Double.valueOf(e.getValue())); //first genre apparition
                        }
                    }
                }
            }
        }
        Sorter sorter = new Sorter();
        return sorter.sortNAlf(arr);
    }
}
