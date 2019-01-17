package com.transing.crawl.web.controller;

import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.ProcessorUtil;
import com.transing.crawl.util.processor.impl.preProcessors.VkuProcessor;
import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:VkuProcessorTest.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年04月11日
 */
public class VkuProcessorTest
{

    @Test
    public void VkuCommentTest(){
        String url= "http://vku.youku.com/live/newplay?id=15469&sharekey=fc75dc5d1b8a45ffd3740f2246f8a6616&from=groupmessage&isappinstalled=0";
        BaseProcessor baseProcessor=new VkuProcessor();
        JSONObject obj=JSONObject.fromObject("{}");
        obj.put(ProcessorUtil.URL,url);
        baseProcessor.runProcessor(url,obj,"");
    }
}
