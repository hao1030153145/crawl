package com.transing.crawl.util.processor.impl.paramProcessors;

import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.processor.BaseProcessor;
import com.wordnik.swagger.annotations.Api;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 包: com.transing.crawl.util.processor.impl.paramProcessors
 * 源文件:UnicodeProcessor.java
 * url转码
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年01月04日
 */

@Api(value = "关键词转码",description = "processorValue==>utf-8(utf-8一转三，gbk一转二)")
public class UnicodeProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        if(Validate.isEmpty(processorValue))
            processorValue="utf-8";

        try
        {
           return URLEncoder.encode(content,processorValue);
        }
        catch (UnsupportedEncodingException e)
        {
            return content;
        }
    }

    public static void main(String[] args)
    {
        UnicodeProcessor unicodeProcessor=new UnicodeProcessor();
        System.out.println(unicodeProcessor.runProcessor("大众 奥德",new JSONObject(),""));
        System.out.println(unicodeProcessor.runProcessor("大众",new JSONObject(),"GBK"));
    }
}
