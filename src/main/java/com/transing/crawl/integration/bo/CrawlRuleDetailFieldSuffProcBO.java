package com.transing.crawl.integration.bo;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleDetailFieldSuffProcBO.java
 * 页面规则字段的后置处理器
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public class CrawlRuleDetailFieldSuffProcBO extends CrawlRuleBaseProcBO
{
    private long detailFieldId;

    public long getDetailFieldId()
    {
        return detailFieldId;
    }

    public void setDetailFieldId(long detailFieldId)
    {
        this.detailFieldId = detailFieldId;
    }
}
