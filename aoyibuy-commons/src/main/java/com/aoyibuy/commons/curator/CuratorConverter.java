package com.aoyibuy.commons.curator;

import com.aoyibuy.commons.core.Converter;

/**
 * 明确zk节点的数据类型一定是字节数组
 * 
 * @author wh
 * @since 0.0.1
 *
 * @param <T>
 */
public interface CuratorConverter<T> extends Converter<byte[], T>{

}
