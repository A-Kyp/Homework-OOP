package actions.Calculators;

import fileio.ActorInputData;

public class AwardsCalculator {
    /**
     * @param act the actor for whom to calculate
     * @return the total number of awards
     */
    public Double totalAwards(final ActorInputData act) {
        Double sum = 0d;
        for (Integer aw : act.getAwards().values()) {
            sum += aw;
        }
        return sum;
    }
}
