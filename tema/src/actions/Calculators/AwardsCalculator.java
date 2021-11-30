package actions.Calculators;

import entertainment.Season;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.SerialInputData;

public class AwardsCalculator {
    public Double totalAwards(ActorInputData act) {
        Double sum = 0d;
        for(Integer aw : act.getAwards().values()) {
            sum += aw;
        }
        return sum;
    }
}
