package org.cobu.collections;

import junit.framework.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class KeyTest {

    @Test
    public void bothEmpty(){
        Key<Integer> keyOne = new Key<Integer>(new LinkedList<Integer>());
        System.out.println(keyOne.compareTo(keyOne));
    }

    @Test
    public void sizeOneDifferent(){
        Key<String> one = new Key<String>("1");
        Key<String> two = new Key<String>("2");
        Assert.assertEquals(1,one.compareTo(two));
    }

    @Test
    public void bothEqual(){
       Key<String>key = new Key<String>("1,2,3".split(","));
        int c=key.compareTo(key);
        Assert.assertEquals(0, c);
    }

    @Test
    public void anotherTest(){
        Key<String>key1 = new Key<String>("1,2".split(","));
        Key<String>key2 = new Key<String>("1,3".split(","));
        Assert.assertEquals(1,key1.compareTo(key2));
    }

    @Test
    public void yetAnotherTest(){
        Key<String>key1 = new Key<String>("1,2".split(","));
        Key<String>key2 = new Key<String>("1".split(","));
        Assert.assertEquals(-1,key1.compareTo(key2));
    }
}

