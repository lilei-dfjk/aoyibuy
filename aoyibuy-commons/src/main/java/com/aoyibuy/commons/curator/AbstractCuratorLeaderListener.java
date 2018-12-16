package com.aoyibuy.commons.curator;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * leader角色的监听器抽象实现
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class AbstractCuratorLeaderListener extends LeaderSelectorListenerAdapter 
    implements LeaderListener, InitializingBean, DisposableBean {
    
    protected final Logger logger = LoggerFactory.getLogger(AbstractCuratorLeaderListener.class);
    
    private String listenerPath; // 监听器在ZooKeeper上的路径

    private CuratorFramework client;

    public void setListenerPath(String listenerPath) {
        this.listenerPath = listenerPath;
    }

    public void setClient(CuratorFramework client) {
        this.client = client;
    }
    
    private LeaderSelector leaderSelector;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(client);
        Assert.notNull(listenerPath);

        // 利用Curator封装的选举机制, 确保任意时刻有且仅有一个leader
        leaderSelector = new LeaderSelector(client, listenerPath, this);
        leaderSelector.autoRequeue(); // 使丢失leader的服务, 仍可重新加入到竞争leader的行列中
        leaderSelector.start();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        logger.info("{} 被选举为主节点", getNodeName());
        TimeUnit.MINUTES.sleep(10); // 10分钟后放弃leader角色, 重新选举
    }
    
    /**
     * 获取服务节点名称. 应确保每个服务节点有不同的名称
     * 
     * @return
     */
    protected abstract String getNodeName();

    @Override
    public void destroy() throws Exception {
        leaderSelector.close();
    }

    @Override
    public boolean isLeader() {
        return leaderSelector.hasLeadership();
    }

}
