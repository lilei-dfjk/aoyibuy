package com.aoyibuy.commons.core;

/**
 * 转换器接口
 * 
 * @author wh
 * @since 0.0.1
 *
 * @param <S> 源类型
 * @param <T> 目标类型
 */
public interface Converter<S, T> {

    T from(S source);
    
    S to(T target);
    
}
