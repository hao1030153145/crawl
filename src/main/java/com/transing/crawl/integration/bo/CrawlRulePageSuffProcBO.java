package com.transing.crawl.integration.bo;


/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRulePageSuffProcBO.java
 * 页码解析的后置处理器
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月26日
 */
public class CrawlRulePageSuffProcBO extends CrawlRuleBaseProcBO
{
    private long crawlRulePageId;

    public long getCrawlRulePageId()
    {
        return crawlRulePageId;
    }

    public void setCrawlRulePageId(long crawlRulePageId)
    {
        this.crawlRulePageId = crawlRulePageId;
    }
}
