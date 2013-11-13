package org.cobu.collections;

import org.junit.Test;

import java.util.LinkedList;

public class KeyTest {
    @Test
    public void bothEmpty(){
        Key<Integer> keyOne = new Key<Integer>(new LinkedList<Integer>());
//        System.out.println(keyOne.compareTo(keyOne));
    }
}
