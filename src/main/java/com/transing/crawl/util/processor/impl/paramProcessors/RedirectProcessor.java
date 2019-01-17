package com.transing.crawl.util.processor.impl.paramProcessors;

import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.processor.BaseProcessor;
import com.wordnik.swagger.annotations.Api;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 包: com.transing.crawl.util.processor.impl
 * 源文件:RedirectProcessor.java
 * 列表中通过对抓取到的url进行重定向后再解析真实的url
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年10月24日
 */
@Api(value = "重定向",description = "content=>url;processorValue=>GBK;return=> htmlContent")
public class RedirectProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        String encode="utf-8";
        if(!Validate.isEmpty(processorValue)){
            encode=processorValue;
        }
        if(content.startsWith("http")||content.startsWith("https")){
            HttpClientHelper httpClientHelper=new HttpClientHelper();
            Map<String,String> headMap=new HashMap<String, String>();
            try
            {
                byte[] contentBy = httpClientHelper
                        .doGetAndRetBytes(content, encode, encode, headMap,
                                null).getContentBytes();
                return new String(contentBy,encode);
            }catch (Exception e){
                return content;
            }
        }
        return content;
    }
}
