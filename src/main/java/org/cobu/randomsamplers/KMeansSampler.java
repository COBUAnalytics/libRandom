package org.cobu.randomsamplers;

import org.apache.commons.math3.ml.clustering.CentroidCluster;

public class KMeansSampler {
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
}
