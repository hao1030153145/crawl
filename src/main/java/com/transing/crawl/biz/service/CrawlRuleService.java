package com.transing.crawl.biz.service;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BizService;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.web.po.CrawlRulePagePo;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service
 * 源文件:CrawlRuleService.java
 * 抓取规则
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月23日
 */
public interface CrawlRuleService extends BizService
{
    /**
     * 添加默认的规则
     *
     * @return
     * @throws BizException
     */
    int initCrawlRule(CrawlRuleBO crawlRuleBO) throws BizException;

    /**
     * 编辑
     *
     * @param crawlRuleBO
     * @throws BizException
     */
    void modifyCrawlRule(CrawlRuleBO crawlRuleBO) throws BizException;

    /**
     * 根据数据源类型查询
     *
     * @param datasourceTypeId
     * @return
     * @throws BizException
     */
    CrawlRuleBO getCrawlRuleBOByDatasourceTypeId(long datasourceTypeId)
            throws BizException;

    /**
     * 根据id查询
     *
     * @param ruleId
     * @return
     * @throws BizException
     */
    CrawlRuleBO getCrawlRuleBOById(long ruleId) throws BizException;

    /**
     * 根据数据源查询集合
     *
     * @param datasourceId
     * @return
     * @throws BizException
     */
    List<CrawlRuleBO> getCrawlRuleBoListByDatasourceId(long datasourceId,
            Integer page, Integer size) throws BizException;

    /**
     * 查询集合
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleBO> getCrawlRuleBoList(Map<String, Object> param)
            throws BizException;

    /**
     * 查询总数
     *
     * @param param
     * @return
     * @throws BizException
     */
    int getCrawlRuleCount(Map<String, Object> param) throws BizException;

    /**
     * 获取规则的处理器
     *
     * @return
     * @throws BizException
     */
    List<CrawlRuleProcessorBO> getProcessorList() throws BizException;

    /**
     * 获取指定处理器
     *
     * @param id
     * @return
     * @throws BizException
     */
    CrawlRuleProcessorBO getProcessor(int id) throws BizException;

    /**
     * 查询规则的解析方式
     *
     * @return
     * @throws BizException
     */
    List<CrawlRuleAnalysisTypeBO> getCrawlRuleAnalysisTypList()
            throws BizException;

    /**
     * 获取指定的解析方式
     *
     * @param id
     * @return
     * @throws BizException
     */
    CrawlRuleAnalysisTypeBO getCrawlRuleAnalysisTypeBO(int id)
            throws BizException;

    /**
     * 根据规则id查询请求参数
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleRequestHeaderBO> getCrawlRuleRequestHeaederList(
            Map<String, Object> param) throws BizException;

    /**
     * 添加规则的请求头部参数
     *
     * @param crawlRuleRequestHeaderBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleRequestHeaderBO(
            CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO)
            throws BizException;

    /**
     * 编辑规则的请求参数
     *
     * @param crawlRuleRequestHeaderBO
     * @throws BizException
     */
    void modifyCrawlRuleRequestHeaderBO(
            CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO)
            throws BizException;

    /**
     * 删除请求头部参数
     * @param ruleId
     * @throws BizException
     */
    void delCrawlRuleRequestHeaderBO(Long ruleId) throws BizException;

    /**
     * 请求头部参数的后置处理器
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleRequestHeaderSufferProcBO> getCrawlRuleRequestHeaderSufferProcBO(
            Map<String, Object> param) throws BizException;

    /**
     * 保存头部参数的后置处理器
     *
     * @param crawlRuleRequestHeaderSufferProcBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleRequestHeaderSufferProcBO(
            CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO)
            throws BizException;

    /**
     * 编辑头部参数的后置处理器
     *
     * @param crawlRuleRequestHeaderSufferProcBO
     * @throws BizException
     */
    void modifyCrawlRuleRequestHeaderSufferProcBO(
            CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO)
            throws BizException;

    /**
     * 删除请求头部参数后置处理器
     * @param headerId
     * @throws BizException
     */
    void delCrawlRuleRequestHeaderSufferProcBO(Long headerId) throws BizException;

