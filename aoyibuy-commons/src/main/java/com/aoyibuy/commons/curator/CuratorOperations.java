package com.aoyibuy.commons.curator;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;

public interface CuratorOperations {

    <T> T execute(CuratorCallback<T> callback);
    
    CuratorFramework getClient();
    
    boolean checkPathExists(String path);
    
    List<String> getChildren(String path);
    
    List<String> getAbsoluteChildren(String path);
    
    List<byte[]> getChildrenData(String path);
    
    byte[] getData(String path);
    
    void setData(String path, byte[] data);
    
    void delete(String path);
    
    void deleteChildren(String path);
    
    void create(String path, byte[] data);

    void createPathIfAbsent(String path);
    
}