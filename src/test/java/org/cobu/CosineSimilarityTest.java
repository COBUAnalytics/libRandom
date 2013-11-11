package org.cobu;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CosineSimilarityTest {
    @Test
    public void testZeroVectors() {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        double[] a = new double[10];
        double[] b = new double[10];

        double similarity = cosineSimilarity.compute(a,b);

        assertEquals(0.0, similarity);
    }

    @Test
    public void testSameVectors() throws Exception {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        double[] a = {1.0, 1.0, 1.0};
        double[] b = {1.0, 1.0, 1.0};

        double similarity = cosineSimilarity.compute(a,b);

        assertEquals(1.0, similarity);


    }


    @Test
    public void testVectorsScaled() throws Exception {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        double[] a = {1.0, 1.0, 1.0};
        double[] b = {2.0, 2.0, 2.0};

        double similarity = cosineSimilarity.compute(a,b);

        assertEquals(1.0, similarity);


    }

    @Test
    public void testCloseButNoCigar() throws Exception {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        double[] a = {4.0, 4.0, 4.0};
        double[] b = {4.0, 1.0, 4.0};

        double similarity = cosineSimilarity.compute(a,b);

        assertEquals(0.9045, similarity, 1e-4);


    }

    @Test
    public void testRotatedNinety() throws Exception {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        double[] a = {1.0, 1.0};
        double[] b = {2.0, 0.0};

        double similarity = cosineSimilarity.compute(a,b);

        assertEquals(1/Math.sqrt(2), similarity);


    }

}
