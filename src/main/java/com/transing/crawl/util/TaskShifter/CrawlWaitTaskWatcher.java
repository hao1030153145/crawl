package com.transing.crawl.util.TaskShifter;

import com.jeeframework.core.context.support.SpringContextHolder;
import com.jeeframework.jeetask.startup.JeeTaskServer;
import com.jeeframework.jeetask.task.Task;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.job.SubTaskManager;
import com.transing.crawl.job.impl.CrawlSubTask;
import com.transing.crawl.util.WebUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.h2.mvstore.ConcurrentArrayList;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 包: com.transing.crawl.util.TaskShifter
 * 源文件:CrawlWaitTaskWatcher.java
 * 对缓冲任务队列的守护者
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年12月21日
 */

@Component("waitTaskWatcher")
public class CrawlWaitTaskWatcher
{
    private final static String LoggerName="waitTaskWatcher";

    private JeeTaskServer jeeTaskServer;

    private static CrawlWaitTaskWatcher crawlWaitTaskWatcher;

    public static List<Long> runTaskDatasourceId=new ArrayList<Long>();

    private CrawlWaitTaskWatcher()
    {
    }

    public CrawlWaitTaskWatcher get(){

        if(crawlWaitTaskWatcher==null){
            crawlWaitTaskWatcher=new CrawlWaitTaskWatcher();
        }
        return crawlWaitTaskWatcher;
    }


    /**
     * 初始化
     */
    private void init(){
        this.jeeTaskServer   = SpringContextHolder.getBean("jeeTaskServer");
    }



    /**
     * leader服务器守护
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void watcher(){
        if(crawlWaitTaskWatcher==null)
            get();
        if(jeeTaskServer==null)
            init();

        if(jeeTaskServer.isLeader()){
            //定时任务结束前将本次使用的数据源队列清空。
            this.runTaskDatasourceId.clear();

            SubTaskManager subTaskManager=SubTaskManager.newInstace(null);

            List<String> waitTask= subTaskManager.getAllWaitNodeTask();
            for (String node:waitTask){
                if (!Validate.isEmpty(node)){
                    try
                    {
                        Task task = subTaskManager.getWaitNodeTask(node);
                        if (task instanceof CrawlSubTask)
                        {
                            CrawlSubTask crawlSubTask = (CrawlSubTask) task;
                            long datasourceId=crawlSubTask.getDatasourceId();
                            LoggerUtil.debugTrace(LoggerName,"等待任务列表的数据源="+datasourceId);
                            Map connectionParam =null;
                            String connectionId="";
                            try
                            {
                                //在数据源队列中没有 该数据源，则在连接池获取一次连接
                                //如果有连接，则将该任务放置到zk可用任务队列中。
                                //如果在数据源队列中有该数据源，则则将该任务放置到zk可用任务队列中。
                                if(!runTaskDatasourceId.contains(datasourceId))
                                {
                                    connectionParam = WebUtil.doGetConnection(datasourceId,null);
                                    if (connectionParam != null &&connectionParam.containsKey(
                                                    WebUtil.CONNECTION_ID))
                                    {
                                        connectionId = connectionParam
                                                .get(WebUtil.CONNECTION_ID)
                                                .toString();
                                        runTaskDatasourceId.add(datasourceId);
                                        subTaskManager.changeTaskWaitQ2RunQ(task);
                                    }
                                }else{
                                    subTaskManager.changeTaskWaitQ2RunQ(task);
                                }
                            }catch (Exception e){
                            }finally
                            {
                                //如果有连接则最后返回连接
                                if (connectionParam!=null&&!Validate.isEmpty(connectionId))
                                    WebUtil.doRollBackConnection(datasourceId,
                                            null, connectionId,
                                            WebUtil.CONNECTION_NORMAL,"","");
                            }

                        }
                    }catch (Exception e){
                        e.getMessage();
                        LoggerUtil.errorTrace(LoggerName,"等待连接的任务检测： "+e.getMessage(),e);
                    }
                }
            }

        }
    }

    /**
     * 检测 清理 有连接的任务队列中已经执行完的任务
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void HasConnectionQWatcher(){
        if(crawlWaitTaskWatcher==null)
            get();
        if(jeeTaskServer==null)
            init();

        if(jeeTaskServer.isLeader()){
            SubTaskManager subTaskManager=SubTaskManager.newInstace(null);
            List<String> nodes=subTaskManager.getAllHasConnectionNodeTask();
            if(nodes!=null&&nodes.size()>0){
                for (String node:nodes){
                    if (!subTaskManager.isNotRunHasConnectionTask(node))
                    {
                        subTaskManager.removeHasConnectionNodeTask(node);
                        LoggerUtil.debugTrace(LoggerName,"任务队列中编号："+node+" 执行完成，移除待执行队列");
                    }
                }
            }
        }
    }

}
