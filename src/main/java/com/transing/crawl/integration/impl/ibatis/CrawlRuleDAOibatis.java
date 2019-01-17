package com.transing.crawl.integration.impl.ibatis;

import com.jeeframework.logicframework.integration.DataServiceException;
import com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS;
import com.transing.crawl.integration.CrawlRuleDataService;
import com.transing.crawl.integration.bo.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.integration.impl.ibatis
 * 源文件:CrawlRuleDAOibatis.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */
@Scope("prototype")
@Repository("crawlRuleDataService")
public class CrawlRuleDAOibatis extends BaseDaoiBATIS implements
        CrawlRuleDataService
{
    @Override
    public int initCrawlRule(CrawlRuleBO crawlRuleBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleMapper.initCrawlRule", crawlRuleBO);
    }

    @Override
    public void modifyCrawlRule(CrawlRuleBO crawlRuleBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleMapper.modifyCrawlRule", crawlRuleBO);
    }

    @Override
    public CrawlRuleBO getCrawlRuleBOByDatasourceTypeId(long datasourceTypeId)
            throws DataServiceException
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("datasourceTypeId", datasourceTypeId);
        List<CrawlRuleBO> crawlRuleBOList = getCrawlRuleBoList(param);
        if (crawlRuleBOList != null && crawlRuleBOList.size() > 0)
        {
            return crawlRuleBOList.get(0);
        }
        return null;
    }

    @Override
    public CrawlRuleBO getCrawlRuleBOById(long ruleId)
            throws DataServiceException
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ruleId", ruleId);
        List<CrawlRuleBO> crawlRuleBOList = getCrawlRuleBoList(param);
        if (crawlRuleBOList != null && crawlRuleBOList.size() > 0)
        {
            return crawlRuleBOList.get(0);
        }
        return null;
    }

    @Override
    public List<CrawlRuleBO> getCrawlRuleBoListByDatasourceId(long datasourceId,
            Integer page, Integer size)
            throws DataServiceException
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("datasourceId", datasourceId);
        if (page != null)
        {
            param.put("page", page);
        }
        if (size != null)
        {
            param.put("size", size);
        }
        return getCrawlRuleBoList(param);
    }

    @Override
    public List<CrawlRuleBO> getCrawlRuleBoList(Map<String, Object> param)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleMapper.getCrawlRuleBoList", param);
    }

    @Override
    public int getCrawlRuleCount(Map<String, Object> param)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .selectOne("crawlRuleMapper.getCrawlRuleCount", param);
    }

    @Override
    public List<CrawlRuleProcessorBO> getProcessorList()
            throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleMapper.getProcessorList");
    }

    @Override
    public CrawlRuleProcessorBO getProcessor(int id) throws DataServiceException
    {
        return sqlSessionTemplate.selectOne("crawlRuleMapper.getProcessor", id);
    }

    @Override
    public List<CrawlRuleAnalysisTypeBO> getCrawlRuleAnalysisTypList()
            throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleMapper.getCrawlRuleAnalysisTypList");
    }

    @Override
    public CrawlRuleAnalysisTypeBO getCrawlRuleAnalysisTypeBO(int id)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .selectOne("crawlRuleMapper.getCrawlRuleAnalysisTypeBO", id);
    }

    @Override
    public List<CrawlRuleRequestHeaderBO> getCrawlRuleRequestHeaederList(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleMapper.getCrawlRuleRequestHeaederList",
                        param);
    }

    @Override
    public int saveCrawlRuleRequestHeaderBO(
            CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleMapper.saveCrawlRuleRequestHeaderBO",
                        crawlRuleRequestHeaderBO);
    }

    @Override
    public void modifyCrawlRuleRequestHeaderBO(
            CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleMapper.modifyCrawlRuleRequestHeaderBO",
                        crawlRuleRequestHeaderBO);
    }


    @Override
    public void delCrawlRuleRequestHeaderBO(Long ruleId)
            throws DataServiceException
    {
        sqlSessionTemplate
                .delete("crawlRuleMapper.delCrawlRuleRequestHeaderBO", ruleId);
    }

    @Override
    public List<CrawlRuleRequestHeaderSufferProcBO> getCrawlRuleRequestHeaderSufferProcBO(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate.selectList(
                "crawlRuleMapper.getCrawlRuleRequestHeaderSufferProcBO", param);
    }

    @Override
    public int saveCrawlRuleRequestHeaderSufferProcBO(
            CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleMapper.saveCrawlRuleRequestHeaderSufferProcBO",
                        crawlRuleRequestHeaderSufferProcBO);
    }

    @Override
    public void modifyCrawlRuleRequestHeaderSufferProcBO(
            CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleMapper.modifyCrawlRuleRequestHeaderSufferProcBO",
                        crawlRuleRequestHeaderSufferProcBO);
    }

    @Override
    public void delCrawlRuleRequestHeaderSufferProcBO(Long headerId)
            throws DataServiceException
    {
        sqlSessionTemplate
                .delete("crawlRuleMapper.delCrawlRuleRequestHeaderSufferProcBO",
                        headerId);
    }

    @Override
    public int saveCrawlRuleRequestParam(
            CrawlRuleRequestParamBO crawlRuleRequestParamBO)
            throws DataServiceException
    {
        return sqlSessionTemplate.insert("crawlRuleMapper.saveCrawlRuleRequestParam",crawlRuleRequestParamBO);
    }

    @Override
    public void modifyCrawlRuleRequestParam(
            CrawlRuleRequestParamBO crawlRuleRequestParamBO)
            throws DataServiceException
    {
        sqlSessionTemplate.update("crawlRuleMapper.modifyCrawlRuleRequestParam",
                crawlRuleRequestParamBO);
    }


    @Override
    public void delCrawlRuleRequestParam(Long ruleId) throws DataServiceException
    {
        sqlSessionTemplate
                .delete("crawlRuleMapper.delCrawlRuleRequestParam", ruleId);
    }

    @Override
    public List<CrawlRuleRequestParamBO> getCrawlRuleRequestParamList(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleMapper.getCrawlRuleRequestParamList",
                        param);
    }

    @Override
    public int saveCrawlRuleRequestParamSuffProc(
            CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleMapper.saveCrawlRuleRequestParamSuffProc",
                        crawlRuleRequestParamSufferProcBO);
    }

    @Override
    public void modifyCrawlRuleRequestParamSuffProc(
            CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleMapper.modifyCrawlRuleRequestParamSuffProc",
                        crawlRuleRequestParamSufferProcBO);
    }


    @Override
    public void delCrawlRuleRequestParamSuffProc(Long paramId)
            throws DataServiceException
    {
        sqlSessionTemplate
                .delete("crawlRuleMapper.delCrawlRuleRequestParamSuffProc",
                        paramId);
    }

    @Override
    public List<CrawlRuleRequestParamSufferProcBO> getCrawlRuleRequestParamSuffProcList(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate.selectList(
                "crawlRuleMapper.getCrawlRuleRequestParamSuffProcList", param);
    }

    @Override
    public CrawlRulePageBO getCrawlRulePageBO(long crawlRuleId)
            throws DataServiceException
    {
        return sqlSessionTemplate.selectOne("crawlRuleMapper.getCrawlRulePageBO",crawlRuleId);
    }

    @Override
    public void modifyCrawlRulePageBO(CrawlRulePageBO crawlRulePageBO)
            throws DataServiceException
    {
        sqlSessionTemplate.update("crawlRuleMapper.modifyCrawlRulePageBO",crawlRulePageBO);
    }

    @Override
    public void delCrawlRulePageBO(long crawlRuleId) throws DataServiceException
    {
        sqlSessionTemplate.delete("crawlRuleMapper.delCrawlRulePageBO",crawlRuleId);
    }

    @Override
    public int saveCrawlRulePageBO(CrawlRulePageBO crawlRulePageBO)
            throws DataServiceException
    {
        return sqlSessionTemplate.insert("crawlRuleMapper.saveCrawlRulePageBO",crawlRulePageBO);
    }

    @Override
    public List<CrawlRulePageParseBO> getCrawlRulePageParseBO(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate.selectList("crawlRuleMapper.getCrawlRulePageParseBO",param);
    }

    @Override
    public int saveCrawlRulePageParaseBO(
            CrawlRulePageParseBO crawlRulePageParseBO)
            throws DataServiceException
    {
        return sqlSessionTemplate.insert("crawlRuleMapper.saveCrawlRulePageParaseBO",crawlRulePageParseBO);
    }

    @Override
    public void modifyCrawlRulePageParseBO(
            CrawlRulePageParseBO crawlRulePageParseBO)
            throws DataServiceException
    {
        sqlSessionTemplate.update("crawlRuleMapper.modifyCrawlRulePageParseBO",crawlRulePageParseBO);
    }


    @Override
    public void delCrawlRulePageParseBOs(Long pageId) throws DataServiceException
    {
        sqlSessionTemplate.delete("crawlRuleMapper.delCrawlRulePageParseBOs",pageId);
    }

    @Override
    public List<CrawlRulePageSuffProcBO> getCrawlRulePageSuffProcBO(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate.selectList("crawlRuleMapper.getCrawlRulePageSuffProcBO",param);
    }

    @Override
    public int saveCrawlRulePageSuffProcBO(
            CrawlRulePageSuffProcBO crawlRulePageSuffProcBO)
            throws DataServiceException
    {
        return sqlSessionTemplate.insert("crawlRuleMapper.saveCrawlRulePageSuffProcBO",crawlRulePageSuffProcBO);
    }

    @Override
    public void modifyCrawlRulePageSuffProcBO(
            CrawlRulePageSuffProcBO crawlRulePageSuffProcBO)
            throws DataServiceException
    {
        sqlSessionTemplate.update("crawlRuleMapper.modifyCrawlRulePageSuffProcBO",crawlRulePageSuffProcBO);
    }


    @Override
    public void delCrawlRulePageSuffProcBO(Long pageId) throws DataServiceException
    {
        sqlSessionTemplate.delete("crawlRuleMapper.delCrawlRulePageSuffProcBO",pageId);
    }

    @Override
    public boolean getCrawlRuleRequestParamByParamValue(String paramId) throws DataServiceException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("paramValue", paramId);
        List<CrawlRuleRequestParamBO> crawlRuleRequestParamBOList=sqlSessionTemplate.selectList("crawlRuleMapper.delCrawlInputParam",paramId);
        if(crawlRuleRequestParamBOList.size()>0){
            return true;
        }else {
            return false;
        }
    }
}
