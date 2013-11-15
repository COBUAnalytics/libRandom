package org.cobu.randomsamplers;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.cobu.randomsamplers.weightedrecords.CentroidDistanceWeightedRecord;

import java.util.*;

public class KMeansSampler<P extends DoublePoint> {
    private final List<CentroidCluster<P>> centroids;
    private int sampleSize;
    private Random random;
    private final long populationSize;
    private Iterable<P> factory;

    public KMeansSampler(Random random, int numberOfCentroids, int sampleSize, Collection<P> factory) {
        this(random, numberOfCentroids, sampleSize, factory, factory.size());
    }

    public KMeansSampler(Random random, int numberOfCentroids, int sampleSize, Iterable<P> factory, long populationSize) {
        this.populationSize = populationSize;
        this.centroids = new ArrayList<CentroidCluster<P>>();
        this.sampleSize = sampleSize;
        this.factory = factory;
        this.random = random;

        checkReservoirSmallerThanTotal();

        findClusterCentroids(numberOfCentroids);
    }

    public KMeansSampler(Random random, List<CentroidCluster<P>> centroids, int sampleSize, Collection<P> factory) {
        this(random, centroids, sampleSize, factory, factory.size());
    }

    public KMeansSampler(Random random, List<CentroidCluster<P>> centroids, int sampleSize, Iterable<P> factory, long populationSize) {

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

    public List<P> samples() {
        List<P> samples = new ArrayList<P>();
        if (sampleSize > 0) {
            ReservoirSamplerWOR<CentroidDistanceWeightedRecord<P>> sampler =
                    new ReservoirSamplerWOR<CentroidDistanceWeightedRecord<P>>(random, sampleSize);
            final CentroidCluster[] centroids = this.centroids.toArray(new CentroidCluster[this.centroids.size()]);
            for (P vectorEntries : factory) {
                CentroidDistanceWeightedRecord<P> cdwr = new CentroidDistanceWeightedRecord<P>(centroids, vectorEntries);
                sampler.add(cdwr);
            }

            samples = new ArrayList<P>(sampleSize);
            for (int i = 0; i < sampleSize; i++) {
                P record = sampler.getSamples().get(i).getRecord();
                samples.add(record);

            }
        }

        return samples;
    }

    private CentroidCluster<P> travelThroughDataToPickNextCentroid() {
        ReservoirSamplerWOR<CentroidDistanceWeightedRecord> sampler =
                new ReservoirSamplerWOR<CentroidDistanceWeightedRecord>(random, 1);
        final CentroidCluster[] currentClusters = centroids.toArray(new CentroidCluster[centroids.size()]);
        for (P vectorEntries : factory) {
            CentroidDistanceWeightedRecord<P> cdwr = new CentroidDistanceWeightedRecord<P>(currentClusters, vectorEntries);
            sampler.add(cdwr);
        }
        double[] centroidData = sampler.getSamples().get(0).getRecord().getPoint();
        return new CentroidCluster<P>(new DoublePoint(centroidData));
    }

    // Once all centroids have been selected, rerun to get sample of size R

    public List<CentroidCluster<P>> getCentroids() {
        return Collections.unmodifiableList(centroids);
    }

}
