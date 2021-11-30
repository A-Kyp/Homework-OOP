package actions;

import actions.commands.Grader;
import fileio.ActorInputData;

import java.util.LinkedHashMap;

public class ActorAverageCalculator {
    public Double getAverage(Grader grd, ActorInputData act) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        result = grd.getRateForSerial();
        Double sum = 0d;
        int nrOfRate = 0;
        int found = 0;
        for(String title : act.getFilmography()) {
            if(grd.getFilmGrades().containsKey(title)) {
                sum += grd.videoRate(title);
                nrOfRate ++;
                found = 1;
            }
            if(result.containsKey(title)) {
                sum += result.get(title);
                nrOfRate ++;
                found = 1;
            }
        }
        if(found == 1)
            return sum/nrOfRate;
        else
            return 0d;
    }
}
