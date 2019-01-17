package com.transing.crawl.integration.bo;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleRequestParamSufferProcBO.java
 * 请求参数后置处理器
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月26日
 */
public class CrawlRuleRequestParamSufferProcBO extends CrawlRuleBaseProcBO
{
    private long requestParamId;

    public long getRequestParamId()
    {
        return requestParamId;
    }

    public void setRequestParamId(long requestParamId)
    {
        this.requestParamId = requestParamId;
    }
}
