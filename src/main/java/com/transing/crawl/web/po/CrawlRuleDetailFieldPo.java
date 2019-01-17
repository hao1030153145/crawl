package com.transing.crawl.web.po;

import com.transing.crawl.integration.bo.CrawlRuleDetailFieldParseBO;
import com.transing.crawl.integration.bo.CrawlRuleDetailFieldSuffProcBO;
import com.transing.crawl.integration.bo.StorageTypeFieldBO;

import java.util.List;

/**
 * 包: com.transing.crawl.web.po
 * 源文件:CrawlRuleDetailFieldPo.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月03日
 */
public class CrawlRuleDetailFieldPo
{
    private long id;
    private StorageTypeFieldBO storageTypeFieldBO;
    private List<CrawlRuleDetailFieldParseBO> fieldParseBOs;
    private List<CrawlRuleDetailFieldSuffProcBO> fieldSuffProcBOs;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public StorageTypeFieldBO getStorageTypeFieldBO()
    {
        return storageTypeFieldBO;
    }

    public void setStorageTypeFieldBO(
            StorageTypeFieldBO storageTypeFieldBO)
    {
        this.storageTypeFieldBO = storageTypeFieldBO;
    }

    public List<CrawlRuleDetailFieldParseBO> getFieldParseBOs()
    {
        return fieldParseBOs;
    }

    public void setFieldParseBOs(
            List<CrawlRuleDetailFieldParseBO> fieldParseBOs)
    {
        this.fieldParseBOs = fieldParseBOs;
    }

    public List<CrawlRuleDetailFieldSuffProcBO> getFieldSuffProcBOs()
    {
        return fieldSuffProcBOs;
    }

    public void setFieldSuffProcBOs(
            List<CrawlRuleDetailFieldSuffProcBO> fieldSuffProcBOs)
    {
        this.fieldSuffProcBOs = fieldSuffProcBOs;
    }
}
