package org.cobu.experiment;

import junit.framework.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class EventTest {

    @Test
    public void bothEmpty(){
        Event<Integer> keyOne = new Event<Integer>(Event.SampleType.WithReplacement,new LinkedList<Integer>());
        Assert.assertEquals(0,keyOne.compareTo(keyOne));
    }

    @Test
    public void sizeOneDifferent(){
        Event<String> one = Event.<String>createWithReplacement("1");
        Event<String> two = Event.<String>createWithReplacement("2");
        Assert.assertEquals(-1,one.compareTo(two));
    }

    @Test
    public void bothEqual(){
       Event<String> key = Event.<String>createWithReplacement("1,2,3".split(","));
        int c=key.compareTo(key);
        Assert.assertEquals(0, c);
    }

    @Test
    public void anotherTest(){
        Event<String> key1 = Event.<String>createWithReplacement("1,2".split(","));
        Event<String> key2 = Event.<String>createWithReplacement("1,3".split(","));
        Assert.assertEquals(-1,key1.compareTo(key2));
    }

    @Test
    public void yetAnotherTest(){
        Event<String> key1 = Event.<String>createWithReplacement("1,2".split(","));
        Event<String> key2 = Event.<String> createWithReplacement("1");
        Assert.assertEquals(1,key1.compareTo(key2));
    }

    private static int sign(int x){
        return x>0?1:(x<0?-1:0);
    }
}

