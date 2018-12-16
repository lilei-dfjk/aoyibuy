package com.aoyibuy.commons.sftp;

/**
 * Sftp回调类，用于向SftpOperation的使用者暴露Sftp对象
 * 
 * @author wh
 *
 * @param <T> 执行一系列Sftp操作后的返回值类型
 * 
 * @since 0.0.1
 */
@FunctionalInterface
public interface SftpCallback<T> {

    T doInSftp(Sftp sftp);
    
}
