package actions.indexFinders;

import fileio.Input;

public class SerialIndexFinder {
    /**
     * @return the index of a SERIAL in the SERIAL list
     */
    public int getIndex(final String title, final Input input) {
        for (int i = 0; i < input.getSerials().size(); ++i) {
            if (input.getSerials().get(i).getTitle().equals(title)) {
                return i; //return the index of the film
            }
        }
        return -1; // or -1 if the film does not exist -> it must be a serial
    }
}
