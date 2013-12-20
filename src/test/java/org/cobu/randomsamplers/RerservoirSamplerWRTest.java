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
    private double totalWeight = 0;
    @Before
    public void setup() {
        for (int i = 0; i < 4; i++) {
            population.add(new WeightedRecordAdapter(i));
            totalWeight += i;
        }
    }
    @Test
    public void reservoirSizeOfOneStatistialTest() {
        double result = 3;
        double frequency = 0;
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ReservoirSamplerWORFF<WeightedRecord> rswor = new ReservoirSamplerWORFF<>(new MersenneTwisterRandom(), 1);
            for (WeightedRecord p : population) {
                rswor.add(p);
            }
            List<WeightedRecord> sample = rswor.getSamples();
            assertEquals(1, sample.size());
            if(Double.compare(result, sample.get(0).getWeight())==0) frequency++;
        }
        frequency /= count;
        double expected = result/totalWeight;
        assertEquals(expected, frequency, 0.01);
    }

    @Test
    public void reservoirSizeOfTwwoStatistialTest() {
        double frequency = 0;
        int count = 10000;
        double[] results = {2, 2};
        for (int i = 0; i < count; i++) {
            ReservoirSamplerWR<WeightedRecord> rswor = new ReservoirSamplerWR<>(new MersenneTwisterRandom(), 2);
            for (WeightedRecord p : population) {
                rswor.add(p);
            }
            List<WeightedRecord> sample = rswor.getSamples();
            assertEquals(2, sample.size());
            if((Double.compare(sample.get(0).getWeight(), results[0])==0) && (Double.compare(sample.get(1).getWeight(),results[1])==0) ) frequency++;

        }

        frequency /= count;
        double expected = results[0]*results[1]/(totalWeight*totalWeight);
        assertEquals(expected, frequency, 0.01);
    }
}
