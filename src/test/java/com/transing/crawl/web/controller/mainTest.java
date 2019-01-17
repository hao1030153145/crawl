package com.transing.crawl.web.controller;

import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.httpclient.HttpResponse;
import com.transing.crawl.util.CharsetUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

public class mainTest {

    public static void main(String[] args) throws Exception{

        Connection connect = Jsoup.connect("http://www.bsgxw.gov.cn/list-14-1.html");
        connect.method(Connection.Method.GET);
        connect.followRedirects(false);
        Connection.Response execute = connect.execute();
        Map<String, String> cookies = execute.cookies();
        cookies.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });

        HttpClientHelper httpClientHelper = new HttpClientHelper();
        Map<String,String> headerMap = new HashMap<>();
        //headerMap.put("Cookie","__cfduid=d47fe83d6ca7175a19d2e23a09b85c0e01536052060; yunsuo_session_verify=096436fbc935c4dff90f8194a1bba61f; srcurl=687474703a2f2f7777772e62736778772e676f762e636e2f6c6973742d31342d312e68746d6c; security_session_mid_verify=4eb90f3ca4d8ab936c9dd1cf96ad10ee; yjs_id=cf6278d9dc34747b543ea70a324f4dc0; ctrl_time=1");
        headerMap.put("Cookie","__cfduid=d47fe83d6ca7175a19d2e23a09b85c0e01536052060; yunsuo_session_verify=096436fbc935c4dff90f8194a1bba61f; ");
        String crawlURL = "http://www.bsgxw.gov.cn/list-14-1.html";
        HttpResponse httpResponse = httpClientHelper
                .doGetAndRetBytes(crawlURL, "utf-8", "utf-8",
                        headerMap, null);
        CharsetUtils charsetUtils=new CharsetUtils();
        String content= charsetUtils.CharsetCheckEntrance(httpResponse.getContentBytes(), "utf-8", "utf-8");
        System.out.println(content);
    }
}
