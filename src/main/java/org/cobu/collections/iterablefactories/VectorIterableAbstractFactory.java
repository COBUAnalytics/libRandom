package org.cobu.collections.iterablefactories;

import no.uib.cipr.matrix.Vector;

public interface VectorIterableAbstractFactory {
    Iterable<? extends Vector> getIterable();
}
