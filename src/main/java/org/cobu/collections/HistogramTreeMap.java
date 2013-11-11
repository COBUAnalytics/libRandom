package org.cobu.collections;

import java.util.TreeMap;

public class HistogramTreeMap<Key extends Comparable<Key>> {
    TreeMap<Key,Integer> count = new TreeMap<Key,Integer>();
    public HistogramTreeMap(){}
    public void add(Key key){
        if(!count.containsKey(key)){
            count.put(key,0);
        }
        count.put(key,count.get(key)+1);
    }
    public TreeMap<Key,Double> sampleProbabilityDensityFunction(){
        TreeMap<Key,Double> sampleProbabilityDensityFunction=new TreeMap<Key, Double>();
        int total =0;
        for(Integer i:count.values()){
            total+=i;
        }
        double scale =1.0/total;
        for(Key key:count.keySet()){
            double value=count.get(key)*scale;
            sampleProbabilityDensityFunction.put(key, value);
        }
        return sampleProbabilityDensityFunction;
    }
}
