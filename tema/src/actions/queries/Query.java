package actions.queries;

import actions.ActorAverageCalculator;
import actions.commands.Grader;
import actions.MovieIndexFinder;
import actions.SerialIndexFinder;
import actions.Sorter;
import fileio.*;

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
        QueryFilter video = new QueryFilter(a);
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
        DurationCalculator dc = new DurationCalculator();
        QueryBuilder qb = new QueryBuilder();

        for(ShowInput show : in.getSerials()) {
            result.put(show.getTitle(), (double)dc.totalDuration(show));
        }

        return qb.buildQuery(result,order,a,in,a.getNumber(),1);
    }

    public  String favoriteM(int n, String order, Input in, ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        FavorabilityCalculator fc = new FavorabilityCalculator();
        QueryBuilder qb = new QueryBuilder();

        int favor;

        for(MovieInputData m : in.getMovies()) {
            favor = fc.getFavor(m.getTitle(),in);
            if(favor != 0) {
                result.put(m.getTitle(), (double)favor);
            }
        }

        return qb.buildQuery(result,order,a,in,n,0);
    }

    public  String favoriteS(int n, String order, Input in, ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        FavorabilityCalculator fc = new FavorabilityCalculator();
        QueryBuilder qb = new QueryBuilder();

        int favor;

        for(SerialInputData ser : in.getSerials()) {
            favor = fc.getFavor(ser.getTitle(),in);
            if(favor != 0) {
                result.put(ser.getTitle(), (double)favor);
            }
        }

        return qb.buildQuery(result,order,a,in,n,1);
    }

    public String mostViewM(int n, String order, Input in, ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        ViewsCalculator vc = new ViewsCalculator();
        QueryBuilder qb = new QueryBuilder();
        int views;

        for(MovieInputData movie : in.getMovies()) {
            views = vc.getViews(movie.getTitle(),in);
            if(views != 0) {
                result.put(movie.getTitle(), (double)views);
            }
        }
        return qb.buildQuery(result,order,a,in,n,0);
    }

    public String mostViewS(int n, String order, Input in, ActionInputData a) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        ViewsCalculator vc = new ViewsCalculator();
        QueryBuilder qb = new QueryBuilder();
        int views;

        for(SerialInputData movie : in.getSerials()) {
            views = vc.getViews(movie.getTitle(),in);
            if(views != 0) {
                result.put(movie.getTitle(), (double)views);
            }
        }
        return qb.buildQuery(result,order,a,in,n,2);
    }

    public String average(int n, String order, Input in, ActionInputData a, Grader grd) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        ActorAverageCalculator ac = new ActorAverageCalculator();
        QueryBuilder qb = new QueryBuilder();
        Double average;
        for(ActorInputData act : in.getActors()) {
            average = ac.getAverage(grd,act);
            if(average != 0d) {
                result.put(act.getName(), average);
            }
        }
        return qb.buildQuery(result,order,a,in,n,-1);
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
            return longestS(a.getNumber(),a.getSortType(),in,a);
        }
        else if(a.getObjectType().equals("movies") && (a.getCriteria().equals("favorite")
                                                        || a.getCriteria().equals("favourite"))) {
            return favoriteM(a.getNumber(),a.getSortType(),in,a);
        }
        else if(a.getObjectType().equals("shows") && (a.getCriteria().equals("favorite")
                || a.getCriteria().equals("favourite"))) {
            return favoriteS(a.getNumber(),a.getSortType(),in,a);
        }
        else if(a.getObjectType().equals("movies") && a.getCriteria().equals("most_viewed")) {
            return mostViewM(a.getNumber(),a.getSortType(),in,a);
        }
        else if(a.getObjectType().equals("shows") && a.getCriteria().equals("most_viewed")) {
            return mostViewS(a.getNumber(),a.getSortType(),in,a);
        }
        else if(a.getObjectType().equals("actors") && a.getCriteria().equals("average")) {
            return average(a.getNumber(),a.getSortType(),in,a,grd);
        }

        return "";
    }
}
