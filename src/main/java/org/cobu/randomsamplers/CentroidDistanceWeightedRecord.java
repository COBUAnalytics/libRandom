package org.cobu.randomsamplers;

import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;


public class CentroidDistanceWeightedRecord implements WeightedRecord {
    private double weight = 1.0;
    private double[] dataDouble;
    private DistanceMeasure distanceMeasure;
    private CentroidCluster[] clusters;

    public CentroidDistanceWeightedRecord(CentroidCluster[] clusters, double[] dataDouble, DistanceMeasure distanceMeasure) {
        this.dataDouble = dataDouble;
        this.distanceMeasure = distanceMeasure;
        this.clusters = clusters;
        if (clusters.length == 0) {
            weight = 1.0;
        } else {

            weight = calculateDistanceToNearestCentroid();
        }

    }

    public CentroidDistanceWeightedRecord(CentroidCluster[] currentClusters, DenseVector dataVector) {
        this(currentClusters, dataVector.getData(), new EuclideanDistance());
    }

    public CentroidDistanceWeightedRecord(CentroidCluster[] currentClusters, double[] dataDouble) {
        this(currentClusters, dataDouble, new EuclideanDistance());
    }


    private double calculateDistanceToNearestCentroid() {
        double distanceToNearestCentroid = Double.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) {

            double[] centroid = clusters[i].getCenter().getPoint();
            if (centroid.length != dataDouble.length) {
                throw new IllegalArgumentException();
            }


            double distanceToCurrentCentroid = distanceMeasure.compute(this.dataDouble, centroid);

            if (distanceToNearestCentroid > distanceToCurrentCentroid) {
                distanceToNearestCentroid = distanceToCurrentCentroid;
            }
        }
        return distanceToNearestCentroid;
    }


    @Override
    public double getWeight() {
        return weight;
    }
}

