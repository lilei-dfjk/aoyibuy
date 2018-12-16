package com.aoyibuy.commons.sftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aoyibuy.commons.utils.NumberUtils;
import com.google.common.collect.Lists;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

/**
 * Sftp工具类
 * 
 * @author wh
 * @since 0.0.1
 */
public final class Sftp {
    private static final Logger logger = LoggerFactory.getLogger(Sftp.class); 
    
    public static final int DEFAULT_PORT = 22;
    public static final int DEFAULT_CONNECT_TIMEOUT_IN_MS = 30000;
    
    private String host; // SFTP主机IP
    private int port; // SFTP端口
    private String username; // 用户
    private String password; // 密码
    private int connectTimeoutInMs; // 连接超时时间(单位: 毫秒)

    private JSch jsch;
    private Session session;
    private ChannelSftp channelSftp;
    
    public Sftp(String host, String username, String password) {
        this(host, DEFAULT_PORT, username, password, DEFAULT_CONNECT_TIMEOUT_IN_MS);
    }
    
    public Sftp(String host, int port, String username, String password) {
        this(host, port, username, password, DEFAULT_CONNECT_TIMEOUT_IN_MS);
    }
    
    public Sftp(String host, String username, String password, int connectTimeoutInMs) {
        this(host, DEFAULT_PORT, username, password, connectTimeoutInMs);
    }

    public Sftp(String host, int port, String username, String password, int connectTimeoutInMs) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.connectTimeoutInMs = connectTimeoutInMs;
    }
    
    /**
     * 连接sftp服务器
     */
    protected void connect() {
        if (logger.isDebugEnabled()) {
            logger.debug("连接sftp服务器: {}", toString());
        }
        try {
            jsch = new JSch();
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(username, host, port);
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect(connectTimeoutInMs);
            channelSftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 断开sftp连接
     */
    protected void disconnect() {
        if (logger.isDebugEnabled()) {
            logger.debug("断开sftp服务器: {}", toString());
        }
        channelSftp.disconnect();
        session.disconnect();
    }
    
    /**
     * 获取sftp用户的home目录
     * @return
     */
    public String home() {
        try {
            return channelSftp.getHome();
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 打印当前目录
     * @return
     */
    public String pwd() {
        try {
            return channelSftp.pwd();
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 回到home目录
     */
    public void cd() {
        cd(home());
    }
    
    /**
     * 进入到某目录
     * @param path
     */
    public void cd(String path) {
        try {
            channelSftp.cd(path);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 列出当前目录下的所有文件
     * @return
     */
    public List<SftpFile> ls() {
        return ls(pwd(), Filter.ALL);
    }
    
    /**
     * 列出某目录下的所有文件
     * @param path
     * @return
     */
    public List<SftpFile> ls(String path) {
        return ls(path, Filter.ALL);
    }
    
    /**
     * 列出当前目录下的文件
     * @param filter 过滤列出文件
     * @return
     */
    public List<SftpFile> ls(Filter filter) {
        return ls(pwd(), filter);
    }
    
    /**
     * 列出某目录下的文件
     * @param path 目录
     * @param filter 过滤列出文件
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SftpFile> ls(String path, Filter filter) {
        try {
            List<SftpFile> list = Lists.newArrayList();
            Vector<LsEntry> lsEntries = channelSftp.ls(path);
            for (LsEntry lsEntry : lsEntries) {
                if (filter(lsEntry, filter)) {
                    SftpFile file = new SftpFile(lsEntry.getFilename(), lsEntry.getAttrs().isDir());
                    list.add(file);
                }
            }
            return list;
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
    
    private boolean filter(LsEntry lsEntry, Filter filter) {
        if (lsEntry.getFilename().startsWith(".") || lsEntry.getFilename().startsWith("..")) {
            return false;
        }
        switch (filter) {
            case ALL:
                return true;
            case DIR:
                return lsEntry.getAttrs().isDir();
            case FILE:
                return !lsEntry.getAttrs().isDir();
            default:
                break;
        }
        return false;
    }
    
    /**
     * 获取某目录下文件的字节数组形式
     * @param remotePath
     * @param fileName
     * @return
     */
    public byte[] getBytes(String remotePath, String fileName) {
        try {
            InputStream inputStream = getInputStream(remotePath, fileName);
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取某目录下文件的输入流形式
     * @param remotePath
     * @param fileName
     * @return
     */
    public InputStream getInputStream(String remotePath, String fileName) {
        try {
            channelSftp.cd(remotePath);
            InputStream inputStream = channelSftp.get(fileName, new SftpFileTransferProgressMonitor(fileName));
            return inputStream;
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 下载文件
     * @param remotePath
     * @param fileName
     * @param localPath
     */
    public void get(String remotePath, String fileName, String localPath) {
        try {
            String currentPath = pwd();
            cd(remotePath);
            String localFilePath = localPath + "/" + fileName;
            FileUtils.forceMkdir(new File(localPath));
            channelSftp.get(fileName, localFilePath, new SftpFileTransferProgressMonitor(fileName));
            cd(currentPath);
        } catch (SftpException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private class SftpFileTransferProgressMonitor implements SftpProgressMonitor {
        
        private static final double _1B = 1.00D;
        private static final double _1KB = _1B * 1024;
        private static final double _1MB = _1KB * 1024;
        private static final double _1GB = _1MB * 1024;
        
        private String fileName;
        private long transfered;
        
        private SftpFileTransferProgressMonitor(String fileName) {
            this.fileName = fileName;
        }
        
        @Override
        public void init(int op, String src, String dest, long max) {
            logger.info("文件{}开始传输", this.fileName);
        }
        
        @Override
        public void end() {
            logger.info("文件{}传输完毕", this.fileName);
        }
        
        @Override
        public boolean count(long count) {
            transfered = transfered + count;
            
            if (logger.isDebugEnabled()) {
                if (transfered < _1KB) {
                    logger.debug("文件{}已传输{}B", this.fileName, NumberUtils.formatNumber(transfered / _1B, "#,##0.00"));
                }
                if (transfered >= _1KB && transfered < _1MB) {
                    logger.debug("文件{}已传输{}KB", this.fileName, NumberUtils.formatNumber(transfered / _1KB, "#,##0.00"));
                }
                if (transfered >= _1MB && transfered < _1GB) {
                    logger.debug("文件{}已传输{}MB", this.fileName, NumberUtils.formatNumber(transfered / _1MB, "#,##0.00"));
                }
                if (transfered >= _1GB) {
                    logger.debug("文件{}已传输{}GB", this.fileName, NumberUtils.formatNumber(transfered / _1GB, "#,##0.00"));
                }
            }
            return true;
        }
    }
    
    /**
     * 用于过滤列出的文件
     */
    public enum Filter {
        ALL, DIR, FILE;
    }
     
    @Override
    public String toString() {
        return username + "@" + host + ":" + port;
    }

}
