package org.cobu.collections;

import java.util.Iterator;

public class Key<K extends Comparable<K>> implements Comparable<Key<K>>{
    private final Iterable<K> keyIterable;

    public Key(Iterable<K> keyIterable){
        this.keyIterable = keyIterable;

    }

    @Override
    public int compareTo(Key<K> o) {
        Iterator<K> thisIterator=keyIterable.iterator();
        Iterator<K> otherIterator=o.keyIterable.iterator();
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
