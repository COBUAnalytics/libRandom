package org.cobu.randomsamplers;

import org.cobu.random.MersenneTwisterRandom;
import org.cobu.randomsamplers.weightedrecords.WeightedRecord;
import org.cobu.randomsamplers.weightedrecords.WeightedRecordAdapter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by HY on 12/19/13.
 */
public class RerservoirSamplerWRTest {
    private List<WeightedRecord> population = new ArrayList<WeightedRecord>();
    @Before
    public void setup() {
        for (int i = 0; i < 4; i++) {
            population.add(new WeightedRecordAdapter(i));
        }
    }
    @Test
    public void reservoirSizeOfOneStatistialTest() {
        double[] histogram = new double[4];
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ReservoirSamplerWORFF<WeightedRecord> rswor = new ReservoirSamplerWORFF<>(new MersenneTwisterRandom(), 1);
            for (WeightedRecord p : population) {
                rswor.add(p);
            }
            List<WeightedRecord> sample = rswor.getSamples();
            assertEquals(1, sample.size());
            int weight = (int) sample.get(0).getWeight();
            histogram[weight] = histogram[weight] + 1;
        }
        for (int i = 0; i < histogram.length; i++) {
            histogram[i]= histogram[i]/count;
            System.out.println(histogram[i]);
        }
        double[] expected = {0, 1.0/6, 2.0/6, 3.0/6};
        assertArrayEquals(expected, histogram, 0.01);
    }

    @Test
    public void reservoirSizeOfTwwoStatistialTest() {
        double frequency11 = 0;
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ReservoirSamplerWR<WeightedRecord> rswor = new ReservoirSamplerWR<>(new MersenneTwisterRandom(), 2);
            for (WeightedRecord p : population) {
                rswor.add(p);
            }
            List<WeightedRecord> sample = rswor.getSamples();
            assertEquals(2, sample.size());
            if((Double.compare(sample.get(0).getWeight(),2)==0) && (Double.compare(sample.get(1).getWeight(),2.0)==0) ) frequency11++;

        }

       frequency11 /= count;
        double expected = 4.0/36;

        assertEquals(expected, frequency11, 0.01);
    }
}
