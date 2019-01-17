package com.transing.crawl.util.TaskShifter;

import com.transing.crawl.integration.bo.CrawlSubTaskBO;

import java.util.List;

/**
 * 包: com.transing.crawl.util.TaskShifter
 * 源文件:CrawlTaskBO.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年10月31日
 */
public class CrawlTaskBO
{
    private com.transing.crawl.integration.bo.CrawlTaskBO crawlTaskBO;
    private List<CrawlSubTaskBO> crawlSubTaskBOs;

    public com.transing.crawl.integration.bo.CrawlTaskBO getCrawlTaskBO()
    {
        return crawlTaskBO;
    }

    public void setCrawlTaskBO(
            com.transing.crawl.integration.bo.CrawlTaskBO crawlTaskBO)
    {
        this.crawlTaskBO = crawlTaskBO;
    }

    public List<CrawlSubTaskBO> getCrawlSubTaskBOs()
    {
        return crawlSubTaskBOs;
    }

    public void setCrawlSubTaskBOs(
            List<CrawlSubTaskBO> crawlSubTaskBOs)
    {
        this.crawlSubTaskBOs = crawlSubTaskBOs;
    }
}
