package org.cobu.randomsamplers;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class KMeansSamplerTest {
    private NormalDistribution x0 = new NormalDistribution(10,1, 1e-9);
    private NormalDistribution y0 = new NormalDistribution(10,1, 1e-9);
    private List<DenseVector> A = new ArrayList<DenseVector>();


    @Before
    public void setup() {
        for (int i = 0; i < 100; i++) {
            A.add(new DenseVector(new double[]{x0.sample(), y0.sample()}));
        }
    }


    @Test
    public void testGetCentroidOneCluster(){

        int numberOfCentroids = 1;
        double[] randsToSelectSecond = {0.25, 0.5};
        Vector first = new DenseVector(new double[]{.1, .2, .3});
        Vector second = new DenseVector(new double[]{.1, .5, .3});

        KMeansSampler sampler = new ArraySamples(numberOfCentroids, randsToSelectSecond, first, second);
        Vector[] centroids = sampler.getCentroids();
        assertEquals(1, centroids.length);
        assertEquals(second, centroids[0]);
     }

    private class ArrayIterable<T extends Vector> implements Iterable<T> {
        private final T[] vectors;

        ArrayIterable(T... vectors) {

            this.vectors = vectors;
        }

        @Override
        public Iterator<T> iterator() {
            return Arrays.asList(vectors).iterator();
        }
    }

    private class ArraySamples extends KMeansSampler {

        private final double[] values;
        private int i;

        public ArraySamples(int numberOfClusters, double[] values, Vector... vectors) {
            super(null, numberOfClusters, new ArrayIterable<Vector>(vectors));
            this.values = values;
        }

        @Override
        protected double nextRandomDouble() {
            return values[i++];
        }
    }


}
