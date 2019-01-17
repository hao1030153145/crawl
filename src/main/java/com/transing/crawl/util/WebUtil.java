/**
 * @project: with
 * @Title: WebUtil.java
 * @Package: com.transing.crawl.util
 * <p/>
 * Copyright (c) 2014-2017 Jeeframework.com Limited, Inc.
 * All rights reserved.
 */
package com.transing.crawl.util;

import com.jeeframework.core.context.support.SpringContextHolder;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.httpclient.HttpResponse;
import com.jeeframework.util.net.IPUtils;
import com.jeeframework.util.validate.Validate;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.http.HttpException;
import org.apache.struts2.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Web端数据工具类
 * <p/>
 *
 * @author lance
 * @version 1.0 2015-3-4 下午02:34:50
 * @Description: 所有web相关的操作工具方法
 */
public class WebUtil {

    public static String loggerName = "WebUtil";
    public static final int RESPONSE_CODE_SUCCESS = 0;//返回成功
    public static final int RESPONSE_CODE_ERROR = 1;//返回失败

    private static Properties configProperties = SpringContextHolder.getBean("configProperties");

    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    public static final String CONNECTION_COOKIE="connectionCookie";
    public static final String CONNECTION_HOST="connectionHost";
    public static final String CONNECTION_PORT="connectionPort";
    public static final String CONNECTION_ID="connectionId";
    public static final String CONNECTION_REQINTERVAL="reqInterval";
    public static final String CONNECTION_EXCEPTIONMARK="exceptionMarks";

    public static final String  POST_METHED="POST";
    public static final String  GET_METHED="GET";

    public static final String TRAVEL_PARAMS="travelParams";


    public static final String CONNECTION_NORMAL="0";

    public static final String CONNECTION_EXCEPTION="1";

    private WebUtil() {
    }

