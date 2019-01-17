package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleBaseProcBO.java
 * 规则处理器基础类
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */
public class CrawlRuleBaseProcBO
{
    private long id;

    private int processorId;

    private String processorValue;

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

    public int getProcessorId()
    {
        return processorId;
    }

    public void setProcessorId(int processorId)
    {
        this.processorId = processorId;
    }

    public String getProcessorValue()
    {
        return processorValue;
    }

    public void setProcessorValue(String processorValue)
    {
        this.processorValue = processorValue;
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
