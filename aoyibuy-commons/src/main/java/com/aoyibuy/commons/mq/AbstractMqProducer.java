package com.aoyibuy.commons.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象的mq生产者
 * 
 * @author wh
 *
 * @param <T> 发送的对象类型
 * 
 * @since 0.0.1
 */
public abstract class AbstractMqProducer<T> implements MqProducer<T> {

	@Autowired
	protected AmqpTemplate amqpTemplate;
	
}
