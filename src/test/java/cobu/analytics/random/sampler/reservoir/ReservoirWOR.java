package cobu.analytics.random.sampler.reservoir;

import com.google.common.base.Function;

import java.util.Iterator;

public class ReservoirWOR extends AbstractReservoir {

    public ReservoirWOR(int size,Function<Object,Double> weight,Iterator<Double> uniformSource){
        super(size,weight,uniformSource);
    }
    @Override
    public double createVariate() {
        return -Math.log(uniformSource.next())/ getWeight();
    }

    @Override
    public void add(Object object) {
        updateWeightsAndCounter(object);
        if(index()<getSampleSize()){
            sample.put(createVariate(),object);
        }else{
            double randomVariate = createVariate();
            if(randomVariate< sample.lastKey()){
                sample.remove(sample.lastKey());
                sample.put(randomVariate,object);
            }
        }
    }
}
