package org.cobu.randomsamplers;

import org.cobu.randomsamplers.weightedrecords.ScoredWeightedRecord;
import org.cobu.randomsamplers.weightedrecords.WeightedRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class
        ReservoirSamplerWOR<T extends WeightedRecord> {
    private final TreeSet<ScoredWeightedRecord<T>> weightedRecords = new TreeSet<ScoredWeightedRecord<T>>();
    private final Random random;
    private final int reservoirSize;


    public ReservoirSamplerWOR(Random random, int reservoirSize) {
        this.random = random;
        this.reservoirSize = reservoirSize;
    }

    public void add(T weightedRecord) {
        ScoredWeightedRecord<T> newScore = new ScoredWeightedRecord<T>(nextRandomDouble(), weightedRecord);

        boolean notEnoughValues = weightedRecords.size() < reservoirSize;
        boolean smallerThanCurrentMax = !weightedRecords.isEmpty() && weightedRecords.last().compareTo(newScore) > 0;
        if (notEnoughValues || smallerThanCurrentMax) {
            weightedRecords.add(newScore);
            removeMaxValueIfOverReservoirSize();
        }

    }

    public List<T> getSamples() {
        List<T> toReturn = new ArrayList<T>(weightedRecords.size());
        for (ScoredWeightedRecord<T> weightedRecord : weightedRecords) {
            toReturn.add(weightedRecord.getRecord());
        }
        return toReturn;
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
