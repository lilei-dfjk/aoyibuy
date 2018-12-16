package com.aoyibuy.commons.mq;

/**
 * 指定发送的对象为字符串
 * 
 * @author wh
 * @since 0.0.1
 */
public class SimpleMqProducer extends AbstractMqProducer<String> {

	@Override
	public void send(String exchange, String routingKey, String message) {
		amqpTemplate.convertAndSend(exchange, routingKey, message);
	}

}
