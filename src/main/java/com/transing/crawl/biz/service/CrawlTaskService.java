package com.transing.crawl.biz.service;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BizService;
import com.transing.crawl.integration.bo.BossUser;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.integration.bo.CrawlTaskBO;
import com.transing.crawl.job.impl.CrawlSubTask;
import com.transing.crawl.web.filter.CrawlTaskFilter;
import com.transing.crawl.web.filter.GetBossUsersFilter;

import java.util.List;
import java.util.Map;

public interface CrawlTaskService extends BizService
{
    /**
     * 简单描述：根据CrawlTaskFilter返回用户对象列表
     * <p>
     *
     * @param crawlTaskFilter
     * @
     */
    List<CrawlTaskBO> getCrawlTaskListByFilter(CrawlTaskFilter crawlTaskFilter);

    int getCrawlTaskCountByFilter(CrawlTaskFilter crawlTaskFilter);

    /**
     * 简单描述：根据taskid返回抓取子任务
     * <p>
     *
     * @param taskId
     * @
     */
    List<Integer> getCrawlSubTaskListByTaskId(long taskId);

    int saveCrawlTaskInfoBo(CrawlTaskBO crawlTaskBO) throws BizException;

    void modifyCrawlTaskInfoBO(Map<String, Object> param) throws BizException;

    /**
     * 简单描述：根据taskid和subId返回抓取子任务
     * <p>
     *
     * @param param
     * @
     */
    CrawlSubTaskBO getCrawlSubTaskListByTaskIdAndSubId(
            Map<String, Object> param);

    /**
     * 简单描述：根据taskid和subId返回抓取子任务
     * <p>
     *
     * @param param
     * @
     */
    CrawlTaskBO getCrawlTaskListById(Map<String, Object> param);

    int saveCrawlSubTaskInfo(CrawlSubTaskBO crawlSubTaskBO) throws BizException;

    void modifyCrawlSubTaskInfo(Map<String, Object> param) throws BizException;

    List<CrawlTaskBO> getCrawlTaskInfoByDetailId(String detailId)
            throws BizException;

    List<CrawlSubTaskBO> getCrawlSubTaskBos(Map<String, Object> param);

    long getSubTaskCount(Map<String, Object> param);

    /**
     * 查询任务调度中的所有数据
     *
     * @return
     */
    List<CrawlSubTask> getCrawlSubTask(Map<String, Object> param);

    void delCrawlTaskInfo(long id) throws BizException;

    void delCrawlSubTask(long taskId) throws BizException;
}
