package com.transing.crawl.integration.bo;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleDetailPreProcBO.java
 * 页面规则的前置处理器
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public class CrawlRuleDetailPreProcBO extends CrawlRuleBaseProcBO
{
    private long crawlRuleDetailId;

    public long getCrawlRuleDetailId()
    {
        return crawlRuleDetailId;
    }

    public void setCrawlRuleDetailId(long crawlRuleDetailId)
    {
        this.crawlRuleDetailId = crawlRuleDetailId;
    }
}
