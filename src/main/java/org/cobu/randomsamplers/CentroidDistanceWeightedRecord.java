package org.cobu.randomsamplers;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;

public class CentroidDistanceWeightedRecord implements WeightedRecord {
    private final double weight;

    public CentroidDistanceWeightedRecord(CentroidCluster[] currentClusters, DenseVector vector) {
        if (currentClusters.length == 0) {
            weight = 1.0;
        } else {
            final double[] centroid = currentClusters[0].getCenter().getPoint();
            if (centroid.length != vector.size()) {
                throw new IllegalArgumentException();
            }
            final Vector distanceToCentroid = vector.add(-1, new DenseVector(centroid));
            weight = distanceToCentroid.norm(Vector.Norm.Two);
        }

    }

    @Override
    public double getWeight() {
        return weight;
    }
}
