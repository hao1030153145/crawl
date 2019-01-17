package com.transing.crawl.web.controller;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
//import com.jeeframework.util.httpclient.HttpClientHelper;
import com.transing.crawl.util.client.HttpClientHelper;
//import com.jeeframework.util.httpclient.HttpResponse;
import com.transing.crawl.util.client.HttpResponse;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.json.JSONUtils;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.biz.service.CrawlRuleService;
import com.transing.crawl.biz.service.StorageTypeFieldService;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.util.CharsetUtils;
import com.transing.crawl.util.DateUtil;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.WebUtil;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.client.HttpResponse;
import com.transing.crawl.util.processor.ProcessorUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.http.Header;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:CrawlRuleTestController.java
 * 规则的测试
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月04日
 */
@Controller
@Api(value = "规则的测试")
@RequestMapping(value = "/crawlRule")
public class CrawlRuleTestController
{
    private static String loggerName = "crawlRuleTest";

    @Resource
    private CrawlRuleService crawlRuleService;
    @Resource
    private StorageTypeFieldService storageTypeFieldService;



    @RequestMapping(value = "/testDetailRuleFieldProcessor.json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "字段的解析")
    public List<Map<String, Object>> crawlRuleFieldTest(
            @RequestParam(value = "testUrl", required = true) @ApiParam(value = "测试网址", required = true) String testUrl,
            @RequestParam(value = "param", required = true) @ApiParam(value = "规则请求参数名称与值", required = true) String param,
            @RequestParam(value = "headers", required = true) @ApiParam(value = "header", required = true) String headers,
            @RequestParam(value = "crawlMethod", required = true) @ApiParam(value = "抓取方式", required = true) String crawlMethod,
            @RequestParam(value = "requestEncoding", required = true) @ApiParam(value = "请求编码", required = true) String requestEncoding,
            @RequestParam(value = "responseEncoding", required = true) @ApiParam(value = "响应编码", required = true) String responseEncoding,
            @RequestParam(value = "detailPreProc", required = true) @ApiParam(value = "页面规则的前置处理器", required = true) String detailPreProc,
            @RequestParam(value = "detailParse", required = true) @ApiParam(value = "页面规则的解析", required = true) String detailParse,
            @RequestParam(value = "detailSuffProc", required = true) @ApiParam(value = "页面规则的后置处理器", required = true) String detailSuffProc,
            @RequestParam(value = "pageType", required = true) @ApiParam(value = "页面类型", required = true) String pageType,
            @RequestParam(value = "field", required = true) @ApiParam(value = "解析的字段", required = true) String field,    //比如标题 内容之类的, 是个 jsonArray
            @RequestParam(value = "datasourceId", required = false) @ApiParam(value = "数据源id", required = false) String datasourceId)
    {

        //可分别在表crawl_rule_detail_pre_proc, crawl_rule_detail_suff_proc, crawl_rule_detail_parse
        //查看数据 detailPreProc, detailParse, detailSuffProc 数据格式

        //页面规则的前置处理器
        JSONArray detailPreProcArray = JSONArray.fromObject(detailPreProc);
        //页面规则的解析
        JSONArray detailParseArray = JSONArray.fromObject(detailParse);
        //页面规则的后置处理器
        JSONArray detailSuffProcArray = JSONArray.fromObject(detailSuffProc);
        //页面规则的字段
        JSONArray jsonObject = JSONArray.fromObject(field);

        HttpClientHelper httpClientHelper = new HttpClientHelper();

        ProcessorUtil processorUtil = new ProcessorUtil();
        //根据header处理器组装map
        Map<String, String> headerMap = headerMapInstall(headers,
                processorUtil);
        //组合解析方式 XPATH, REGEX, JSON, 自定义参数
        Map<Integer, CrawlRuleAnalysisTypeBO> parseTypes = analysisTypeInstall();

        String[] cralwURL = testUrl.split("\n");
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        /**
         * 获取连接
         */
        SiteProxyIp siteProxyIp=null;
        String connectionId=null;
        //重试三次连接 crawlbs
        Map<String,String> connection=cellConnectionService(datasourceId);
        List<String> marks=new ArrayList<String>();
        if(connection!=null){
            String cookie=connection.get(WebUtil.CONNECTION_COOKIE);
            if(!Validate.isEmpty(cookie.trim())){
                headerMap.put("Cookie",cookie);
            }
            String host=connection.get(WebUtil.CONNECTION_HOST);
            String port=connection.get(WebUtil.CONNECTION_PORT);
            connectionId=connection.get(WebUtil.CONNECTION_ID);
            String mark=connection.get(WebUtil.CONNECTION_EXCEPTIONMARK);
            if(!Validate.isEmpty(mark)&& JSONUtils.isJSONValid(mark)){
                JSONArray jsonArray=JSONArray.fromObject(mark);
                marks=JSONArray.toList(jsonArray,new String(),new JsonConfig());
            }
            if(!Validate.isEmpty(host)&&port.matches("\\d+")){
                siteProxyIp=new SiteProxyIp(host,Integer.parseInt(port));
            }
        }
        LoggerUtil.debugTrace("获取连接成功："+connectionId);

        Map<String,StorageTypeFieldTypeBO> fieldTypes=new HashMap<String,StorageTypeFieldTypeBO>();
        //获取该数据源下的存储字段类型字段 例如 标题, url, 内容, 时间, 内容, 摘要... 的类型
        List<StorageTypeFieldTypeBO> storageTypeFieldTypeBOs=storageTypeFieldService.getStorageTypeFieldTypeList();
        for (StorageTypeFieldTypeBO storageTypeFieldTypeBO:storageTypeFieldTypeBOs){
            fieldTypes.put(storageTypeFieldTypeBO.getId()+"",storageTypeFieldTypeBO);
        }

        try
        {
            JSONObject fiexdJSON=null;
            Integer random = (int)((Math.random()*9+1)*10000);
            for (int i = 0; i < cralwURL.length; i++)
            {
                //根据设置的测试网址抓取数据,返回抓取页面content
                String content = crawlRoteServer(crawlMethod, httpClientHelper,
                        cralwURL[i], requestEncoding, responseEncoding,
                        headerMap,siteProxyIp,param);

                LoggerUtil.debugTrace(content);
                String exception=ParseUtil.checkHtmlException(marks,content);
                if(!Validate.isEmpty(exception)){
                   String exptonstatus=WebUtil.CONNECTION_EXCEPTION;
                    if(!Validate.isEmpty(connectionId)){
                        //crawlbs 连接池回收连接
                        WebUtil.doRollBackConnection(Long.parseLong(datasourceId),null,connectionId,
                                exptonstatus,content,exception);
                        LoggerUtil.debugTrace("====="+connectionId);
                        return null;
                    }
                }
                fiexdJSON=JSONObject.fromObject("{}");
                fiexdJSON.put("_URL_",cralwURL[i]);
                fiexdJSON.put("_crawlTime_", DateUtil.formatDate(new Date()));

                ParseUtil parseUtil = new ParseUtil();
                JSONObject paramJson=JSONObject.fromObject("{}");
                paramJson.put(ProcessorUtil.URL,cralwURL[i]);
                //页面规则的前置处理器 processor表, 反射处理页面内容
                content = parseUtil
                        .proprecParse(detailPreProcArray, processorUtil,
                                content,paramJson);

                //列表解析
                List<String> parseResult = new ArrayList<String>();
                if (pageType.equalsIgnoreCase("1"))
                {
                    //测试的列表解析
                    parseResult = parseUtil
                            .listParse(detailParseArray, detailSuffProcArray,
                                    parseResult, content, parseTypes,
                                    processorUtil,fiexdJSON);
                    if(parseResult==null){
                        parseResult = new ArrayList<String>();
                    }
                }
                else
                {
                    parseResult.add(content);
                }
                //按照列表的item循环
                for (int r = 0; r < parseResult.size(); r++) {
                    fiexdJSON.put("_projectId_", random + r);
                    //按照字段循环
                    Map<String, Object> itemMap = parseUtil
                            .fieldParse(jsonObject, parseResult.get(r), parseTypes,
                                    processorUtil,fieldTypes,fiexdJSON);
                    resultList.add(itemMap);
                }
            }
        }
        catch (Exception e)
        {
            LoggerUtil.errorTrace(loggerName, e.getMessage(), e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("error", e.getMessage());
            resultList.add(map);
        }finally
        {
            LoggerUtil.debugTrace("====="+connectionId);
            if(!Validate.isEmpty(connectionId)){
                WebUtil.doRollBackConnection(Long.parseLong(datasourceId),null,connectionId,WebUtil.CONNECTION_NORMAL,null,null);
            }
        }
        return resultList;
    }

    //和上面的接口 crawlRuleFieldTest(字段解析)类似,只是现在这个接口只测试前置处理器的解析,返回页面被前置处理器解析后的数据
    @RequestMapping(value = "/testDetailRulePreProcessor.json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "前置处理器的解析")
    public Map<String, Object> crawlRuleFieldTest(
            @RequestParam(value = "testUrl", required = true) @ApiParam(value = "测试网址", required = true) String testUrl,
            @RequestParam(value = "param", required = true) @ApiParam(value = "param", required = true) String param,
            @RequestParam(value = "headers", required = true) @ApiParam(value = "header", required = true) String headers,
            @RequestParam(value = "crawlMethod", required = true) @ApiParam(value = "抓取方式", required = true) String crawlMethod,
            @RequestParam(value = "requestEncoding", required = true) @ApiParam(value = "请求编码", required = true) String requestEncoding,
            @RequestParam(value = "responseEncoding", required = true) @ApiParam(value = "响应编码", required = true) String responseEncoding,
            @RequestParam(value = "detailPreProc", required = true) @ApiParam(value = "页面规则的前置处理器", required = true) String detailPreProc,
            @RequestParam(value = "datasourceId", required = false) @ApiParam(value = "数据源id", required = false) String datasourceId)
    {
        LoggerUtil.debugTrace("================开始测试前置处理器==========================");
        //页面规则的前置处理器
        JSONArray detailPreProcArray = JSONArray.fromObject(detailPreProc);

        HttpClientHelper httpClientHelper = new HttpClientHelper();

        ProcessorUtil processorUtil = new ProcessorUtil();
        Map<String, String> headerMap = headerMapInstall(headers,
                processorUtil);

        List<CrawlRuleAnalysisTypeBO> crawlRuleAnalysisTypeBos = crawlRuleService
                .getCrawlRuleAnalysisTypList();
        Map<Integer, CrawlRuleAnalysisTypeBO> parseTypes = new HashMap<Integer, CrawlRuleAnalysisTypeBO>();
        for (CrawlRuleAnalysisTypeBO crawlRuleAnalysisTypeBO : crawlRuleAnalysisTypeBos)
        {
            parseTypes.put(crawlRuleAnalysisTypeBO.getId(),
                    crawlRuleAnalysisTypeBO);
        }
        List<String> result = new ArrayList<String>();

        /**
         * 获取连接
         */
        SiteProxyIp siteProxyIp=null;
        String connectionId=null;
        Map<String,String> connection=cellConnectionService(datasourceId);
        if(connection!=null){
            String cookie=connection.get(WebUtil.CONNECTION_COOKIE);
            if(!Validate.isEmpty(cookie.trim())){
                headerMap.put("Cookie",cookie);
            }
            String host=connection.get(WebUtil.CONNECTION_HOST);
            String port=connection.get(WebUtil.CONNECTION_PORT);
            connectionId=connection.get(WebUtil.CONNECTION_ID);
            if(!Validate.isEmpty(host)&&port.matches("\\d+")){
                siteProxyIp=new SiteProxyIp(host,Integer.parseInt(port));
            }
        }



        String[] cralwURL = testUrl.split("\n");

        String content = "";
        try
        {
            for (int i = 0; i < cralwURL.length; i++)
            {

                content = crawlRoteServer(crawlMethod, httpClientHelper,
                        cralwURL[i], requestEncoding, responseEncoding,
                        headerMap,siteProxyIp,param);
                ParseUtil parseUtil = new ParseUtil();
                JSONObject paramJson=JSONObject.fromObject("{}");
                paramJson.put(ProcessorUtil.URL,cralwURL[i]);
                //页面规则的前置处理器
                content = parseUtil
                        .proprecParse(detailPreProcArray, processorUtil,
                                content,paramJson);

                if (!Validate.isEmpty(content))
                {
                    break;
                }
                //LoggerUtil.debugTrace("处理结果"+content);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("content", content);
            return resultMap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtil.errorTrace(loggerName, e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("error", e.getMessage());
            return resultMap;
        }finally
        {
            if(!Validate.isEmpty(connectionId)){
                LoggerUtil.debugTrace("================返回连接:数据源"+datasourceId+",连接connectionId:"+connectionId+"====================");
                WebUtil.doRollBackConnection(Long.parseLong(datasourceId),null,connectionId,WebUtil.CONNECTION_NORMAL,null,null);
            }
        }
    }

    @RequestMapping(value = "/testDetailRuleListProcessor.json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "列表的解析测试")
    public Map<String, Object> crawlRuleDetailRuleFieldProcessor(
            @RequestParam(value = "testUrl", required = false) @ApiParam(value = "测试网址", required = false) String testUrl,
            @RequestParam(value = "headParameters", required = false) @ApiParam(value = "头部参数", required = false) String headers,
            @RequestParam(value = "param", required = false) @ApiParam(value = "param", required = false) String param,
            @RequestParam(value = "crawlMethod", required = false) @ApiParam(value = "抓取方式", required = false) String crawlMethod,
            @RequestParam(value = "requestEncoding", required = false) @ApiParam(value = "请求编码", required = false) String requestEncoding,
            @RequestParam(value = "responseEncoding", required = false) @ApiParam(value = "响应编码", required = false) String responseEncoding,
            @RequestParam(value = "detailPreProc", required = false) @ApiParam(value = "前置处理器", required = false) String detailPreProc,
            @RequestParam(value = "detailParse", required = false) @ApiParam(value = "页面规则的解析", required = false) String detailParse,
            @RequestParam(value = "detailSuffProc", required = false) @ApiParam(value = "页面规则的后置处理器", required = false) String detailSuffProc,
            @RequestParam(value = "pageType", required = false) @ApiParam(value = "页面类型", required = false) String pageType,
            @RequestParam(value = "datasourceId", required = false) @ApiParam(value = "数据源id", required = false) String datasourceId)
    {


        if (Validate.isEmpty(detailSuffProc)){
            detailSuffProc = "[]";
        }
        if (Validate.isEmpty(param)){
            param = "";
        }
        if (Validate.isEmpty(detailPreProc)){
            detailPreProc = "[]";
        }
        if (Validate.isEmpty(detailParse)){
            detailParse = "[]";
        }
        if (Validate.isEmpty(headers)){
            headers="[]";
        }
        //前置处理器
        JSONArray detailPreProcArray=JSONArray.fromObject(detailPreProc);
        //页面规则的解析
        JSONArray detailParseArray = JSONArray.fromObject(detailParse);
        //页面规则的后置处理器
        JSONArray detailSuffProcArray = JSONArray.fromObject(detailSuffProc);

        HttpClientHelper httpClientHelper = new HttpClientHelper();

        ProcessorUtil processorUtil = new ProcessorUtil();
        Map<String, String> headerMap = headerMapInstall(headers,
                processorUtil);

        /**
         * 获取连接
         */
        SiteProxyIp siteProxyIp=null;
        String connectionId=null;
        Map<String,String> connection=cellConnectionService(datasourceId);
        if(connection!=null){
            String cookie=connection.get(WebUtil.CONNECTION_COOKIE);
            if(!Validate.isEmpty(cookie.trim())){
                headerMap.put("Cookie",cookie);
            }
            String host=connection.get(WebUtil.CONNECTION_HOST);
            String port=connection.get(WebUtil.CONNECTION_PORT);
            connectionId=connection.get(WebUtil.CONNECTION_ID);
            if(!Validate.isEmpty(host)&&port.matches("\\d+")){
                siteProxyIp=new SiteProxyIp(host,Integer.parseInt(port));
            }
        }


        List<CrawlRuleAnalysisTypeBO> crawlRuleAnalysisTypeBos = crawlRuleService
                .getCrawlRuleAnalysisTypList();
        Map<Integer, CrawlRuleAnalysisTypeBO> parseTypes = new HashMap<Integer, CrawlRuleAnalysisTypeBO>();
        for (CrawlRuleAnalysisTypeBO crawlRuleAnalysisTypeBO : crawlRuleAnalysisTypeBos)
        {
            parseTypes.put(crawlRuleAnalysisTypeBO.getId(),
                    crawlRuleAnalysisTypeBO);
        }

        String[] cralwURL = testUrl.split("\n");
        try
        {
            List<String> parseResult = new ArrayList<String>();
            JSONObject jsonObject=null;
            for (int i = 0; i < cralwURL.length; i++)
            {
                String content = crawlRoteServer(crawlMethod, httpClientHelper,
                        cralwURL[i], requestEncoding, responseEncoding,
                        headerMap,siteProxyIp,param);
                ParseUtil parseUtil = new ParseUtil();
                JSONObject paramJson=JSONObject.fromObject("{}");
                paramJson.put(ProcessorUtil.URL,cralwURL[i]);
                //页面规则的前置处理器
                content = parseUtil
                        .proprecParse(detailPreProcArray, processorUtil,
                                content,paramJson);

                jsonObject=JSONObject.fromObject("{}");
                jsonObject.put("_URL_",cralwURL[i]);
                //列表解析
                parseResult = parseUtil
                        .listParse(detailParseArray, detailSuffProcArray,
                                parseResult, content, parseTypes,
                                processorUtil,jsonObject);
                if (parseResult!=null&&parseResult.size() > 0)
                {
                    break;
                }
            }

            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("parseResult", parseResult==null?new ArrayList<String>():parseResult);
            return resultMap;
        }
        catch (Exception e)
        {
            LoggerUtil.errorTrace(loggerName, e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("error", e.getMessage());
            return resultMap;
        }finally
        {
            if(!Validate.isEmpty(connectionId)){
                WebUtil.doRollBackConnection(Long.parseLong(datasourceId),null,connectionId,WebUtil.CONNECTION_NORMAL,null,null);
            }
        }
    }

    /**
     * 规则的页码配置
     * @param testUrl
     * @param headers
     * @param crawlMethod
     * @param requestEncoding
     * @param responseEncoding
     * @param rulePage
     * @return
     */
    @RequestMapping(value = "/testCrawlRulePage.json",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> testCrawlRulePage(
            @RequestParam(value = "testUrl", required = true) @ApiParam(value = "测试网址", required = true) String testUrl,
            @RequestParam(value = "headers", required = true) @ApiParam(value = "header", required = true) String headers,
            @RequestParam(value = "param", required = true) @ApiParam(value = "param", required = true) String param,
            @RequestParam(value = "crawlMethod", required = true) @ApiParam(value = "抓取方式", required = true) String crawlMethod,
            @RequestParam(value = "requestEncoding", required = true) @ApiParam(value = "请求编码", required = true) String requestEncoding,
            @RequestParam(value = "responseEncoding", required = true) @ApiParam(value = "响应编码", required = true) String responseEncoding,
            @RequestParam(value = "rulePage", required = true) @ApiParam(value = "规则页码配置", required = true)String rulePage,
            @RequestParam(value = "datasourceId", required = false) @ApiParam(value = "数据源id", required = false) String datasourceId)
    {
        Map<String,Object> resultMap=new HashMap<String, Object>();
        ProcessorUtil processorUtil = new ProcessorUtil();
        Map<String, String> headerMap = headerMapInstall(headers,
                processorUtil);
        String[] cralwURL = testUrl.split("\n");

        Map<Integer,CrawlRuleAnalysisTypeBO> parseTypes=new HashMap<Integer,CrawlRuleAnalysisTypeBO>();
        List<CrawlRuleAnalysisTypeBO> crawlRuleAnalysisTypeBos=crawlRuleService.getCrawlRuleAnalysisTypList();
        for (CrawlRuleAnalysisTypeBO crawlRuleAnalysisTypeBO:crawlRuleAnalysisTypeBos){
            parseTypes.put(crawlRuleAnalysisTypeBO.getId(),crawlRuleAnalysisTypeBO);
        }

        JSONObject pageJSON=JSONObject.fromObject(rulePage);
        JSONArray pageParseArray=pageJSON.getJSONArray("parseList");
        JSONArray pageParseProcArray=pageJSON.getJSONArray("pageSuffProc");

        /**
         * 获取连接
         */
        SiteProxyIp siteProxyIp=null;
        String connectionId=null;
        Map<String,String> connection=cellConnectionService(datasourceId);
        if(connection!=null){
            String cookie=connection.get(WebUtil.CONNECTION_COOKIE);
            if(!Validate.isEmpty(cookie.trim())){
                headerMap.put("Cookie",cookie);
            }
            String host=connection.get(WebUtil.CONNECTION_HOST);
            String port=connection.get(WebUtil.CONNECTION_PORT);
            connectionId=connection.get(WebUtil.CONNECTION_ID);
            if(!Validate.isEmpty(host)&&port.matches("\\d+")){
                siteProxyIp=new SiteProxyIp(host,Integer.parseInt(port));
            }
        }


        try
        {
            HttpClientHelper httpClientHelper=new HttpClientHelper();
            for (int i = 0; i < cralwURL.length; i++)
            {

                String  htmlContent = crawlRoteServer(crawlMethod, httpClientHelper,
                        cralwURL[i], requestEncoding, responseEncoding,
                        headerMap,siteProxyIp,param);

                //参数可对照 crawl_rule_page 表, 计算方式（1，批量替换，2总页码，3总条数，4下一页）
                if (htmlContent.contains("function YunSuoAutoJump()")&&htmlContent.contains("function stringToHex(str)")){
                    htmlContent = charContent(cralwURL[i],httpClientHelper);
                }

                int calculation = Integer.parseInt(pageJSON.getString("calculation"));

                int curentPageI=1;
                //判定是否包含结束标示符。
                String endIdentifier = pageJSON.getString("endPageIdentifier");
                if (!Validate.isEmpty(endIdentifier)&&htmlContent.matches(".*" + endIdentifier + ".*"))
                {
                    resultMap.put("page",curentPageI);
                    return resultMap;
                }

                List<String> result=new ArrayList<String>();

                JSONObject paramJSON=JSONObject.fromObject("{}");
                paramJSON.put("_URL_",cralwURL[i]);
                if(calculation==2||calculation==3||calculation==4){
                    String pageResult="";
                    for (Object crawlRulePageParseBO:pageParseArray)
                    {
                        JSONObject jsonObject= (JSONObject) crawlRulePageParseBO;
                        if(jsonObject.containsKey("parseType")&&jsonObject.containsKey("parseExpression"))
                        {
                            String expression=jsonObject.getString("parseExpression");
                            Integer parseType=Integer.parseInt(jsonObject.getString("parseType"));
                            result = ParseUtil
                                    .parseValue(htmlContent,
                                            expression,
                                            parseTypes.get(parseType)
                                                    .getParseName(),null);
                            if (result == null || result.size() < 1)
                            {
                                continue;
                            }
                            else
                            {
                                pageResult = result.get(0);
                            }
                        }
                    }


                    for (Object proceObj : pageParseProcArray)
                    {
                        JSONObject pocJSOn= (JSONObject) proceObj;
                        if(pocJSOn.containsKey("processorId")&&pocJSOn.containsKey("processorValue"))
                        {
                            Integer processorId=Integer.parseInt(pocJSOn.getString("processorId"));
                            String processorValue=pocJSOn.getString("processorValue");
                            pageResult = processorUtil.startRunProcessor(
                                    processorId,
                                    pageResult, paramJSON,
                                    processorValue);
                        }
                    }
                    if(pageResult.matches("\\d+")){
                        if(calculation==2)
                        { //总页码
                            resultMap.put("page",pageResult);
                        }else if(calculation==3)
                        {//总条数
                            int pageSize=Integer.parseInt(pageJSON.getString("pageSize"));
                            double numCount=Double.parseDouble(pageResult);
                            double count=Math.ceil(numCount/pageSize);
                            resultMap.put("page",count);
                        }else
                        {//下一页
                            resultMap.put("page",pageResult);
                        }
                    }
                }
                if(result.size()>0){
                    break;
                }
            }
            return resultMap;
        } catch (Exception e)
        {
            LoggerUtil.errorTrace(loggerName, e.getMessage(), e);
            resultMap.put("page", e.getMessage());
            return resultMap;
        }finally
        {
            if(!Validate.isEmpty(connectionId)){
                WebUtil.doRollBackConnection(Long.parseLong(datasourceId),null,connectionId,WebUtil.CONNECTION_NORMAL,null,null);
            }
        }
    }

    /**
     * 根据header处理器组装map
     *
     * @param headers
     * @return
     */
    public Map<String, String> headerMapInstall(String headers,
            ProcessorUtil processorUtil)
    {
        Map<String, String> headMap = new HashMap<String, String>();
        JSONArray headerArray = JSONArray.fromObject(headers);

        if (headerArray.size() > 0)
        {
            for (Object obj : headerArray)
            {
                JSONObject jsonObject = (JSONObject) obj;
                String headName = jsonObject.containsKey("headerName") ?
                        jsonObject.getString("headerName") :
                        "";
                String values = jsonObject.containsKey("headerValue") ?
                        jsonObject.getString("headerValue") :
                        "";
                if (!Validate.isEmpty(headName) && !Validate.isEmpty(values))
                {
                    JSONArray headerSuffArray = null;
                    if(jsonObject.containsKey("reqHeaderSuffProcs")){
                        headerSuffArray= jsonObject.getJSONArray("reqHeaderSuffProcs");
                    }else{
                        headerSuffArray= jsonObject.getJSONArray("headerSufferProc");
                    }
                    for (Object suffObj : headerSuffArray)
                    {
                        JSONObject suffJSON = (JSONObject) suffObj;
                        String ProcessorId = suffJSON.getString("processorId");
                        String processorValue = suffJSON
                                .getString("processorValue");
                        values = processorUtil.startRunProcessor(
                                Integer.parseInt(ProcessorId), values,
                                JSONObject.fromObject("{}"), processorValue);
                    }
                    headMap.put(headName, values);
                }
            }
        }
        return headMap;
    }

    /**
     * 组合解析方式
     *
     * @return
     */
    private Map<Integer, CrawlRuleAnalysisTypeBO> analysisTypeInstall()
    {
        List<CrawlRuleAnalysisTypeBO> crawlRuleAnalysisTypeBos = crawlRuleService
                .getCrawlRuleAnalysisTypList();
        Map<Integer, CrawlRuleAnalysisTypeBO> parseTypes = new HashMap<Integer, CrawlRuleAnalysisTypeBO>();
        for (CrawlRuleAnalysisTypeBO crawlRuleAnalysisTypeBO : crawlRuleAnalysisTypeBos)
        {
            parseTypes.put(crawlRuleAnalysisTypeBO.getId(),
                    crawlRuleAnalysisTypeBO);
        }
        return parseTypes;
    }

    /**
     * 数据抓取
     *
     * @param crawlMethod
     * @param httpClientHelper
     * @param crawlURL
     * @param requestEncoding
     * @param responseEncoding
     * @param headerMap
     * @return
     * @throws Exception
     */
    public String crawlRoteServer(String crawlMethod,
            HttpClientHelper httpClientHelper,
            String crawlURL, String requestEncoding, String responseEncoding,
            Map<String, String> headerMap,SiteProxyIp siteProxyIp,String param)
            throws Exception
    {

        if (crawlMethod.equalsIgnoreCase("post"))
        {
            Map<String,String> postMap=new HashMap<String, String>();
            JSONObject jsonObject=JSONObject.fromObject(param);
            if(jsonObject.size()>0)
                postMap= (Map<String, String>) JSONObject
                        .toBean(jsonObject,new HashMap<String,String>(),new JsonConfig());

            LoggerUtil.debugTrace(loggerName,crawlMethod+"  url= "+crawlURL+", head= "+JSONObject.fromObject(headerMap).toString()+" " +
                    "|| param: "+JSONObject.fromObject(postMap).toString());
            HttpResponse response = httpClientHelper
                    .doPostAndRetBytes(crawlURL, postMap,requestEncoding, responseEncoding,
                            headerMap,siteProxyIp);
            return new String(response.getContentBytes(),responseEncoding);

        } else {
            HttpResponse response = httpClientHelper
                    .doGetAndRetBytes(crawlURL, requestEncoding, responseEncoding,
                            headerMap, siteProxyIp);
            CharsetUtils charsetUtils=new CharsetUtils();
            String content= charsetUtils.CharsetCheckEntrance(response.getContentBytes(), responseEncoding, response.getResponseEncode());
            if (content.contains("function YunSuoAutoJump()")&&content.contains("function stringToHex(str)")){
                Map<String, String> cookiess = Jsoup.connect(testScriptVariables(crawlURL)).execute().cookies();
                String security_session_mid_verify = cookiess.get("security_session_mid_verify");
                headerMap.put("Cookie","security_session_mid_verify="+security_session_mid_verify);
                HttpResponse responsess = httpClientHelper
                        .doGetAndRetBytes(crawlURL, requestEncoding, responseEncoding,
                                headerMap, siteProxyIp);
                String contents= charsetUtils.CharsetCheckEntrance(responsess.getContentBytes(), responseEncoding, response.getResponseEncode());
                System.out.println(contents);
                return contents;
            }

            return charsetUtils.CharsetCheckEntrance(response.getContentBytes(),responseEncoding,response.getResponseEncode());
        }
    }

    /**
     * 获取连接
     * @param datasourceId
     */
    public  Map<String,String> cellConnectionService(String datasourceId){
        if(!Validate.isEmpty(datasourceId)){
            Map<String,String> connection=null;
            int count=0;
            while (connection==null&&count<3){
                connection= WebUtil.doGetConnection(Long.parseLong(datasourceId),null);
                if(connection==null){
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){}
                }
                count++;
            }
            return connection;
        }
        return null;
    }

    public void getContent(String crawlURL,Map<String,String> headerMap ) throws Exception {
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        Map<String,String> headerMaps = new HashMap<>();
        HttpResponse responses = httpClientHelper
                .doGetAndRetBytes(crawlURL, "utf-8", "utf-8",
                        headerMaps, null);
        CharsetUtils charsetUtil=new CharsetUtils();
        String content= charsetUtil.CharsetCheckEntrance(responses.getContentBytes(), "utf-8", "utf-8");
        if (content.contains("function YunSuoAutoJump()")&&content.contains("function stringToHex(str)")){
            String variablesUrl = testScriptVariables(crawlURL);
            Map<String, String> cookiess = Jsoup.connect(variablesUrl).execute().cookies();
            String security_session_mid_verify = cookiess.get("security_session_mid_verify");
            String cfduid = cookiess.get("__cfduid");
            String yunsuo_session_verify = cookiess.get("yunsuo_session_verify");
            String srcUrl = testScriptSrcUrl();
            String cookies = "security_session_mid_verify="+security_session_mid_verify+";"+
                    "__cfduid="+cfduid+";"+"yunsuo_session_verify="+yunsuo_session_verify+";"+srcUrl+";";
            headerMap.put("Cookie",cookies);
            HttpResponse responsess = httpClientHelper
                    .doGetAndRetBytes(variablesUrl, "utf-8", "utf-8",
                            headerMap, null);
            String contents= charsetUtil.CharsetCheckEntrance(responsess.getContentBytes(), "utf-8", "utf-8");
            System.out.println(contents);
        }
    }
    //获取重定向链接
    public static String testScriptVariables(String url) throws Exception {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String res = null;
        try {
            String script = "function YunSuoAutoJump() {\n" +
                    "\t\t\t\tvar width = 1920;\n" +
                    "\t\t\t\tvar height = 1080;\n" +
                    "\t\t\t\tvar screendate = width + \",\" + height;\n" +
                    "\t\t\t\tvar val = \"\";\n" +
                    "\t\t\t\tfor (var i = 0; i < screendate.length; i++) {\n" +
                    "\t\t\t\t\tif (val == \"\") val = screendate.charCodeAt(i).toString(16);\n" +
                    "\t\t\t\t\telse val += screendate.charCodeAt(i).toString(16);\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\t return \""+url+"?security_verify_data=\" + val;\n" +
                    "\t\t\t}";
            engine.eval(script);
            Invocable inv2 = (Invocable)engine;
            res = (String)inv2.invokeFunction("YunSuoAutoJump");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    //获取重定向过后的srcurl
    public static String testScriptSrcUrl() throws Exception {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String res = null;
        try {
            String url = "http://www.bsgxw.gov.cn/list-14-1.html";
            String script = "function stringToHex(str) {\n" +
                    "\t\t\t\tvar val = \"\";\n" +
                    "\t\t\t\tfor (var i = 0; i < str.length; i++) {\n" +
                    "\t\t\t\t\tif (val == \"\") val = str.charCodeAt(i).toString(16);\n" +
                    "\t\t\t\t\telse val += str.charCodeAt(i).toString(16);\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\treturn val;\n" +
                    "\t\t\t}\n" +
                    "\t\t\tfunction YunSuoAutoJump() {\n" +
                    "\t\t\t\tvar curlocation = '"+url+"';\n" +
                    "\t\t\t\tvar srcurl = \"\";\n" +
                    "\t\t\t\tif ( - 1 == curlocation.indexOf(\"security_verify_\")) {\n" +
                    "\t\t\t\t\tvar zzz = stringToHex('"+url+"');\n" +
                    "\t\t\t\t\tsrcurl = \"srcurl=\"+zzz;\n" +
                    "\t\t\t\t}\n" +
                    "return srcurl;\n" +
                    "\t\t\t}";
            engine.eval(script);
            Invocable inv2 = (Invocable)engine;
            res = (String)inv2.invokeFunction("YunSuoAutoJump");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String charContent(String crawlURL,HttpClientHelper httpClientHelper) throws Exception{
            Map<String,String> headerMap = new HashMap<>();
            String variablesUrl = testScriptVariables(crawlURL);
            LoggerUtil.infoTrace(loggerName+"得到重定向链接"+variablesUrl);
            Map<String, String> cookiess = Jsoup.connect(variablesUrl).execute().cookies();
            String security_session_mid_verify = cookiess.get("security_session_mid_verify");
            String cfduid = cookiess.get("__cfduid");
            String yunsuo_session_verify = cookiess.get("yunsuo_session_verify");
            String srcUrl = testScriptSrcUrl();
            String cookies = "security_session_mid_verify="+security_session_mid_verify+";"+
                    "__cfduid="+cfduid+";"+"yunsuo_session_verify="+yunsuo_session_verify+";"+srcUrl+";";
            LoggerUtil.infoTrace(loggerName+"重定向过后生成的新的cookie"+variablesUrl);
            headerMap.put("Cookie",cookies);
            HttpResponse responses = httpClientHelper
                    .doGetAndRetBytes(variablesUrl, "utf-8", "utf-8",
                            headerMap, null);
            CharsetUtils charsetUtils = new CharsetUtils();
            String contents= charsetUtils.CharsetCheckEntrance(responses.getContentBytes(), "utf-8", "utf-8");
            LoggerUtil.infoTrace(loggerName+"新的网页内容"+variablesUrl);

            Map<String,String> mapCookie = new HashMap<>();
            mapCookie.put("Cookie","ecurity_session_mid_verify="+security_session_mid_verify);
            Connection.Response executex = Jsoup.connect(crawlURL).cookies(mapCookie).execute();
            LoggerUtil.infoTrace(loggerName+"使用jsonp方式获取网页内容"+executex.body());

            return contents;

    }

    public static void main(String[] args) throws Exception{
        CrawlRuleTestController crawlRuleTestController = new CrawlRuleTestController();
        Map<String,String> headerMaps = new HashMap<>();
        //headerMaps.put("Cookie","__cfduid=d802e39a4cc3cf10730fae8bde9ce34da1536050606; yjs_id=a5e25a0d9a0827e9521139f417eecc57; yunsuo_session_verify=096436fbc935c4dff90f8194a1bba61f; security_session_mid_verify=4eb90f3ca4d8ab936c9dd1cf96ad10ee; ctrl_time=1; Hm_lvt_6383856887607a690cb22bde40d51692=1536201331,1536207184,1536226088,1536304134; Hm_lpvt_6383856887607a690cb22bde40d51692=1536304134");
        headerMaps.put("Cookie","[]");
        crawlRuleTestController.getContent("http://www.bsgxw.gov.cn/list-14-1.html",headerMaps);
        try {
            //crawlRuleTestController.getContent("http://www.bsgxw.gov.cn/list-14-1.html",headerMaps);
        }catch (Exception e){
            LoggerUtil.infoTrace(loggerName, "页面源码2" + e);
        }

    }

}
