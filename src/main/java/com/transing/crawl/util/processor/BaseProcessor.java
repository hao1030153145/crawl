package com.transing.crawl.util.processor;

import net.sf.json.JSONObject;

/**
 * 包: com.transing.crawl.util.processor
 * 源文件:BaseProcessor.java
 * 处理器的父类
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月03日
 */
public abstract class BaseProcessor
{
    protected static String loggerName = "processor";




    /**
     * 处理器的运行块
     * @param content 需要处理的内容
     * @param param 传入的参数
     * @return
     */
    public abstract String runProcessor(String content,
            JSONObject param,String processorValue);

}
