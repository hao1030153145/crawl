package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRulePageParseBO.java
 * 页码解析
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月26日
 */
public class CrawlRulePageParseBO
{
    private long id;

    private long crawlRulePageId;

    private int parseType;

    private String parseExpression;

    private Date createTime;

    private Date lastmodifyTime;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getCrawlRulePageId()
    {
        return crawlRulePageId;
    }

    public void setCrawlRulePageId(long crawlRulePageId)
    {
        this.crawlRulePageId = crawlRulePageId;
    }

    public int getParseType()
    {
        return parseType;
    }

    public void setParseType(int parseType)
    {
        this.parseType = parseType;
    }

    public String getParseExpression()
    {
        return parseExpression;
    }

    public void setParseExpression(String parseExpression)
    {
        this.parseExpression = parseExpression;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getLastmodifyTime()
    {
        return lastmodifyTime;
    }

    public void setLastmodifyTime(Date lastmodifyTime)
    {
        this.lastmodifyTime = lastmodifyTime;
    }
}
