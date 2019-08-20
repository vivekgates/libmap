package org.unyde.mapintegrationlib.InternalNavigation.indoornav;

import java.util.*;
import java.util.Map.Entry;

public class Sorted_Beacon_Signal {

    public TreeMap<String, Double> sorted_map;
    public List<String> active_key;
    public List<Double> active_value;
    class ValueComparator implements Comparator<String> {
        Map<String, Double> base;

        public ValueComparator(Map<String, Double> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }

    public HashMap<String, Double> signal_hashmap,sorted_signal_hashmap;

    //public  Set<Entry<String,Double>> sorted_signal;

    public Sorted_Beacon_Signal() {
        signal_hashmap = new HashMap<String, Double>();
        sorted_signal_hashmap = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(signal_hashmap);
        sorted_map = new TreeMap<String, Double>(bvc);
        active_key = new ArrayList<String>();
        active_value = new ArrayList<Double>();

    }
    public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Double)val);
                    break;
                }

            }

        }
        return sortedMap;
    }
    public void apply_value_sorting()
    {
        sorted_map.clear();
        active_key.clear();
        active_value.clear();
        HashMap<String, Double> ranged_beacon_hashmap = new HashMap<String, Double>();
        for (Entry<String, Double> entry : signal_hashmap.entrySet()) {

                ranged_beacon_hashmap.put(entry.getKey(),entry.getValue());
                active_key.add(entry.getKey());
                active_value.add(entry.getValue());


        }
        sorted_map.putAll(ranged_beacon_hashmap);
        //sorted_map =signal_hashmap;
        //sorted_signal_hashmap=sortHashMapByValuesD(signal_hashmap);
        /*Comparator<Entry<String,Double>> valueComparator = new Comparator<Entry<String,Double>>() {
            @Override
            public int compare(Entry<String, Double> e1, Entry<String, Double> e2) {
                Double v1 = e1.getValue();
                Double v2 = e2.getValue();

                if(v1>v2)
                {
                    return 1;
                }
                else if(v2>v1)
                {
                    return -1;
                }

                return 0;


               // return v1.compareTo(v2);
            }

        };
        Set<Entry<String, Double>> entries = signal_hashmap.entrySet();

        // Sort method needs a List, so let's first convert Set to List in Java
        List<Entry<String, Double>> listOfEntries = new ArrayList<Entry<String, Double>>(entries);
        // sorting HashMap by values using comparator
        Collections.sort(listOfEntries, valueComparator);
        LinkedHashMap<String, Double> sortedByValue = new LinkedHashMap<String, Double>(listOfEntries.size());

        for(Entry<String, Double> entry : listOfEntries)
        {
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        Set<Entry<String, Double>> entrySetSortedByValue = sortedByValue.entrySet();

        //sorted_signal = entrySetSortedByValue;



        for(Entry<String, Double> mapping : entrySetSortedByValue)
        {
            sorted_signal_hashmap.put(mapping.getKey(),mapping.getValue());
            //System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
        }
*/

    }
}
