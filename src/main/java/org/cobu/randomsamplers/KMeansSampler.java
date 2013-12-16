package org.cobu.randomsamplers;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.cobu.randomsamplers.weightedrecords.CentroidDistanceWeightedRecord;

import java.util.*;

public class KMeansSampler<P extends DoublePoint> {
    protected final List<CentroidCluster<P>> centroids;
    protected int sampleSize;
    protected Random random;
    protected final long populationSize;
    protected Iterable<P> factory;
    protected DistanceMeasure distanceMeasure;


    public KMeansSampler(Random random, int numberOfCentroids, int sampleSize, Iterable<P> factory, long populationSize, DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
        this.populationSize = populationSize;
        this.centroids = new ArrayList<CentroidCluster<P>>();
        this.sampleSize = sampleSize;
        this.factory = factory;
        this.random = random;

        checkReservoirSmallerThanTotal();

        findClusterCentroids(numberOfCentroids);
    }


    public KMeansSampler(Random random, List<CentroidCluster<P>> centroids, int sampleSize, Iterable<P> factory, long populationSize, DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
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

    protected void findClusterCentroids(int numberOfClusters) {
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



            List<CentroidDistanceWeightedRecord<P>> samplerResults = sampler.getSamples();
            if(samplerResults.size() != sampleSize){
                System.out.println(String.format("Expected %d samples but got %d", sampleSize, samplerResults.size()));
            }
            samples = new ArrayList<P>(samplerResults.size());
            for (CentroidDistanceWeightedRecord<P> record : samplerResults) {
                samples.add(record.getRecord());
            }
        }
        return samples;
    }

    private CentroidCluster<P> travelThroughDataToPickNextCentroid() {
        ReservoirSamplerWOR<CentroidDistanceWeightedRecord> sampler =
                new ReservoirSamplerWOR<CentroidDistanceWeightedRecord>(random, 1);
        final CentroidCluster[] currentClusters = centroids.toArray(new CentroidCluster[centroids.size()]);
        for (P vectorEntries : factory) {
            CentroidDistanceWeightedRecord<P> cdwr = new CentroidDistanceWeightedRecord<P>(currentClusters, vectorEntries, distanceMeasure);
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
