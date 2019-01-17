package com.transing.crawl.biz.service;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BizService;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.web.po.CrawlRuleDetailFieldPo;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service
 * 源文件:CrawlRuleDetailService.java
 * 页面规则service
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
public interface CrawlRuleDetailService extends BizService
{
    /**
     * 获取页面规则参数
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleDetailBO> getCrawlRuleDetailList(Map<String, Object> param)
            throws BizException;

    /**
     * 查询页面规则
     *
     * @param id
     * @return
     * @throws BizException
     */
    CrawlRuleDetailBO getCrawlRuleDetailBO(long id) throws BizException;

    /**
     * 编辑
     *
     * @param crawlRuleDetailBO
     * @throws BizException
     */
    void modifyCrawlRuleDetailBO(CrawlRuleDetailBO crawlRuleDetailBO)
            throws BizException;

    /**
     * 保存页面规则
     *
     * @param crawlRuleDetailBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleDetailBo(CrawlRuleDetailBO crawlRuleDetailBO)
            throws BizException;



    /**
     * 查询页面规则的解析
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleDetailParseBO> getCrawlRuleDetailParseBo(
            Map<String, Object> param) throws BizException;

    /**
     * 保存页面规则的解析
     *
     * @param crawlRuleDetailParseBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleDetailParseBo(
            CrawlRuleDetailParseBO crawlRuleDetailParseBO) throws BizException;

    /**
     * 修改页面规则的解析
     *
     * @param crawlRuleDetailParseBO
     * @throws BizException
     */
    void modifyCrawlRuleDetailParseBo(
            CrawlRuleDetailParseBO crawlRuleDetailParseBO) throws BizException;



    /**
     * 查询页面规则的前置处理器
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleDetailPreProcBO> getCrawlRuleDetailPreProcBO(
            Map<String, Object> param) throws BizException;

    /**
     * 保存页面规则的前置处理器
     *
     * @param crawlRuleDetailPreProcBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleDetailPreProcBO(
            CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO)
            throws BizException;

    /**
     * 修改页面规则的前置处理器
     *
     * @param crawlRuleDetailPreProcBO
     * @throws BizException
     */
    void modifyCrawlRuleDetailPreProcBO(
            CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO)
            throws BizException;



    /**
     * 查询页面规则的后置处理器
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleDetailSuffProcBO> getCrawlRuleDetailSuffProcBos(
            Map<String, Object> param) throws BizException;

    /**
     * 保存页面规则的后置处理器
     *
     * @param crawlRuleDetailSuffProcBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleDetailSuffProcBO(
            CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO)
            throws BizException;

    /**
     * 修改页面规则的后置处理器
     *
     * @param crawlRuleDetailSuffProcBO
     * @throws BizException
     */
    void modifyCrawlRuleDetailSuffProcBO(
            CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO)
            throws BizException;



    /**
     * 查询页面规则的字段
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleDetailFieldBO> getCrawlRuleDetailFieldBos(
            Map<String, Object> param) throws BizException;

    /**
     * 查询页面规则的字段
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleDetailFieldPo> getCrawlRuleDetailFieldPos(
            Map<String, Object> param, Long datasourceTypeId)
            throws BizException;

    /**
     * 保存页面规则的字段
     *
     * @param crawlRuleDetailFieldBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleDetailFieldBO(
            CrawlRuleDetailFieldBO crawlRuleDetailFieldBO) throws BizException;

    /**
     * 编辑页面规则的字段
     *
     * @param crawlRuleDetailFieldBO
     * @throws BizException
     */
    void modifyCrawlRuleDetailFieldBO(
            CrawlRuleDetailFieldBO crawlRuleDetailFieldBO) throws BizException;



    /**
     * 查询页面规则的字段的解析
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleDetailFieldParseBO> getCrawlRuleDetailFieldParseBOs(
            Map<String, Object> param) throws BizException;

    /**
     * 保存页面规则的字段的解析
     *
     * @param crawlRuleDetailFieldParseBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleDetailFieldParseBO(
            CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO)
            throws BizException;

    /**
     * 编辑页面规则的字段的解析
     *
     * @param crawlRuleDetailFieldParseBO
     * @throws BizException
     */
    void modifyCrawlRuleDetailFieldParseBO(
            CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO)
            throws BizException;

    /**
     * 删除页面规则的字段的解析
     *
     * @param detailFieldId
     * @throws BizException
     */
    void delCrawlRuleDetailFieldParseBO(long detailFieldId) throws BizException;

    /**
     * 查询页面规则的字段的后置处理器
     *
     * @param param
     * @return
     * @throws BizException
     */
    List<CrawlRuleDetailFieldSuffProcBO> getCrawlRuleDetailFieldSuffProcBO(
            Map<String, Object> param) throws BizException;

    /**
     * 保存页面规则的字段的后置处理器
     *
     * @param crawlRuleDetailFieldSuffProcBO
     * @return
     * @throws BizException
     */
    int saveCrawlRuleDetailFieldSuffProcBO(
            CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO)
            throws BizException;

    /**
     * 编辑页面规则的字段的后置处理器
     *
     * @param crawlRuleDetailFieldSuffProcBO
     * @throws BizException
     */
    void modifyCrawlRuleDetailFieldSuffProcBO(
            CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO)
            throws BizException;

    /**
     * 删除页面规则的字段的后置处理器
     *
     * @param detailFieldId
     * @throws BizException
     */
    void delCrawlRuleDetailFieldSuffProcBO(Long detailFieldId)
            throws BizException;

    void delCrawlRuleDetailParseBoByDetailId(Long crawlRuleDetailId);

    void delCrawlRuleDetailPreProcBOByDetailId(Long crawlRuleDetailId);

    void delCrawlRuleDetailSuffProcBOByDetailId(Long crawlRuleDetailId);

    void delCrawlRuleDetailFieldBOByDetailId(Long crawlRuleDetailId);

    void delCrawlRUleDetailByRuleId(Long ruleId);

}
