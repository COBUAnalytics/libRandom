package org.cobu.randomsamplers;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ReservoirSamplerWORTest {
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
