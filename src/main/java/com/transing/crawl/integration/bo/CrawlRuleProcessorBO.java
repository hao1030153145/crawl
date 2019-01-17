package com.transing.crawl.integration.bo;


/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:ProcessorBO.java
 * 规则的处理器
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */
public class CrawlRuleProcessorBO
{

    private int id;

    private String processorName;

    private String processorPath;

    private int sortNo;



    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getProcessorName()
    {
        return processorName;
    }

    public void setProcessorName(String processorName)
    {
        this.processorName = processorName;
    }

    public String getProcessorPath()
    {
        return processorPath;
    }

    public void setProcessorPath(String processorPath)
    {
        this.processorPath = processorPath;
    }

    public int getSortNo()
    {
        return sortNo;
    }

    public void setSortNo(int sortNo)
    {
        this.sortNo = sortNo;
    }


}
