package org.cobu.randomsamplers;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeansSamplerTest {


    @Test
    public void testGetCentroidOneCluster() {
        int numberOfCentroids = 1;
        double[] randsToSelectSecond = {0.25, 0.5};
        DoublePoint first = new DoublePoint(new double[]{.1, .2, .3});
        DoublePoint second = new DoublePoint(new double[]{.1, .5, .3});

        int sampleSize = 0;
        long populationSize = 2;

        KMeansSampler<DoublePoint> sampler = new KMeansSampler<DoublePoint>(new ArrayRandom(randsToSelectSecond),
                numberOfCentroids, sampleSize, Arrays.asList(first, second), populationSize, new EuclideanDistance());
        final List<CentroidCluster<DoublePoint>> centroids = sampler.getCentroids();
        Assert.assertEquals(1, centroids.size());

        assertEquals(second.getPoint(), centroids.get(0).getCenter().getPoint());


    }

    @Test
    public void testGetTwoCentroidsCluster() {
        int numberOfCentroids = 2;
        int sampleSize = 0;
        long populationSize = 3;

        double[] randsToSelectSecond = {0.00001, 0.99999, .0001, .99, .99, .99, 0.1, 0.1, 0.1};
        DoublePoint secondCentroid = new DoublePoint(new double[]{.1, .2, .3});
        DoublePoint fistCentroidPicked = new DoublePoint(new double[]{100, 300, 300});
        DoublePoint closeToFirstCentroid = createVectorCloseBy(fistCentroidPicked);

        KMeansSampler<DoublePoint> sampler = new KMeansSampler<DoublePoint>(new ArrayRandom(randsToSelectSecond),
                numberOfCentroids, sampleSize, Arrays.asList(secondCentroid, fistCentroidPicked, closeToFirstCentroid), populationSize, new EuclideanDistance());
        List<CentroidCluster<DoublePoint>> centroids = sampler.getCentroids();
        Assert.assertEquals(2, centroids.size());

        assertEquals(fistCentroidPicked.getPoint(), centroids.get(0).getCenter().getPoint());
        assertEquals(secondCentroid.getPoint(), centroids.get(1).getCenter().getPoint());



    }

    @Test
    public void testGetTwoCentroidsAndSample() {
        int numberOfCentroids = 2;
        int sizeOfSample = 2;
        double[] randsToSelectSecond = {0.25, 0.5, .001, .001, .001, 1e-16, 1.0, 0.5, 1e-16, 1e-16, 1e-8, 1e-16,
                .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99, .99};
        DoublePoint secondCentroid = new DoublePoint(new double[]{.1, .2, .3});
        DoublePoint fistCentroidPicked = new DoublePoint(new double[]{.5, .5, .5});
        DoublePoint closeToFirstCentroid = createVectorCloseBy(secondCentroid);

        DoublePoint closeToSecondCentroid = createVectorCloseBy(secondCentroid);
        DoublePoint farAway = new DoublePoint(new double[]{4, 5, 6});
        DoublePoint fartherAway = new DoublePoint(new double[]{1, 1, 1});
        long populationSize = 6;
        KMeansSampler<DoublePoint> sampler = new KMeansSampler<DoublePoint>(new ArrayRandom(randsToSelectSecond),
                numberOfCentroids, sizeOfSample, Arrays.asList(secondCentroid, fistCentroidPicked, closeToFirstCentroid,
                closeToSecondCentroid, farAway, fartherAway), populationSize, new EuclideanDistance());
        List<CentroidCluster<DoublePoint>> centroids = sampler.getCentroids();
        Assert.assertEquals(2, centroids.size());

        assertEquals(fistCentroidPicked.getPoint(), centroids.get(0).getCenter().getPoint());
        assertEquals(secondCentroid.getPoint(), centroids.get(1).getCenter().getPoint());

        List<DoublePoint> samples1 = sampler.samples();
        DoublePoint[] samples = samples1.toArray(new DoublePoint[samples1.size()]);
        Assert.assertEquals(2, samples.length);
        assertEquals(samples[0].getPoint(), farAway.getPoint());
        assertEquals(samples[1].getPoint(), fartherAway.getPoint());




    }

    @Test(expected = IllegalStateException.class)
    public void failsIfNotEnoughPointsForSample() {

        int numberOfCentroids = 1;
        double[] randsToSelectSecond = {0.25, 0.5};
        DoublePoint first = new DoublePoint(new double[]{.1, .2, .3});
        DoublePoint second = new DoublePoint(new double[]{.1, .5, .3});

        long populationSize =2;
        new KMeansSampler<DoublePoint>(new ArrayRandom(randsToSelectSecond),
                numberOfCentroids, Integer.MAX_VALUE, Arrays.asList(first, second), populationSize, new EuclideanDistance());



    }

    private DoublePoint createVectorCloseBy(DoublePoint vector) {
        double[] closeToVector = vector.getPoint();
        double[] smallPerturbation = {.01, .02, .03};
        for (int i = 0; i < closeToVector.length; i++) {
            closeToVector[i] += smallPerturbation[i];
        }
        return new DoublePoint(closeToVector);
    }

    private void assertEquals(double[] a, double[] b) {

        Assert.assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; i++) {
            Assert.assertEquals(a[i], b[i], 1.0E-9);
        }
    }

    private class ArrayRandom extends Random {

        private static final long serialVersionUID = 7161839673348881306L;
        private final double[] values;
        private int i;

        public ArrayRandom(double[] values) {
            this.values = values;
        }

        @Override
        public double nextDouble() {
            return values[i++];
        }
    }

}

