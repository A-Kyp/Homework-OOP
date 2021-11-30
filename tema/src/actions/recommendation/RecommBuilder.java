package actions.recommendation;

public class RecommBuilder {
    private String rec = "Recommendation ";
    private String std = "Standard";
    private String search = "Search";
    private String best = "BestRatedUnseen";
    private String pop = "Popular";
    private String fav = "Favourite";
    private String not = "cannot be applied!";
    private String res = "result: ";

    /**
     * @param type For recommendation type:
     *             p - popular; b - bestRatedUnseen
     *             f - favourite; h - search
     *             s - standard
     * @param possible 0 - for result impossible
     *                 1 - for result possible
     *
     * */
    public String buildRec (String name, char type, int possible){
        if(possible == 0) {
            switch (type) {
                case 's' :
                    return std + rec + not;

                case 'h' :
                    return search + rec + not;

                case 'b' :
                    return best + rec + not;

                case 'p' :
                    return pop + rec + not;

                case 'f' :
                    return fav + rec + not;

                default:
                    return "";
            }
        }
        else {
            switch (type) {
                case 's':
                    return std + rec + res + name;

                case 'h':
                    return search + rec + res + name;

                case 'b':
                    return best + rec + res + name;

                case 'p':
                    return pop + rec + res + name;

                case 'f':
                    return fav + rec + res + name;

                default:
                    return "";
            }
        }
    }
}
