package actions.calculators;

import actions.commands.Grader;
import fileio.ActorInputData;

import java.util.LinkedHashMap;

public class ActorAverageCalculator {
    /**
     * Returns the average grade for an actor
     * */
    public Double getAverage(final Grader grd, final ActorInputData act) {
        LinkedHashMap<String, Double> result;
        result = grd.getRateForSerial();
        Double sum = 0d;
        int nrOfRate = 0;
        int found = 0;
        for (String title : act.getFilmography()) {
            if (grd.getRatedFilms().containsKey(title)) {
                sum += grd.videoRate(title);
                nrOfRate++;
                found = 1;
            }
            if (result.containsKey(title)) {
                sum += result.get(title);
                nrOfRate++;
                found = 1;
            }
        }
        if (found == 1) {
            return sum / nrOfRate;
        } else {
            return 0d;
        }
    }
}
