package org.cobu.randomsamplers;

import org.cobu.randomsamplers.weightedrecords.ScoredWeightedRecord;
import org.cobu.randomsamplers.weightedrecords.WeightedRecord;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: HY
 * Date: 11/14/13
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */

class ScoredWeightedRecordComparator implements Comparator<ScoredWeightedRecord> {
    @Override
    public int compare(ScoredWeightedRecord o1, ScoredWeightedRecord o2) {
        return o2.compareTo(o1);
    }
}

public class ReservoirSamplerWORFF<T extends WeightedRecord> {
    private final PriorityQueue<ScoredWeightedRecord<T>> weightedRecords = new PriorityQueue<>(5, new ScoredWeightedRecordComparator());
    private double F;
    private double y;
    private double r;
    private final Random random;
    private final int reservoirSize;

    public ReservoirSamplerWORFF(Random random, int reservoirSize) {
        this.random = random;
        this.reservoirSize = reservoirSize;
        this.F = 0;
        this.y = 0;
    }

    protected double nextRandomDouble() {
        return random.nextDouble();
    }

    public List<T> getSamples() {
        List<T> toReturn = new ArrayList<T>(weightedRecords.size());
        for (ScoredWeightedRecord<T> weightedRecord : weightedRecords) {
            toReturn.add(weightedRecord.getRecord());
        }
        return toReturn;
    }

    public void add(T weightedRecord) {
        if (weightedRecords.size() < this.reservoirSize) {
            ScoredWeightedRecord<T> newScore = new ScoredWeightedRecord<T>(nextRandomDouble(), weightedRecord);
            weightedRecords.add(newScore);
            if (weightedRecords.size() == this.reservoirSize) initializeFRY();
        } else {
            double weight = weightedRecord.getWeight();
            this.F += weight;
            if (this.F > this.y) {
                double v = 1 - nextRandomDouble() * (1 - Math.exp(-r * weight));
                ScoredWeightedRecord<T> newScore = new ScoredWeightedRecord<T>(v, weightedRecord);
                weightedRecords.remove(weightedRecords.peek());
                weightedRecords.add(newScore);
                this.F = 0;
                y = -Math.log(nextRandomDouble())/newScore.getScore();
                r = newScore.getScore();
            }
        }
    }

    private void initializeFRY() {
        F = 0;
        r = weightedRecords.peek().getScore();
        y = -Math.log(nextRandomDouble()) / r;
    }

    public static void main(String[] args) {
        exp1();
    }

    public static void exp1() {
        PriorityQueue<ScoredWeightedRecord> p = new PriorityQueue<>(5, new ScoredWeightedRecordComparator());
        WeightedRecord w1 = new WeightedRecord() {
            @Override
            public double getWeight() {
                return 1;
            }
        };
        WeightedRecord w2 = new WeightedRecord() {

            @Override
            public double getWeight() {
                return 2;
            }
        };
        ScoredWeightedRecord<WeightedRecord> sw1 = new ScoredWeightedRecord<>(0.5, w1);
        ScoredWeightedRecord<WeightedRecord> sw2 = new ScoredWeightedRecord<>(0.5, w2);
        p.add(sw1);
        p.add(sw2);
        System.out.println(p.peek().getRecord().getWeight());
        System.out.println(Double.compare(2, 3));
        p.remove(p.peek());
        System.out.println(p.peek().getRecord().getWeight());
    }

}
