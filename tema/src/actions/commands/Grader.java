package actions.commands;

import fileio.ActionInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Grader {
    private final Map<String, ArrayList<Double>> filmGrades = new HashMap<>();
    private final Map<String, Integer> filmUserActivity = new HashMap<>();
    private final ArrayList<ArrayList<Boolean>> filmRateOnce = new ArrayList<>();
    private final ArrayList<SerialsGrader> serialsGraders = new ArrayList<>();

    public Map<String, ArrayList<Double>> getFilmGrades() {
        return filmGrades;
    }

    public Map<String, Integer> getFilmUserActivity() {
        return filmUserActivity;
    }

    public ArrayList<ArrayList<Boolean>> getFilmRateOnce() {
        return filmRateOnce;
    }

    public ArrayList<SerialsGrader> getSerialsGraders() {
        return serialsGraders;
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

    public int addAndGradeSerial(String title, ActionInputData a, UserInputData u, int sNo, int tSeasons) {
        if(tSeasons < sNo) {
            return 0; //action denied -> you tried to rate a nonexistent season
        }

        for(SerialsGrader g : serialsGraders) {
            /*Check if the serial has been rated yet*/
            if(g.getTitle().equals(title)) {
                /*Check if the user has already given feedback for any of the seasons*/
                for(SerialGradesByUser s : g.getRatings()) {
                    if(s.getUsername().equals(u.getUsername())) {
                        /*If the season is not graded */
                        if(s.getSeasonsGrades().get(sNo) == 0) {
                            s.getSeasonsGrades().set(sNo, a.getGrade());
                            return 1; //action permitted
                        }
                        else {
                            return 0; //action denied -> don't cheat!!! you already rated this season
                        }
                    }
                }
                /*Didn't find the user. We have to add a new entry for the user*/
                SerialGradesByUser sgbu = new SerialGradesByUser();
                sgbu.setUsername(u.getUsername());
                sgbu.initialize(tSeasons);
                sgbu.getSeasonsGrades().set(sNo, a.getGrade());
                return 1; //action permitted
            }
        }
        /*Didn't find the serial. We have to introduce new entry for the serial*/
        SerialsGrader newSG = new SerialsGrader(title);
        /*Add new user entry*/
        SerialGradesByUser sgbu = new SerialGradesByUser();
        sgbu.setUsername(u.getUsername());
        sgbu.initialize(tSeasons);
        sgbu.getSeasonsGrades().set(sNo, a.getGrade());
        newSG.getRatings().add(sgbu);
        serialsGraders.add(newSG);
        return 1; //action permitted
    }

    public LinkedHashMap<String, Double> getRateForFilm () {
        LinkedHashMap<String, Double> list = new LinkedHashMap<>();
        double sum = 0;
        for(Map.Entry<String, ArrayList<Double>> e : filmGrades.entrySet()) {
            sum = 0;
            for(Double d: e.getValue()) {
                sum += d;
            }
            list.put(e.getKey(),sum/e.getValue().size());
        }
        return list;
    }

    public LinkedHashMap<String, Double> getRateForSerial () {
        LinkedHashMap<String, Double> list = new LinkedHashMap<>();
        double sum = 0;
        for(SerialsGrader sg : serialsGraders) {
            list.put(sg.getTitle(), sg.calculateGrade());
        }
        return list;
    }

    public Double videoRate(String title) {
        Double sum = 0d;
        for(Double d : filmGrades.get(title)) {
            sum += d;
        }

        return sum/filmGrades.get(title).size();
    }
}
