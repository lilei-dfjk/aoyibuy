package com.aoyibuy.commons.dynamicquery;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.aoyibuy.dao.jpa.BaseEntity;

/**
 * 动态jpql/nativesql查询的实现类
 * 
 * @author wh
 * @since 0.0.1
 */
public class DynamicQueryImpl implements DynamicQuery {

    @PersistenceContext
    private EntityManager em;

    /**
     * 从EntityManager获取Hibernate的Session
     * 下面所有方式的实现都基于Hibernate
     */
    private Session getHibernateSession() { 
        return em.unwrap(Session.class);
    }
    
    /**
     * 获取Hibernate的SessionFactory对象
     * @return
     */
    private SessionFactory getHibernateSessionFactory() {
        return getHibernateSession().getSessionFactory();
    }
    
    
    
    // -====================================== jpql查询 ======================================-
    @Override
    public <T> T querySingleResult(Class<T> resultClass, String jpql, Object...params) {
        return createTypedQuery(resultClass, jpql, params).getSingleResult();
    }
    
    @Override
    public <T> List<T> query(Class<T> resultClass, String jpql, Object... params) {
        return createTypedQuery(resultClass, jpql, params).getResultList();
    }

    @Override
    public <T> List<T> queryPagingList(Class<T> resultClass, Pageable pageable, String jpql, Object... params) {
        Integer pageNumber = pageable.getPageNumber();
        Integer pageSize = pageable.getPageSize();
        Integer startPosition = pageNumber * pageSize;
        return createTypedQuery(resultClass, jpql, params).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
    }
    
    @Override
    public <T> Page<T> query(Class<T> resultClass, Pageable pageable, String jpql, Object... params) {
        List<T> rows = queryPagingList(resultClass, pageable, jpql, params);
        Long total = queryCount(jpql, params);
        return new PageImpl<T>(rows, pageable, total);
    }
    
    @Override
    public Long queryCount(String jpql, Object... params) {
        jpql = StringUtils.substringBefore(jpql, "order by"); // 去掉order by, 提升执行效率
        
        // 去重和分组只能使用NativeSQL统计查询
        if (jpql.contains("distinct") || jpql.contains("group by")) {
            String countSql = generateCountSql(jpql);
            Object count = createNativeQuery(countSql, params).getSingleResult();
            return ((Number) count).longValue();
        } else { // 使用jpql统计查询
            String countJpql = generateCountJpql(jpql);
            return (Long) createQuery(countJpql, params).getSingleResult();
        }
    }
    
    @Override
    public int executeUpdate(String jpql, Object... params) {
        return createQuery(jpql, params).executeUpdate();
    }
    
    
    
