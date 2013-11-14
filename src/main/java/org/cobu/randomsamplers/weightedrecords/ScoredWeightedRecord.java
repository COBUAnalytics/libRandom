package org.cobu.randomsamplers.weightedrecords;

public class ScoredWeightedRecord<T extends WeightedRecord> implements Comparable<ScoredWeightedRecord> {
    private final T record;
    private final double score;

    public ScoredWeightedRecord(double randomDouble, T record) {
        this.record = record;
        this.score = -Math.log(randomDouble) / record.getWeight();
    }

    @Override
    public int compareTo(ScoredWeightedRecord o) {
        return Double.compare(score,o.score);
    }

    public T getRecord() {
        return record;
    }
}
