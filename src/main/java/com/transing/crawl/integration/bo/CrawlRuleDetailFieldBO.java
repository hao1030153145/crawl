package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRuleDetailFieldBO.java
 * 页面规则的字段
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public class CrawlRuleDetailFieldBO
{
    private long id;
    private long crawlRuleDetailId;
    private long ruleId;
    private long storageTypeFieldId;
    private long isNull;
    private Date createTime;
    private Date lastmodifyTime;
    private long isUnique;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getIsNull()
    {
        return isNull;
    }

    public void setIsNull(long isNull)
    {
        this.isNull = isNull;
    }

    public long getCrawlRuleDetailId()
    {
        return crawlRuleDetailId;
    }

    public void setCrawlRuleDetailId(long crawlRuleDetailId)
    {
        this.crawlRuleDetailId = crawlRuleDetailId;
    }

    public long getRuleId()
    {
        return ruleId;
    }

    public void setRuleId(long ruleId)
    {
        this.ruleId = ruleId;
    }

    public long getStorageTypeFieldId()
    {
        return storageTypeFieldId;
    }

    public void setStorageTypeFieldId(long storageTypeFieldId)
    {
        this.storageTypeFieldId = storageTypeFieldId;
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

    public long getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(long isUnique) {
        this.isUnique = isUnique;
    }
}
