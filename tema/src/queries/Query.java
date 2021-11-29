package queries;

import commands.Grader;
import fileio.ActionInputData;
import fileio.Input;

import java.util.*;

public class Query {
    public static Map<String, Integer> sortByComparator (Map<String, Integer> unsorted, String order) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsorted.entrySet());

        //Sort base on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if(order.equals("asc")){
                    return o1.getValue().compareTo(o2.getValue());
                }
                else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        //Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public String numberOfRatings(int n, String order, Grader grd, Input in) {
        Map<String, Integer> userByRateCount = new LinkedHashMap<>();
        if(order.equals("asc")) {
            userByRateCount = sortByComparator(grd.getFilmUserActivity(), "asc");
        }
        else {
            userByRateCount = sortByComparator(grd.getFilmUserActivity(), "desc");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Query result: [");
        if(n > in.getUsers().size()) {
            for(String s : userByRateCount.keySet()) {
                sb.append(s);
                sb.append(", ");
            }
        }
        else {
            int i = 0;
            for(String s : userByRateCount.keySet()) {
                sb.append(s);
                sb.append(", ");
                i++;
                if(i == n) {
                    break;
                }
            }
        }
        int len = sb.toString().length();
        len --;
        sb.deleteCharAt(len);
        sb.deleteCharAt(len-1);
        sb.append("]");

        return sb.toString();
    }

    public String ratingMovie(int n, String order, Grader grd, Input in) {
        StringBuilder sb = new StringBuilder();
        sb.append("Query result [");

        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        grd.getRateForFilm().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> result.put(entry.getKey(), entry.getValue()));


        if(n > in.getMovies().size()) {
            for(String s : result.keySet()) {
                sb.append(s);
                sb.append(", ");
            }
        }
        else {
            int i = 0;
            for(String s : result.keySet()) {
                sb.append(s);
                sb.append(", ");
                i++;
                if(i == n) {
                    break;
                }
            }
        }
        int len = sb.toString().length();
        len --;
        sb.deleteCharAt(len);
        sb.deleteCharAt(len-1);

        sb.append("]");

        return sb.toString();
    }

    public String execute(Input in, ActionInputData actionInputData, Grader grd) {
        if(actionInputData.getObjectType().equals("users")) {
            return numberOfRatings(actionInputData.getNumber(), actionInputData.getSortType(), grd, in);
        }
        else if(actionInputData.getObjectType().equals("movies")) {
            return ratingMovie(actionInputData.getNumber(), actionInputData.getSortType(), grd, in);
        }
        return "";
    }
}
