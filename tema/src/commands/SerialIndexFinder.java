package commands;

import fileio.Input;

public class SerialIndexFinder {
    public int getIndex(String title, Input input) {
        for(int i = 0; i < input.getSerials().size(); ++i) {
            if(input.getSerials().get(i).getTitle().equals(title)) {
                return i; //return the index of the film
            }
        }
        return -1; // or -1 if the film does not exist -> it must be a serial
    }
}
