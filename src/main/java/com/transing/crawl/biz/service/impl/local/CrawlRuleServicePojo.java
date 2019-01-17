package com.transing.crawl.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.transing.crawl.biz.service.CrawlRuleService;
import com.transing.crawl.integration.CrawlRuleDataService;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.web.po.CrawlRulePagePo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service.impl.local
 * 源文件:CrawlRuleServicePojo.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */

@Service("crawlRuleService")
public class CrawlRuleServicePojo extends BaseService implements
        CrawlRuleService
{
    @Resource
    private CrawlRuleDataService crawlRuleDataService;

    @Override
    public int initCrawlRule(CrawlRuleBO crawlRuleBO) throws BizException
    {
        return crawlRuleDataService.initCrawlRule(crawlRuleBO);
    }

    @Override
    public void modifyCrawlRule(CrawlRuleBO crawlRuleBO) throws BizException
    {
        crawlRuleDataService.modifyCrawlRule(crawlRuleBO);
    }

    @Override
    public CrawlRuleBO getCrawlRuleBOByDatasourceTypeId(long datasourceTypeId)
            throws BizException
    {
        return crawlRuleDataService.getCrawlRuleBOByDatasourceTypeId(datasourceTypeId);
    }

    @Override
    public CrawlRuleBO getCrawlRuleBOById(long ruleId) throws BizException
    {
        return crawlRuleDataService.getCrawlRuleBOById(ruleId);
    }

    @Override
    public List<CrawlRuleBO> getCrawlRuleBoListByDatasourceId(long datasourceId,Integer page,Integer size)
            throws BizException
    {
        return crawlRuleDataService.getCrawlRuleBoListByDatasourceId(datasourceId,page,size);
    }

    @Override
    public List<CrawlRuleBO> getCrawlRuleBoList(Map<String, Object> param)
            throws BizException
    {
        return crawlRuleDataService.getCrawlRuleBoList(param);
    }

    @Override
    public int getCrawlRuleCount(Map<String, Object> param) throws BizException
    {
        return crawlRuleDataService.getCrawlRuleCount(param);
    }

    @Override
    public List<CrawlRuleProcessorBO> getProcessorList() throws BizException
    {
        return crawlRuleDataService.getProcessorList();
    }

    @Override
    public CrawlRuleProcessorBO getProcessor(int id) throws BizException
    {
        return crawlRuleDataService.getProcessor(id);
    }

    @Override
    public List<CrawlRuleAnalysisTypeBO> getCrawlRuleAnalysisTypList()
            throws BizException
    {
        return crawlRuleDataService.getCrawlRuleAnalysisTypList();
    }

    @Override
    public CrawlRuleAnalysisTypeBO getCrawlRuleAnalysisTypeBO(int id)
            throws BizException
    {
        return crawlRuleDataService.getCrawlRuleAnalysisTypeBO(id);
    }

    @Override
    public List<CrawlRuleRequestHeaderBO> getCrawlRuleRequestHeaederList(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDataService.getCrawlRuleRequestHeaederList(param);
    }

    @Override
    public int saveCrawlRuleRequestHeaderBO(
            CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO)
            throws BizException
    {
        return crawlRuleDataService.saveCrawlRuleRequestHeaderBO(crawlRuleRequestHeaderBO);
    }

    @Override
    public void modifyCrawlRuleRequestHeaderBO(
            CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO)
            throws BizException
    {
        crawlRuleDataService.modifyCrawlRuleRequestHeaderBO(crawlRuleRequestHeaderBO);
    }

    @Override
    public void delCrawlRuleRequestHeaderBO(Long ruleId) throws BizException
    {
        crawlRuleDataService.delCrawlRuleRequestHeaderBO(ruleId);
    }

    @Override
    public List<CrawlRuleRequestHeaderSufferProcBO> getCrawlRuleRequestHeaderSufferProcBO(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDataService.getCrawlRuleRequestHeaderSufferProcBO(param);
    }

    @Override
    public int saveCrawlRuleRequestHeaderSufferProcBO(
            CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO)
            throws BizException
    {
        return crawlRuleDataService.saveCrawlRuleRequestHeaderSufferProcBO(crawlRuleRequestHeaderSufferProcBO);
    }

    @Override
    public void modifyCrawlRuleRequestHeaderSufferProcBO(
            CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO)
            throws BizException
    {
        crawlRuleDataService.modifyCrawlRuleRequestHeaderSufferProcBO(crawlRuleRequestHeaderSufferProcBO);
    }

    @Override
    public void delCrawlRuleRequestHeaderSufferProcBO(Long headerId)
            throws BizException
    {
        crawlRuleDataService.delCrawlRuleRequestHeaderSufferProcBO(headerId);
    }

    @Override
    public int saveCrawlRuleRequestParam(
            CrawlRuleRequestParamBO crawlRuleRequestParamBO) throws BizException
    {
        return crawlRuleDataService.saveCrawlRuleRequestParam(crawlRuleRequestParamBO);
    }

    @Override
    public void modifyCrawlRuleRequestParam(
            CrawlRuleRequestParamBO crawlRuleRequestParamBO) throws BizException
    {
        crawlRuleDataService.modifyCrawlRuleRequestParam(crawlRuleRequestParamBO);
    }

    @Override
    public void delCrawlRuleRequestParam(Long ruleId) throws BizException
        {
        crawlRuleDataService.delCrawlRuleRequestParam(ruleId);
    }

    @Override
    public List<CrawlRuleRequestParamBO> getCrawlRuleRequestParamList(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDataService.getCrawlRuleRequestParamList(param);
    }

    @Override
    public int saveCrawlRuleRequestParamSuffProc(
            CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO)
            throws BizException
    {
        return crawlRuleDataService.saveCrawlRuleRequestParamSuffProc(crawlRuleRequestParamSufferProcBO);
    }

    @Override
    public void modifyCrawlRuleRequestParamSuffProc(
            CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO)
            throws BizException
    {
        crawlRuleDataService.modifyCrawlRuleRequestParamSuffProc(crawlRuleRequestParamSufferProcBO);
    }

    @Override
    public void delCrawlRuleRequestParamSuffProc(Long paramId) throws BizException
    {
        crawlRuleDataService.delCrawlRuleRequestParamSuffProc(paramId);
    }

    @Override
    public List<CrawlRuleRequestParamSufferProcBO> getCrawlRuleRequestParamSuffProcList(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDataService.getCrawlRuleRequestParamSuffProcList(param);
    }

    @Override
    public CrawlRulePageBO getCrawlRulePageBO(long crawlRuleId)
            throws BizException
    {
        return crawlRuleDataService.getCrawlRulePageBO(crawlRuleId);
    }

    @Override
    public void modifyCrawlRulePageBO(CrawlRulePageBO crawlRulePageBO)
            throws BizException
    {
        crawlRuleDataService.modifyCrawlRulePageBO(crawlRulePageBO);
    }

    @Override
    public void delCrawlRulePageBO(long crawlRuleId) throws BizException
    {
        crawlRuleDataService.delCrawlRulePageBO(crawlRuleId);
    }

    @Override
    public int saveCrawlRulePageBO(CrawlRulePageBO crawlRulePageBO)
            throws BizException
    {
        return crawlRuleDataService.saveCrawlRulePageBO(crawlRulePageBO);
    }

    @Override
    public List<CrawlRulePageParseBO> getCrawlRulePageParseBO(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDataService.getCrawlRulePageParseBO(param);
    }

    @Override
    public int saveCrawlRulePageParaseBO(
            CrawlRulePageParseBO crawlRulePageParseBO) throws BizException
    {
        return crawlRuleDataService.saveCrawlRulePageParaseBO(crawlRulePageParseBO);
    }

    @Override
    public void modifyCrawlRulePageParseBO(
            CrawlRulePageParseBO crawlRulePageParseBO) throws BizException
    {
        crawlRuleDataService.modifyCrawlRulePageParseBO(crawlRulePageParseBO);
    }


    @Override
    public void delCrawlRulePageParseBOs(Long pageId) throws BizException
    {
        crawlRuleDataService.delCrawlRulePageParseBOs(pageId);
    }

    @Override
    public List<CrawlRulePageSuffProcBO> getCrawlRulePageSuffProcBO(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDataService.getCrawlRulePageSuffProcBO(param);
    }

    @Override
    public int saveCrawlRulePageSuffProcBO(
            CrawlRulePageSuffProcBO crawlRulePageSuffProcBO) throws BizException
    {
        return crawlRuleDataService.saveCrawlRulePageSuffProcBO(crawlRulePageSuffProcBO);
    }

    @Override
    public void modifyCrawlRulePageSuffProcBO(
            CrawlRulePageSuffProcBO crawlRulePageSuffProcBO) throws BizException
    {
        crawlRuleDataService.modifyCrawlRulePageSuffProcBO(crawlRulePageSuffProcBO);
    }

    @Override
    public void delCrawlRulePageSuffProcBO(Long pageId) throws BizException
    {
        crawlRuleDataService.delCrawlRulePageSuffProcBO(pageId);
    }

    @Override
    public boolean getCrawlRuleRequestParamByParamValue(String paramId) throws BizException {
        return crawlRuleDataService.getCrawlRuleRequestParamByParamValue(paramId);
    }

    @Override
    public CrawlRulePagePo getCrawlRulePagePO(long crawlRuleId)
            throws BizException
    {
        CrawlRulePagePo crawlRulePagePo=new CrawlRulePagePo();
        CrawlRulePageBO crawlRulePageBO=getCrawlRulePageBO(crawlRuleId);
        crawlRulePagePo.setId(crawlRulePageBO.getId());
        crawlRulePagePo.setCrawlRulePageBO(crawlRulePageBO);
        Map<String,Object> param=new HashMap<String, Object>();
        param.put("crawlRulePageId",crawlRulePageBO.getId());
        List<CrawlRulePageParseBO> crawlRulePageParseBOList=getCrawlRulePageParseBO(param);
        crawlRulePagePo.setCrawlRulePageParseBO(crawlRulePageParseBOList);
        List<CrawlRulePageSuffProcBO> crawlRulePageSuffProcBOList=getCrawlRulePageSuffProcBO(param);
        crawlRulePagePo.setCrawlRulePageSuffProcBOs(crawlRulePageSuffProcBOList);
        return crawlRulePagePo;
    }
}
