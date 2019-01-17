package com.transing.crawl.util.processor.impl.preProcessors;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.httpclient.HttpResponse;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.ProcessorUtil;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 包: com.transing.crawl.util.processor.impl
 * 源文件:HuanQiuProcessor.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年08月16日
 */
public class HuanQiuProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        try
        {
            int k = content.indexOf("return p}(");
            int j = content.indexOf("))</script>");
            String paraStr = content
                    .substring(k + 10, j);
            String[] param2 = paraStr.split(",");
            String bot = "";
            for (int ii = 0;
                 ii < param2.length; ii++)
            {
                if (ii == 3)
                {
                    String[] pp = param2[3]
                            .split("\\|");
                    for (int w = 0;
                         w < pp.length; w++)
                    {
                        System.out
                                .println(
                                        pp[w]);

                        if (pp[w].length() ==40)
                        {
                            bot = param2[ii];
                        }
                    }
                }
            }
            Map<String,String> header=null;
            try{
                String headstr=param.getString(ProcessorUtil.HEADMAP);
                if(!Validate.isEmpty(headstr)){
                    header= (Map<String, String>) JSONObject.toBean(JSONObject.fromObject(headstr),new HashMap(),new JsonConfig());
                }else{
                    header =new HashMap<String, String>();
                }
            }catch (Exception e){
                header =new HashMap<String, String>();
            }
            header.put("Cookie",
                    "HQ-ANTI-BOT=" + bot);

            String url=param.getString("url");
            String host=param.getString(ProcessorUtil.HOST);
            int port=param.getInt(ProcessorUtil.PORT);
            SiteProxyIp siteProxyIp=null;
            if(!Validate.isEmpty(host)){
                siteProxyIp=new SiteProxyIp(host,port);
            }

            HttpClientHelper httpClientHelper=new HttpClientHelper();
            HttpResponse httpResponse=httpClientHelper.doGet(url,"utf-8","utf-8",header,siteProxyIp);
            return httpResponse.getContent();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtil.errorTrace(loggerName,e.getMessage(),e);
            return content;
        }
    }
}
