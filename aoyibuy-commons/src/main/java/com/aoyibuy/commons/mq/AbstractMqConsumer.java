package com.aoyibuy.commons.mq;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象的Mq消费者，提供模板方法规定消费的过程
 * 
 * @author wh
 *
 * @param <T> 消费的对象类型
 * 
 * @since 0.0.1
 */
public abstract class AbstractMqConsumer<T> implements MqConsumer<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractMqConsumer.class);

	public final void handle(T msg) {
		try {
			preConsume(msg);
			consume(msg);
			postConsume(msg);
		} catch (Throwable e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			processException(e);
		} finally {
			afterCompletion(msg);
		}
	}

	protected void preConsume(T msg) {
	}

	protected void postConsume(T msg) {
	}
	
	protected void afterCompletion(T msg) {
	}

	protected void processException(Throwable e) {
	    throw new RuntimeException(e);
	}
	
}
