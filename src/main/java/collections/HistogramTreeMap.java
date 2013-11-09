package collections;

import java.util.Comparator;
import java.util.TreeMap;

public class HistogramTreeMap<Key,C extends Comparator<Key>> {
    TreeMap<Key,Integer> count = new TreeMap<Key,Integer>();
    public HistogramTreeMap(){}
    public void add(Key key){
        if(!count.containsKey(key)){
            count.put(key,0);
        }
        count.put(key,count.get(key)+1);
    }
    public TreeMap<Key,Double> samplePDF(){
        TreeMap<Key,Double> samplePDF=new TreeMap<Key, Double>();
        int total =0;
        for(Integer i:count.values()){
            total+=i;
        }
        double scale =1.0/total;
        for(Key key:count.keySet()){
            double value=count.get(key)*scale;
            samplePDF.put(key,value);
        }
        return samplePDF;
    }
}
