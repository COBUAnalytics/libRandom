package org.cobu.randomsamplers.weightedrecords;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CentroidDistanceWeightedRecordTest {

    @Test
    public void zeroCentroidsReturnsWeightOfOne() {
        DoublePoint smallVector = new DoublePoint(new double[]{.1, .5, .8});
        DoublePoint largeVector = new DoublePoint(new double[]{100, 299, 4587});
        CentroidCluster[] currentClusters = new CentroidCluster[0];
        CentroidDistanceWeightedRecord<DoublePoint> smallWeightedRecord = new CentroidDistanceWeightedRecord<DoublePoint>(currentClusters, smallVector);
        CentroidDistanceWeightedRecord<DoublePoint> largeWeightedRecord = new CentroidDistanceWeightedRecord<DoublePoint>(currentClusters, largeVector);
        assertEquals(1.0, smallWeightedRecord.getWeight(), 0.0);
        assertEquals(1.0, largeWeightedRecord.getWeight(), 0.0);


    }


    @Test
     public void oneCentroid() {
        final DoublePoint centroid = new DoublePoint(new double[]{1, 1, 1});
        CentroidCluster[] currentClusters = new CentroidCluster[] {new CentroidCluster(centroid)};

        DoublePoint threeDistance = new DoublePoint(new double[]{4, 4, 4});
        Vector difference = new DenseVector(threeDistance.getPoint()).add(-1, new DenseVector(centroid.getPoint()));

        assertEquals(Math.pow(difference.norm(Vector.Norm.Two),2), new CentroidDistanceWeightedRecord<DoublePoint>(currentClusters, threeDistance).getWeight(), 0);
     }


    @Test
    public void twoCentroidsPicksDistanceToNearestCentroidFromPoint() {
        CentroidCluster centroidCluster0 = new CentroidCluster(new DoublePoint(new double[]{1, 1, 1}));
        CentroidCluster centroidCluster1 = new CentroidCluster(new DoublePoint(new double[]{4, 4, 4}));
        CentroidCluster[] currentClusters = new CentroidCluster[] {centroidCluster0,centroidCluster1};

        DoublePoint threeDistance = new DoublePoint(new double[]{4, 4, 4});
        Vector difference = new DenseVector(threeDistance.getPoint()).add(-1, new DenseVector(centroidCluster1.getCenter().getPoint()));
        double expectedWeight = difference.norm(Vector.Norm.Two);

        double actualWeight = new CentroidDistanceWeightedRecord<DoublePoint>(currentClusters, threeDistance).getWeight();
        assertEquals(expectedWeight, actualWeight, 0);

    }

    @Test
     public void threeCentroidsPicksDistanceToNearestCentroidFromPoint() {
        CentroidCluster centroidCluster0 = new CentroidCluster(new DoublePoint(new double[]{1, 1, 1}));
        CentroidCluster centroidCluster1 = new CentroidCluster(new DoublePoint(new double[]{4, 4, 4}));
        CentroidCluster centroidCluster2 = new CentroidCluster(new DoublePoint(new double[]{4, 1, 4}));

        CentroidCluster[] currentClusters = new CentroidCluster[] {centroidCluster0,centroidCluster1, centroidCluster2};

        DoublePoint threeDistance = new DoublePoint(new double[]{4, 4, 4});
        Vector difference = new DenseVector(threeDistance.getPoint()).add(-1, new DenseVector(centroidCluster1.getCenter().getPoint()));
        double expectedWeight = difference.norm(Vector.Norm.Two);

        double actualWeight = new CentroidDistanceWeightedRecord<DoublePoint>(currentClusters, threeDistance).getWeight();
        assertEquals(expectedWeight, actualWeight, 0);

    }


    @Test(expected = IllegalArgumentException.class)
    public void vectorsOfDifferentDimensionFromCentroidExcepts() {
        DoublePoint wrongDimension = new DoublePoint(new double[]{.1, .5});
        final DoublePoint centroid = new DoublePoint(new double[]{1, 1, 1});
        CentroidCluster[] currentClusters = new CentroidCluster[] {new CentroidCluster(centroid)};
        new CentroidDistanceWeightedRecord<DoublePoint>(currentClusters, wrongDimension);
    }
    }
