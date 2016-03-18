package com.dlvr.continuum.slice;

import java.util.concurrent.CompletableFuture;

/**
 * Slice whose results are updated in real time
 * Created by zack on 3/17/16.
 */
public interface ContinuousSlice {

    /**
     * Continuously scan
     * @param scan
     * @return
     */
    CompletableFuture<Slice> slice(Scan scan);
}
