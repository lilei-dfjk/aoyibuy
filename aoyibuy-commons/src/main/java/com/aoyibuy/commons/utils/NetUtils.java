package com.aoyibuy.commons.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class NetUtils {

    public static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}