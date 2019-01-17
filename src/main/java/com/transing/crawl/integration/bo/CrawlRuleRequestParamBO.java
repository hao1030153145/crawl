package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleRequestParamBO.java
 * 抓取规则的请求参数
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月26日
 */
public class CrawlRuleRequestParamBO
{
    private long id;

    private long crawlRuleId;

    private String paramName;

    private long paramType;

    private long inputType;

    private String paramValue;

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

    public String getParamName()
    {
        return paramName;
    }

    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }

    public long getParamType()
    {
        return paramType;
    }

    public void setParamType(long paramType)
    {
        this.paramType = paramType;
    }

    public long getInputType()
    {
        return inputType;
    }

    public void setInputType(long inputType)
    {
        this.inputType = inputType;
    }

    public String getParamValue()
    {
        return paramValue;
    }

    public void setParamValue(String paramValue)
    {
        this.paramValue = paramValue;
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
