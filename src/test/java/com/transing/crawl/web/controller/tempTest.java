package com.transing.crawl.web.controller;

import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.transing.crawl.crawlParse.CrawlThread;
import com.transing.crawl.job.SubTaskManager;
import com.transing.crawl.job.impl.CrawlSubTask;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.client.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:tempTest.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年04月18日
 */
public class tempTest
{
    @Test
    public void  yangguangTest() throws Exception{
        String url="http://sou.xcar.com.cn/XcarSearch/infobbssearchresult/bbs/%E5%A5%A5%E8%BF%AA/none/none/none/none/1?rand=1524047112118";
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        Map<String,String> headMap=new HashMap<String, String>();
        headMap.put("Referer","http://search.xcar.com.cn/bbssearch.php");
        /*headMap.put("Cookie","wdcid=0aeb1b4ca7b3c704");*/
        headMap.put("Cookie","_appuv_www=6f8ced2957a4ca6dd3e8d709f3de460e;_PVXuv=5ad71cd0901da;");
        SiteProxyIp siteProxyIp=null;//new SiteProxyIp("118.190.138.155",9001);
        String content=httpClientHelper.doGet(url,"utf-8","utf-8",headMap,siteProxyIp).getContent();
        System.out.println(content);
    }

    @Test
    public void yanggaungPost() throws Exception{
        String url="http://was.cnr.cn/was5/web/search";
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        Map<String,String> headMap=new HashMap<String, String>();
        headMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headMap.put("Host","was.cnr.cn");
        headMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3294.6 Safari/537.36");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Referer","http://www.cnr.cn/");
       /* headMap.put("Content-type","application/x-www-form-urlencoded");
        headMap.put("Referer","http://search.mofcom.gov.cn/swb/searchList.jsp");
        headMap.put("Origin","http://search.mofcom.gov.cn");*/
        //headMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3294.6 Safari/537.36");
        Map<String,String> postMap=new HashMap<String, String>();
       /* postMap.put("value","%E5%A4%A7%E4%BC%97");
        postMap.put("where","(DOCTITLE/10,DOCCONTENT/1 =((%E5%A4%A7%E4%BC%97)))");
        postMap.put("flag","0");
        postMap.put("curpage","1");
        postMap.put("pagesize","10");
        postMap.put("order","1");
        postMap.put("time","0");
        postMap.put("class","2");
        postMap.put("level","1");*/
       postMap.put("channelid","234439");
       postMap.put("page","1");
        postMap.put("searchword","特斯拉");
        postMap.put("keyword","特斯拉");
        postMap.put("was_custom_expr","(特斯拉)29");
        postMap.put("perpage","10");
        postMap.put("outlinepage","10");
        postMap.put("orderby","LIFO");
        SiteProxyIp siteProxyIp=new SiteProxyIp("118.190.138.155",9001);
        String content=httpClientHelper.doPost(url,postMap,"utf-8","utf-8",headMap,siteProxyIp).getContent();
        System.out.println(content);
    }

    @Test
    public void TestXCar() throws Exception{
        String url1="http://www.xcar.com.cn/site_js/header/new_login_2015.php";
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        Map<String,String> headMap=new HashMap<String, String>();
        headMap.put("Referer","http://search.xcar.com.cn/bbssearch.php");
        httpClientHelper.doGet(url1,"utf-8","utf-8",headMap,null);
        String url="http://sou.xcar.com.cn/XcarSearch/infobbssearchresult/bbs/%E5%A5%A5%E8%BF%AA/none/none/none/none/1?rand=1524047112118";
        /*headMap.put("Cookie","wdcid=0aeb1b4ca7b3c704");*/
/*        headMap.put("Cookie","_appuv_www=6f8ced2957a4ca6dd3e8d709f3de460e;_PVXuv=5ad71cd0901da;");*/
        String t=ParseUtil.jsParse(getPCXuV(),"test");
        headMap.put("Cookie","_PVXuv="+t);
        SiteProxyIp siteProxyIp=null;//new SiteProxyIp("118.190.138.155",9001);
        String content=httpClientHelper.doGet(url,"utf-8","utf-8",headMap,siteProxyIp).getContent();
        System.out.println(content);
    }

    public String getPCXuV(){
        String test ="";
        return test;
    }

    @Test
    public void tes(){
        String js=getPCXuV();
        String t=ParseUtil.jsParse(js,"test");
        System.out.println(t);
    }


    @Test
    public void testGetList() throws Exception {
        String url = "http://was.cnr.cn/was5/web/search?page=1&channelid=234439&searchword=&keyword=&orderby=LIFO&was_custom_expr=&" +
                "perpage=10&outlinepage=10&searchscope=&timescope=&timescopecolumn=&orderby=LIFO&andsen=&total=&orsen=&exclude=";

        HttpClientHelper httpClientHelper = new HttpClientHelper();
        HttpResponse response = httpClientHelper.doGet(url, "gbk", null, null, null);
        HttpResponse httpResponse = httpClientHelper.doGetAndRetBytes(url, "gbk", null, null, null);

        System.out.println(response.getContent());
        System.out.println(httpResponse.getStatusCode());
    }

    @Test
    public void testGetList2() throws Exception {
        String url = "http://sou.xcar.com.cn/XcarSearch/infobbssearchresult/information/比亚迪/none/一周内/最新/1?rand=1508292946917";

        HttpClientHelper httpClientHelper = new HttpClientHelper();

        String cookie = "JSESSIONID=AD704DF4DA8B11C1CD4C721C78621B67; _Xdwuv=5244529021382; " +
                "_fwck_www=41db6d0096b824bb0e271e230d7547c5; _appuv_www=3f56e7e8dde138fef9d8d2eed6f6017e; _Xdwnewuv=1; " +
                "_PVXuv=5add40790ba96; place_prid=17; place_crid=386; place_ip=222.211.212.127_1; _fwck_my=74ca24865f88d8e5aa1964069e67d99c; " +
                "_appuv_my=47352b2e0a5f42dd717270c3c8642aa0; _locationInfo_=%7Burl%3A%22http%3A%2F%2Fchengdu.xcar.com.cn%2F%22%2Ccity_id%3A%22386%22%2Cprovince_id%3A%2217%22%2C%20" +
                "city_name%3A%22%25E6%2588%2590%25E9%2583%25BD%22%7D; Hm_lvt_53eb54d089f7b5dd4ae2927686b183e0=1524450950,1524452983,1524453735; BIGipServerpool-c26-xcar-data-80=" +
                "2462650122.20480.0000; _fwck_a=51156d2bce0815b857c442abab2ac574; _appuv_a=46b46b5afca5850f1ed86fc15d457bc6; bbs_visitedfid=1982D468D1137D1536; bbs_sid=90UIMk;" +
                " Hm_lpvt_53eb54d089f7b5dd4ae2927686b183e0=1524564365; pt_142454b2=uid=VqACUcqB6lQZTwj2lacAAg&nid=0&vid=zuX2fmfv7RwNDwN/s-sYKQ&vn=2&pvn=1&sact=1524638979345&" +
                "to_flag=0&pl=GYMMyYc306vV5Sjzw76ifA*pt*1524638979345; pt_s_142454b2=vt=1524638979345&cad=";

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Host", "sou.xcar.com.cn");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        headerMap.put("Cache-Control", "max-age=0");
        headerMap.put("Connection", "keep-alive");
        headerMap.put("Cookie", cookie);

        HttpResponse response = httpClientHelper.doGet(url, "gbk", null, headerMap, null);
        HttpResponse httpResponse = httpClientHelper.doGetAndRetBytes(url, "gbk", null, headerMap, null);

        System.out.println(response.getContent());
        System.out.println(httpResponse.getStatusCode());
    }

    @Test
    public void testGetList3() throws Exception {
        String url = "http://sou.xcar.com.cn/XcarSearch/infobbssearchresult/bbs/%E6%AF%94%E4%BA%9A%E8%BF%AA/none/none/none/none/1";

        HttpClientHelper httpClientHelper = new HttpClientHelper();

        String cookie = "JSESSIONID=AD704DF4DA8B11C1CD4C721C78621B67; _Xdwuv=5244529021382; " +
                "_fwck_www=41db6d0096b824bb0e271e230d7547c5; _appuv_www=3f56e7e8dde138fef9d8d2eed6f6017e; _Xdwnewuv=1; " +
                "_PVXuv=5add40790ba96; place_prid=17; place_crid=386; place_ip=222.211.212.127_1; _fwck_my=74ca24865f88d8e5aa1964069e67d99c; " +
                "_appuv_my=47352b2e0a5f42dd717270c3c8642aa0; _locationInfo_=%7Burl%3A%22http%3A%2F%2Fchengdu.xcar.com.cn%2F%22%2Ccity_id%3A%22386%22%2Cprovince_id%3A%2217%22%2C%20" +
                "city_name%3A%22%25E6%2588%2590%25E9%2583%25BD%22%7D; Hm_lvt_53eb54d089f7b5dd4ae2927686b183e0=1524450950,1524452983,1524453735; BIGipServerpool-c26-xcar-data-80=" +
                "2462650122.20480.0000; _fwck_a=51156d2bce0815b857c442abab2ac574; _appuv_a=46b46b5afca5850f1ed86fc15d457bc6; bbs_visitedfid=1982D468D1137D1536; bbs_sid=90UIMk;" +
                " Hm_lpvt_53eb54d089f7b5dd4ae2927686b183e0=1524564365; pt_142454b2=uid=VqACUcqB6lQZTwj2lacAAg&nid=0&vid=zuX2fmfv7RwNDwN/s-sYKQ&vn=2&pvn=1&sact=1524638979345&" +
                "to_flag=0&pl=GYMMyYc306vV5Sjzw76ifA*pt*1524638979345; pt_s_142454b2=vt=1524638979345&cad=";

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);

        HttpResponse response = httpClientHelper.doGet(url, "gbk", null, headerMap, null);
        HttpResponse httpResponse = httpClientHelper.doGetAndRetBytes(url, "gbk", null, headerMap, null);

        System.out.println(response.getContent());
        System.out.println(httpResponse.getStatusCode());
    }

    @Test
    public void get() throws IOException {
        String url = "http://was.cnr.cn/was5/web/search?page=1&channelid=234439&searchword=&keyword=&orderby=LIFO&was_custom_expr=&" +
                "perpage=10&outlinepage=10&searchscope=&timescope=&timescopecolumn=&orderby=LIFO&andsen=&total=&orsen=&exclude=";

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpGet httpget = new HttpGet(url);

            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    System.out.println("Response content length: " + entity.getContentLength());
                    System.out.println("Response content: " + EntityUtils.toString(entity));
                }
            }
        } catch (ClientProtocolException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //只会影响参数顺序，不影响参数个数和正确性
    @Test
    public void getEncodeUrlTest() throws Exception {
        String url = "http://was.cnr.cn/was5/web/search?page=1&channelid=234439&searchword=&keyword=&orderby=LIFO&was_custom_expr=&" +
                "perpage=10&outlinepage=10&searchscope=&timescope=&timescopecolumn=&orderby=LIFO&andsen=&total=&orsen=&exclude=";
        String url2 = "http://was.cnr.cn/was5/web/search?1=2&3=4&5=6&7=8";
        String url3 = "http://was.cnr.cn/was5/web/search/info/1";

        //GBK
        String byMapGBK = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url, "gbk");
        String byListGBK = HttpClientHelper.getEncodeUrl(url, "gbk");
        String byMap2GBK = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url2, "gbk");
        String byList2GBK = HttpClientHelper.getEncodeUrl(url2, "gbk");
        String byMap3GBK = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url3, "gbk");
        String byList3GBK = HttpClientHelper.getEncodeUrl(url3, "gbk");

        //UTF-8
        String byMapUTF8 = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url, "UTF-8");
        String byListUTF8 = HttpClientHelper.getEncodeUrl(url, "UTF-8");
        String byMap2UTF8 = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url2, "UTF-8");
        String byList2UTF8 = HttpClientHelper.getEncodeUrl(url2, "UTF-8");
        String byMap3UTF8 = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url3, "UTF-8");
        String byList3UTF8 = HttpClientHelper.getEncodeUrl(url3, "UTF-8");

        //null
        String byMap = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url, null);
        String byList = HttpClientHelper.getEncodeUrl(url, null);
        String byMap2 = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url2, null);
        String byList2 = HttpClientHelper.getEncodeUrl(url2, null);
        String byMap3 = com.jeeframework.util.httpclient.URLUtil.getEncodeUrl(url3, null);
        String byList3 = HttpClientHelper.getEncodeUrl(url3, null);

        HttpClientHelper httpClientHelper = new HttpClientHelper();

        HttpResponse response = httpClientHelper.doGet(byMapGBK, "gbk", null, null, null);
        HttpResponse httpResponse = httpClientHelper.doGetAndRetBytes(byListGBK, "gbk", null, null, null);

        HttpResponse response2 = httpClientHelper.doGet(byMapUTF8, "UTF-8", null, null, null);
        HttpResponse httpResponse2 = httpClientHelper.doGetAndRetBytes(byListUTF8, "UTF-8", null, null, null);

        HttpResponse response3 = httpClientHelper.doGet(byMap, null, null, null, null);
        HttpResponse httpResponse3 = httpClientHelper.doGetAndRetBytes(byList, null, null, null, null);

        System.out.println("byMapGBK: " + byMapGBK);
        System.out.println("byListGBK: " + byListGBK);
        System.out.println("byMap2GBK: " + byMap2GBK);
        System.out.println("byList2GBK: " + byList2GBK);
        System.out.println("byMap3GBK: " + byMap3GBK);
        System.out.println("byList3GBK: " + byList3GBK);

        System.out.println("byMapUTF8: " + byMapUTF8);
        System.out.println("byListUTF8: " + byListUTF8);
        System.out.println("byMap2UTF8: " + byMap2UTF8);
        System.out.println("byList2UTF8: " + byList2UTF8);
        System.out.println("byMap3UTF8: " + byMap3UTF8);
        System.out.println("byList3UTF8: " + byList3UTF8);

        System.out.println("byMap: " + byMap);
        System.out.println("byList: " + byList);
        System.out.println("byMap2: " + byMap2);
        System.out.println("byList2: " + byList2);
        System.out.println("byMap3: " + byMap3);
        System.out.println("byList3: " + byList3);

        Assert.assertEquals(url, byListGBK);
        Assert.assertEquals(url2, byList2GBK);
        Assert.assertEquals(url3, byMap3GBK);
        Assert.assertEquals(url3, byList3GBK);

        Assert.assertEquals(url, byListUTF8);
        Assert.assertEquals(url2, byList2UTF8);
        Assert.assertEquals(url3, byMap3UTF8);
        Assert.assertEquals(url3, byList3UTF8);

        Assert.assertEquals(url, byList);
        Assert.assertEquals(url2, byList2);
        Assert.assertEquals(url3, byMap3);
        Assert.assertEquals(url3, byList3);

        Assert.assertNotEquals(response.getStatusCode(), 200);
        Assert.assertEquals(httpResponse.getStatusCode(), 200);

        Assert.assertNotEquals(response2.getStatusCode(), 200);
        Assert.assertEquals(httpResponse2.getStatusCode(), 200);

        Assert.assertNotEquals(response3.getStatusCode(), 200);
        Assert.assertEquals(httpResponse3.getStatusCode(), 200);
    }

}
