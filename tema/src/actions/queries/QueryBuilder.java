package actions.queries;

import actions.Sorter;
import fileio.ActionInputData;
import fileio.Input;

import java.util.LinkedHashMap;

public class QueryBuilder {
    /**
     * @param filter indicates witch type of query filter to use
     *               put anything >0 (an int) for videos
     *               and anything else (an int) for actors
     *
     * @param type indicate witch filter to apply
     *             put 0 for movies,
     *             anything else positive (an int) for serials
     *             and anything else negative (an int) for actors
     *
     * @return build the result of a general query
     *
     * */
    public String buildQuery(LinkedHashMap<String, Double> result, final String ord,
                             final ActionInputData a, final Input in, final int n,
                             final int filter, final int type) {
        Sorter sorter = new Sorter();
        QueryFilter video = new QueryFilter(a, filter);

        if (ord.equals("asc")) {
            result =  sorter.sortAscOrder(result);
        } else {
            result = sorter.sortDescOrder(result);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Query result: [");

        int found = 0;
        if (n > in.getSerials().size()) {
            for (String s : result.keySet()) {
                if (type >= 0) { //if it is a video
                    if (type == 0) { //if ot is a movie
                        if (video.fitFilterM(in, s) == 1) {
                            sb.append(s);
                            sb.append(", ");
                            found = 1;
                        }

                    } else { //if it is a serial
                        if (video.fitFilterS(in, s) == 1) {
                            sb.append(s);
                            sb.append(", ");
                            found = 1;
                        }
                    }
                }
            }
        } else {
            int i = 0;
            for (String s : result.keySet()) {
                if (type >= 0) { //video
                    if (type == 0) { //movie
                        if (video.fitFilterM(in, s) == 1) {
                            sb.append(s);
                            sb.append(", ");
                            found = 1;
                            i++;
                        }

                    } else { //serial
                        if (video.fitFilterS(in, s) == 1) {
                            sb.append(s);
                            sb.append(", ");
                            found = 1;
                            i++;
                        }
                    }
                }
                if (i == n) {
                    break;
                }
            }
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

    /**
     * @return build the result of an actor query
     */
    public String buildQueryA(LinkedHashMap<String, Double> result, final String ord,
                            final ActionInputData a, final Input in, final int n,
                              final int filter, final char query) {
        Sorter sorter = new Sorter();
        QueryFilter video = new QueryFilter(a, filter);

        if (ord.equals("asc")) {
            result =  sorter.sortAscOrder(result);
        } else {
            result = sorter.sortDescOrder(result);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Query result: [");

        int found = 0;
        if (n == -1 || n > in.getActors().size()) {
            for (String s : result.keySet()) {
                if (video.fitFilterAw(in, s, query) == 1) {
                    sb.append(s);
                    sb.append(", ");
                    found = 1;
                }
            }
        } else {
            int i = 0;
            for (String s : result.keySet()) {
                //actor
                if (video.fitFilterAw(in, s, query) == 1) {
                    sb.append(s);
                    sb.append(", ");
                    found = 1;
                    i++;
                }
                if (i == n) {
                    break;
                }
            }
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
