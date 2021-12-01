package actions.recommendation;

import java.util.LinkedHashMap;
import java.util.Map;

public class RecommBuilder {
    private final String rec = "Recommendation ";
    private final String search = "Search";
    private final String res = "result: ";
    private final String not = "cannot be applied!";

    /**
     * @param type For recommendation type:
     *             p - popular; b - bestRatedUnseen
     *             f - favourite; h - search
     *             s - standard
     *
     * @param possible 0 - for result impossible
     *                 1 - for result possible
     *
     * @return build the general result of a recommendation
     * */
    public String buildRec(final String name, final char type, final int possible) {
        String std = "Standard";
        String best = "BestRatedUnseen";
        String pop = "Popular";
        String fav = "Favorite";
        if (possible == 0) {
            return switch (type) {
                case 's' -> std + rec + not;
                case 'h' -> search + rec + not;
                case 'b' -> best + rec + not;
                case 'p' -> pop + rec + not;
                case 'f' -> fav + rec + not;
                default -> "";
            };
        } else {
            return switch (type) {
                case 's' -> std + rec + res + name;
                case 'h' -> search + rec + res + name;
                case 'b' -> best + rec + res + name;
                case 'p' -> pop + rec + res + name;
                case 'f' -> fav + rec + res + name;
                default -> "";
            };
        }
    }

    /**
     * @return build the result of a search recommendation
     */
    public String buildRecSearch(final LinkedHashMap<String, Double> arr) {
        StringBuilder sb = new StringBuilder();
        sb.append(search);
        sb.append(rec);
        sb.append(res);
        sb.append("[");

        int found = 0;
        for (Map.Entry<String, Double> s : arr.entrySet()) {
            sb.append(s.getKey());
            sb.append(", ");
            found = 1;
        }

        if (found == 1) {
            int len = sb.toString().length();
            len--;
            sb.deleteCharAt(len);
            sb.deleteCharAt(len - 1);
        }

        sb.append("]");

        return sb.toString();
    }
}
