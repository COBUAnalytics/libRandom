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
    private double F = 0;

    @Before
    public void setup() {
        for (int i = 1; i <= 9; i++) {
            population.add(new WeightedRecordAdapter(i));
            F += i;
        }
    }

    @Test
    public void reservoirSizeOfOneStatisticalTest() {
        double frequency = 0;
        double value = 3;
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ReservoirSamplerWORFF<WeightedRecord> rswor = new ReservoirSamplerWORFF<>(new MersenneTwisterRandom(), 1);
            for (WeightedRecord p : population) {
                rswor.add(p);
            }
            List<WeightedRecord> sample = rswor.getSamples();
            assertEquals(1, sample.size());
            if (Double.compare(sample.get(0).getWeight(), value) == 0) {
                frequency++;
            }

        }
        frequency /= count;
        double expected = value / F;
        assertEquals(expected, frequency, 0.01);
    }

    @Test
    public void reservoirSizeOfTwoStatisticalTest() {
        double frequency11 = 0;
        int count = 10000;
        double[] r = {2, 3};
        for (int i = 0; i < count; i++) {
            ReservoirSamplerWORFF<WeightedRecord> rswor = new ReservoirSamplerWORFF<>(new MersenneTwisterRandom(), 2);
            for (WeightedRecord p : population) {
                rswor.add(p);
            }
            List<WeightedRecord> sample = rswor.getSamples();
            assertEquals(2, sample.size());
            if ((Double.compare(sample.get(0).getWeight(), r[0]) == 0) && (Double.compare(sample.get(1).getWeight(), r[1]) == 0))
                frequency11++;

        }

        frequency11 /= count;
        double expected = r[0] * r[1] / (F * (F - r[0]));

        assertEquals(expected, frequency11, 0.01);
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
