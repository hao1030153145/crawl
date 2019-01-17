package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleDetailParseBO.java
 * 页面规则 列表的解析
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public class CrawlRuleDetailParseBO
{
    private long id;

    private long crawlRuleDetailId;

    private long parseType;

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

    public long getCrawlRuleDetailId()
    {
        return crawlRuleDetailId;
    }

    public void setCrawlRuleDetailId(long crawlRuleDetailId)
    {
        this.crawlRuleDetailId = crawlRuleDetailId;
    }

    public long getParseType()
    {
        return parseType;
    }

    public void setParseType(long parseType)
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
