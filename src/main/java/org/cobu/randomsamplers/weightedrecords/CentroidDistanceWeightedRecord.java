package org.cobu.randomsamplers.weightedrecords;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;


public class CentroidDistanceWeightedRecord<P extends DoublePoint> implements WeightedRecord {
    private double weight = 1.0;
    private P record;
    private DistanceMeasure distanceMeasure;
    private CentroidCluster[] clusters;

    public CentroidDistanceWeightedRecord(CentroidCluster[] clusters, P record, DistanceMeasure distanceMeasure) {
        this.record = record;
        this.distanceMeasure = distanceMeasure;
        this.clusters = clusters;
        if (clusters.length == 0) {
            weight = 1.0;
        } else {
            weight = calculateDistanceToNearestCentroid();
        }

    }

    public CentroidDistanceWeightedRecord(CentroidCluster[] currentClusters, P dataVector) {
        this(currentClusters, dataVector, new EuclideanDistance());
    }

    public CentroidDistanceWeightedRecord(CentroidCluster<P> newAddedCluster, double minDistanceBefore,  P record, DistanceMeasure distanceMeasure){
        this.record = record;
        this.distanceMeasure = distanceMeasure;
        this.clusters = null;
        if(newAddedCluster == null){
            weight = 1.0;
        } else{
            double newDistance = distanceMeasure.compute(newAddedCluster.getCenter().getPoint(), record.getPoint());
            weight = Math.min(minDistanceBefore, newDistance);
        }
    };

    public P getRecord() {
        return record;
    }

    private double calculateDistanceToNearestCentroid() {
        double distanceToNearestCentroid = Double.MAX_VALUE;
        for (CentroidCluster cluster : clusters) {

            double[] centroid = cluster.getCenter().getPoint();
            if (centroid.length != record.getPoint().length) {
                throw new IllegalArgumentException();
            }


            double distanceToCurrentCentroid = distanceMeasure.compute(this.record.getPoint(), centroid);

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

