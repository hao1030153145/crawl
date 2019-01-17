package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleBO.java
 * 抓取规则BO
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */
public class CrawlRuleBO
{
    private static final long serialVersionUID = 1L;

    private long id;

    private long datasourceId;

    private long datasourceTypeId;

    private String ruleName;

    private String requestMethod;

    private String requestEncoding;

    private String responseEncoding;

    private String crawlUrl;

    private String testUrl;

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

    public long getDatasourceId()
    {
        return datasourceId;
    }

    public void setDatasourceId(long datasourceId)
    {
        this.datasourceId = datasourceId;
    }

    public long getDatasourceTypeId()
    {
        return datasourceTypeId;
    }

    public void setDatasourceTypeId(long datasourceTypeId)
    {
        this.datasourceTypeId = datasourceTypeId;
    }

    public String getRuleName()
    {
        return ruleName;
    }

    public void setRuleName(String ruleName)
    {
        this.ruleName = ruleName;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public String getRequestEncoding()
    {
        return requestEncoding;
    }

    public void setRequestEncoding(String requestEncoding)
    {
        this.requestEncoding = requestEncoding;
    }

    public String getResponseEncoding()
    {
        return responseEncoding;
    }

    public void setResponseEncoding(String responseEncoding)
    {
        this.responseEncoding = responseEncoding;
    }

    public String getCrawlUrl()
    {
        return crawlUrl;
    }

    public void setCrawlUrl(String crawlUrl)
    {
        this.crawlUrl = crawlUrl;
    }

    public String getTestUrl()
    {
        return testUrl;
    }

    public void setTestUrl(String testUrl)
    {
        this.testUrl = testUrl;
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
