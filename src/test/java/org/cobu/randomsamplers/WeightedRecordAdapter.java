package org.cobu.randomsamplers;


public class WeightedRecordAdapter implements WeightedRecord {

    private final double weight;

    public WeightedRecordAdapter(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}