package actions.queries;

import actions.Sorter;
import fileio.ActionInputData;
import fileio.Input;

import java.util.LinkedHashMap;

public class QueryBuilder {
    /**
     * @param type indicate witch filter to apply
     *             put 0 for movies, anything else positive (an int) for serials
     *             and anything else negative (an int) for actors
     * */
    public String buildQuery(LinkedHashMap<String, Double> result, String ord,
                             ActionInputData a, Input in, int n, int type) {
        Sorter sorter = new Sorter();
        QueryFilter video = new QueryFilter(a);

        if (ord.equals("asc")) {
            result =  sorter.sortAscOrder(result);
        }
        else {
            result = sorter.sortDescOrder(result);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Query result: [");

        int found = 0;
        if(n > in.getSerials().size()) {
            for(String s : result.keySet()) {
                if(type >= 0) { //if it is a video
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
                else {//if it is an actor
                    sb.append(s);
                    sb.append(", ");
                    found = 1;
                }
            }
        }
        else {
            int i = 0;
            for(String s : result.keySet()) {
                if(type >= 0) { //video
                    if (type == 0) { //movie
                        if (video.fitFilterM(in, s) == 1) {
                            sb.append(s);
                            sb.append(", ");
                            found = 1;
                        }

                    } else {//serial
                        if (video.fitFilterS(in, s) == 1) {
                            sb.append(s);
                            sb.append(", ");
                            found = 1;
                        }
                    }
                }
                else {//actor
                    sb.append(s);
                    sb.append(", ");
                    found = 1;
                }
                i++;
                if(i == n) {
                    break;
                }
            }
        }
        if(found == 1) {
            int len = sb.toString().length();
            len--;
            sb.deleteCharAt(len);
            sb.deleteCharAt(len - 1);
        }

        sb.append("]");
        return sb.toString();
    }
}
