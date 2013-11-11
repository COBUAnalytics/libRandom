package org.cobu.randomsamplers;

import java.util.Random;
import java.util.TreeSet;

public class ReservoirSamplerWOR {
    private final TreeSet<ScoredWeightedRecord> weightedRecords = new TreeSet<ScoredWeightedRecord>();
    private final Random random;
    private final int reservoirSize;


    public ReservoirSamplerWOR(Random random, int reservoirSize) {
        this.random = random;
        this.reservoirSize = reservoirSize;
    }

    public void add(WeightedRecord weightedRecord) {
        ScoredWeightedRecord newScore = new ScoredWeightedRecord(nextRandomDouble(), weightedRecord);

        boolean notEnoughValues = weightedRecords.size() < reservoirSize;
        boolean smallerThanCurrentMax = weightedRecords.last().compareTo(newScore) > 0;
        if (notEnoughValues || smallerThanCurrentMax) {
            weightedRecords.add(newScore);
            removeMaxValueIfOverReservoirSize();
        }

    }

    public WeightedRecord[] getSamples() {
        return weightedRecords.toArray(new WeightedRecord[weightedRecords.size()]);
    }

    protected double nextRandomDouble() {
            return random.nextDouble();
        }

    private void removeMaxValueIfOverReservoirSize() {
        if (weightedRecords.size() > reservoirSize) {
            ScoredWeightedRecord evictee=weightedRecords.last();
            weightedRecords.remove(evictee);
        }
    }
}
