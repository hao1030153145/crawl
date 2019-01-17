package com.transing.crawl.job.impl;

import com.jeeframework.jeetask.task.Task;

/**
 * 包: com.transing.crawl.job.impl
 * 源文件:crawlSubTaskJob.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年09月28日
 */
public class CrawlSubTask extends Task
{
    private long taskId;

    private long flowdetailId;

    private long projectId;

    private long datasourceId;

    public long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(long taskId)
    {
        this.taskId = taskId;
    }

    public long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(long projectId)
    {
        this.projectId = projectId;
    }

    public long getFlowdetailId()
    {
        return flowdetailId;
    }

    public void setFlowdetailId(long flowdetailId)
    {
        this.flowdetailId = flowdetailId;
    }

    public long getDatasourceId()
    {
        return datasourceId;
    }

    public void setDatasourceId(long datasourceId)
    {
        this.datasourceId = datasourceId;
    }
}
