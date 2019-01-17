package com.transing.crawl.integration.bo;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleDetailSuffProcBO.java
 * 页面规则 列表的后置处理器
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public class CrawlRuleDetailSuffProcBO extends CrawlRuleBaseProcBO
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
