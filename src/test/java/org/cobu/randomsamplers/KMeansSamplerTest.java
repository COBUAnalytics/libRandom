package org.cobu.randomsamplers;

import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void TestReservoirSizeOnePopulation100k(){
        int reservoirSize = 1;
        ReservoirSamplerWOR rswor = new ReservoirSamplerWOR(new MersenneTwisterRandom(3234), reservoirSize);
        CentroidCluster[] currentClusters = new CentroidCluster[0];

        for (DenseVector dataVector : A) {
            CentroidDistanceWeightedRecord weightedRecord = new CentroidDistanceWeightedRecord(currentClusters, dataVector);
            rswor.add(weightedRecord);
        }


        WeightedRecord[] samples = rswor.getSamples();

//        for (WeightedRecord sample : samples) {
//            sample.getRecord();
//        }
        System.out.println(samples[0].getWeight());
    }
}
