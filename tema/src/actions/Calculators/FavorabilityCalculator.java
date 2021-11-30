package actions.Calculators;

import fileio.Input;
import fileio.UserInputData;

public class FavorabilityCalculator {
    public int getFavor(String title, Input in) {
        int favor = 0;
        for(UserInputData u : in.getUsers()) {
            if(u.getFavoriteMovies().contains(title)) {
                favor++;
            }
        }
        return  favor;
    }
}
