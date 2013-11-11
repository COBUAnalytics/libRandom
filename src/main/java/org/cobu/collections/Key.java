package org.cobu.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Key<K extends Comparable<K>> implements Comparable<Key<K>>{
    private final List<K> list;

    private static <K> List<K> createKey(K... elements){
        List<K>list = new ArrayList<K>(elements.length);
        for(K k:elements){
            list.add(k);
        }
        return list;
    }

    public Key(K... elements){
        this(Key.<K>createKey(elements));
    }
    public Key(List<K> list){
        this.list = list;
    }

    @Override
    public int compareTo(Key<K> o) {
        Iterator<K> thisIterator= list.iterator();
        Iterator<K> otherIterator=o.list.iterator();
        while(thisIterator.hasNext()&&otherIterator.hasNext()){
            int sign = thisIterator.next().compareTo(otherIterator.next());
            if(sign!=0)return sign;
        }
        if(!thisIterator.hasNext()&&!otherIterator.hasNext())return 0;
        else{
            if(thisIterator.hasNext()){
                return -1;
            }else{
                return 1;
            }
        }
    }
}
