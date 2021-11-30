package actions.IndexFinders;

import fileio.Input;

public class ActorIndexFinder {
    public int getIndex(String name, Input in) {
        for(int i = 0; i < in.getActors().size(); ++i) {
            if(in.getActors().get(i).getName().equals(name)) {
                return i; //if the actor is found
            }
        }
        return -1;//if the actor could not be found
    }
}
