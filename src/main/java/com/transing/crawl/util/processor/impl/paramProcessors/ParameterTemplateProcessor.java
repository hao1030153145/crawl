package com.transing.crawl.util.processor.impl.paramProcessors;

import com.transing.crawl.util.processor.BaseProcessor;
import com.wordnik.swagger.annotations.Api;
import net.sf.json.JSONObject;

/**
 * 包: com.transing.crawl.util.processor.impl
 * 源文件:ParameterTemplateProcessor.java
 * 参数模板输入
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年08月29日
 */
@Api(value = "模板参数配置",description = "替换输入参数为指定样式,如替换成?&scope=ori&suball=1")
public class ParameterTemplateProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        return processorValue;
    }
}
