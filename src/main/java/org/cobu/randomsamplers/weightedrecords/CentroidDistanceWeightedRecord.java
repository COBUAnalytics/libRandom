package org.cobu.randomsamplers.weightedrecords;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;


public class CentroidDistanceWeightedRecord implements WeightedRecord {
    private double weight = 1.0;
    private double[] record;
    private DistanceMeasure distanceMeasure;
    private CentroidCluster[] clusters;

    public CentroidDistanceWeightedRecord(CentroidCluster[] clusters, double[] record, DistanceMeasure distanceMeasure) {
        this.record = record;
        this.distanceMeasure = distanceMeasure;
        this.clusters = clusters;
        if (clusters.length == 0) {
            weight = 1.0;
        } else {
            weight = calculateDistanceToNearestCentroid();
        }

    }

    public CentroidDistanceWeightedRecord(CentroidCluster[] currentClusters, DoublePoint dataVector) {
        this(currentClusters, dataVector.getPoint(), new EuclideanDistance());
    }

    public double[] getRecord() {
        return record;
    }

    private double calculateDistanceToNearestCentroid() {
        double distanceToNearestCentroid = Double.MAX_VALUE;
        for (CentroidCluster cluster : clusters) {

            double[] centroid = cluster.getCenter().getPoint();
            if (centroid.length != record.length) {
                throw new IllegalArgumentException();
            }


            double distanceToCurrentCentroid = distanceMeasure.compute(this.record, centroid);

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

