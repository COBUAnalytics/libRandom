package org.cobu.randomsamplers;

import no.uib.cipr.matrix.Vector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.cobu.collections.iterablefactories.VectorIterableAbstractFactory;

import java.util.Random;

public class KMeansSampler {
    private final Random random;
    private final int numberOfCentroids;
    private final Iterable<? extends Vector> factory;


    public KMeansSampler(Random random, int numberOfCentroids, Iterable<? extends Vector> factory) {
        this.random = random;
        this.numberOfCentroids = numberOfCentroids;
        this.factory = factory;
    }
    // Run across all data with sampler on reservoir size of 1 for each of K clusters
    // at first centroid count is zero (use 1 as weight to get random point)
    // after that, number of centroids you have already selected
    // Once all centroids have been selected, rerun to get sample of size R

    public static void main(String[] args) {
        ReservoirSamplerWOR rswor = new ReservoirSamplerWOR(new MersenneTwisterRandom(3234), 1);
        CentroidCluster[] currentClusters = new CentroidCluster[0];


//        new CentroidDistanceWeightedRecord(currentClusters, )
//        rswor.add(dataVector);



        WeightedRecord[] samples = rswor.getSamples();
    }

    protected double nextRandomDouble() {
        return random.nextDouble();
    }

    public Vector[] getCentroids() {
        return null;
    }
}
