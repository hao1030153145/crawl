package com.transing.crawl.integration.impl.ibatis;

import com.jeeframework.logicframework.integration.DataServiceException;
import com.jeeframework.logicframework.integration.dao.DAOException;
import com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS;
import com.transing.crawl.integration.CrawlTaskDataService;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.integration.bo.CrawlTaskBO;
import com.transing.crawl.job.impl.CrawlSubTask;
import com.transing.crawl.web.filter.CrawlTaskFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Repository("crawlTaskDataService")
public class CrawlTaskDAPibatis extends BaseDaoiBATIS implements
        CrawlTaskDataService {
    @Override
    public List<CrawlTaskBO> getCrawlTaskListByFilter(CrawlTaskFilter crawlTaskFilter) throws DataServiceException {
        try {
            return sqlSessionTemplate.selectList("crawlTaskMapper.getCrawlTaskListByFilter", crawlTaskFilter);
        } catch (DataAccessException e) {
            throw new DAOException("查询列表失败", e);
        }
    }

    @Override
    public int getCrawlTaskCountByFilter(CrawlTaskFilter crawlTaskFilter) throws DataServiceException {
        try {
            Integer listCount=sqlSessionTemplate.selectOne("crawlTaskMapper.getCrawlTaskCountByFilter", crawlTaskFilter);
            return listCount;
        } catch (DataAccessException e) {
            throw new DAOException("查询列表数量", e);
        }
    }

    @Override
    public List<Integer> getCrawlSubTaskListByTaskId(long taskId) throws DataServiceException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskId", taskId);
        return sqlSessionTemplate.selectList("crawlTaskMapper.getCrawlSubTaskListByTaskId", param);
    }

    @Override
    public CrawlSubTaskBO getCrawlSubTaskListByTaskIdAndSubId(Map<String, Object> param) throws DataServiceException {
        return sqlSessionTemplate.selectOne("crawlTaskMapper.getCrawlSubTaskListByTaskIdAndSubId", param);
    }

    @Override
    public CrawlTaskBO getCrawlTaskListById(Map<String, Object> param) throws DataServiceException {
        return sqlSessionTemplate.selectOne("crawlTaskMapper.getCrawlTaskListById", param);
    }

    @Override
    public int saveCrawlTaskInfoBo(CrawlTaskBO crawlTaskBO)
            throws DataServiceException {
        return sqlSessionTemplate.insert("crawlTaskMapper.saveCrawlTaskInfoBo", crawlTaskBO);
    }

    @Override
    public void modifyCrawlTaskInfoBO(Map<String, Object> param)
            throws DataServiceException {
        sqlSessionTemplate.update("crawlTaskMapper.modifyCrawlTaskInfoBO", param);
    }

    @Override
    public int saveCrawlSubTaskInfo(CrawlSubTaskBO crawlSubTaskBO)
            throws DataServiceException {
        return sqlSessionTemplate.insert("crawlTaskMapper.saveCrawlSubTaskInfo", crawlSubTaskBO);
    }

    @Override
    public void modifyCrawlSubTaskInfo(Map<String, Object> param)
            throws DataServiceException {
        sqlSessionTemplate.update("crawlTaskMapper.modifyCrawlSubTaskInfo", param);
    }

    @Override
    public List<CrawlTaskBO> getCrawlTaskInfoByDetailId(String detailId) throws DataServiceException {
        List<String> detailIds= Arrays.asList(detailId.split(","));
        return sqlSessionTemplate.selectList("crawlTaskMapper.getCrawlTaskInfoByDetailId", detailIds);
    }

    @Override
    public List<CrawlSubTaskBO> getCrawlSubTaskBos(Map<String, Object> param)
    {
        return sqlSessionTemplate.selectList("crawlTaskMapper.getCrawlSubTaskBos",param);
    }

    @Override
    public long getSubTaskCount(Map<String, Object> param)
    {
        return sqlSessionTemplate.selectOne("crawlTaskMapper.getSubTaskCount",param);
    }

    @Override
    public List<CrawlSubTask> getCrawlSubTask(Map<String,Object> param)
    {
        return sqlSessionTemplate.selectList("crawlTaskMapper.getCrawlSubTaskJob",param);
    }

    @Override
    public void delCrawlTaskInfo(long id) throws DataServiceException
    {
        sqlSessionTemplate.delete("crawlTaskMapper.delCrawlTaskBo",id);
    }

    @Override
    public void delCrawlSubTask(long taskId) throws DataServiceException
    {
        sqlSessionTemplate.delete("crawlTaskMapper.delCrawlSubTaskBo",taskId);
    }
}
