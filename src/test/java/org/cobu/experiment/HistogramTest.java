package org.cobu.experiment;

import junit.framework.Assert;
import org.junit.Test;

public class HistogramTest {
    @Test
    public void testOneElement(){
        Histogram<Integer> histogram = new Histogram<Integer>();
        histogram.add(0);
        double p= histogram.sampleProbabilityDensityFunction().values().iterator().next();
        Assert.assertEquals(1.0,p,0.0);
    }

    @Test
    public void testTwoElements(){
        Histogram<Integer> histogram = new Histogram<Integer>();
        histogram.add(0);
        histogram.add(1);
        double p= histogram.sampleProbabilityDensityFunction().values().iterator().next();
        Assert.assertEquals(0.5,p,0.0);
    }
}
