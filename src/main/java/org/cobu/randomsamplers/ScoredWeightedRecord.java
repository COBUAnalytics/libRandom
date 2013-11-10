package org.cobu.randomsamplers;

public class ScoredWeightedRecord implements Comparable<ScoredWeightedRecord> {
    private final WeightedRecord record;
    private final double score;

    public ScoredWeightedRecord(double randomDouble, WeightedRecord record) {
        this.record = record;
        this.score = -Math.log(randomDouble) / record.getWeight();
    }

    @Override
    public int compareTo(ScoredWeightedRecord o) {
        return Double.compare(score,o.score);
    }

    public WeightedRecord getRecord() {
        return record;
    }
}
