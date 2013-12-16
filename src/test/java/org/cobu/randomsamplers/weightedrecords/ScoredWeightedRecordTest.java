package org.cobu.randomsamplers.weightedrecords;

import org.junit.Test;

import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class ScoredWeightedRecordTest {

    private ScoredWeightedRecord createScoredWeightedRecord(final double random, final double weight) {
        return new ScoredWeightedRecord<WeightedRecord>(random, new WeightedRecord() {
            @Override
            public double getWeight() {
                return weight;
            }
        });
    }
    @Test
    public void testPriorityQueue(){

    }
    @Test
    public void twoWeightedRecordsSameWeight() {
        ScoredWeightedRecord one = createScoredWeightedRecord(1.0, 1.0);
        ScoredWeightedRecord two = createScoredWeightedRecord(0.5, 1.0);
        assertEquals(-1, one.compareTo(two));
    }

    @Test
    public void twoScoresSmallIncrementOnExponent() {
        double random = 0.5;
        ScoredWeightedRecord one = createScoredWeightedRecord(random, 1.0);

        ScoredWeightedRecord slightlyLarger = createScoredWeightedRecord(random * random + 1.0e-14, 2.0);
        ScoredWeightedRecord slightlySmaller = createScoredWeightedRecord(random * random - 1.0e-14, 2.0);

        assertEquals(1, one.compareTo(slightlyLarger));
        assertEquals(-1, one.compareTo(slightlySmaller));
    }

}
