package org.cobu.randomsamplers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ReservoirSamplerWORTest {
    @Test
    public void reservoirSizeOfOnePopulationSizeOfOne() {
        ReservoirSamplerWOR rswor = new ArraySamples(new double[]{1});
        WeightedRecord weightedRecord = new WeightedRecordAdapter(Double.NaN);
        rswor.add(weightedRecord);
        WeightedRecord[] samples = rswor.getSamples();
        assertEquals(1, samples.length);
        assertSame(weightedRecord, samples[0]);
    }

    @Test
    public void reservoirSizeOfOnePopulationSizeTwoSelectsLowestWeight() {
        final double[] probabilities = new double[]{0.5, 0.5};
        ReservoirSamplerWOR rswor = new ArraySamples(probabilities);
        WeightedRecord biggerOne = new WeightedRecordAdapter(1.0);
        WeightedRecord smallerOne = new WeightedRecordAdapter(2.0);

        rswor.add(biggerOne);
        rswor.add(smallerOne);
        WeightedRecord[] samples = rswor.getSamples();
        assertEquals(1, samples.length);
        assertSame(smallerOne, samples[0]);
    }


    private class ArraySamples extends ReservoirSamplerWOR {

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
