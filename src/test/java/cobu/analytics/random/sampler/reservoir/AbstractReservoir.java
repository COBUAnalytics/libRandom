package cobu.analytics.random.sampler.reservoir;

import com.google.common.base.Function;
import com.sun.istack.internal.Nullable;

import java.util.Iterator;
import java.util.TreeMap;

abstract public class AbstractReservoir {
    private final int sampleSize;
    private final Function<Object, Double> weightFunction;
    protected final Iterator<Double> uniformSource;
    long recordCount;
    double weight, cumulativeWeight;
    protected TreeMap<Double,Object> sample  = new TreeMap<Double,Object>();
    public AbstractReservoir(int sampleSize,Function<Object,Double> weightFunction,Iterator<Double> uniformSource){
        this.sampleSize = sampleSize;
        this.weightFunction = weightFunction;
        this.uniformSource = uniformSource;
    }

    abstract public double createVariate();

    abstract public void add(Object object);

    protected void updateWeightsAndCounter(Object object){
        recordCount++;
        weight = weightFunction.apply(object);
        cumulativeWeight += weight;
    }

    public long index(){
        return recordCount-1;
    }

    public double getWeight() {
        return weight;
    }

    public double getCumulativeWeight() {
        return cumulativeWeight;
    }

    static public Function<Object,Double> uniformWeight = new Function<Object, Double>() {
        @Override
        public Double apply(@Nullable java.lang.Object o) {
            return 1.0;
        }
    };

    public int getSampleSize() {
        return sampleSize;
    }
}
