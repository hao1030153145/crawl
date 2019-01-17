package com.transing.crawl.web.controller;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.json.JSONUtils;
import com.jeeframework.webframework.exception.WebException;
import com.transing.crawl.biz.service.*;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.web.exception.MySystemCode;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:SynchronousDatasourceController.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月28日
 */
@Controller
@Api(value = "同步控制器", description = "对数据源的同步")
@RequestMapping(value = "/synchronous")
public class SynchronousDatasourceController
{
    private static String loggerName = "datasourceRelease";

    @Resource
    private CrawlRuleService crawlRuleService;

    @Resource
    private CrawlRuleDetailService crawlRuleDetailService;

    @Resource
    private DatasourceService datasourceService;

    @Resource
    private DatasourceTypeService datasourceTypeService;

    /**
     * 同步数据源基础数据
     * @return
     */
    @RequestMapping(value = "/releaseDatasourceBo.json",method = RequestMethod.POST)
    @ResponseBody
    public Map releaseDatasourceBO(@RequestParam(value = "id",required = true)@ApiParam(value = "id",required = true) String id,
            @RequestParam(value = "isNeedLogin",required = false)@ApiParam(value = "是否需要登陆",required = true) String isNeedLogin,
            @RequestParam(value = "isNeedProxyIp",required = false)@ApiParam(value = "是否需要代理ip",required = true) String isNeedProxyIp)
    {
        Map<String,Object> resultMap=new HashMap<String, Object>();
        Datasource datasourceBo=new Datasource();
        datasourceBo.setIsNeedLogin(Integer.parseInt(isNeedLogin));
        datasourceBo.setIsNeedProxyIp(Integer.parseInt(isNeedProxyIp));
        datasourceBo.setId(Long.parseLong(id));
        datasourceService.addOrUpdate(datasourceBo);
        return resultMap;
    }



    /**
     * 同步数据源基础数据
     * @return
     */
    @RequestMapping(value = "/releaseDatasourceTypeBo.json",method = RequestMethod.POST)
    @ResponseBody
    public Map releaseDatasourceTypeBO(@RequestParam(value = "id",required = true)@ApiParam(value = "id",required = true) String id,
            @RequestParam(value = "datasourceId",required = true)@ApiParam(value = "datasourceId",required = true) String datasourceId,
            @RequestParam(value = "isNeedLogin",required = false)@ApiParam(value = "是否需要登陆",required = true) String isNeedLogin,
            @RequestParam(value = "isNeedProxyIp",required = false)@ApiParam(value = "是否需要代理ip",required = true) String isNeedProxyIp)
    {
        Map<String,Object> resultMap=new HashMap<String, Object>();
        DatasourceTypeBO datasourceTypeBO=new DatasourceTypeBO();
        datasourceTypeBO.setId(Long.parseLong(id));
        datasourceTypeBO.setDatasourceId(Long.parseLong(datasourceId));
        datasourceTypeBO.setIsNeedLogin(Integer.parseInt(isNeedLogin));
        datasourceTypeBO.setIsNeedProxyIp(Integer.parseInt(isNeedProxyIp));
        datasourceTypeService.addOrUpdate(datasourceTypeBO);
        return resultMap;
    }

