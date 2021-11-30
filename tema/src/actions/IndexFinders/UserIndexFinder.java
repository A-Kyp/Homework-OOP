package actions.IndexFinders;

import fileio.Input;
public class UserIndexFinder {
    public int getindex(String name, Input input) {
        for(int i = 0; i < input.getUsers().size(); ++i) {
            if(input.getUsers().get(i).getUsername().equals(name)) {
                return i; //return the index of the user
            }
        }
        return -1; // or -1 if the user does not exist
    }
}
