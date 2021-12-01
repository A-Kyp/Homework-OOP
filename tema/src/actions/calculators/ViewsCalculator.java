package actions.calculators;

import fileio.Input;
import fileio.UserInputData;

public class ViewsCalculator {
    /**
     * @param title title of the VIDEO
     * @param in input data
     * @return total number of views for the VIDEO
     */
    public int getViews(final String title, final Input in) {
        int views = 0;
        for (UserInputData u : in.getUsers()) {
            if (u.getHistory().containsKey(title)) {
                views += u.getHistory().get(title);
            }
        }
        return views;
    }
}
