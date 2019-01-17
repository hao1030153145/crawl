package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleDetailFieldParseBO.java
 * 页面规则字段的解析
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public class CrawlRuleDetailFieldParseBO
{
    private long id;

    private long detailFieldId;

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

    public long getDetailFieldId()
    {
        return detailFieldId;
    }

    public void setDetailFieldId(long detailFieldId)
    {
        this.detailFieldId = detailFieldId;
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
