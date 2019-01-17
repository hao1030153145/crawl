package com.transing.crawl.integration;

import com.jeeframework.logicframework.integration.DataService;
import com.jeeframework.logicframework.integration.DataServiceException;
import com.transing.crawl.integration.bo.*;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.integration
 * 源文件:CrawlRuleDataService.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */
public interface CrawlRuleDataService extends DataService
{
    /**
     * 添加默认的规则
     *
     * @return
     * @throws DataServiceException
     */
    int initCrawlRule(CrawlRuleBO crawlRuleBO) throws DataServiceException;

    /**
     * 编辑
     *
     * @param crawlRuleBO
     * @throws DataServiceException
     */
    void modifyCrawlRule(CrawlRuleBO crawlRuleBO) throws DataServiceException;

    /**
     * 根据数据源类型查询
     *
     * @param datasourceTypeId
     * @return
     * @throws DataServiceException
     */
    CrawlRuleBO getCrawlRuleBOByDatasourceTypeId(long datasourceTypeId)
            throws DataServiceException;

    /**
     * 根据id查询
     *
     * @param ruleId
     * @return
     * @throws DataServiceException
     */
    CrawlRuleBO getCrawlRuleBOById(long ruleId) throws DataServiceException;

    /**
     * 根据数据源查询集合
     *
     * @param datasourceId
     * @return
     * @throws DataServiceException
     */
    List<CrawlRuleBO> getCrawlRuleBoListByDatasourceId(long datasourceId,
            Integer page, Integer size) throws DataServiceException;

    /**
     * 查询集合
     *
     * @param param
     * @return
     * @throws DataServiceException
     */
    List<CrawlRuleBO> getCrawlRuleBoList(Map<String, Object> param)
            throws DataServiceException;

    /**
     * 查询总数
     *
     * @param param
     * @return
     * @throws DataServiceException
     */
    int getCrawlRuleCount(Map<String, Object> param)
            throws DataServiceException;

    /**
     * 获取规则的处理器
     *
     * @return
     * @throws DataServiceException
     */
    List<CrawlRuleProcessorBO> getProcessorList() throws DataServiceException;

    /**
     * 获取指定处理器
     *
     * @param id
     * @return
     * @throws DataServiceException
     */
    CrawlRuleProcessorBO getProcessor(int id) throws DataServiceException;

    /**
     * 查询规则的解析方式
     *
     * @return
     * @throws DataServiceException
     */
    List<CrawlRuleAnalysisTypeBO> getCrawlRuleAnalysisTypList()
            throws DataServiceException;

    /**
     * 获取指定的解析方式
     *
     * @param id
     * @return
     * @throws DataServiceException
     */
    CrawlRuleAnalysisTypeBO getCrawlRuleAnalysisTypeBO(int id)
            throws DataServiceException;

    /**
     * 根据规则id查询请求参数
     *
     * @param param
     * @return
     * @throws DataServiceException
     */
    List<CrawlRuleRequestHeaderBO> getCrawlRuleRequestHeaederList(
            Map<String, Object> param) throws DataServiceException;

    /**
     * 添加规则的请求头部参数
     *
     * @param crawlRuleRequestHeaderBO
     * @return
     * @throws DataServiceException
     */
    int saveCrawlRuleRequestHeaderBO(
            CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO)
            throws DataServiceException;

    /**
     * 编辑规则的请求参数
     *
     * @param crawlRuleRequestHeaderBO
     * @throws DataServiceException
     */
    void modifyCrawlRuleRequestHeaderBO(
            CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO)
            throws DataServiceException;

    /**
     * 删除请求头部参数
     *
     * @param ruleId
     * @throws DataServiceException
     */
    void delCrawlRuleRequestHeaderBO(Long ruleId) throws DataServiceException;

    /**
     * 请求头部参数的后置处理器
     *
     * @param param
     * @return
     * @throws DataServiceException
     */
    List<CrawlRuleRequestHeaderSufferProcBO> getCrawlRuleRequestHeaderSufferProcBO(
            Map<String, Object> param) throws DataServiceException;

    /**
     * 保存头部参数的后置处理器
     *
     * @param crawlRuleRequestHeaderSufferProcBO
     * @return
     * @throws DataServiceException
     */
    int saveCrawlRuleRequestHeaderSufferProcBO(
            CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO)
            throws DataServiceException;

    /**
     * 编辑头部参数的后置处理器
     *
     * @param crawlRuleRequestHeaderSufferProcBO
     * @throws DataServiceException
     */
    void modifyCrawlRuleRequestHeaderSufferProcBO(
            CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO)
            throws DataServiceException;

    /**
     * 删除请求头部参数后置处理器
     *
     * @param headerId
     * @throws DataServiceException
     */
    void delCrawlRuleRequestHeaderSufferProcBO(Long headerId)
            throws DataServiceException;

    /**
     * 保存请求参数
     *
     * @param crawlRuleRequestParamBO
     * @return
     * @throws DataServiceException
     */
    int saveCrawlRuleRequestParam(
            CrawlRuleRequestParamBO crawlRuleRequestParamBO)
            throws DataServiceException;

