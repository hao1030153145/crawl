package com.transing.crawl.job.server;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.jeeframework.jeetask.event.JobEventBus;
import com.jeeframework.jeetask.zookeeper.election.LeaderService;
import com.jeeframework.jeetask.zookeeper.server.ServerService;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.transing.crawl.job.SubTaskManager;
import com.transing.crawl.util.TaskShifter.CrawlWaitTaskWatcher;
import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 包: com.transing.crawl.job.server
 * 源文件:CrawlTaskService.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年12月21日
 */
public class CrawlTaskService extends LeaderService
{

    private static String loggerName="crawlJeeTaskService";
    private SubTaskManager subTaskManager;


    public CrawlTaskService(
            CoordinatorRegistryCenter regCenter,
            JobEventBus jobEventBus,
            BeanFactory context,
            ServerService serverService,
            String roles)
    {
        super(regCenter, jobEventBus, context, serverService, roles);
        //subTaskManager=new SubTaskManager();
        subTaskManager=SubTaskManager.newInstace(regCenter);
    }

    @Override
    protected List<String> getTaskList()
    {


        List<String> tasks=new ArrayList<>();
        List<String> task=taskService.getTaskIds();
        List<String> hasConnectionTask=subTaskManager.getAllHasConnectionNodeTask();
        List<String> waitConnectionTask=subTaskManager.getAllWaitNodeTask();
        LoggerUtil.infoTrace(loggerName,"==任务分配开始，待分配有 "+(task.size()+waitConnectionTask.size())+"  条任务");
        if(hasConnectionTask!=null&&hasConnectionTask.size()>0)
            addTaskToList(tasks,hasConnectionTask);
        addTaskToList(tasks,task);
        return tasks;
    }


    /**
     * 有序输入任务
     * @param tasks
     * @param fromTasks
     */
    private void addTaskToList(List<String> tasks,List<String> fromTasks){
        int i=tasks.size();
        for (String string:fromTasks){
            tasks.add(i,string);
            i++;
        }
    }









}
