package com.aoyibuy.commons.curator;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

/**
 * Curator模板，主要实现在CuratorOperations中定义的方法
 * 
 * @author wh
 * @since 0.0.1
 */
public class CuratorTemplate extends CuratorSupport implements CuratorOperations {
    
    private CuratorFramework curatorFramework;
    
    public CuratorTemplate(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }
    
    @Override
    public CuratorFramework getClient() {
        return curatorFramework;
    }
    
    @Override
    public <T> T execute(CuratorCallback<T> callback) {
        return doExecute(callback);
    }
    
    private <T> T doExecute(CuratorCallback<T> callback) throws CuratorException {
        try {
            return callback.doInCurator(curatorFramework);
        } catch (Exception e) {
            throw convertToCuratorException(e);
        }
    }
    
    @Override
    public List<String> getChildren(final String path) {
        return execute(client -> {
            return client.getChildren().forPath(path);
        });
    }
    
    @Override
    public List<String> getAbsoluteChildren(final String path) {
        List<String> result = new ArrayList<String>(0);
        
        List<String> children = getChildren(path);
        if (children != null && !children.isEmpty()) {
            result = new ArrayList<String>(children.size());
            for (String child : children) {
                String absoluteChildPath = path + "/" + child;
                result.add(absoluteChildPath);
            }
        }
        return result;
    }
    
    @Override
    public List<byte[]> getChildrenData(String path) {
        List<String> children = getAbsoluteChildren(path);
        List<byte[]> result = new ArrayList<byte[]>(children.size());
        for (String child : children) {
            result.add(getData(child));
        }
        return result;
    }

    @Override
    public void create(final String path, final byte[] data) {
        execute(client -> {
            client.create().creatingParentsIfNeeded().forPath(path, data);
            return null;
        });
    }

    @Override
    public byte[] getData(final String path) {
        return execute(client -> {
            return client.getData().forPath(path);
        });
    }

    @Override
    public void setData(final String path, final byte[] data) {
        execute(client -> {
            client.setData().forPath(path, data);
            return null;
        });
    }
    
    @Override
    public void delete(final String path) {
        execute(client -> {
            client.delete().deletingChildrenIfNeeded().forPath(path);
            return null;
        });
    }
    
    @Override
    public void deleteChildren(final String path) {
        List<String> children = getAbsoluteChildren(path);
        for (String child : children) {
            delete(child);
        }
    }

    @Override
    public boolean checkPathExists(final String path) {
        return execute(client -> {
            Stat stat = client.checkExists().forPath(path);
            return stat != null;
        });
    }

    @Override
    public void createPathIfAbsent(String path) {
        if (!checkPathExists(path)) {
            create(path, new byte[0]);
        }
    }

}
