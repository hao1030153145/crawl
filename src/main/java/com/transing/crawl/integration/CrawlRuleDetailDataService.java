package com.transing.crawl.integration;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.integration.DataService;
import com.jeeframework.logicframework.integration.DataServiceException;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.web.po.CrawlRuleDetailFieldPo;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.integration
 * 源文件:CrawlRuleDetailDataService.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public interface CrawlRuleDetailDataService extends DataService
{
    List<CrawlRuleDetailBO> getCrawlRuleDetailList(Map<String, Object> param)
            throws DataServiceException;

    CrawlRuleDetailBO getCrawlRuleDetailBO(long id) throws DataServiceException;

    void modifyCrawlRuleDetailBO(CrawlRuleDetailBO crawlRuleDetailBO) throws
            DataServiceException;

    int saveCrawlRuleDetailBo(CrawlRuleDetailBO crawlRuleDetailBO)
            throws DataServiceException;


    List<CrawlRuleDetailParseBO> getCrawlRuleDetailParseBo(
            Map<String, Object> param) throws DataServiceException;

    int saveCrawlRuleDetailParseBo(
            CrawlRuleDetailParseBO crawlRuleDetailParseBO)
            throws DataServiceException;

    void modifyCrawlRuleDetailParseBo(
            CrawlRuleDetailParseBO crawlRuleDetailParseBO)
            throws DataServiceException;


    List<CrawlRuleDetailPreProcBO> getCrawlRuleDetailPreProcBO(
            Map<String, Object> param) throws DataServiceException;

    int saveCrawlRuleDetailPreProcBO(
            CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO)
            throws DataServiceException;

    void modifyCrawlRuleDetailPreProcBO(
            CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO)
            throws DataServiceException;


    List<CrawlRuleDetailSuffProcBO> getCrawlRuleDetailSuffProcBos(
            Map<String, Object> param) throws DataServiceException;

    int saveCrawlRuleDetailSuffProcBO(
            CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO)
            throws DataServiceException;

    void modifyCrawlRuleDetailSuffProcBO(
            CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO)
            throws DataServiceException;


    List<CrawlRuleDetailFieldBO> getCrawlRuleDetailFieldBos(
            Map<String, Object> param) throws DataServiceException;

    int saveCrawlRuleDetailFieldBO(
            CrawlRuleDetailFieldBO crawlRuleDetailFieldBO)
            throws DataServiceException;

    void modifyCrawlRuleDetailFieldBO(
            CrawlRuleDetailFieldBO crawlRuleDetailFieldBO)
            throws DataServiceException;


    List<CrawlRuleDetailFieldParseBO> getCrawlRuleDetailFieldParseBOs(
            Map<String, Object> param) throws DataServiceException;

    int saveCrawlRuleDetailFieldParseBO(
            CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO)
            throws DataServiceException;

    void modifyCrawlRuleDetailFieldParseBO(
            CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO)
            throws DataServiceException;

    List<CrawlRuleDetailFieldSuffProcBO> getCrawlRuleDetailFieldSuffProcBO(
            Map<String, Object> param) throws DataServiceException;

    int saveCrawlRuleDetailFieldSuffProcBO(
            CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO)
            throws DataServiceException;

    void modifyCrawlRuleDetailFieldSuffProcBO(
            CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO)
            throws DataServiceException;

    List<CrawlRuleDetailFieldPo> getCrawlRuleDetailFieldPos(
            Map<String, Object> param, Long datasourceTypeId)
            throws BizException;

    void delCrawlRuleDetailFieldSuffProcBO(Long detailFieldId)
            throws BizException;

    void delCrawlRuleDetailParseBoByDetailId(Long crawlRuleDetailId);

    void delCrawlRuleDetailPreProcBOByDetailId(Long crawlRuleDetailId);

    void delCrawlRuleDetailSuffProcBOByDetailId(Long crawlRuleDetailId);

    void delCrawlRuleDetailFieldBOByDetailId(Long crawlRuleDetailId);

    void delCrawlRuleDetailFieldParseBO(long detailFieldId) throws BizException;

    void delCrawlRUleDetailByRuleId(Long ruleId);
}
