package actions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;


public class Sorter {
    /**
     * @param arr linkedMap to be sorted
     * @return the sorted linked map:
     *          1st criteria -> ascending order of values
     *          2nd criteria -> ascending alphabetical order of keys
     */
    public LinkedHashMap<String, Double> sortAscOrder(final LinkedHashMap<String, Double> arr) {
        // 1. get entrySet from LinkedHashMap object
        Set<Entry<String, Double>> entrySet = arr.entrySet();

        // 2. convert LinkedHashMap to List of Map.Entry
        List<Entry<String, Double>> entryList =
                new ArrayList<Map.Entry<String, Double>>(
                        entrySet);

        // 3. sort list of entries using Collections class'
        // utility method sort(ls, cmptr)
        Collections.sort(entryList,
                new Comparator<Map.Entry<String, Double>>() {

                    @Override
                    public int compare(final Entry<String, Double> o1,
                                       final Entry<String, Double> o2) {
                        if (o1.getValue().compareTo(o2.getValue()) == 0) {
                            return o1.getKey().compareTo(o2.getKey());
                        }
                        return o1.getValue().compareTo(o2.getValue());
                    }
                });

         /*4. iterating list and storing in LinkedHahsMap*/
        LinkedHashMap<String, Double> sorted = new LinkedHashMap<>();
        for (Entry<String, Double> map : entryList) {
            sorted.put(map.getKey(), map.getValue());
        }
        return sorted;
    }

    /**
     * @param arr linkedMap to be sorted
     * @return the sorted linked map:
     *          1st criteria -> descending order of values
     *          2nd criteria -> descending alphabetical order of keys
     */
    public LinkedHashMap<String, Double> sortDescOrder(final LinkedHashMap<String, Double> arr) {
        // 1. get entrySet from LinkedHashMap object
        Set<Entry<String, Double>> entrySet = arr.entrySet();

        // 2. convert LinkedHashMap to List of Map.Entry
        List<Entry<String, Double>> entryList =
                new ArrayList<Map.Entry<String, Double>>(
                        entrySet);


        // 3. sort list of entries using Collections class'
        // utility method sort(ls, cmptr)
        Collections.sort(entryList,
                new Comparator<Map.Entry<String, Double>>() {

                    @Override
                    public int compare(final Entry<String, Double> o1,
                                       final Entry<String, Double> o2) {
                        if (o1.getValue().compareTo(o2.getValue()) == 0) {
                            return o2.getKey().compareTo(o1.getKey());
                        }
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });

        /*4. iterating list and storing in LinkedHahsMap*/
        LinkedHashMap<String, Double> sorted = new LinkedHashMap<>();
        for (Entry<String, Double> map : entryList) {
            sorted.put(map.getKey(), map.getValue());
        }
        return sorted;
    }

    /**
     * @param arr linkedMap to be sorted
     * @return the sorted linked map:
     *          1st criteria -> descending order of values
     *          2nd criteria -> apparition order of keys
     */
    public LinkedHashMap<String, Double> sortNAlf(final LinkedHashMap<String, Double> arr) {
        // 1. get entrySet from LinkedHashMap object
        Set<Entry<String, Double>> entrySet = arr.entrySet();

        // 2. convert LinkedHashMap to List of Map.Entry
        List<Entry<String, Double>> entryList =
                new ArrayList<Map.Entry<String, Double>>(
                        entrySet);


        // 3. sort list of entries using Collections class'
        // utility method sort(ls, cmptr)
        Collections.sort(entryList,
                new Comparator<Map.Entry<String, Double>>() {

                    @Override
                    public int compare(final Entry<String, Double> o1,
                                       final Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });

        /*4. iterating list and storing in LinkedHahsMap*/
        LinkedHashMap<String, Double> sorted = new LinkedHashMap<>();
        for (Entry<String, Double> map : entryList) {
            sorted.put(map.getKey(), map.getValue());
        }
        return sorted;
    }
}
