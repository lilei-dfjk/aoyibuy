package com.aoyibuy.commons.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 使用单线程来异步处理队列中的数据, 相当于模拟了一个轻量级的消息队列机制
 * 
 * @author wh
 * @since 0.0.1
 * 
 * @param <T>
 */
public abstract class AsyncOperation<T> implements InitializingBean {
    
    protected Logger logger = LoggerFactory.getLogger(AsyncOperation.class);

    private BlockingQueue<T> queue = new LinkedBlockingQueue<T>();

    public void setQueue(BlockingQueue<T> queue) {
        this.queue = queue;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        takeAndSend();
    }

    private void takeAndSend() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    T element = queue.take();
                    consume(element);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }, this.getClass().getName());
        t.setDaemon(true);
        t.start();
    }

    protected abstract void consume(T element);
    
    public void produce(T element) {
        try {
            queue.put(element);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
    
    public final BlockingQueue<T> getQueue() {
        return this.queue;
    }
}
