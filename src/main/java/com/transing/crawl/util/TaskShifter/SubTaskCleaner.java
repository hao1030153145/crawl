package com.transing.crawl.util.TaskShifter;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.transing.crawl.util.DateUtil;
import com.transing.crawl.util.WebUtil;
import net.sf.json.JSONArray;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 包: com.transing.crawl.util.TaskShifter
 * 源文件:SubTaskCleaner.java
 * 对任务表的数据定时转移
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年10月30日
 */
@Component("subTask")
public class SubTaskCleaner
{

    private final static String BEFORE_TIME="1个月";
    private final static String TASK_INDEX_NAME="historytask";
    private final static String SUB_TASK_URI="/addDataInSearcher.json";
    private final static String SUB_TASK_ALIAS_URI="/addAliasForIndex.json";
    private final static String LOGGER_NAME="historyTask";


    @Scheduled(cron = "0 30 16 * * ?")
    public void cleanerServerIsLeader(){
        TaskCleanerFactory taskCleanerFactory = TaskCleanerFactory
                .getInstance();
        LoggerUtil.debugTrace("开始清理任务的数据...");
        if(taskCleanerFactory.currentServerIsLeader())
        {

            Date selBeforTime = DateUtil.transformDate(BEFORE_TIME);
            Integer startRows=0;
            Integer size=1;

            List<CrawlTaskBO> crawlTaskBOs=taskCleanerFactory.getNeedCleanerTasks(selBeforTime,startRows,size);
            int pageCount=0;
            do
            {
                try
                {
                    if (crawlTaskBOs.size() == 0)
                        return;

                    List<CrawlTaskBO> currentTasklist = new ArrayList<CrawlTaskBO>();
                    for (int i = 0; i < crawlTaskBOs.size(); i++)
                    {
                        currentTasklist.add(crawlTaskBOs.get(i));
                    }
                    if (currentTasklist.size() > 0)
                    {
                        submitSubTask(currentTasklist);
                        taskCleanerFactory.removeSubmitedTasks(currentTasklist);
                    }
                    /**
                     * 请求修改索引的别名
                     */
                    Map<String, String> postJSON = new HashMap<String, String>();
                    postJSON.put("dataType", TASK_INDEX_NAME);
                    postJSON.put("alias", WebUtil.getTaskIndexAlias());
                    WebUtil.callRemoteService(
                            WebUtil.getCorpusServerByEnv() + SUB_TASK_ALIAS_URI,
                            "POST", postJSON);
                    pageCount++;
                    startRows += size * pageCount;
                    Thread.sleep(500);
                }catch (Exception e){
                }
                crawlTaskBOs = taskCleanerFactory
                        .getNeedCleanerTasks(selBeforTime, startRows, size);
            }while (crawlTaskBOs.size()>0);

        }
    }

    private void submitSubTask(List<CrawlTaskBO> currentTasklist){
        JSONArray subTaskArray = JSONArray.fromObject(currentTasklist);

        Map<String, String> postJSON = new HashMap<String, String>();
        postJSON.put("dataType", TASK_INDEX_NAME);
        postJSON.put("dataJSON", subTaskArray.toString());
        WebUtil.callRemoteService(WebUtil.getCorpusServerByEnv() +
                        SUB_TASK_URI,
                "POST", postJSON);
    }



}
