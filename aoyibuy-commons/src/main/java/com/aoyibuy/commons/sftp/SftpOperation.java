package com.aoyibuy.commons.sftp;

/**
 * Sftp操作类
 * 
 * @author wh
 * @since 0.0.1
 */
public interface SftpOperation {

    /**
     * 执行Sftp的操作
     * 
     * @param sftpCallback
     * @return
     */
    <T> T execute(SftpCallback<T> sftpCallback);
    
}
