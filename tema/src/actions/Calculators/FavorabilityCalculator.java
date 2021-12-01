package actions.Calculators;

import fileio.Input;
import fileio.UserInputData;

public class FavorabilityCalculator {
    /**
     * @param title the title of the VIDEO for which to calculate
     *
     * @param in the Input data which contains all user info
     *
     * @return total number of user who marked the video as favourite
     * */
    public int getFavor(final String title, final Input in) {
        int favor = 0;
        for (UserInputData u : in.getUsers()) {
            if (u.getFavoriteMovies().contains(title)) {
                favor++;
            }
        }
        return favor;
    }
}
