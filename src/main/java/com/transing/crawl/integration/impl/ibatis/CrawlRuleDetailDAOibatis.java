package com.transing.crawl.integration.impl.ibatis;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.integration.DataServiceException;
import com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS;
import com.transing.crawl.biz.service.StorageTypeFieldService;
import com.transing.crawl.integration.CrawlRuleDetailDataService;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.web.po.CrawlRuleDetailFieldPo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.integration.impl.ibatis
 * 源文件:CrawlRuleDetailDAOibatis.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月27日
 */
@Scope("prototype")
@Repository("crawlRuleDetailDataService")
public class CrawlRuleDetailDAOibatis extends BaseDaoiBATIS implements
        CrawlRuleDetailDataService
{
    @Resource
    private StorageTypeFieldService storageTypeFieldService;

    @Override
    public List<CrawlRuleDetailBO> getCrawlRuleDetailList(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleDetailMapper.getCrawlRuleDetailList",
                        param);
    }

    @Override
    public CrawlRuleDetailBO getCrawlRuleDetailBO(long id)
            throws DataServiceException
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        List<CrawlRuleDetailBO> crawlRuleDetailBOList = getCrawlRuleDetailList(
                param);
        if (crawlRuleDetailBOList != null && crawlRuleDetailBOList.size() > 0)
        {
            return crawlRuleDetailBOList.get(0);
        }
        return null;
    }

    @Override
    public void modifyCrawlRuleDetailBO(CrawlRuleDetailBO crawlRuleDetailBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleDetailMapper.modifyCrawlRuleDetailBO",
                        crawlRuleDetailBO);
    }

    @Override
    public int saveCrawlRuleDetailBo(CrawlRuleDetailBO crawlRuleDetailBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleDetailMapper.saveCrawlRuleDetailBo",
                        crawlRuleDetailBO);
    }


    @Override
    public List<CrawlRuleDetailParseBO> getCrawlRuleDetailParseBo(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleDetailMapper.getCrawlRuleDetailParseBo",
                        param);
    }

    @Override
    public int saveCrawlRuleDetailParseBo(
            CrawlRuleDetailParseBO crawlRuleDetailParseBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleDetailMapper.saveCrawlRuleDetailParseBo",
                        crawlRuleDetailParseBO);
    }

    @Override
    public void modifyCrawlRuleDetailParseBo(
            CrawlRuleDetailParseBO crawlRuleDetailParseBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleDetailMapper.modifyCrawlRuleDetailParseBo",
                        crawlRuleDetailParseBO);
    }


    @Override
    public List<CrawlRuleDetailPreProcBO> getCrawlRuleDetailPreProcBO(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleDetailMapper.getCrawlRuleDetailPreProcBO",
                        param);
    }

    @Override
    public int saveCrawlRuleDetailPreProcBO(
            CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleDetailMapper.saveCrawlRuleDetailPreProcBO",
                        crawlRuleDetailPreProcBO);
    }

    @Override
    public void modifyCrawlRuleDetailPreProcBO(
            CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleDetailMapper.modifyCrawlRuleDetailPreProcBO",
                        crawlRuleDetailPreProcBO);
    }


    @Override
    public List<CrawlRuleDetailSuffProcBO> getCrawlRuleDetailSuffProcBos(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList(
                        "crawlRuleDetailMapper.getCrawlRuleDetailSuffProcBos",
                        param);
    }

    @Override
    public int saveCrawlRuleDetailSuffProcBO(
            CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleDetailMapper.saveCrawlRuleDetailSuffProcBO",
                        crawlRuleDetailSuffProcBO);
    }

    @Override
    public void modifyCrawlRuleDetailSuffProcBO(
            CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleDetailMapper.modifyCrawlRuleDetailSuffProcBO",
                        crawlRuleDetailSuffProcBO);
    }


    @Override
    public List<CrawlRuleDetailFieldBO> getCrawlRuleDetailFieldBos(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList("crawlRuleDetailMapper.getCrawlRuleDetailFieldBos"
                        , param);
    }

    @Override
    public int saveCrawlRuleDetailFieldBO(
            CrawlRuleDetailFieldBO crawlRuleDetailFieldBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleDetailMapper.saveCrawlRuleDetailFieldBO",
                        crawlRuleDetailFieldBO);
    }

    @Override
    public void modifyCrawlRuleDetailFieldBO(
            CrawlRuleDetailFieldBO crawlRuleDetailFieldBO)
            throws DataServiceException
    {

    }


    @Override
    public List<CrawlRuleDetailFieldParseBO> getCrawlRuleDetailFieldParseBOs(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList(
                        "crawlRuleDetailMapper.getCrawlRuleDetailFieldParseBOs",
                        param);
    }

    @Override
    public int saveCrawlRuleDetailFieldParseBO(
            CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleDetailMapper.saveCrawlRuleDetailFieldParseBO",
                        crawlRuleDetailFieldParseBO);
    }

    @Override
    public void modifyCrawlRuleDetailFieldParseBO(
            CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleDetailMapper.modifyCrawlRuleDetailFieldParseBO",
                        crawlRuleDetailFieldParseBO);
    }

    @Override
    public List<CrawlRuleDetailFieldSuffProcBO> getCrawlRuleDetailFieldSuffProcBO(
            Map<String, Object> param) throws DataServiceException
    {
        return sqlSessionTemplate
                .selectList(
                        "crawlRuleDetailMapper.getCrawlRuleDetailFieldSuffProcBO",
                        param);
    }

    @Override
    public int saveCrawlRuleDetailFieldSuffProcBO(
            CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO)
            throws DataServiceException
    {
        return sqlSessionTemplate
                .insert("crawlRuleDetailMapper.saveCrawlRuleDetailFieldSuffProcBO",
                        crawlRuleDetailFieldSuffProcBO);
    }

    @Override
    public void modifyCrawlRuleDetailFieldSuffProcBO(
            CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO)
            throws DataServiceException
    {
        sqlSessionTemplate
                .update("crawlRuleDetailMapper.modifyCrawlRuleDetailFieldSuffProcBO",
                        crawlRuleDetailFieldSuffProcBO);
    }

    @Override
    public List<CrawlRuleDetailFieldPo> getCrawlRuleDetailFieldPos(
            Map<String, Object> param, Long datasourceTypeId)
            throws BizException
    {
        Map<Long, StorageTypeFieldBO> storageTypeFieldBOMap = storageTypeFieldService
                .getStorageTypeFields(datasourceTypeId);

        List<CrawlRuleDetailFieldPo> crawlRuleDetailFieldPos = new ArrayList<CrawlRuleDetailFieldPo>();
        List<CrawlRuleDetailFieldBO> crawlRuleDetailFieldBOList = getCrawlRuleDetailFieldBos(
                param);
        CrawlRuleDetailFieldPo crawlRuleDetailFieldPo = null;
        Map<String, Object> paramMap = null;
        for (CrawlRuleDetailFieldBO crawlRuleDetailFieldBO : crawlRuleDetailFieldBOList)
        {
            crawlRuleDetailFieldPo = new CrawlRuleDetailFieldPo();
            paramMap = new HashMap<String, Object>();
            paramMap.put("detailFieldId", crawlRuleDetailFieldBO.getId());
            paramMap.put("fieldId",
                    crawlRuleDetailFieldBO.getStorageTypeFieldId());
            List<CrawlRuleDetailFieldParseBO> crawlRuleDetailFieldParseBOs = getCrawlRuleDetailFieldParseBOs(
                    paramMap);
            crawlRuleDetailFieldPo
                    .setFieldParseBOs(crawlRuleDetailFieldParseBOs);
            List<CrawlRuleDetailFieldSuffProcBO> crawlRuleDetailFieldSuffProcBOs = getCrawlRuleDetailFieldSuffProcBO(
                    paramMap);
            crawlRuleDetailFieldPo
                    .setFieldSuffProcBOs(crawlRuleDetailFieldSuffProcBOs);
            crawlRuleDetailFieldPo.setStorageTypeFieldBO(storageTypeFieldBOMap
                    .get(crawlRuleDetailFieldBO.getStorageTypeFieldId()));
            crawlRuleDetailFieldPos.add(crawlRuleDetailFieldPo);
        }
        return crawlRuleDetailFieldPos;
    }

    @Override
    public void delCrawlRuleDetailFieldSuffProcBO(Long detailFieldId)
            throws BizException
    {
        sqlSessionTemplate
                .delete("crawlRuleDetailMapper.delCrawlRuleDetailFieldSuffProcBo",
                        detailFieldId);
    }

    @Override
    public void delCrawlRuleDetailParseBoByDetailId(Long crawlRuleDetailId)
    {
        sqlSessionTemplate
                .delete("crawlRuleDetailMapper.delCrawlRuleDetailParseBo",
                        crawlRuleDetailId);
    }

    @Override
    public void delCrawlRuleDetailPreProcBOByDetailId(Long crawlRuleDetailId)
    {
        sqlSessionTemplate
                .delete("crawlRuleDetailMapper.delCrawlRuleDetailPreProcBo",
                        crawlRuleDetailId);
    }

    @Override
    public void delCrawlRuleDetailSuffProcBOByDetailId(Long crawlRuleDetailId)
    {
        sqlSessionTemplate
                .delete("crawlRuleDetailMapper.delCrawlRuleDetailSuffProcBo",
                        crawlRuleDetailId);
    }

    @Override
    public void delCrawlRuleDetailFieldBOByDetailId(Long crawlRuleDetailId)
    {
        sqlSessionTemplate
                .delete("crawlRuleDetailMapper.delCrawlRuleDetailFieldBo",
                        crawlRuleDetailId);
    }

    @Override
    public void delCrawlRuleDetailFieldParseBO(long detailFieldId)
            throws BizException
    {
        sqlSessionTemplate
                .delete("crawlRuleDetailMapper.delCrawlRuleDetailFieldParseBo",
                        detailFieldId);
    }

    @Override
    public void delCrawlRUleDetailByRuleId(Long ruleId)
    {
        sqlSessionTemplate.delete("crawlRuleDetailMapper.delCrawlRuleDetailBo",ruleId);
    }
}
