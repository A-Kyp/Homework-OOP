package actions.indexFinders;

import fileio.Input;
public class UserIndexFinder {
    /**
     * @return the index of a user in the user list
     */
    public int getIndex(final String name, final Input input) {
        for (int i = 0; i < input.getUsers().size(); ++i) {
            if (input.getUsers().get(i).getUsername().equals(name)) {
                return i; //return the index of the user
            }
        }
        return -1; // or -1 if the user does not exist
    }
}
