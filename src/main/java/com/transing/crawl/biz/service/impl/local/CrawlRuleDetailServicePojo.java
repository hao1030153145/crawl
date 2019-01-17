package com.transing.crawl.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.transing.crawl.biz.service.CrawlRuleDetailService;
import com.transing.crawl.integration.CrawlRuleDetailDataService;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.web.po.CrawlRuleDetailFieldPo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service.impl.local
 * 源文件:CrawlRuleDetailServicePojo.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
@Service("crawlRuleDetailService")
public class CrawlRuleDetailServicePojo extends BaseService implements
        CrawlRuleDetailService
{

    @Resource
    private CrawlRuleDetailDataService crawlRuleDetailDataService;

    @Override
    public List<CrawlRuleDetailBO> getCrawlRuleDetailList(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDetailDataService.getCrawlRuleDetailList(param);
    }

    @Override
    public CrawlRuleDetailBO getCrawlRuleDetailBO(long id) throws BizException
    {
        return crawlRuleDetailDataService.getCrawlRuleDetailBO(id);
    }

    @Override
    public void modifyCrawlRuleDetailBO(CrawlRuleDetailBO crawlRuleDetailBO)
            throws BizException
    {
        crawlRuleDetailDataService.modifyCrawlRuleDetailBO(crawlRuleDetailBO);
    }

    @Override
    public int saveCrawlRuleDetailBo(CrawlRuleDetailBO crawlRuleDetailBO)
            throws BizException
    {
        return crawlRuleDetailDataService
                .saveCrawlRuleDetailBo(crawlRuleDetailBO);
    }


    @Override
    public List<CrawlRuleDetailParseBO> getCrawlRuleDetailParseBo(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDetailDataService.getCrawlRuleDetailParseBo(param);
    }

    @Override
    public int saveCrawlRuleDetailParseBo(
            CrawlRuleDetailParseBO crawlRuleDetailParseBO) throws BizException
    {
        return crawlRuleDetailDataService
                .saveCrawlRuleDetailParseBo(crawlRuleDetailParseBO);
    }

    @Override
    public void modifyCrawlRuleDetailParseBo(
            CrawlRuleDetailParseBO crawlRuleDetailParseBO) throws BizException
    {
        crawlRuleDetailDataService
                .modifyCrawlRuleDetailParseBo(crawlRuleDetailParseBO);
    }


    @Override
    public List<CrawlRuleDetailPreProcBO> getCrawlRuleDetailPreProcBO(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDetailDataService.getCrawlRuleDetailPreProcBO(param);
    }

    @Override
    public int saveCrawlRuleDetailPreProcBO(
            CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO)
            throws BizException
    {
        return crawlRuleDetailDataService
                .saveCrawlRuleDetailPreProcBO(crawlRuleDetailPreProcBO);
    }

    @Override
    public void modifyCrawlRuleDetailPreProcBO(
            CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO)
            throws BizException
    {
        crawlRuleDetailDataService
                .modifyCrawlRuleDetailPreProcBO(crawlRuleDetailPreProcBO);
    }



    @Override
    public List<CrawlRuleDetailSuffProcBO> getCrawlRuleDetailSuffProcBos(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDetailDataService.getCrawlRuleDetailSuffProcBos(param);
    }

    @Override
    public int saveCrawlRuleDetailSuffProcBO(
            CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO)
            throws BizException
    {
        return crawlRuleDetailDataService
                .saveCrawlRuleDetailSuffProcBO(crawlRuleDetailSuffProcBO);
    }

    @Override
    public void modifyCrawlRuleDetailSuffProcBO(
            CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO)
            throws BizException
    {
        crawlRuleDetailDataService
                .modifyCrawlRuleDetailSuffProcBO(crawlRuleDetailSuffProcBO);
    }



    @Override
    public List<CrawlRuleDetailFieldBO> getCrawlRuleDetailFieldBos(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDetailDataService.getCrawlRuleDetailFieldBos(param);
    }

    @Override
    public List<CrawlRuleDetailFieldPo> getCrawlRuleDetailFieldPos(
            Map<String, Object> param, Long datasourceTypeId)
            throws BizException
    {
        return crawlRuleDetailDataService
                .getCrawlRuleDetailFieldPos(param, datasourceTypeId);
    }

    @Override
    public int saveCrawlRuleDetailFieldBO(
            CrawlRuleDetailFieldBO crawlRuleDetailFieldBO) throws BizException
    {
        return crawlRuleDetailDataService
                .saveCrawlRuleDetailFieldBO(crawlRuleDetailFieldBO);
    }

    @Override
    public void modifyCrawlRuleDetailFieldBO(
            CrawlRuleDetailFieldBO crawlRuleDetailFieldBO) throws BizException
    {
        crawlRuleDetailDataService
                .modifyCrawlRuleDetailFieldBO(crawlRuleDetailFieldBO);
    }



    @Override
    public List<CrawlRuleDetailFieldParseBO> getCrawlRuleDetailFieldParseBOs(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDetailDataService
                .getCrawlRuleDetailFieldParseBOs(param);
    }

    @Override
    public int saveCrawlRuleDetailFieldParseBO(
            CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO)
            throws BizException
    {
        return crawlRuleDetailDataService
                .saveCrawlRuleDetailFieldParseBO(crawlRuleDetailFieldParseBO);
    }

    @Override
    public void modifyCrawlRuleDetailFieldParseBO(
            CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO)
            throws BizException
    {
        crawlRuleDetailDataService
                .modifyCrawlRuleDetailFieldParseBO(crawlRuleDetailFieldParseBO);
    }

    @Override
    public void delCrawlRuleDetailFieldParseBO(long detailFieldId)
            throws BizException
    {
        crawlRuleDetailDataService
                .delCrawlRuleDetailFieldParseBO(detailFieldId);
    }

    @Override
    public List<CrawlRuleDetailFieldSuffProcBO> getCrawlRuleDetailFieldSuffProcBO(
            Map<String, Object> param) throws BizException
    {
        return crawlRuleDetailDataService
                .getCrawlRuleDetailFieldSuffProcBO(param);
    }

    @Override
    public int saveCrawlRuleDetailFieldSuffProcBO(
            CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO)
            throws BizException
    {
        return crawlRuleDetailDataService.saveCrawlRuleDetailFieldSuffProcBO(
                crawlRuleDetailFieldSuffProcBO);
    }

    @Override
    public void modifyCrawlRuleDetailFieldSuffProcBO(
            CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO)
            throws BizException
    {
        crawlRuleDetailDataService.modifyCrawlRuleDetailFieldSuffProcBO(
                crawlRuleDetailFieldSuffProcBO);
    }

    @Override
    public void delCrawlRuleDetailFieldSuffProcBO(Long detailFieldId)
            throws BizException
    {
        crawlRuleDetailDataService
                .delCrawlRuleDetailFieldSuffProcBO(detailFieldId);
    }

    @Override
    public void delCrawlRuleDetailParseBoByDetailId(Long crawlRuleDetailId)
    {
        crawlRuleDetailDataService
                .delCrawlRuleDetailParseBoByDetailId(crawlRuleDetailId);
    }

    @Override
    public void delCrawlRuleDetailPreProcBOByDetailId(Long crawlRuleDetailId)
    {
        crawlRuleDetailDataService
                .delCrawlRuleDetailPreProcBOByDetailId(crawlRuleDetailId);
    }

    @Override
    public void delCrawlRuleDetailSuffProcBOByDetailId(Long crawlRuleDetailId)
    {
        crawlRuleDetailDataService
                .delCrawlRuleDetailSuffProcBOByDetailId(crawlRuleDetailId);
    }

    @Override
    public void delCrawlRuleDetailFieldBOByDetailId(Long crawlRuleDetailId)
    {
        crawlRuleDetailDataService
                .delCrawlRuleDetailFieldBOByDetailId(crawlRuleDetailId);
    }

    @Override
    public void delCrawlRUleDetailByRuleId(Long ruleId)
    {
        crawlRuleDetailDataService.delCrawlRUleDetailByRuleId(ruleId);
    }
}
