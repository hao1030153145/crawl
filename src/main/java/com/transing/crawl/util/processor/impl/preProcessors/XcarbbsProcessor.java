package com.transing.crawl.util.processor.impl.preProcessors;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.ProcessorUtil;
import com.wordnik.swagger.annotations.Api;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.http.cookie.Cookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.util.processor.impl.preProcessors
 * 源文件:XcarbbsProcessor.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年04月18日
 */
@Api(value = "爱卡汽车bbs",description = "爱卡汽车bbs需要获取cookie的时候重新获取Cookie并重新抓取")
public class XcarbbsProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        try{
            String url=param.getString(ProcessorUtil.URL);
            String beforUrl="http://www.xcar.com.cn/site_js/header/new_login_2015.php";

            SiteProxyIp siteProxyIp=null;
            if(param.containsKey(ProcessorUtil.HOST))
            {
                String host = param.getString(ProcessorUtil.HOST);
                int port = param.getInt(ProcessorUtil.PORT);
                if (!Validate.isEmpty(host)&&port!=0)
                    siteProxyIp=new SiteProxyIp(host,port);
            }

            Map<String,String> header=new HashMap<String, String>();
            if (param.containsKey(ProcessorUtil.HEADMAP)){
                header= (Map<String, String>) JSONObject.toBean(param
                        .getJSONObject(ProcessorUtil.HEADMAP),HashMap.class,new JsonConfig());
            }
            header.put("Referer","http://search.xcar.com.cn/bbssearch.php");
            HttpClientHelper httpClientHelper=new HttpClientHelper();
            httpClientHelper.doGet(beforUrl,"utf-8","utf-8",header,siteProxyIp);
            List<Cookie> cookies= httpClientHelper.getCookies();
            header.put("Cookie","_PVXuv="+jsrun());
            LoggerUtil.debugTrace(loggerName,"Xcar cookie="+JSONObject.fromObject(header)+" "+JSONObject.fromObject(cookies) );
            byte[] bytes=httpClientHelper.doGetAndRetBytes(url,"GBK","GBK",header,siteProxyIp).getContentBytes();
            content = new String(bytes,"GBK");
        }catch (Exception e){e.printStackTrace();}
        return content;
    }

    private String jsrun(){
        return ParseUtil.jsParse(getCookiePCXuV(),"test");
    }

    private String getCookiePCXuV(){
        return "function test(){var A=function(){var a=function(a,b){a=parseInt(a,10).toString(16);return b<a.length?a.slice(a.length-b):b>a.length?Array(1+(b-a.length)).join(\"0\")+a:a};this.php_js||(this.php_js={});this.php_js.uniqidSeed||(this.php_js.uniqidSeed=Math.floor(123456789*Math.random()));this.php_js.uniqidSeed++;var b;b=\"\"+a(parseInt((new Date).getTime()/1000,10),8);b+=a(this.php_js.uniqidSeed,5);return b.replace(new RegExp(/(e)/g),\"\"+parseInt(10*Math.random()))}();return A};";
    }
}