    /**
     * 保存请求参数
     * @param crawlRuleRequestParamBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleRequestParam(
            CrawlRuleRequestParamBO crawlRuleRequestParamBO) throws BizException;

    /**
     * 编辑请求参数
     * @param crawlRuleRequestParamBO
     * @throws BizException
     */
    void modifyCrawlRuleRequestParam(
            CrawlRuleRequestParamBO crawlRuleRequestParamBO) throws BizException;

    /**
     * 删除请求参数
     * @param ruleId
     * @throws BizException
     */
    void delCrawlRuleRequestParam(Long ruleId) throws BizException;

    /**
     * 查询请求参数
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleRequestParamBO> getCrawlRuleRequestParamList(
            Map<String, Object> param) throws BizException;

    /**
     * 保存请求参数后置处理器
     * @param crawlRuleRequestParamSufferProcBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleRequestParamSuffProc(
            CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO) throws BizException;

    /**
     * 编辑请求参数后置处理器
     * @param crawlRuleRequestParamSufferProcBO
     * @throws BizException
     */
    void modifyCrawlRuleRequestParamSuffProc(
            CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO) throws BizException;

    /**
     * 删除请求参数后置处理器
     * @param paramId
     * @throws BizException
     */
    void delCrawlRuleRequestParamSuffProc(Long paramId) throws BizException;

    /**
     * 查询请求参数后置处理器
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleRequestParamSufferProcBO> getCrawlRuleRequestParamSuffProcList(
            Map<String, Object> param) throws BizException;

    /**
     * 查询规则页码
     * @param crawlRuleId
     * @return
     * @throws BizException
     */
    CrawlRulePageBO getCrawlRulePageBO(long crawlRuleId) throws BizException;

    /**
     * 编辑规则页码
     * @param crawlRulePageBO
     * @throws BizException
     */
    void modifyCrawlRulePageBO(CrawlRulePageBO crawlRulePageBO) throws BizException;

    /**
     * 删除规则的页码
     * @param crawlRuleId
     * @return
     * @throws BizException
     */
    void delCrawlRulePageBO(long crawlRuleId) throws BizException;

    /**
     * 保存规则的页码
     * @param crawlRulePageBO
     * @return
     * @throws BizException
     */
    int saveCrawlRulePageBO(CrawlRulePageBO crawlRulePageBO) throws BizException;

    /**
     * 查询规则的页码解析
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRulePageParseBO> getCrawlRulePageParseBO(
            Map<String, Object> param) throws BizException;

    /**
     * 保存规则的页码解析
     * @param crawlRulePageParseBO
     * @return
     * @throws BizException
     */
    int saveCrawlRulePageParaseBO(CrawlRulePageParseBO crawlRulePageParseBO) throws BizException;

    /**
     * 编辑规则的页码解析
     * @param crawlRulePageParseBO
     * @throws BizException
     */
    void modifyCrawlRulePageParseBO(CrawlRulePageParseBO crawlRulePageParseBO) throws BizException;

    /**
     * 删除规则的页码解析
     * @param ruleId
     * @throws BizException
     */
    void  delCrawlRulePageParseBOs(Long pageId) throws BizException;

    /**
     * 查询规则的页码解析后置处理器
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRulePageSuffProcBO> getCrawlRulePageSuffProcBO(
            Map<String, Object> param) throws BizException;
    /**
     * 保存页码解析后置处理器
     * @param crawlRulePageSuffProcBO
     * @return
     * @throws BizException
     */
    int saveCrawlRulePageSuffProcBO(
            CrawlRulePageSuffProcBO crawlRulePageSuffProcBO) throws BizException;

    /**
     * 编辑页码解析后置处理器
     * @param crawlRulePageSuffProcBO
     * @throws BizException
     */
    void modifyCrawlRulePageSuffProcBO(
            CrawlRulePageSuffProcBO crawlRulePageSuffProcBO) throws BizException;

    /**
     * 删除页码解析后置处理器
     * @param pageId
     * @throws BizException
     */
    void delCrawlRulePageSuffProcBO(Long pageId) throws BizException;

    /**
     * 查询字段在规则中是否使用
     * @param paramId
     * @throws BizException
     */
    boolean getCrawlRuleRequestParamByParamValue(String paramId) throws BizException;

    /**
     * 查询规则的页码信息
     * @param crawlRuleId
     * @return
     * @throws BizException
     */
    CrawlRulePagePo getCrawlRulePagePO(long crawlRuleId) throws BizException;

}
