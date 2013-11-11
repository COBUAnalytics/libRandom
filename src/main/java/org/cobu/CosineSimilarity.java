package org.cobu;

import org.apache.commons.math3.ml.distance.DistanceMeasure;

public class CosineSimilarity implements DistanceMeasure{
    @Override
    public double compute(double[] a, double[] b) {
        double dotProduct = 0.0;
        double a_2 = 0.0;
        double b_2 = 0.0;
        for (int i = 0; i < a.length; i++) {
            a_2 += a[i] * a[i];
            b_2 += b[i] * b[i];
            dotProduct += a[i] * b[i];
        }
        double denominator = Math.sqrt(a_2) * Math.sqrt(b_2);

        // correct for floating-point rounding errors
        if (denominator < dotProduct) {
            denominator = dotProduct;
        }

        if (denominator == 0 && dotProduct == 0) {
            return 0;
        } else {
            return dotProduct / denominator;
        }

    }
}
