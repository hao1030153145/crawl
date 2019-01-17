package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:CrawlRulePageBO.java
 * 规则页码
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月26日
 */
public class CrawlRulePageBO
{
    private long id;

    private long crawlRuleId;

    private String firstUrl;

    private int calculation;//计算方式（1，批量替换，2总页码，3总条数，4下一页）

    private int formatType;//格式(1为数字，2为日期，3为字母)

    private int startPage;

    private int endPage;

    private int stepLength;

    private String fomat;

    private String endPageIdentifier;//结束标识符

    private int endPageSimilarity;//相识度

    private int pageSize;

    private int maxPage;

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

    public String getFirstUrl()
    {
        return firstUrl;
    }

    public void setFirstUrl(String firstUrl)
    {
        this.firstUrl = firstUrl;
    }

    public int getCalculation()
    {
        return calculation;
    }

    public void setCalculation(int calculation)
    {
        this.calculation = calculation;
    }

    public int getFormatType()
    {
        return formatType;
    }

    public void setFormatType(int formatType)
    {
        this.formatType = formatType;
    }

    public int getStartPage()
    {
        return startPage;
    }

    public void setStartPage(int startPage)
    {
        this.startPage = startPage;
    }

    public int getEndPage()
    {
        return endPage;
    }

    public void setEndPage(int endPage)
    {
        this.endPage = endPage;
    }

    public int getStepLength()
    {
        return stepLength;
    }

    public void setStepLength(int stepLength)
    {
        this.stepLength = stepLength;
    }

    public String getFomat()
    {
        return fomat;
    }

    public void setFomat(String fomat)
    {
        this.fomat = fomat;
    }

    public String getEndPageIdentifier()
    {
        return endPageIdentifier;
    }

    public void setEndPageIdentifier(String endPageIdentifier)
    {
        this.endPageIdentifier = endPageIdentifier;
    }

    public int getEndPageSimilarity()
    {
        return endPageSimilarity;
    }

    public void setEndPageSimilarity(int endPageSimilarity)
    {
        this.endPageSimilarity = endPageSimilarity;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getMaxPage()
    {
        return maxPage;
    }

    public void setMaxPage(int maxPage)
    {
        this.maxPage = maxPage;
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
