package com.aoyibuy.commons.sftp;

/**
 * Sftp操作模板
 * 
 * @author wh
 * @since 0.0.1
 */
public class SftpTemplate implements SftpOperation {
    
    private String host; // SFTP主机IP
    private int port; // SFTP端口
    private String username; // 用户
    private String password; // 密码
    private int connectTimeoutInMs; // 连接超时时间(单位: 毫秒)
    
    public SftpTemplate(String host, String username, String password) {
        this(host, Sftp.DEFAULT_PORT, username, password, Sftp.DEFAULT_CONNECT_TIMEOUT_IN_MS);
    }
    
    public SftpTemplate(String host, int port, String username, String password) {
        this(host, port, username, password, Sftp.DEFAULT_CONNECT_TIMEOUT_IN_MS);
    }
    
    public SftpTemplate(String host, String username, String password, int connectTimeoutInMs) {
        this(host, Sftp.DEFAULT_PORT, username, password, connectTimeoutInMs);
    }
    
    public SftpTemplate(String host, int port, String username, String password, int connectTimeoutInMs) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.connectTimeoutInMs = connectTimeoutInMs;
    }

    @Override
    public <T> T execute(SftpCallback<T> sftpCallback) {
        T result = null;
        Sftp sftp = new Sftp(host, port, username, password, connectTimeoutInMs);
        sftp.connect();
        result = sftpCallback.doInSftp(sftp);
        sftp.disconnect();
        return result;
    }

}
