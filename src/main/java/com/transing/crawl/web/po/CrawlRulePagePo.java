package com.transing.crawl.web.po;

import com.transing.crawl.integration.bo.CrawlRulePageBO;
import com.transing.crawl.integration.bo.CrawlRulePageParseBO;
import com.transing.crawl.integration.bo.CrawlRulePageSuffProcBO;

import java.util.List;

/**
 * 包: com.transing.crawl.web.po
 * 源文件:CrawlRulePagePo.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月03日
 */
public class CrawlRulePagePo
{
    private long id;
    private CrawlRulePageBO crawlRulePageBO;
    private List<CrawlRulePageParseBO> crawlRulePageParseBO;
    private List<CrawlRulePageSuffProcBO> crawlRulePageSuffProcBOs;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public CrawlRulePageBO getCrawlRulePageBO()
    {
        return crawlRulePageBO;
    }

    public void setCrawlRulePageBO(
            CrawlRulePageBO crawlRulePageBO)
    {
        this.crawlRulePageBO = crawlRulePageBO;
    }

    public List<CrawlRulePageParseBO> getCrawlRulePageParseBO()
    {
        return crawlRulePageParseBO;
    }

    public void setCrawlRulePageParseBO(
            List<CrawlRulePageParseBO> crawlRulePageParseBO)
    {
        this.crawlRulePageParseBO = crawlRulePageParseBO;
    }

    public List<CrawlRulePageSuffProcBO> getCrawlRulePageSuffProcBOs()
    {
        return crawlRulePageSuffProcBOs;
    }

    public void setCrawlRulePageSuffProcBOs(
            List<CrawlRulePageSuffProcBO> crawlRulePageSuffProcBOs)
    {
        this.crawlRulePageSuffProcBOs = crawlRulePageSuffProcBOs;
    }
}
