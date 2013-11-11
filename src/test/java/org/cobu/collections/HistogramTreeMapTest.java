package org.cobu.collections;

import junit.framework.Assert;
import org.junit.Test;

public class HistogramTreeMapTest {
    @Test
    public void testOneElement(){
        HistogramTreeMap<Integer> histogramTreeMap = new HistogramTreeMap<Integer>();
        histogramTreeMap.add(0);
        double p=histogramTreeMap.sampleProbabilityDensityFunction().values().iterator().next();
        Assert.assertEquals(1.0,p,0.0);
    }

    @Test
    public void testTwoElements(){
        HistogramTreeMap<Integer> histogramTreeMap = new HistogramTreeMap<Integer>();
        histogramTreeMap.add(0);
        histogramTreeMap.add(1);
        double p=histogramTreeMap.sampleProbabilityDensityFunction().values().iterator().next();
        Assert.assertEquals(0.5,p,0.0);
    }
}
