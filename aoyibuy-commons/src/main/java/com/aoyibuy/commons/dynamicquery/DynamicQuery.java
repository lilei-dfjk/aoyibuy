package com.aoyibuy.commons.dynamicquery;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 扩展SpringDataJpa, 支持动态jpql/nativesql查询并支持分页查询<br/>
 * 使用方法：注入ServiceImpl
 * 
 * @author wh
 * @since 0.0.1
 */
public interface DynamicQuery {
    
    // -====================================== jpql查询 ======================================-
    
    /**
     * 执行jpql查询
     * @param resultClass 查询结果类型
     * @param jpql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return
     */
    <T> List<T> query(Class<T> resultClass, String jpql, Object... params);
    
    /**
     * 执行jpql查询一行
     * @param resultClass 查询结果类型
     * @param jpql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return
     */
    <T> T querySingleResult(Class<T> resultClass, String jpql, Object... params);
    
    /**
     * 执行jpql分页查询
     * @param resultClass 查询结果类型
     * @param pageable 分页数据
     * @param jpql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return 分页结果
     */
    <T> List<T> queryPagingList(Class<T> resultClass, Pageable pageable, String jpql, Object... params);
    
    /**
     * 执行jpql分页查询
     * @param resultClass 查询结果类型
     * @param pageable 分页数据
     * @param jpql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return 分页对象
     */
    <T> Page<T> query(Class<T> resultClass, Pageable pageable, String jpql, Object... params);
    
    /**
     * 执行jpql统计查询
     * @param jpql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return 统计条数
     */
    Long queryCount(String jpql, Object... params);
    
    /**
     * 执行jpql的update,delete操作
     * @param nativeSql
     * @param params
     * @return
     */
    int executeUpdate(String jpql, Object... params);
    
    
    
    // -====================================== 本地查询 ======================================-
    
    /**
     * 执行nativeSql查询
     * @param resultClass 查询结果类型
     * @param nativeSql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return
     */
    <T> List<T> nativeQuery(Class<T> resultClass, String nativeSql, Object... params);
    
    /**
     * 执行nativeSql查询一行
     * @param resultClass 查询结果类型
     * @param nativeSql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return
     */
    <T> T nativeQuerySingleResult(Class<T> resultClass, String nativeSql, Object... params);
    
    /**
     * 执行nativeSql分页查询
     * @param resultClass 查询结果类型
     * @param pageable 分页数据
     * @param nativeSql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return 分页结果
     */
    <T> List<T> nativeQueryPagingList(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params);
    
    /**
     * 执行nativeSql分页查询
     * @param resultClass 查询结果类型
     * @param pageable 分页数据
     * @param nativeSql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return 分页对象
     */
    <T> Page<T> nativeQuery(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params);
    
    /**
     * 执行nativeSql统计查询
     * @param nativeSql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return 统计条数
     */
    Long nativeQueryCount(String nativeSql, Object... params);
    
    /**
     * 执行nativeSql的update,delete操作
     * @param nativeSql
     * @param params
     * @return
     */
    int nativeExecuteUpdate(String nativeSql, Object... params);

    Map<String, Object> queryMapSingleResult(String jpql, Object... params);

    List<Map<String, Object>> queryMap(String jpql, Object... params);

    List<Map<String, Object>> queryMapPagingList(Pageable pageable, String jpql, Object... params);

    Page<Map<String, Object>> queryMap(Pageable pageable, String jpql, Object... params);

    Map<String, Object> nativeQueryMapSingleResult(String nativeSql, Object... params);

    List<Map<String, Object>> nativeQueryMap(String nativeSql, Object... params);

    List<Map<String, Object>> nativeQueryMapPagingList(Pageable pageable, String nativeSql, Object... params);

    Page<Map<String, Object>> nativeQueryMap(Pageable pageable, String nativeSql, Object... params);

}