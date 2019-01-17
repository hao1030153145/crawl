package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleRequestHeaderBO.java
 * 抓取规则的请求参数
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */
public class CrawlRuleRequestHeaderBO
{
    private long id;

    private long crawlRuleId;

    private String headerName;

    private int headerType;//参数类型（1为url，2为form）

    private int inputType;//输入类型（1为输入值，2为选择输入参数，3为内部变量）

    private String headerValue;

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

    public long getCrawlRuleId()
    {
        return crawlRuleId;
    }

    public void setCrawlRuleId(long crawlRuleId)
    {
        this.crawlRuleId = crawlRuleId;
    }

    public String getHeaderName()
    {
        return headerName;
    }

    public void setHeaderName(String headerName)
    {
        this.headerName = headerName;
    }

    public int getHeaderType()
    {
        return headerType;
    }

    public void setHeaderType(int headerType)
    {
        this.headerType = headerType;
    }

    public int getInputType()
    {
        return inputType;
    }

    public void setInputType(int inputType)
    {
        this.inputType = inputType;
    }

    public String getHeaderValue()
    {
        return headerValue;
    }

    public void setHeaderValue(String headerValue)
    {
        this.headerValue = headerValue;
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
