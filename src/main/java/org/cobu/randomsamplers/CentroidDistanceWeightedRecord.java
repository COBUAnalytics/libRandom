package org.cobu.randomsamplers;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;


public class CentroidDistanceWeightedRecord implements WeightedRecord {
    private final double weight;
    private final CentroidCluster[] currentClusters;
    private final DenseVector vector;

    public CentroidDistanceWeightedRecord(CentroidCluster[] currentClusters, DenseVector dataVector) {
        this.currentClusters = currentClusters;
        this.vector = dataVector;
        if (currentClusters.length == 0) {
            weight = 1.0;
        } else {
            double distanceToNearestCentroid = Double.MAX_VALUE;

            for (int i = 0; i < currentClusters.length; i++) {

                double[] centroid = currentClusters[i].getCenter().getPoint();
                if (centroid.length != dataVector.size()) {
                    throw new IllegalArgumentException();
                }

                double distanceToCurrentCentroid = distanceToCurrentCentroid(dataVector, centroid);

                if(distanceToNearestCentroid> distanceToCurrentCentroid){
                    distanceToNearestCentroid = distanceToCurrentCentroid;
                }
            }


            weight = distanceToNearestCentroid;
        }

    }

    private double distanceToCurrentCentroid(DenseVector dataVector, double[] centroid) {
        return new DenseVector(dataVector).add(-1.0, new DenseVector(centroid)).norm(Vector.Norm.Two);
    }

    private int findClosestCenter() {
        int closestCenterIndex = 0;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0, currentClustersLength = currentClusters.length; i < currentClustersLength; i++) {
            CentroidCluster currentCluster = currentClusters[i];
            double[] center = currentCluster.getCenter().getPoint();
            double difference = new DenseVector(vector).add(-1, new DenseVector(center)).norm(Vector.Norm.Two);
            if (minDistance > difference) {
                minDistance = difference;
                closestCenterIndex = i;
            }
        }

        return closestCenterIndex;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}

