package com.transing.crawl.integration;

import com.jeeframework.logicframework.integration.DataService;
import com.jeeframework.logicframework.integration.DataServiceException;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.integration.bo.CrawlTaskBO;
import com.transing.crawl.job.impl.CrawlSubTask;
import com.transing.crawl.web.filter.CrawlTaskFilter;

import java.util.List;
import java.util.Map;

public interface CrawlTaskDataService extends DataService
{

    List<CrawlTaskBO> getCrawlTaskListByFilter(CrawlTaskFilter crawlTaskFilter)
            throws DataServiceException;

    int getCrawlTaskCountByFilter(CrawlTaskFilter crawlTaskFilter)
            throws DataServiceException;

    List<Integer> getCrawlSubTaskListByTaskId(long taskId)
            throws DataServiceException;

    CrawlSubTaskBO getCrawlSubTaskListByTaskIdAndSubId(
            Map<String, Object> param)
            throws DataServiceException;

    CrawlTaskBO getCrawlTaskListById(Map<String, Object> param)
            throws DataServiceException;

    int saveCrawlTaskInfoBo(CrawlTaskBO crawlTaskBO)
            throws DataServiceException;

    void modifyCrawlTaskInfoBO(Map<String, Object> param)
            throws DataServiceException;

    int saveCrawlSubTaskInfo(CrawlSubTaskBO crawlSubTaskBO) throws
            DataServiceException;

    void modifyCrawlSubTaskInfo(Map<String, Object> param)
            throws DataServiceException;

    List<CrawlTaskBO> getCrawlTaskInfoByDetailId(String detailId)
            throws DataServiceException;

    List<CrawlSubTaskBO> getCrawlSubTaskBos(Map<String, Object> param);

    long getSubTaskCount(Map<String, Object> param);

    /**
     * 查询任务调度中的所有数据
     *
     * @return
     */
    List<CrawlSubTask> getCrawlSubTask(Map<String, Object> param);

    void delCrawlTaskInfo(long id) throws DataServiceException;

    void delCrawlSubTask(long taskId) throws DataServiceException;
}
