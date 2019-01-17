package com.transing.crawl.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.transing.crawl.biz.service.CrawlTaskService;
import com.transing.crawl.integration.CrawlRuleDataService;
import com.transing.crawl.integration.CrawlTaskDataService;
import com.transing.crawl.integration.bo.BossUser;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.integration.bo.CrawlTaskBO;
import com.transing.crawl.job.impl.CrawlSubTask;
import com.transing.crawl.web.filter.CrawlTaskFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("crawlTaskService" )
public class CrawlTaskServicePojo extends BaseService implements
        CrawlTaskService {

    @Resource
    private CrawlTaskDataService crawlTaskDataService;
    @Override
    public List<CrawlTaskBO> getCrawlTaskListByFilter(CrawlTaskFilter crawlTaskFilter) {
        return crawlTaskDataService.getCrawlTaskListByFilter(crawlTaskFilter);
    }

    @Override
    public int getCrawlTaskCountByFilter(CrawlTaskFilter crawlTaskFilter) {
        return crawlTaskDataService.getCrawlTaskCountByFilter(crawlTaskFilter);

    }

    @Override
    public List<Integer> getCrawlSubTaskListByTaskId(long taskId) {
        return crawlTaskDataService.getCrawlSubTaskListByTaskId(taskId);
    }

    @Override
    public int saveCrawlTaskInfoBo(CrawlTaskBO crawlTaskBO) throws BizException
    {
        return crawlTaskDataService.saveCrawlTaskInfoBo(crawlTaskBO);
    }

    @Override
    public void modifyCrawlTaskInfoBO(Map<String,Object> param)
            throws BizException
    {
        Map<String, Object> paramBo = new HashMap<String, Object>();
        paramBo.put("taskId", param.get("id"));
        CrawlTaskBO crawlTaskBO =getCrawlTaskListById(paramBo);
        if (crawlTaskBO.getTaskStatus()==4){
            if(param.containsKey("taskStatus"))
                param.remove("taskStatus");
        }
        crawlTaskDataService.modifyCrawlTaskInfoBO(param);
    }

    @Override
    public CrawlSubTaskBO getCrawlSubTaskListByTaskIdAndSubId(Map<String, Object> param) {
        return crawlTaskDataService.getCrawlSubTaskListByTaskIdAndSubId(param);
    }

    @Override
    public CrawlTaskBO getCrawlTaskListById(Map<String, Object> param) {
        return crawlTaskDataService.getCrawlTaskListById(param);
    }

    @Override
    public int saveCrawlSubTaskInfo(CrawlSubTaskBO crawlSubTaskBO)
            throws BizException
    {
        return crawlTaskDataService.saveCrawlSubTaskInfo(crawlSubTaskBO);
    }

    @Override
    public void modifyCrawlSubTaskInfo(Map<String, Object> param)
            throws BizException
    {
        crawlTaskDataService.modifyCrawlSubTaskInfo(param);
    }

    @Override
    public List<CrawlTaskBO> getCrawlTaskInfoByDetailId(String detailId) throws BizException {
        return crawlTaskDataService.getCrawlTaskInfoByDetailId(detailId);
    }

    @Override
    public List<CrawlSubTaskBO> getCrawlSubTaskBos(Map<String, Object> param)
    {
        return crawlTaskDataService.getCrawlSubTaskBos(param);
    }

    @Override
    public long getSubTaskCount(Map<String, Object> param)
    {
        return crawlTaskDataService.getSubTaskCount(param);
    }

    @Override
    public List<CrawlSubTask> getCrawlSubTask(Map<String,Object> param)
    {
        param.put("tableName","t_task");
        return crawlTaskDataService.getCrawlSubTask(param);
    }

    @Override
    public void delCrawlTaskInfo(long id) throws BizException
    {
        crawlTaskDataService.delCrawlTaskInfo(id);
    }

    @Override
    public void delCrawlSubTask(long taskId) throws BizException
    {
        crawlTaskDataService.delCrawlSubTask(taskId);
    }
}