    /**
     * 根据request获取uri请求
     *
     * @param request
     * @return
     */
    public static String getUri(HttpServletRequest request) {
        // handle http dispatcher includes.
        String uri = (String) request
                .getAttribute("javax.servlet.include.servlet_path");
        if (uri != null) {
            return uri;
        }

        uri = RequestUtils.getServletPath(request);
        if (!Validate.isEmpty(uri)) {
            return uri;
        }

        uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());
    }

    /**
     * 简单描述：组装静态资源的绝对URL
     * <p/>
     *
     * @param staticURI
     * @return
     */
    public static String combinateStaticURL(String staticURI) {

        if (Validate.isEmpty(staticURI)) {
            return "";
        }
        if (staticURI.startsWith(HTTP_PREFIX)) {
            return staticURI;
        }

        String host = getStaticServerByEnv();

        return HTTP_PREFIX + host + staticURI;

    }

    /**
     * 简单描述：返回用户的头像
     * <p/>
     *
     * @param avatarURI
     * @param width     0 原图 640 260 100 4种规格
     * @return
     */
    public static String getUserAvatar(String avatarURI, int width) {

        if (Validate.isEmpty(avatarURI)) {
            return ""; //返回系统默认头像
        }
        if (avatarURI.startsWith(HTTP_PREFIX) || avatarURI.startsWith(HTTPS_PREFIX)) {
            return avatarURI;
        }

        String ext = com.jeeframework.util.io.FileUtils.getExtention(avatarURI);
        int posOfUnderScode = avatarURI.lastIndexOf('_');
        String destImagePrefix = "";
        if (posOfUnderScode >= 0) {
            destImagePrefix = avatarURI.substring(0, posOfUnderScode);
        }
        String suffix ;
        if (!Validate.isEmpty(destImagePrefix)) {
            suffix = destImagePrefix + "_" + width + ext;
        } else {
            int posOfLastDot = avatarURI.lastIndexOf('.');
            String avatarURITmp = posOfLastDot > 0 ? avatarURI.substring(0, posOfLastDot) : avatarURI;
            suffix = avatarURITmp + "_" + width + ext;
        }

        if (!suffix.startsWith("/")) {
            suffix = "/" + suffix;
        }

        String host = getStaticServerByEnv();

        return HTTP_PREFIX + host + suffix;

    }

    /**
     * 根据环境配置返回web服务器的地址，这个是动态代码的服务器
     *
     * @return
     */
    public static String getWebServerByEnv() {
        String host = "www.jeeframework.com";

        return getHostNameByEnv(host, IPUtils.LOCAL_IP + ":8080", "dev.jeeframework.com", "test.jeeframework.com", "www.jeeframework.com");
    }

    public static String getHostNameByEnv(String host, String localhost, String devHost, String testHost, String idcHost) {
        String confEnv = System.getProperty("conf.env");
        String hostTmp = null;
        if (!Validate.isEmpty(confEnv)) {
            if ("local".equals(confEnv)) {
                hostTmp = localhost;
            }
            if ("dev".equals(confEnv)) {
                hostTmp = devHost;
            }
            if ("test".equals(confEnv)) {
                hostTmp = testHost;
            }
            if ("idc".equals(confEnv)) {
                hostTmp = idcHost;
            }
        }
        if (Validate.isEmpty(hostTmp)) {
            hostTmp = host;
        }
        return hostTmp;
    }

    /**
     * 根据环境配置返回静态资源服务器的地址
     *
     * @return
     */
    public static String getStaticServerByEnv() {
        String host = "static.jeeframework.com";
        return getHostNameByEnv(host, "staticdev.jeeframework.com", "staticdev.jeeframework.com", "statictest.jeeframework.com", "static.jeeframework.com");
    }

    /**
     * 查询dpmbs的服务器地址
     * @return
     */
    public static String getDpmbsServerByEnv(){
        return configProperties.getProperty("dpmbs.url");
    }

    /**
     * 语料库的服务器地址
     * @return
     */
    public static String getCorpusServerByEnv(){
        return configProperties.getProperty("corpus.url");
    }

    /**
     * 连接池的服务器地址
     * @return
     */
    public static String getConnectionPoolByEnv(){
        return configProperties.getProperty("crawlbs.url");
    }

    /**
     * 连接池的服务器地址
     * @return
     */
    public static String getBasebsByEnv(){
        return configProperties.getProperty("basebs.url");
    }

    public static String getTaskIndexAlias(){
        return configProperties.getProperty("index.alias");
    }

    /**
     * 获取连接等待最大时长
     * @return
     */
    public static Integer getConnectionWaitTime(){
        return Integer.parseInt(configProperties.getProperty("connection.max.timeLength"));
    }


    public static Object callRemoteService(String serviceURL, String method, Map<String, String> postData) {
        String getTermListStr = doHttpRequest(serviceURL, method, postData);

        try {
            JSONObject getTermListJsonObject = JSONObject.fromObject(getTermListStr);
            int code = getTermListJsonObject.getInt("code");

            if (code == RESPONSE_CODE_SUCCESS) {
                return getTermListJsonObject.get("data");
            }
            if (code == RESPONSE_CODE_ERROR) {
                return code;
            }

        } catch (JSONException e) {
            LoggerUtil.errorTrace(loggerName, "访问远程接口出错，跳出执行。返回内容为：" + getTermListStr, e);
            throw e;
        }
        return 9;
    }

    private static String doHttpRequest(String serviceURL, String method, Map<String, String> postData) {
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        httpClientHelper.setConnectionTimeout(30000);
        int retryTimes = 0;
        String getTermListStr = "{}";
        while (retryTimes < 3) {
            try {
                HttpResponse getTermListResponse = null;

                if (method.equalsIgnoreCase("get")) {
                    getTermListResponse = httpClientHelper.doGet(serviceURL,
                            "utf-8", "utf-8", null,
                            null);
                } else {
                    getTermListResponse = httpClientHelper.doPost(serviceURL, postData, "utf-8", "utf-8", null, null);
                }
                getTermListStr = getTermListResponse.getContent();
                break;
            } catch (HttpException e) {
                LoggerUtil.errorTrace(loggerName, e);
                break;
            } catch (IOException e) {
                retryTimes++;
                try
                {
                    Thread.sleep(6000);
                }
                catch (InterruptedException e1)
                {
                }
                getTermListStr = "{}";
                LoggerUtil.errorTrace(loggerName, e);
                LoggerUtil.debugTrace(loggerName, "出现IO错误，重试 " + retryTimes + " 次");
            }
        }
        return getTermListStr;
    }

    /**
     * 获取连接
     * @param datasourceId
     * @return
     */
    public static Map<String,String> doGetConnection(long datasourceId,Long datasourceTypeId){
        try
        {

            String connectionPoolAddr = getConnectionPoolByEnv() +
                    "/connection/get.json?datasourceId=" + datasourceId;
            if(datasourceTypeId!=null){
                connectionPoolAddr+="&datasourceTypeId="+datasourceTypeId;
            }
            Object object = WebUtil
                    .callRemoteService(connectionPoolAddr, "get", null);
            LoggerUtil.debugTrace(loggerName,connectionPoolAddr+"，连接="+object.toString());
            if (object instanceof JSONObject)
            {
                JSONObject json = (JSONObject) object;
                String cookie = json.getString("cookie");
                String host = json.getString("host");
                int port = json.getInt("port");
                int reqInterval = json.getInt("reqInterval");
                String connectionId = json.getString("connectionId");
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put(CONNECTION_COOKIE, cookie);
                resultMap.put(CONNECTION_HOST, host);
                resultMap.put(CONNECTION_PORT, port + "");
                resultMap.put(CONNECTION_ID, connectionId);
                resultMap.put(CONNECTION_REQINTERVAL, reqInterval + "");
                resultMap.put(CONNECTION_EXCEPTIONMARK,json.getString("exceptionMark"));
                return resultMap;
            }
            else
            {
                return null;
            }
        }catch (Exception e){
            LoggerUtil.errorTrace(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 回收连接
     * @param datasourceId
     * @param connectionId
     * @param status
     * @param html
     */
    public static void doRollBackConnection(long datasourceId,Long datasourceTypeId,String connectionId,String status,String html,String mark){
        String connectionPoolAddr=getConnectionPoolByEnv()+"/connection/recovery.json";
        Map<String,String> postParma=new HashMap<String, String>();
        postParma.put("connectionId",connectionId);
        postParma.put("datasourceId",datasourceId+"");
        postParma.put("datasourceTypeId",datasourceTypeId+"");
        postParma.put("status",status);
        postParma.put("html",html);
        postParma.put("mark",mark);
        Object object=WebUtil.callRemoteService(connectionPoolAddr,"post",postParma);
    }




}