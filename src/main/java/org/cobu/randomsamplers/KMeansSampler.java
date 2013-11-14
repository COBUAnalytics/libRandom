package org.cobu.randomsamplers;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.cobu.randomsamplers.weightedrecords.CentroidDistanceWeightedRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansSampler {
    private final List<CentroidCluster<DoublePoint>> centroids;
    private int sampleSize;
    private final DoublePoint[] samples;

    public KMeansSampler(Random random, int numberOfClusters, int sampleSize, Iterable<DoublePoint> factory) {

        this.centroids = new ArrayList<CentroidCluster<DoublePoint>>();
        this.sampleSize = sampleSize;
        checkReservoirSmallerThanTotal(factory);

        findClusterCentroids(random, numberOfClusters, factory);
        this.samples = sampleUsingCentroids(random, factory);

    }

    private void checkReservoirSmallerThanTotal(Iterable<DoublePoint> factory) {
        int i = 0;
        for (DoublePoint doublePoint : factory) {
            i++;
        }
        if (this.sampleSize > i) throw new IllegalStateException();
    }

    private void findClusterCentroids(Random random, int numberOfClusters, Iterable<DoublePoint> factory) {
        for (int i = 0; i < numberOfClusters; i++) {
            centroids.add(travelThroughDataToPickNextCentroid(random, factory));
        }
    }

    private DoublePoint[] sampleUsingCentroids(Random random, Iterable<DoublePoint> factory) {
        DoublePoint[] samples = new DoublePoint[0];
        if (sampleSize > 0) {
            ReservoirSamplerWOR<CentroidDistanceWeightedRecord> sampler =
                    new ReservoirSamplerWOR<CentroidDistanceWeightedRecord>(random, sampleSize);
            final CentroidCluster[] centroids = this.centroids.toArray(new CentroidCluster[this.centroids.size()]);
            for (DoublePoint vectorEntries : factory) {
                CentroidDistanceWeightedRecord cdwr = new CentroidDistanceWeightedRecord(centroids, vectorEntries);
                sampler.add(cdwr);
            }

            samples = new DoublePoint[sampleSize];
            for (int i = 0; i < samples.length; i++) {
                samples[i] = new DoublePoint(sampler.getSamples().get(i).getRecord());

            }
        }
        return samples;
    }

    private CentroidCluster<DoublePoint> travelThroughDataToPickNextCentroid(Random random, Iterable<DoublePoint> factory) {
        ReservoirSamplerWOR<CentroidDistanceWeightedRecord> sampler =
                new ReservoirSamplerWOR<CentroidDistanceWeightedRecord>(random, 1);
        final CentroidCluster[] currentClusters = centroids.toArray(new CentroidCluster[centroids.size()]);
        for (DoublePoint vectorEntries : factory) {
            CentroidDistanceWeightedRecord cdwr = new CentroidDistanceWeightedRecord(currentClusters, vectorEntries);
            sampler.add(cdwr);
        }
        return new CentroidCluster<DoublePoint>(new DoublePoint(sampler.getSamples().get(0).getRecord()));
    }

    // Once all centroids have been selected, rerun to get sample of size R

    public List<CentroidCluster<DoublePoint>> getCentroids() {
        return Collections.unmodifiableList(centroids);
    }

    public DoublePoint[] samples() {
        return samples;
    }
}
