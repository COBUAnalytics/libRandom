package org.cobu.randomsamplers;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.cobu.randomsamplers.weightedrecords.CentroidDistanceWeightedRecord;

import java.util.*;

public class KMeansSampler {
    private final List<CentroidCluster<DoublePoint>> centroids;
    private int sampleSize;
    private Random random;
    private final long populationSize;
    private Iterable<DoublePoint> factory;

    public KMeansSampler(Random random, int numberOfCentroids, int sampleSize, Collection<DoublePoint> factory) {
        this(random, numberOfCentroids, sampleSize, factory, factory.size());
    }

    public KMeansSampler(Random random, int numberOfCentroids, int sampleSize, Iterable<DoublePoint> factory, long populationSize) {
        this.populationSize = populationSize;
        this.centroids = new ArrayList<CentroidCluster<DoublePoint>>();
        this.sampleSize = sampleSize;
        this.factory = factory;
        this.random = random;

        checkReservoirSmallerThanTotal();

        findClusterCentroids(numberOfCentroids);
    }

    public KMeansSampler(Random random, List<CentroidCluster<DoublePoint>> centroids, int sampleSize, Collection<DoublePoint> factory) {
        this(random, centroids, sampleSize, factory, factory.size());
    }

    public KMeansSampler(Random random, List<CentroidCluster<DoublePoint>> centroids, int sampleSize, Iterable<DoublePoint> factory, long populationSize) {

        this.centroids = centroids;
        this.sampleSize = sampleSize;
        this.factory = factory;
        this.random = random;
        this.populationSize = populationSize;
        checkReservoirSmallerThanTotal();
    }

    private void checkReservoirSmallerThanTotal() {
        if (sampleSize > populationSize){
            throw new IllegalStateException("Sample size cannot be larger than population size");
        }
    }

    private void findClusterCentroids(int numberOfClusters) {
        for (int i = 0; i < numberOfClusters; i++) {
            centroids.add(travelThroughDataToPickNextCentroid());
        }
    }

    private DoublePoint[] sampleUsingCentroids() {
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

    private CentroidCluster<DoublePoint> travelThroughDataToPickNextCentroid() {
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
        return sampleUsingCentroids();
    }
}