    /**
     * 编辑请求参数
     *
     * @param crawlRuleRequestParamBO
     * @throws DataServiceException
     */
    void modifyCrawlRuleRequestParam(
            CrawlRuleRequestParamBO crawlRuleRequestParamBO)
            throws DataServiceException;

    /**
     * 删除请求参数
     *
     * @param ruleId
     * @throws DataServiceException
     */
    void delCrawlRuleRequestParam(Long ruleId) throws DataServiceException;

    /**
     * 查询请求参数
     *
     * @param param
     * @return
     * @throws DataServiceException
     */
    List<CrawlRuleRequestParamBO> getCrawlRuleRequestParamList(
            Map<String, Object> param) throws DataServiceException;

    /**
     * 保存请求参数后置处理器
     *
     * @param crawlRuleRequestParamSufferProcBO
     * @return
     * @throws DataServiceException
     */
    int saveCrawlRuleRequestParamSuffProc(
            CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO)
            throws DataServiceException;

    /**
     * 编辑请求参数后置处理器
     *
     * @param crawlRuleRequestParamSufferProcBO
     * @throws DataServiceException
     */
    void modifyCrawlRuleRequestParamSuffProc(
            CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO)
            throws DataServiceException;

    /**
     * 删除请求参数后置处理器
     *
     * @param paramId
     * @throws DataServiceException
     */
    void delCrawlRuleRequestParamSuffProc(Long paramId)
            throws DataServiceException;

    /**
     * 查询请求参数后置处理器
     *
     * @param param
     * @return
     * @throws DataServiceException
     */
    List<CrawlRuleRequestParamSufferProcBO> getCrawlRuleRequestParamSuffProcList(
            Map<String, Object> param) throws DataServiceException;

    /**
     * 查询规则页码
     *
     * @param crawlRuleId
     * @return
     * @throws DataServiceException
     */
    CrawlRulePageBO getCrawlRulePageBO(long crawlRuleId)
            throws DataServiceException;

    /**
     * 编辑规则页码
     *
     * @param crawlRulePageBO
     * @throws DataServiceException
     */
    void modifyCrawlRulePageBO(CrawlRulePageBO crawlRulePageBO)
            throws DataServiceException;

    /**
     * 删除规则的页码
     *
     * @param crawlRuleId
     * @return
     * @throws DataServiceException
     */
    void delCrawlRulePageBO(long crawlRuleId) throws DataServiceException;

    /**
     * 保存规则的页码
     *
     * @param crawlRulePageBO
     * @return
     * @throws DataServiceException
     */
    int saveCrawlRulePageBO(CrawlRulePageBO crawlRulePageBO)
            throws DataServiceException;

    /**
     * 查询规则的页码解析
     *
     * @param param
     * @return
     * @throws DataServiceException
     */
    List<CrawlRulePageParseBO> getCrawlRulePageParseBO(
            Map<String, Object> param) throws DataServiceException;

    /**
     * 保存规则的页码解析
     *
     * @param crawlRulePageParseBO
     * @return
     * @throws DataServiceException
     */
    int saveCrawlRulePageParaseBO(CrawlRulePageParseBO crawlRulePageParseBO)
            throws DataServiceException;

    /**
     * 编辑规则的页码解析
     *
     * @param crawlRulePageParseBO
     * @throws DataServiceException
     */
    void modifyCrawlRulePageParseBO(CrawlRulePageParseBO crawlRulePageParseBO)
            throws DataServiceException;

    /**
     * 删除规则的页码解析
     *
     * @param pageId
     * @throws DataServiceException
     */
    void delCrawlRulePageParseBOs(Long pageId) throws DataServiceException;

    /**
     * 查询规则的页码解析后置处理器
     *
     * @param param
     * @return
     * @throws DataServiceException
     */
    List<CrawlRulePageSuffProcBO> getCrawlRulePageSuffProcBO(
            Map<String, Object> param) throws DataServiceException;

    /**
     * 保存页码解析后置处理器
     *
     * @param crawlRulePageSuffProcBO
     * @return
     * @throws DataServiceException
     */
    int saveCrawlRulePageSuffProcBO(
            CrawlRulePageSuffProcBO crawlRulePageSuffProcBO)
            throws DataServiceException;

    /**
     * 编辑页码解析后置处理器
     *
     * @param crawlRulePageSuffProcBO
     * @throws DataServiceException
     */
    void modifyCrawlRulePageSuffProcBO(
            CrawlRulePageSuffProcBO crawlRulePageSuffProcBO)
            throws DataServiceException;

    /**
     * 删除页码解析后置处理器
     *
     * @param pageId
     * @throws DataServiceException
     */
    void delCrawlRulePageSuffProcBO(Long pageId) throws DataServiceException;

    /**
     * 查询字段在规则中是否使用
     *
     * @param paramId
     * @throws DataServiceException
     */
    boolean getCrawlRuleRequestParamByParamValue(String paramId) throws DataServiceException;
}


