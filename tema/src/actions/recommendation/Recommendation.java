package actions.recommendation;

import actions.IndexFinders.UserIndexFinder;
import actions.Sorter;
import actions.commands.UserCommands;
import fileio.*;

import java.util.LinkedHashMap;

public class Recommendation {
    private final RecommBuilder rb = new RecommBuilder();
    private final UserIndexFinder ui = new UserIndexFinder();

    public String standard(Input in, UserCommands comm, UserInputData u) {
        for(MovieInputData m : in.getMovies()) {
            if(comm.hasSeen(m.getTitle(), u) == 0) {
                return rb.buildRec(m.getTitle(),'s',1);
            }
        }
        for(SerialInputData serial : in.getSerials()) {
            if(comm.hasSeen(serial.getTitle(),u) == 0) {
                return rb.buildRec(serial.getTitle(),'s',1);
            }
        }
        return rb.buildRec("",'s',0);
    }

    public String bestUnseen(Input in, UserCommands comm, UserInputData u) {
        Sorter sorter = new Sorter();
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        
        return "";
    }

    public String popular(Input in, UserCommands comm, UserInputData u) {
        if(u.getSubscriptionType().equals("BASIC")) {
            return rb.buildRec("",'p',0);
        }
        return "";
    }

    public String favorite(Input in, UserCommands comm, UserInputData u) {
        if(u.getSubscriptionType().equals("BASIC")) {
            return rb.buildRec("",'f',0);
        }
        return "";
    }

    public String search(Input in, UserCommands comm, UserInputData u) {
        if(u.getSubscriptionType().equals("BASIC")) {
            return rb.buildRec("",'f',0);
        }
        return "";
    }



    public String execute(Input in, ActionInputData a, UserCommands comm) {
        if(a.getType().equals("standard")) {
            int index = ui.getindex(a.getUsername(),in);
            return standard(in,comm,in.getUsers().get(index));
        }
        else if(a.getType().equals("best_unseen")) {
            int index = ui.getindex(a.getUsername(),in);
            return bestUnseen(in,comm,in.getUsers().get(index));
        }
        else if(a.getType().equals("popular")) {
            int index = ui.getindex(a.getUsername(),in);
            return popular(in,comm,in.getUsers().get(index));
        }
        else if(a.getType().equals("favorite") || a.getType().equals("favourite")) {
            int index = ui.getindex(a.getUsername(),in);
            return favorite(in,comm,in.getUsers().get(index));
        }
        else if(a.getType().equals("search")) {
            int index = ui.getindex(a.getUsername(),in);
            return search(in,comm,in.getUsers().get(index));
        }

        return "";
    }
}
