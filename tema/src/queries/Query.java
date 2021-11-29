package queries;

import commands.Grader;
import commands.MovieIndexFinder;
import commands.SerialIndexFinder;
import commands.Sorter;
import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.ShowInput;

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

    public String ratingMovie(int n, String order, Grader grd, Input in, ActionInputData a) {
        QueryBuilder qb = new QueryBuilder();
        MovieIndexFinder mIndex = new MovieIndexFinder();
        int index = mIndex.getIndex(a.getTitle(), in);

        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        result = grd.getRateForFilm();

        return qb.buildQuery(result,order,a,in,a.getNumber(),0);
    }

    public String ratingShow(int n, String order, Grader grd, Input in, ActionInputData a) {
        QueryVideo video = new QueryVideo(a);
        StringBuilder sb = new StringBuilder();
        SerialIndexFinder sIndex = new SerialIndexFinder();
        int index = sIndex.getIndex(a.getTitle(), in);

        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        result = grd.getRateForSerial();
        QueryBuilder qb = new QueryBuilder();

        return qb.buildQuery(result,order,a,in,a.getNumber(),1);
    }

    public String longestM(int n, String order, Input in, ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        Sorter sorter = new Sorter();
        DurationCalculator dc = new DurationCalculator();
        QueryBuilder qb = new QueryBuilder();

        for(ShowInput show : in.getMovies()) {
            result.put(show.getTitle(), (double)dc.totalDuration(show));
        }

        return qb.buildQuery(result,order,a,in,a.getNumber(),0);
    }

    public String longestS(int n, String order, Input in, ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        Sorter sorter = new Sorter();
        DurationCalculator dc = new DurationCalculator();
        QueryBuilder qb = new QueryBuilder();

        for(ShowInput show : in.getSerials()) {
            result.put(show.getTitle(), (double)dc.totalDuration(show));
        }

        return qb.buildQuery(result,order,a,in,a.getNumber(),1);
    }

    public String execute(Input in, ActionInputData a, Grader grd) {
        if(a.getObjectType().equals("users")) {
            return numberOfRatings(a.getNumber(), a.getSortType(), grd, in);
        }
        else if(a.getObjectType().equals("movies") && a.getCriteria().equals("ratings")) {
            return ratingMovie(a.getNumber(), a.getSortType(), grd, in, a);
        }
        else if(a.getObjectType().equals("shows") && a.getCriteria().equals("ratings")) {
            return ratingShow(a.getNumber(), a.getSortType(), grd, in, a);
        }
        else if(a.getObjectType().equals("movies") && a.getCriteria().equals("longest")) {
            return longestM(a.getNumber(),a.getSortType(),in,a);
        }
        else if(a.getObjectType().equals("shows") && a.getCriteria().equals("longest")) {
            return ratingShow(a.getNumber(), a.getSortType(), grd, in, a);
        }
        return "";
    }
}
