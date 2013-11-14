package org.cobu.experiment;

import com.google.common.base.Function;

import java.util.HashSet;
import java.util.Set;

public class EventDistribution<K extends Comparable<K>> {
    private final Function<K, Double> weightFunction;
    private final Set<K> universe;
    private final double weightOfUniverse;

    public EventDistribution(Function<K, Double> weightFunction, Set<K> universe){
        this.weightFunction = weightFunction;
        this.universe = universe;
        weightOfUniverse=weight(universe);
    }

    private double weight(K element){
        return weightFunction.apply(element);
    }

    private double weight(Set<K> set) {
        double weight = 0.0;
        for(K element:set){
            weight+= weight(element);
        }
        return weight;
    }

    public double probabilityOfOutcome(Event event){
        double p = Double.NaN;
        switch(event.getSampleType()){
            case WithReplacement:
                p=probabilityWR(event);
                break;
            case WithoutReplacement:
                p=probabilityWOR(event);
                break;
        }
        return p;
    }

    private double probabilityWR(Event<K> event){
        double p=1.0;
        for(K x: event){
            p*=weight(x)/weightOfUniverse;
        }
        return p;
    }

    private double probabilityWOR(Event<K> event){
        double p=1.0;
        Set<K> seen = new HashSet<K>();
        for(K x: event){
            p*=weight(x)/(weightOfUniverse-weight(seen));
            seen.add(x);
        }
        return p;
    }
}
