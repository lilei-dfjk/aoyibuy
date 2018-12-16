package com.aoyibuy.commons.curator;

import com.aoyibuy.commons.utils.NetUtils;


/**
 * 应用的leader监听器
 * 
 * @author wh
 * @since 0.0.1
 */
public class AppLeaderListener extends AbstractCuratorLeaderListener  {
    
    private static final String APP = "app";
    
    private String nodeName;
    
    public AppLeaderListener() {
        setApp(APP);
    }
    
    public void setApp(String app) {
        this.nodeName = app + "@" + NetUtils.getLocalIpAddress(); // app@IP
    }
    
    @Override
    protected String getNodeName() {
        return nodeName;
    }

}
