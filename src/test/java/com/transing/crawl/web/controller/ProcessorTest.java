package com.transing.crawl.web.controller;

import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.ProcessorUtil;
import com.transing.crawl.util.processor.impl.preProcessors.AqsiqProcessor;
import com.transing.crawl.util.processor.impl.preProcessors.XcarbbsProcessor;
import net.sf.json.JSONObject;
import org.apache.http.HttpException;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:ProcessorTest.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年04月12日
 */
public class ProcessorTest
{

    @Test
    public void xcarTest(){
        try
        {
            //String url = "http://sou.xcar.com.cn/XcarSearch/infobbssearchresult/information/排放标准/none/一周内/最新/1?rand=1508292946917";
            String url1="http://info.xcar.com.cn/201710/news_1988388_1.html";
            BaseProcessor processor=new XcarbbsProcessor();
            JSONObject param=JSONObject.fromObject("{}");
            param.put(ProcessorUtil.URL,url1);
            Map<String,String> headMap=new HashMap<String, String>();
            //String url1="http://dw.xcar.com.cn/analytics.php";
            //headMap.put("Cookie","_Xdwuv=5ad04fd856752; _PVXuv=5ad050210305a;");//_appuv_www=51205b4502ff58db1737fc7cf20bda8a;_fwck_www=f9543bb0934db511243d3aefc57a5072;
            HttpClientHelper httpClientHelper=new HttpClientHelper();
            String content=httpClientHelper.doGet(url1,"utf-8","utf-8",headMap,null).getContent();
            String object=processor.runProcessor("",param,"");
            //content=httpClientHelper.doGet(url,"utf-8","utf-8",headMap,null).getContent();
            System.out.println(object);
        }catch (Exception e){

        }
    }

    @Test
    public void AqsiqProcessor() throws Exception{
        String url="http://so.aqsiq.gov.cn/view?qt=%E5%A5%A5%E8%BF%AA&location=1&reference=ee7e365a522dd590cfc4cbfb54ed1e2c&url=816D9628B2B6451D1D0F91C71EEBAE78EDFDD39CDFA1B5A6104BC9DFD51CC814E31BA12E31B5B7A0D2AB9CBD79AF63FA15D796B8E072A3DB1FA50A6554C0B9386821D04F03BCA47F&title=%E4%B8%80%E6%B1%BD-%E5%A4%A7%E4%BC%97%E6%B1%BD%E8%BD%A6%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E5%8F%AC%E5%9B%9E%E9%83%A8%E5%88%86%E5%9B%BD%E4%BA%A7%E5%A5%A5%E8%BF%AAA4L%E8%BF%9B%E5%8F%A3%E5%A5%A5%E8%BF%AAA4+allroadA5%E7%B3%BB%E5%88%97%E6%B1%BD%E8%BD%A6&database=all";
        BaseProcessor baseProcessor=new AqsiqProcessor();
        JSONObject param=JSONObject.fromObject("{}");
        param.put(ProcessorUtil.URL,url);
        Map<String,String> headMap=new HashMap<String, String>();
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        String content=httpClientHelper.doGet(url,"utf-8","utf-8",headMap,null).getContent();
        baseProcessor.runProcessor(content,param,"");

    }

    @Test
    public void xcarBBsTest() throws Exception{
        String url="http://sou.xcar.com.cn/XcarSearch/infobbssearchresult/information/%E5%A4%A7%E4%BC%97/none/none/none/1?rand=1524051503186";
        BaseProcessor processor=new XcarbbsProcessor();
        JSONObject param=JSONObject.fromObject("{}");
        param.put(ProcessorUtil.URL,url);
        String content=processor.runProcessor("",param,"");
        System.out.println(content);
    }

    @Test
    public void XcarTest() throws IOException, HttpException
    {
        String url="http://club.autohome.com.cn/bbs/thread/cff6e69a732e697a/41604391-1.html";
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        String content=httpClientHelper.doGet(url,"utf-8","GBK",null,null).getContent();
        System.out.println(content);
    }
}
