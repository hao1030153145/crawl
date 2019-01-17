package com.transing.crawl.job;

import com.jeeframework.jeetask.task.Job;
import com.jeeframework.jeetask.task.context.JobContext;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.transing.crawl.crawlParse.CrawlThread;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.job.impl.CrawlSubTask;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 包: com.transing.crawl.job
 * 源文件:SubTaskJob.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年09月28日
 */
public class SubTaskJob implements Job
{
    private static String log="subTaskReader";

    @Override
    public void doJob(JobContext jobContext)
    {
            CrawlSubTask crawlSubTask = (CrawlSubTask) jobContext.getTask();
            LoggerUtil.debugTrace(log,
                    "获取任务==>" + crawlSubTask.getId());

            String param = crawlSubTask.getParam();
            JSONObject jsonObject = JSONObject.fromObject(param);
            CrawlSubTaskBO crawlSubTaskBO = (CrawlSubTaskBO) JSONObject
                    .toBean(jsonObject, new CrawlSubTaskBO(), new JsonConfig());

            LoggerUtil.debugTrace(log,
                    "执行任务==>" + crawlSubTaskBO.getCrawlUrl());
            Long projectId = crawlSubTask.getProjectId();
            Long detailId = crawlSubTask.getFlowdetailId();
            Long taskId = crawlSubTask.getTaskId();
        LoggerUtil.debugTrace(log,
                "执行任务==>" + JSONObject.fromObject(crawlSubTaskBO));

            boolean isSuccess=new CrawlThread(crawlSubTaskBO, taskId, detailId + "",
                    projectId + "",crawlSubTask.getId()).doJob();
            if (!isSuccess)
                SubTaskManager.newInstace(null).submitWaitTask(crawlSubTask);
            LoggerUtil.debugTrace(log,
                    "任务 ==》" + crawlSubTask.getId() + " 任务执行状态："+isSuccess);
    }
}
