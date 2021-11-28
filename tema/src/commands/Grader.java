package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Grader {
    private Map<String, ArrayList<Double>> filmGrades = new HashMap<>();
    private Map<String, Integer> filmUserActivity = new HashMap<>();
    private ArrayList<ArrayList<Boolean>> filmRateOnce = new ArrayList<>();
    private ArrayList<GraderSerials> serialGrads = new ArrayList<>();

    public Map<String, ArrayList<Double>> getFilmGrades() {
        return filmGrades;
    }

    public Map<String, Integer> getFilmUserActivity() {
        return filmUserActivity;
    }

    public ArrayList<ArrayList<Boolean>> getFilmRateOnce() {
        return filmRateOnce;
    }

    public ArrayList<GraderSerials> getSerialGrads() {
        return serialGrads;
    }

    public void initializeMatrix(int nrOfFilms, int nrOfUser) {
        for(int i = 0; i < nrOfFilms; ++i) {
            ArrayList<Boolean> col = new ArrayList<>();

            for(int j = 0; j < nrOfUser; ++j) {
                col.add(Boolean.FALSE);
            }

            filmRateOnce.add(col);
        }
    }

//    public void initializeSerials(int nrSeasons,)
}
