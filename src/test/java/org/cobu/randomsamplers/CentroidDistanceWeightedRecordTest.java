package org.cobu.randomsamplers;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CentroidDistanceWeightedRecordTest {

    @Test
    public void zeroCentroidsReturnsWeightOfOne() {
        DenseVector smallVector = new DenseVector(new double[]{.1, .5, .8});
        DenseVector largeVector = new DenseVector(new double[]{100, 299, 4587});
        CentroidCluster[] currentClusters = new CentroidCluster[0];
        CentroidDistanceWeightedRecord smallWeightedRecord = new CentroidDistanceWeightedRecord(currentClusters, smallVector);
        CentroidDistanceWeightedRecord largeWeightedRecord = new CentroidDistanceWeightedRecord(currentClusters, largeVector);
        assertEquals(1.0, smallWeightedRecord.getWeight(), 0.0);
        assertEquals(1.0, largeWeightedRecord.getWeight(), 0.0);


    }


    @Test
     public void oneCentroid() {
        final DoublePoint centroid = new DoublePoint(new double[]{1, 1, 1});
        CentroidCluster[] currentClusters = new CentroidCluster[] {new CentroidCluster(centroid)};
        DenseVector threeDistance = new DenseVector(new double[]{4, 4, 4});

        Vector difference = new DenseVector(threeDistance).add(-1, new DenseVector(centroid.getPoint()));
        assertEquals(difference.norm(Vector.Norm.Two), new CentroidDistanceWeightedRecord(currentClusters, threeDistance).getWeight(), 0);

     }


    @Test
    public void twoCentroidsPicksDistanceToNearestCentroidFromPoint() {
        fail();
    }

    @Test
     public void threeCentroidsPicksDistanceToNearestCentroidFromPoint() {
         fail();
     }


    @Test(expected = IllegalArgumentException.class)
    public void vectorsOfDifferentDimensionFromCentroidExcepts() {
        DenseVector wrongDimension = new DenseVector(new double[]{.1, .5, .8});
        final DoublePoint centroid = new DoublePoint(new double[]{1, 1, 1});
        CentroidCluster[] currentClusters = new CentroidCluster[] {new CentroidCluster(centroid)};
        new CentroidDistanceWeightedRecord(currentClusters, wrongDimension);
    }
}
