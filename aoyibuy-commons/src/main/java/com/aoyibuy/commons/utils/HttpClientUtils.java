package com.aoyibuy.commons.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * httpclient的工具类
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public static String httpPost(String uri, String requestBody, Map<String, String> headers) {
        return httpPost(uri, requestBody, "UTF-8", headers);
    }

    public static String httpPost(String uri, String requestBody, String contentEncoding, Map<String, String> headers) {
        return httpPost(uri, requestBody, contentEncoding, headers, 10000, 30000);
    }
    
    public static String httpPost(String uri, String requestBody, String contentEncoding, Map<String, String> headers, 
            int connectionTimeout, int soTimeout) {
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setConnectionTimeout(connectionTimeout);
        params.setSoTimeout(soTimeout);
        params.setDefaultMaxConnectionsPerHost(10);
        params.setMaxTotalConnections(200);

        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.setParams(params);

        HttpClient httpClient = new HttpClient(connectionManager);

        PostMethod method = new PostMethod(uri);
        headers.forEach((headerName, headerValue) -> {
            method.addRequestHeader(headerName, headerValue);
        });

        String responseString = "";
        try {
            method.setRequestEntity(new StringRequestEntity(requestBody, null, contentEncoding));
            httpClient.executeMethod(method);
            responseString = method.getResponseBodyAsString();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return responseString;
    }

}
