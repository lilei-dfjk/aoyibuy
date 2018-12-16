package com.aoyibuy.commons.mq;

/**
 * Mq生产者
 * 
 * @author wh
 *
 * @param <T> 发送的对象类型
 * 
 * @since 0.0.1
 */
public interface MqProducer<T> {

	void send(String exchange, String routingKey, T message);
	
}