    // -====================================== 本地查询 ======================================-
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQuery(Class<T> resultClass, String nativeSql, Object... params) {
        return getNativeQuery(resultClass, nativeSql, params).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T nativeQuerySingleResult(Class<T> resultClass, String nativeSql, Object... params) {
        return (T) getNativeQuery(resultClass, nativeSql, params).getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQueryPagingList(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params) {
        Integer pageNumber = pageable.getPageNumber();
        Integer pageSize = pageable.getPageSize();
        Integer startPosition = pageNumber * pageSize;
        return getNativeQuery(resultClass, nativeSql, params).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
    }
    
    @Override
    public <T> Page<T> nativeQuery(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params) {
        List<T> rows = nativeQueryPagingList(resultClass, pageable, nativeSql, params);
        Long total = nativeQueryCount(nativeSql, params);
        return new PageImpl<T>(rows, pageable, total);
    }
    
    @Override
    public Long nativeQueryCount(String nativeSql, Object... params) {
        nativeSql = StringUtils.substringBefore(nativeSql, "order by"); // 去掉order by, 提升执行效率
        String countSql = "select count(*) from (" + nativeSql + ")";
        Object count = createNativeQuery(countSql, params).getSingleResult();
        return ((Number) count).longValue();
    }
    
    @Override
    public int nativeExecuteUpdate(String nativeSql, Object... params) {
        return createNativeQuery(nativeSql, params).executeUpdate();
    }
    
    
    
    // -================================== Map查询 ========================================-
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryMapSingleResult(String jpql, Object... params) {
        return (Map<String, Object>) createQueryMap(jpql, params).getSingleResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryMap(String jpql, Object... params) {
        return createQueryMap(jpql, params).getResultList();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryMapPagingList(Pageable pageable, String jpql, Object... params) {
        Integer pageNumber = pageable.getPageNumber();
        Integer pageSize = pageable.getPageSize();
        Integer startPosition = pageNumber * pageSize;
        return createQueryMap(jpql, params).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
    }
    
    @Override
    public Page<Map<String, Object>> queryMap(Pageable pageable, String jpql, Object... params) {
        List<Map<String, Object>> rows = queryMapPagingList(pageable, jpql, params);
        Long total = queryCount(jpql, params);
        return new PageImpl<Map<String, Object>>(rows, pageable, total);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> nativeQueryMapSingleResult(String nativeSql, Object... params) {
        return (Map<String, Object>) createNativeQueryMap(nativeSql, params).getSingleResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> nativeQueryMap(String nativeSql, Object... params) {
        return createNativeQueryMap(nativeSql, params).getResultList();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> nativeQueryMapPagingList(Pageable pageable, String nativeSql, Object... params) {
        Integer pageNumber = pageable.getPageNumber();
        Integer pageSize = pageable.getPageSize();
        Integer startPosition = pageNumber * pageSize;
        return createNativeQueryMap(nativeSql, params).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
    }
    
    @Override
    public Page<Map<String, Object>> nativeQueryMap(Pageable pageable, String nativeSql, Object... params) {
        List<Map<String, Object>> rows = nativeQueryMapPagingList(pageable, nativeSql, params);
        Long total = nativeQueryCount(nativeSql, params);
        return new PageImpl<Map<String, Object>>(rows, pageable, total);
    }
    
    
    // -=================================== 其它 ===========================================-
    
    private Query createQuery(String jpql, Object... params) {
        Query q = em.createQuery(jpql);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]); // 与Hiberante不同,jpa query从位置1开始
        }
        return q;
    }
    
    private <T> TypedQuery<T> createTypedQuery(Class<T> resultClass, String jpql, Object... params) {
        TypedQuery<T> q = em.createQuery(jpql, resultClass);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]); // 与Hiberante不同,jpa query从位置1开始
        }
        return q;
    }
    
    private <T> Query getNativeQuery(Class<T> resultClass, String sql, Object... params) {
        Query q = isEntity(resultClass) ? 
                createNativeQuery(resultClass, sql, params) : createNativeQuery(sql, params);
        return q;
    }
    
    private <T> Boolean isEntity(Class<T> resultClass) {
        return BaseEntity.class.isAssignableFrom(resultClass);
    }
    
    private Query createNativeQuery(String sql, Object... params) {
        Query q = em.createNativeQuery(sql);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]); // 与Hiberante不同,jpa query从位置1开始
        }
        return q;
    }
    
    private <T> Query createNativeQuery(Class<T> resultClass, String sql, Object... params) {
        Query q = em.createNativeQuery(sql, resultClass);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]); // 与Hiberante不同,jpa query从位置1开始
        }
        return q;
    }
    
    /**
     * 执行统计查询
     * @param jpql
     * @param params 命名参数
     * @return
     */
    private String generateCountJpql(String jpql) {
        return "select count(*) from " + StringUtils.substringAfter(jpql, "from");
    }

    /**
     * 通过jpql生成统计sql
     * @param jpql
     * @return
     */
    private String generateCountSql(String jpql) {
        return "select count(*) c from (" + jpqlToSql(jpql) + ") _count";
    }

    /**
     * 通过hibernate的翻译器(QueryTranslator)将jpql翻译成sql
     * @param jpql
     * @return
     */
    private String jpqlToSql(String jpql) {
        QueryTranslator qt = new QueryTranslatorImpl(jpql, jpql,
                Collections.EMPTY_MAP,
                (SessionFactoryImplementor) getHibernateSessionFactory());
        qt.compile(Collections.EMPTY_MAP, false);
        return qt.getSQLString();
    }
    
    private Query createQueryMap(String jpql, Object... params) {
        Query q = createQuery(jpql, params);
        q.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q;
    }
    
    private Query createNativeQueryMap(String jpql, Object... params) {
        Query q = createQuery(jpql, params);
        q.unwrap(org.hibernate.SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q;
    }

}