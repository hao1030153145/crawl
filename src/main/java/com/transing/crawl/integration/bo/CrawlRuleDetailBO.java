package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleDetailBO.java
 * 页面规则
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public class CrawlRuleDetailBO
{
    private long id;

    private long ruleId;

    private String detailName;

    private int pageType;//页面类型（1为列表，2为内容）

    private int status;//通过状态（0不通过，1通过）

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

    public long getRuleId()
    {
        return ruleId;
    }

    public void setRuleId(long ruleId)
    {
        this.ruleId = ruleId;
    }

    public String getDetailName()
    {
        return detailName;
    }

    public void setDetailName(String detailName)
    {
        this.detailName = detailName;
    }

    public int getPageType()
    {
        return pageType;
    }

    public void setPageType(int pageType)
    {
        this.pageType = pageType;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
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
