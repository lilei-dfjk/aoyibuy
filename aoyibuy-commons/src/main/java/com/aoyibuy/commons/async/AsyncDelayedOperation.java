package com.aoyibuy.commons.async;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
 * 窄化泛型, 要求必须实现java.util.concurrent.Delayed接口, 通道阻塞队列也必须是java.util.concurrent.DelayQueue
 * 
 * @author wh
 * @since 0.0.1
 *
 * @param <T>
 */
public abstract class AsyncDelayedOperation<T extends Delayed> extends AsyncOperation<T> {

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!(getQueue() instanceof DelayQueue)) {
            setQueue(new DelayQueue<T>());
        }
        super.afterPropertiesSet();
    }
    
}
