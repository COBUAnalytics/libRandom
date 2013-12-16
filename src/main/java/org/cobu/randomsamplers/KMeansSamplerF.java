package org.cobu.randomsamplers;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.cobu.randomsamplers.weightedrecords.CentroidDistanceWeightedRecord;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by HY on 12/16/13.
 */
public class KMeansSamplerF<P extends DoublePoint>  extends KMeansSampler<P> {

    public KMeansSamplerF(Random random, int numberOfCentroids, int sampleSize, Iterable<P> factory, long populationSize, DistanceMeasure distanceMeasure) {
        super(random, numberOfCentroids, sampleSize, factory, populationSize, distanceMeasure);
    }
    @Override
    protected void findClusterCentroids(int numberOfClusters) {
        HashMap<Integer, Double> minDistanceMap = new HashMap<>();

        for (int i = 0; i < numberOfClusters; i++) {
            ReservoirSamplerWOR<CentroidDistanceWeightedRecord> sampler =
                    new ReservoirSamplerWOR<CentroidDistanceWeightedRecord>(super.random, 1);
            int entryKey = 0;
            CentroidCluster<P> newAddedCluster = super.centroids.isEmpty() ? null : super.centroids.get(centroids.size()-1);
            for (P vectorEntries : super.factory) {
                double minDistanceBefore = minDistanceMap.containsKey(entryKey) ? minDistanceMap.get(entryKey) : (double) Integer.MAX_VALUE;
                CentroidDistanceWeightedRecord<P> cdwr = new CentroidDistanceWeightedRecord<P>(newAddedCluster, minDistanceBefore,  vectorEntries, distanceMeasure);
                if(i >0 && cdwr.getWeight() < minDistanceBefore) minDistanceMap.put(entryKey, cdwr.getWeight());
                sampler.add(cdwr);
                entryKey++;
            }
            double[] centroidData = sampler.getSamples().get(0).getRecord().getPoint();
            super.centroids.add(new CentroidCluster<P>(new DoublePoint(centroidData)));
        }

    }
}

