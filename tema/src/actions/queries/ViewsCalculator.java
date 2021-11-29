package actions.queries;

import fileio.Input;
import fileio.UserInputData;

public class ViewsCalculator {
    public int getViews(String title, Input in){
        int views = 0;
        for(UserInputData u : in.getUsers()) {
            if(u.getHistory().containsKey(title)) {
                views += u.getHistory().get(title);
            }
        }

        return views;
    }
}