    /**
     * 发布数据源
     * 1、先同步storage_type 以及 storage_type_field。先查询，有则修改，没有则添加（带id）
     * 4、同步crawl_input_param
     * 5、同步crawl_rule
     * 6、同步crawl_rule_detail
     *
     * @param datasourceParam
     * @return
     */
    @RequestMapping(value = "/releaseDatasource.json", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> releaseDatasource(
            @RequestParam(value = "datasourceParam", required = true) String datasourceParam)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (!JSONUtils.isJSONValid(datasourceParam))
        {
            throw new WebException(MySystemCode.SYS_REQUEST_EXCEPTION);
        }
        try
        {
            JSONObject datasourceParamJSON = JSONObject
                    .fromObject(datasourceParam);

            //规则
            JSONArray crawlRuleArray = datasourceParamJSON
                    .getJSONArray("crawlRuleBos");
            for (Object obj : crawlRuleArray)
            {
                JSONObject crawlRuleJSON = (JSONObject) obj;

                //规则参数
                JSONArray requestParamArray = crawlRuleJSON
                        .containsKey("crawlRuleRequestParamBos") ?
                        crawlRuleJSON.getJSONArray("crawlRuleRequestParamBos") :
                        null;
                //删除规则以及规则相对应的数据
                Long ruleId=crawlRuleJSON.getLong("id");
                Map<String,Object> paramMap=new HashMap<String, Object>();
                paramMap.put("ruleId",ruleId);
                //1、删除param.
                List<CrawlRuleRequestParamBO> crawlRuleRequestParamBOList= crawlRuleService.getCrawlRuleRequestParamList(paramMap);
                for (CrawlRuleRequestParamBO crawlRuleRequestParamBO:crawlRuleRequestParamBOList){
                    crawlRuleService.delCrawlRuleRequestParamSuffProc(crawlRuleRequestParamBO.getId());
                }
                crawlRuleService.delCrawlRuleRequestParam(ruleId);
                //2、删除header
                List<CrawlRuleRequestHeaderBO> crawlRuleRequestHeaderBOs=crawlRuleService.getCrawlRuleRequestHeaederList(paramMap);
                for (CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO:crawlRuleRequestHeaderBOs){
                    crawlRuleService.delCrawlRuleRequestHeaderSufferProcBO(crawlRuleRequestHeaderBO.getId());
                }
                crawlRuleService.delCrawlRuleRequestHeaderBO(ruleId);
                //2、删除page
                CrawlRulePageBO crawlRulePage=crawlRuleService.getCrawlRulePageBO(ruleId);
                if(crawlRulePage!=null){
                    crawlRuleService.delCrawlRulePageSuffProcBO(crawlRulePage.getId());
                    crawlRuleService.delCrawlRulePageParseBOs(crawlRulePage.getId());
                    crawlRuleService.delCrawlRulePageBO(ruleId);
                }
                //3、查询detail
               List<CrawlRuleDetailBO> crawlRuleDetailBOList=crawlRuleDetailService.getCrawlRuleDetailList(paramMap);

                for (CrawlRuleDetailBO crawlRuleDetailBO:crawlRuleDetailBOList){
                    paramMap.clear();
                    paramMap.put("detailId",crawlRuleDetailBO.getId());
                    //前置处理器
                    crawlRuleDetailService.delCrawlRuleDetailPreProcBOByDetailId(crawlRuleDetailBO.getId());
                   //列表解析
                    crawlRuleDetailService.delCrawlRuleDetailParseBoByDetailId(crawlRuleDetailBO.getId());
                    //列表的后置处理器
                    crawlRuleDetailService.delCrawlRuleDetailSuffProcBOByDetailId(crawlRuleDetailBO.getId());
                    //字段的后置处理器
                    List<CrawlRuleDetailFieldBO> detailFieldbos=crawlRuleDetailService.getCrawlRuleDetailFieldBos(paramMap);
                    for (CrawlRuleDetailFieldBO crawlRuleDetailFieldBO:detailFieldbos){
                        crawlRuleDetailService.delCrawlRuleDetailFieldSuffProcBO(crawlRuleDetailFieldBO.getId());
                        crawlRuleDetailService.delCrawlRuleDetailFieldParseBO(crawlRuleDetailFieldBO.getId());
                    }
                    crawlRuleDetailService.delCrawlRuleDetailFieldBOByDetailId(crawlRuleDetailBO.getId());
                }

                crawlRuleDetailService.delCrawlRUleDetailByRuleId(ruleId);

                if (requestParamArray != null)
                {
                    for (Object paramobj : requestParamArray)
                    {
                        JSONObject paramJSON = (JSONObject) paramobj;

                        long paramId=paramJSON.getLong("id");

                        //参数的后置处理器
                        JSONArray paramSuffProcJSON = paramJSON
                                .containsKey("crawlRuleRequestParamSuffProcBos")
                                ?
                                paramJSON.getJSONArray(
                                        "crawlRuleRequestParamSuffProcBos") :
                                null;
                        if (paramSuffProcJSON != null)
                        {

                            for (Object suffProcObj : paramSuffProcJSON)
                            {
                                JSONObject suffProcJSON = (JSONObject) suffProcObj;
                                CrawlRuleRequestParamSufferProcBO crawlRuleRequestParamSufferProcBO = (CrawlRuleRequestParamSufferProcBO) JSONObject
                                        .toBean(suffProcJSON,
                                                CrawlRuleRequestParamSufferProcBO.class);
                                try
                                {
                                    crawlRuleService
                                            .saveCrawlRuleRequestParamSuffProc(
                                                    crawlRuleRequestParamSufferProcBO);
                                }
                                catch (Exception e)
                                {
                                    crawlRuleService
                                            .modifyCrawlRuleRequestParamSuffProc(
                                                    crawlRuleRequestParamSufferProcBO);
                                }
                            }
                            paramJSON.remove("crawlRuleRequestParamSuffProcBos");
                        }

                        //参数
                        CrawlRuleRequestParamBO crawlRuleRequestParamBO = (CrawlRuleRequestParamBO) JSONObject
                                .toBean(paramJSON,
                                        CrawlRuleRequestParamBO.class);
                        try
                        {
                            crawlRuleService.saveCrawlRuleRequestParam(
                                    crawlRuleRequestParamBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleService.modifyCrawlRuleRequestParam(
                                    crawlRuleRequestParamBO);
                        }
                        LoggerUtil.debugTrace(loggerName,
                                crawlRuleJSON.getString("ruleName") + "-" +
                                        crawlRuleRequestParamBO.getParamName() +
                                        "发布成功！");
                    }
                    crawlRuleJSON.remove("crawlRuleRequestParamBos");
                }
                //请求头
                JSONArray requestHeaderArray = crawlRuleJSON
                        .containsKey("crawlRuleRequestHeaderBos")
                        ?crawlRuleJSON.getJSONArray("crawlRuleRequestHeaderBos") :
                        null;
                if (requestHeaderArray != null)
                {
                    for (Object crawlRuleRequestHeaderObj : requestHeaderArray)
                    {
                        JSONObject crawlRuleRequestHeaderJSON = (JSONObject) crawlRuleRequestHeaderObj;

                        //请求后置处理器
                        JSONArray headerSuffProcArray =
                                crawlRuleRequestHeaderJSON.containsKey("crawlRuleRequestHeaderSuffProcBos")
                                        ?crawlRuleRequestHeaderJSON.getJSONArray("crawlRuleRequestHeaderSuffProcBos")
                                        :null;
                        if (headerSuffProcArray != null)
                        {
                            for (Object headerSuffProcObj : headerSuffProcArray)
                            {
                                JSONObject headerSuffProcJSON = (JSONObject) headerSuffProcObj;
                                CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO = (CrawlRuleRequestHeaderSufferProcBO) JSONObject
                                        .toBean(headerSuffProcJSON,
                                                CrawlRuleRequestHeaderSufferProcBO.class);
                                try
                                {
                                    crawlRuleService
                                            .saveCrawlRuleRequestHeaderSufferProcBO(
                                                    crawlRuleRequestHeaderSufferProcBO);
                                }
                                catch (Exception e)
                                {
                                    crawlRuleService
                                            .modifyCrawlRuleRequestHeaderSufferProcBO(
                                                    crawlRuleRequestHeaderSufferProcBO);
                                }
                            }
                            crawlRuleRequestHeaderJSON.remove("crawlRuleRequestHeaderSuffProcBos");
                        }

                        //请求头
                        CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBO = (CrawlRuleRequestHeaderBO) JSONObject
                                .toBean(crawlRuleRequestHeaderJSON,
                                        CrawlRuleRequestHeaderBO.class);
                        try
                        {
                            crawlRuleService.saveCrawlRuleRequestHeaderBO(
                                    crawlRuleRequestHeaderBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleService.modifyCrawlRuleRequestHeaderBO(
                                    crawlRuleRequestHeaderBO);
                        }
                        LoggerUtil.debugTrace(loggerName,
                                crawlRuleJSON.getString("ruleName") + "-" +
                                        crawlRuleRequestHeaderBO
                                                .getHeaderName() + "发布成功！");
                    }
                    crawlRuleJSON.remove("crawlRuleRequestHeaderBos");
                }
                //页码
                JSONObject pageObj = crawlRuleJSON
                        .getJSONObject("crawlRulePageBo");

                //页码的解析
                JSONArray pageParseArray = pageObj
                        .containsKey("crawlRulePageParseBos")
                        ? pageObj.getJSONArray("crawlRulePageParseBos") : null;
                if (pageParseArray != null)
                {
                    for (Object pageParseObj : pageParseArray)
                    {
                        JSONObject pageParseJSON = (JSONObject) pageParseObj;
                        CrawlRulePageParseBO crawlRulePageParseBO = (CrawlRulePageParseBO) JSONObject
                                .toBean(pageParseJSON,
                                        CrawlRulePageParseBO.class);
                        try
                        {
                            crawlRuleService.saveCrawlRulePageParaseBO(
                                    crawlRulePageParseBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleService.modifyCrawlRulePageParseBO(
                                    crawlRulePageParseBO);
                        }
                    }
                    pageObj.remove("crawlRulePageParseBos");
                }
                //页码的后置处理器
                JSONArray pageSuffProcArray = pageObj
                        .containsKey("crawlRulePageSuffProcBos")
                        ?pageObj.getJSONArray("crawlRulePageSuffProcBos") :
                        null;
                if (pageSuffProcArray != null)
                {
                    for (Object suffProcObj : pageSuffProcArray)
                    {
                        JSONObject pageSuffprocObj = (JSONObject) suffProcObj;
                        CrawlRulePageSuffProcBO crawlRulePageSuffProcBO = (CrawlRulePageSuffProcBO) JSONObject
                                .toBean(pageSuffprocObj,
                                        CrawlRulePageSuffProcBO.class);
                        try
                        {
                            crawlRuleService.saveCrawlRulePageSuffProcBO(
                                    crawlRulePageSuffProcBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleService.modifyCrawlRulePageSuffProcBO(
                                    crawlRulePageSuffProcBO);
                        }
                    }
                    pageObj.remove("crawlRulePageSuffProcBos");
                }

                LoggerUtil.debugTrace(loggerName,
                        crawlRuleJSON.getString("ruleName") + "页码配置 发布成功！");

                CrawlRulePageBO crawlRulePageBO = (CrawlRulePageBO) JSONObject
                        .toBean(pageObj, CrawlRulePageBO.class);
                try
                {
                    crawlRuleService.saveCrawlRulePageBO(crawlRulePageBO);
                }
                catch (Exception e)
                {
                    crawlRuleService.modifyCrawlRulePageBO(crawlRulePageBO);
                }
                crawlRuleJSON.remove("crawlRulePageBo");
                //规则
                CrawlRuleBO crawlRuleBO = (CrawlRuleBO) JSONObject
                        .toBean(crawlRuleJSON, CrawlRuleBO.class);
                try
                {
                    crawlRuleService.initCrawlRule(crawlRuleBO);
                    crawlRuleService.modifyCrawlRule(crawlRuleBO);
                }
                catch (Exception e)
                {
                    crawlRuleService.modifyCrawlRule(crawlRuleBO);
                }
                LoggerUtil.debugTrace(loggerName,
                        "规则" + crawlRuleJSON.getString("ruleName") + "发布成功！");
            }


            //页面规则
            JSONArray crawlRuleDetailArray = datasourceParamJSON
                    .getJSONArray("crawlRuleDetailBos");
            for (Object crawlRuleDetailObj : crawlRuleDetailArray)
            {
                JSONObject crawlRuleDetailJSON = (JSONObject) crawlRuleDetailObj;

                //前置处理器
                JSONArray detailPreProcArray = crawlRuleDetailJSON
                        .containsKey("crawlRuleDetailPreProcBos")
                        ?crawlRuleDetailJSON.getJSONArray("crawlRuleDetailPreProcBos")
                        :null;
                if (detailPreProcArray != null)
                {
                    for (Object detailPreProcObj : detailPreProcArray)
                    {
                        JSONObject detailPreProcJSON = (JSONObject) detailPreProcObj;
                        CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO = (CrawlRuleDetailPreProcBO) JSONObject
                                .toBean(detailPreProcJSON,
                                        CrawlRuleDetailPreProcBO.class);
                        try
                        {
                            crawlRuleDetailService
                                    .saveCrawlRuleDetailPreProcBO(
                                            crawlRuleDetailPreProcBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleDetailService
                                    .modifyCrawlRuleDetailPreProcBO(
                                            crawlRuleDetailPreProcBO);
                        }
                    }
                    crawlRuleDetailJSON.remove("crawlRuleDetailPreProcBos");
                }

                //列表解析
                JSONArray detailParseArray = crawlRuleDetailJSON
                        .containsKey("crawlRuleDetailParseBos")
                        ?crawlRuleDetailJSON.getJSONArray("crawlRuleDetailParseBos")
                        :null;
                if (detailParseArray != null)
                {
                    for (Object object : detailParseArray)
                    {
                        JSONObject detailParseObj = (JSONObject) object;
                        CrawlRuleDetailParseBO crawlRuleDetailParseBO = (CrawlRuleDetailParseBO) JSONObject
                                .toBean(detailParseObj,
                                        CrawlRuleDetailParseBO.class);
                        try
                        {
                            crawlRuleDetailService
                                    .saveCrawlRuleDetailParseBo(
                                            crawlRuleDetailParseBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleDetailService
                                    .modifyCrawlRuleDetailParseBo(
                                            crawlRuleDetailParseBO);
                        }
                    }
                    crawlRuleDetailJSON.remove("crawlRuleDetailParseBos");
                }

                //后置处理器
                JSONArray detailSuffProcArray = crawlRuleDetailJSON
                        .containsKey("crawlRuleDetailSuffProcBos")
                        ?crawlRuleDetailJSON.getJSONArray("crawlRuleDetailSuffProcBos")
                        :null;
                if (detailSuffProcArray != null)
                {
                    for (Object detailSuffProcObj : detailSuffProcArray)
                    {
                        JSONObject detailSuffProcJSON = (JSONObject) detailSuffProcObj;
                        CrawlRuleDetailSuffProcBO crawlRuleDetailSuffProcBO = (CrawlRuleDetailSuffProcBO) JSONObject
                                .toBean(detailSuffProcJSON,
                                        CrawlRuleDetailSuffProcBO.class);
                        try
                        {
                            crawlRuleDetailService
                                    .saveCrawlRuleDetailSuffProcBO(
                                            crawlRuleDetailSuffProcBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleDetailService
                                    .modifyCrawlRuleDetailSuffProcBO(
                                            crawlRuleDetailSuffProcBO);
                        }
                    }
                    crawlRuleDetailJSON.remove("crawlRuleDetailSuffProcBos");
                }
                //字段
                JSONArray detailFieldArray = crawlRuleDetailJSON
                        .getJSONArray("crawlRuleDetailFieldBos");
                for (Object detailFieldObj : detailFieldArray)
                {
                    JSONObject detailFieldJSON = (JSONObject) detailFieldObj;

                    //字段的解析
                    JSONArray detailFieldParseArray = detailFieldJSON
                            .getJSONArray("crawlRuleDetailFieldParseBos");
                    for (Object detailFieldParseObj : detailFieldParseArray)
                    {
                        JSONObject detailFieldParseJSON = (JSONObject) detailFieldParseObj;
                        CrawlRuleDetailFieldParseBO crawlRuleDetailFieldParseBO = (CrawlRuleDetailFieldParseBO) JSONObject
                                .toBean(detailFieldParseJSON,
                                        CrawlRuleDetailFieldParseBO.class);
                        try
                        {
                            crawlRuleDetailService
                                    .saveCrawlRuleDetailFieldParseBO(
                                            crawlRuleDetailFieldParseBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleDetailService
                                    .modifyCrawlRuleDetailFieldParseBO(
                                            crawlRuleDetailFieldParseBO);
                        }
                    }
                    detailFieldJSON.remove("crawlRuleDetailFieldParseBos");

                    //字段的后置处理器
                    JSONArray detailFieldSuffProcArray = detailFieldJSON
                            .getJSONArray("crawlRuleDetailFieldSuffProcBOs");
                    for (Object detailFieldSuffProcObj : detailFieldSuffProcArray)
                    {
                        JSONObject detailFieldSuffProcJSON = (JSONObject) detailFieldSuffProcObj;
                        CrawlRuleDetailFieldSuffProcBO crawlRuleDetailFieldSuffProcBO = (CrawlRuleDetailFieldSuffProcBO) JSONObject
                                .toBean(detailFieldSuffProcJSON,
                                        CrawlRuleDetailFieldSuffProcBO.class);
                        try
                        {
                            crawlRuleDetailService
                                    .saveCrawlRuleDetailFieldSuffProcBO(
                                            crawlRuleDetailFieldSuffProcBO);
                        }
                        catch (Exception e)
                        {
                            crawlRuleDetailService
                                    .modifyCrawlRuleDetailFieldSuffProcBO(
                                            crawlRuleDetailFieldSuffProcBO);
                        }
                    }
                    detailFieldJSON.remove("crawlRuleDetailFieldSuffProcBOs");

                    CrawlRuleDetailFieldBO crawlRuleDetailFieldBO = (CrawlRuleDetailFieldBO) JSONObject
                            .toBean(detailFieldJSON,
                                    CrawlRuleDetailFieldBO.class);
                    try
                    {
                        crawlRuleDetailService.saveCrawlRuleDetailFieldBO(
                                crawlRuleDetailFieldBO);
                    }
                    catch (Exception e)
                    {
                        crawlRuleDetailService.modifyCrawlRuleDetailFieldBO(
                                crawlRuleDetailFieldBO);
                    }
                }
                crawlRuleDetailJSON.remove("crawlRuleDetailFieldBos");



                //页面规则
                CrawlRuleDetailBO crawlRuleDetailBO = (CrawlRuleDetailBO) JSONObject
                        .toBean(crawlRuleDetailJSON,
                                CrawlRuleDetailBO.class);
                try
                {
                    crawlRuleDetailService
                            .saveCrawlRuleDetailBo(crawlRuleDetailBO);
                }
                catch (Exception e)
                {
                    crawlRuleDetailService
                            .modifyCrawlRuleDetailBO(crawlRuleDetailBO);
                }

            }

        }
        catch (Exception e)
        {
            LoggerUtil.errorTrace(loggerName, e.getMessage(), e);
        }

        return resultMap;
    }

}
