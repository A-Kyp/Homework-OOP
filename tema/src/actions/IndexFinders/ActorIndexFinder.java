package actions.IndexFinders;

import fileio.Input;

public class ActorIndexFinder {
    /**
     * @return the index of a actor in the actor list
     */
    public int getIndex(final String name, final Input in) {
        for (int i = 0; i < in.getActors().size(); ++i) {
            if (in.getActors().get(i).getName().equals(name)) {
                return i; //if the actor is found
            }
        }
        return -1; //if the actor could not be found
    }
}
