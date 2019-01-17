package com.transing.crawl.integration.bo;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleRequestHeaderSufferProcBO.java
 * 规则请求参数的后置处理器
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */
public class CrawlRuleRequestHeaderSufferProcBO extends CrawlRuleBaseProcBO
{
    private long requestHeaderId;

    public long getRequestHeaderId()
    {
        return requestHeaderId;
    }

    public void setRequestHeaderId(long requestHeaderId)
    {
        this.requestHeaderId = requestHeaderId;
    }
}
