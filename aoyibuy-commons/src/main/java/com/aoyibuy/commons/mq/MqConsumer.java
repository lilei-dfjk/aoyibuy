package com.aoyibuy.commons.mq;

/**
 * Mq消费者
 * 
 * @author wh
 *
 * @param <T> MqConsumer
 * 
 * @since 0.0.1
 */
public interface MqConsumer<T> {

	 void consume(T msg) throws Exception;
}
