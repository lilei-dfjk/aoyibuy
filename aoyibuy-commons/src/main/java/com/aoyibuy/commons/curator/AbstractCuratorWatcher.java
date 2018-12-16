package com.aoyibuy.commons.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 实现监视者的启动和销毁
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class AbstractCuratorWatcher<T> implements CuratorWatcher, InitializingBean, DisposableBean {
    
    protected final Logger logger = LoggerFactory.getLogger(AbstractCuratorWatcher.class);

    private CuratorOperations curatorOperations;
    
    private CuratorConverter<T> converter;
    
    private String listenableParentPath;
    
    private PathChildrenCache listenableParentPathCache;
    
    public void setCuratorOperations(CuratorOperations curatorOperations) {
        this.curatorOperations = curatorOperations;
    }
    
    public void setConverter(CuratorConverter<T> converter) {
        this.converter = converter;
    }
    
    public void setListenableParentPath(String listenableParentPath) {
        this.listenableParentPath = listenableParentPath;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(curatorOperations);
        Assert.notNull(listenableParentPath);
        
        curatorOperations.createPathIfAbsent(listenableParentPath);
        listenableParentPathCache = new PathChildrenCache(
                curatorOperations.getClient(), listenableParentPath, true);
    }
    
    @Override
    public void start() throws Exception {
        listenableParentPathCache.start(getStartMode());
        listenableParentPathCache.getListenable().addListener((client, event) -> {
            onPathChildrenCacheEvent(client, event); // 监听子节点变化
        });
    }
    
    protected StartMode getStartMode() {
        return StartMode.POST_INITIALIZED_EVENT; // 初始化完毕后会收到PathChildrenCacheEvent.Type.INITIALIZED事件
    }
    
    protected void onPathChildrenCacheEvent(CuratorFramework client, PathChildrenCacheEvent event) {
        byte[] data = event.getData().getData();
        T child = convertData(data);
        
        switch (event.getType()) {
            case CHILD_ADDED:
                onChildAdded(child, event);
                break;
            case CHILD_REMOVED:
                onChildRemoved(child, event);
                break;
            case CHILD_UPDATED:
                onChildUpdated(child, event);
                break;
            case INITIALIZED:
                onInitialized(child, event);
                break;
            case CONNECTION_LOST:
                onConnectionLost(child, event);
                break;
            case CONNECTION_RECONNECTED:
                onConnectionReconnected(child, event);
                break;
            case CONNECTION_SUSPENDED:
                onConnectionSuspended(child, event);
                break;
            default:
                break;
        }
    }
    
    protected void onChildAdded(T child, PathChildrenCacheEvent event) {
    }

    protected void onChildRemoved(T child, PathChildrenCacheEvent event) {
    }

    protected void onChildUpdated(T child, PathChildrenCacheEvent event) {
    }
    
    protected void onInitialized(T child, PathChildrenCacheEvent event) {
        logger.info("子节点初始化完毕");
    }

    protected void onConnectionLost(T child, PathChildrenCacheEvent event) {
        logger.info("连接丢失");
    }

    protected void onConnectionReconnected(T child, PathChildrenCacheEvent event) {
        logger.info("重新连接");
    }

    protected void onConnectionSuspended(T child, PathChildrenCacheEvent event) {
        logger.info("连接挂起");
    }

    protected T convertData(byte[] data) {
        return this.converter.from(data);
    }
    
    @Override
    public void destroy() throws Exception {
        shutdown();
    }
    
    @Override
    public void shutdown() throws Exception {
        listenableParentPathCache.close();
    }
}
