package com.aoyibuy.commons.sftp;

/**
 * 代表Sftp上的一个文件
 * 
 * @author wh
 * @since 0.0.1
 */
public class SftpFile {

    private String name;
    
    private boolean isDirectory;
    
    public SftpFile(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isDirectory() {
        return isDirectory;
    }
    
    public boolean isFile() {
        return !isDirectory;
    }
}
