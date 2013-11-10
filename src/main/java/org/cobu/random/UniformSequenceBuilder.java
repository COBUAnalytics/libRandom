package org.cobu.random;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class UniformSequenceBuilder {

    Iterable<Double> createFromArray(Double... pArray){
        return Arrays.<Double>asList(pArray);
    }

    Iterable<Double> createFromRandom(final long seed){
        return new Iterable<Double>() {
            @Override
            public Iterator<Double> iterator() {
                final Random random = new Random(seed);
                return new Iterator<Double>() {

                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Double next() {
                        return random.nextDouble();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
