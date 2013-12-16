package org.cobu.randomsamplers;

import org.cobu.random.MersenneTwisterRandom;
import org.cobu.randomsamplers.weightedrecords.WeightedRecord;
import org.cobu.randomsamplers.weightedrecords.WeightedRecordAdapter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ReservoirSamplerWORTest {
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
        int count = 1000;
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
//        double[] expected = {0, 1.0/6, 2.0/6, 3.0/6};
//        assertArrayEquals(expected, histogram, 0.01);
    }

    @Test
    public void reservoirSizeOfOnePopulationSizeOfOne() {
        ReservoirSamplerWOR<WeightedRecord> rswor = new ArraySamples(1);
        WeightedRecord weightedRecord = new WeightedRecordAdapter(Double.NaN);
        rswor.add(weightedRecord);
        List<WeightedRecord> samples = rswor.getSamples();
        assertEquals(1, samples.size());
        assertSame(weightedRecord, samples.get(0));
    }

    @Test
    public void reservoirSizeOfOnePopulationSizeTwoSelectsLowestWeight() {
        final double[] probabilities = new double[]{0.5, 0.5};
        ReservoirSamplerWOR<WeightedRecord> rswor = new ArraySamples(probabilities);
        WeightedRecord smallerOne = new WeightedRecordAdapter(1.0);
        WeightedRecord biggerOne = new WeightedRecordAdapter(2.0);

        rswor.add(smallerOne);
        rswor.add(biggerOne);
        final List<WeightedRecord> samples = rswor.getSamples();
        assertEquals(1, samples.size());
        assertSame(biggerOne, samples.get(0));
    }


    private class ArraySamples extends ReservoirSamplerWOR<WeightedRecord> {

        private final double[] values;
        private int i;

        public ArraySamples(double... values) {
            super(null, 1);
            this.values = values;
        }

        @Override
        protected double nextRandomDouble() {
            return values[i++];
        }
    }

}
