package org.cobu.randomsamplers;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.cobu.random.MersenneTwisterRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HY on 12/20/13.
 */
public class KMeansSamplerFTest {
    List<DoublePoint> allPoints;


    private void assertEquals(double[] a, double[] b) {

        Assert.assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; i++) {
            Assert.assertEquals(a[i], b[i], 1.0E-9);
        }
    }

    private DoublePoint createVectorCloseBy(DoublePoint vector) {
        double[] closeToVector = vector.getPoint();
        double[] smallPerturbation = {.01, .02, .03};
        for (int i = 0; i < closeToVector.length; i++) {
            closeToVector[i] += smallPerturbation[i];
        }
        return new DoublePoint(closeToVector);
    }
    @Before
    public void  setup(){
        allPoints = new ArrayList<>();
        int num = 6;
        for(int i = 0; i < num; i++)    {
           DoublePoint p = new DoublePoint(new double[]{0,i});
           allPoints.add(p);
        }


    }
    @Test
    public void testGetCentroidOneCluster() {
        int numberOfCentroids = 1;
        int sampleSize = 0;
        long populationSize = allPoints.size();

        int count = 10000;
        double frequency = 0;
        double expected = 1.0/allPoints.size();
        DoublePoint result = allPoints.get(0);
        for (int i = 0; i < count; i++) {
            KMeansSamplerF<DoublePoint> sampler = new KMeansSamplerF<DoublePoint>(new MersenneTwisterRandom(),
                    numberOfCentroids, sampleSize, allPoints, populationSize, new EuclideanDistance());
            final List<CentroidCluster<DoublePoint>> centroids = sampler.getCentroids();
            Assert.assertEquals(1, centroids.size());
            if (Arrays.equals(centroids.get(0).getCenter().getPoint(), result.getPoint())) {
                frequency++;
            }
            ;
        }
        frequency /= count;
        Assert.assertEquals(expected, frequency, expected/10);
    }

    @Test
    public void testGetTwoCentroidsCluster() {
        int numberOfCentroids = 2;
        int sampleSize = 0;
        long populationSize = allPoints.size();

        double frequency = 0;
        int count = 10000;
        DoublePoint[] result = {allPoints.get(0), allPoints.get(5)};
        double expected = 25.0/330;
        for (int i = 0; i < count; i++) {
            KMeansSamplerF<DoublePoint> sampler = new KMeansSamplerF<DoublePoint>(new MersenneTwisterRandom(),
                    numberOfCentroids, sampleSize, allPoints, populationSize, new EuclideanDistance());
            List<CentroidCluster<DoublePoint>> centroids = sampler.getCentroids();
            Assert.assertEquals(2, centroids.size());

            boolean firstGotPicked = Arrays.equals(centroids.get(0).getCenter().getPoint(), result[0].getPoint());
            boolean secondGotPicked = Arrays.equals(centroids.get(1).getCenter().getPoint(),result[1].getPoint());

            if(firstGotPicked && secondGotPicked){
                frequency++;
            };
        }
        frequency /= count;
//        System.out.println(expected+" " +frequency);
        Assert.assertEquals(expected, frequency, expected/10);

    }

}
