package org.cobu.experiment;

import java.util.*;

public class Event<K extends Comparable<K>> implements Comparable<Event<K>>,Iterable<K>{
    private final List<K> list;
    private final SampleType sampleType;

    private static <K> List<K> createKey(K... elements){
        List<K>list = new ArrayList<K>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    private static <K> void checkRepetitions(List<K> elements){
        HashSet<K> set = new HashSet<K>();
        for(K k:elements){
            if(set.contains(k))throw new IllegalArgumentException("Elements cannot be repeated.");
            set.add(k);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return list.iterator();
    }

    public enum SampleType{
        WithReplacement,WithoutReplacement
    }

    protected Event(SampleType sampleType,List<K> list){
        this.sampleType = sampleType;
        this.list = new ArrayList(list);
        if(sampleType.equals(SampleType.WithoutReplacement)){
            checkRepetitions(this.list);
        }
    }

    public static <K extends Comparable<K>> Event<K> createWithReplacement(List<K> key){
        return new Event<K>(SampleType.WithReplacement,key);
    }

    public static <K extends Comparable<K>> Event<K> createWithoutReplacement(List<K> key){
        return new Event<K>(SampleType.WithoutReplacement,key);
    }


    public static <K extends Comparable<K>> Event<K> createWithReplacement(K... array){
        return new Event<K>(SampleType.WithReplacement,createKey(array));
    }

    public static <K extends Comparable<K>> Event<K> createWithoutReplacement(K... array){
        return new Event<K>(SampleType.WithoutReplacement,createKey(array));
    }

    private static int sign(int x){
        return x>0?1:(x<0?-1:0);
    }

    @Override
    public int compareTo(Event<K> o) {
        Iterator<K> thisIterator= list.iterator();
        Iterator<K> otherIterator=o.list.iterator();
        while(thisIterator.hasNext()&&otherIterator.hasNext()){
            int sign = sign(thisIterator.next().compareTo(otherIterator.next()));
            if(sign!=0)return sign;
        }
        if(!thisIterator.hasNext()&&!otherIterator.hasNext())return 0;
        else{
            if(thisIterator.hasNext()){
                return 1;
            }else{
                return -1;
            }
        }
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    @Override
    public String toString(){
        return list.toString();
    }
}
