package actions.indexFinders;

import fileio.Input;

public class MovieIndexFinder {
    /**
     * @return the index of a MOVIE in the MOVIE list
     */
    public int getIndex(final String title, final Input input) {
        for (int i = 0; i < input.getMovies().size(); ++i) {
            if (input.getMovies().get(i).getTitle().equals(title)) {
                return i; //return the index of the film
            }
        }
        return -1; // or -1 if the film does not exist -> it must be a serial
    }
}
