package com.transing.crawl.util.processor.impl.preProcessors;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.ProcessorUtil;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.util.processor.impl.preProcessors
 * 源文件:AqsiqProcessor.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年04月16日
 */
public class AqsiqProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        if(content.indexOf("javascript:self_open")>-1){
            try
            {
                List list = ParseUtil
                        .regexMatcher("javascript:self_open[(].{1}(.*).{1}[)];",
                                content);
                if (list.size() > 0)
                {
                    String url = list.get(0).toString();
                    SiteProxyIp siteProxyIp = null;
                    if (param.containsKey(ProcessorUtil.HOST))
                    {
                        String host = param.getString(ProcessorUtil.HOST);
                        Integer port = param.getInt(ProcessorUtil.PORT);
                        siteProxyIp = new SiteProxyIp(host, port);
                    }
                    Map<String, String> headMap = null;
                    if (param.containsKey(ProcessorUtil.HEADMAP))
                    {
                        headMap = (Map<String, String>) JSONObject
                                .toBean(param
                                                .getJSONObject(ProcessorUtil.HEADMAP),
                                        new HashMap<String, String>(),
                                        new JsonConfig());
                    }
                    HttpClientHelper httpClientHelper = new HttpClientHelper();
                   content = httpClientHelper
                            .doGet(url, "utf-8", "utf-8", headMap, siteProxyIp).getContent();
                    LoggerUtil.debugTrace(loggerName,"aqsiqProcessor  url="+url+" content:"+content);
                }
            }catch (Exception e){e.printStackTrace();}
        }
        return content;
    }
}
