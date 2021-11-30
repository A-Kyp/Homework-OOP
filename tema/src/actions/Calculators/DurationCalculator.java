package actions.Calculators;

import entertainment.Season;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.ShowInput;

public class DurationCalculator {
    public int totalDuration(ShowInput si) {
        if(si instanceof MovieInputData) {
            return ((MovieInputData) si).getDuration();
        }
        else{
            int sum = 0;
            for(Season s : ((SerialInputData) si).getSeasons()) {
                sum += s.getDuration();
            }
            return sum;
        }
    }
}
