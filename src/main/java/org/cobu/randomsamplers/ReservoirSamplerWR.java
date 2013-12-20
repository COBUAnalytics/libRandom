package org.cobu.randomsamplers;

import org.cobu.randomsamplers.weightedrecords.ScoredWeightedRecord;
import org.cobu.randomsamplers.weightedrecords.WeightedRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by HY on 12/19/13.
 */
public class ReservoirSamplerWR<T extends WeightedRecord>{
    private final Random random;
    private final int reservoirSize;
    private double F;
    private final double[] Ft;
    private final List<T> weightedRecords;

    public ReservoirSamplerWR(Random random, int reservoirSize) {
        this.random = random;
        this.reservoirSize = reservoirSize;
        F = 0;
        Ft = new double[reservoirSize];
        weightedRecords = new ArrayList<>(reservoirSize);


        //   System.out.println("reservoirSize"+ this.reservoirSize);
    }
    private double nextRandomDouble() {
        return random.nextDouble();
    }
    public void add(T weightedRecord) {
        F += weightedRecord.getWeight();
        if(weightedRecords.isEmpty()){
            for(int i = 0; i < reservoirSize; i++){
                weightedRecords.add(weightedRecord);
                Ft[i] = F/nextRandomDouble();
            }
        }
        else{
            for(int i = 0; i < reservoirSize; i++){
              if(F > Ft[i]) {
                  weightedRecords.set(i, weightedRecord);
                  Ft[i] = F/nextRandomDouble();
              }
            }
        }

    }
    public List<T> getSamples() {
        List<T> toReturn = new ArrayList<>(weightedRecords);
        return toReturn;
    }

}
