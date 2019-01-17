package com.transing.crawl.util.TaskShifter;

import com.jeeframework.core.context.support.SpringContextHolder;
import com.jeeframework.jeetask.startup.JeeTaskServer;
import com.transing.crawl.biz.service.CrawlTaskService;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.util.DateUtil;
import com.transing.crawl.web.filter.CrawlTaskFilter;

import java.util.*;

/**
 * 包: com.transing.crawl.util.TaskShifter
 * 源文件:TaskCleanerFactory.java
 * 任务表的数据查询，以及任务表的清理。
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年10月31日
 */
public class TaskCleanerFactory
{

    private static TaskCleanerFactory taskCleanerFactory;

    private CrawlTaskService crawlTaskService;

    private JeeTaskServer jeeTaskServer;

    public TaskCleanerFactory(){
        init();
    }

    public static TaskCleanerFactory getInstance(){
        if(taskCleanerFactory==null){
            taskCleanerFactory=new TaskCleanerFactory();
        }
        return taskCleanerFactory;
    }

    /**
     * 查询当前时间段一月前的数据。
     */
    public List<CrawlTaskBO> getNeedCleanerTasks(Date date,Integer startRow,Integer size){
        List<CrawlTaskBO> crawlTaskBOs=new ArrayList<CrawlTaskBO>();
        CrawlTaskFilter crawlTaskFilter=new CrawlTaskFilter();
        crawlTaskFilter.setStartRow(startRow);
        crawlTaskFilter.setSize(size);
        crawlTaskFilter.setCreateTimeBefore(DateUtil.formatDate(date));
        List<com.transing.crawl.integration.bo.CrawlTaskBO> crawlTaskBOList=crawlTaskService
                .getCrawlTaskListByFilter(crawlTaskFilter);
        if (crawlTaskBOList.size()>0){
            CrawlTaskBO crawlTaskBO= null;
            Map<String,Object> param=null;
            for (com.transing.crawl.integration.bo.CrawlTaskBO crawlTaskB:crawlTaskBOList){
                crawlTaskBO=new CrawlTaskBO();
                crawlTaskBO.setCrawlTaskBO(crawlTaskB);
                param=new HashMap<String, Object>();
                param.put("taskId",crawlTaskB.getId());
                List<CrawlSubTaskBO> crawlSubTaskBOs=crawlTaskService.getCrawlSubTaskBos(param);
                crawlTaskBO.setCrawlSubTaskBOs(crawlSubTaskBOs);
                crawlTaskBOs.add(crawlTaskBO);
            }
        }
        return  crawlTaskBOs;
    }


    public void removeSubmitedTasks(List<CrawlTaskBO> crawlTaskBOs){
        if(crawlTaskBOs!=null&&crawlTaskBOs.size()>0){
            for (CrawlTaskBO crawlTaskBO:crawlTaskBOs){
                com.transing.crawl.integration.bo.CrawlTaskBO taskBO=crawlTaskBO.getCrawlTaskBO();
                crawlTaskService.delCrawlSubTask(taskBO.getId());
                crawlTaskService.delCrawlTaskInfo(taskBO.getId());
            }
        }
    }

    public boolean currentServerIsLeader()
    {
        return jeeTaskServer.isLeader();
    }


    private void init(){
        this.crawlTaskService= SpringContextHolder.getBean("crawlTaskService");
        this.jeeTaskServer   = SpringContextHolder.getBean("jeeTaskServer");
    }


}
