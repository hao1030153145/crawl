package com.transing.crawl.crawlParse;

import com.google.gson.JsonObject;
import com.jeeframework.core.context.support.SpringContextHolder;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.encrypt.MD5Util;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.biz.service.*;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.util.DateUtil;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.WebUtil;
import com.transing.crawl.util.processor.ProcessorUtil;
import com.transing.crawl.web.po.CrawlRuleDetailFieldPo;
import com.transing.crawl.web.po.CrawlRulePagePo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;

/**
 * 包: com.transing.crawl.crawlParse
 * 源文件:CrawlTaskParseJob.java
 * 解析类
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月03日
 *
 *
 * 自定义参数包括：
1、内置参数：以“_”下划线为参数标示，包括_crawlTime_,_URL_,_projectId_
2、抓取结果：以“$”为参数标示，包括当前规则能够解析到的字段。
3、输入参数：以“@”为参数标示，包括该数据源类型下所有的输入参数。
 */
public class CrawlTaskParseJob
{
    private static String loggerName="crawlTaskParseJob";

    private CrawlRuleService crawlRuleService;

    private long datasourceTypeId;

    private CrawlRuleDetailService crawlRuleDetailService;

    private StorageTypeService storageTypeService;

    private StorageTypeFieldService storageTypeFieldService;

    private CrawlRuleBO crawlRuleBO;

    private CrawlRulePagePo crawlRulePagePo;

    private List<CrawlRuleDetailBO> crawlRuleDetailBOs;

    private CrawlTaskBO crawlTaskBO;

    private String flowDetailId;

    private String projectId;

    private String storageTypeTable;

    private CrawlSubTaskBO crawlSubTaskBO;

    private Map<Integer,CrawlRuleAnalysisTypeBO> parseTypes=new HashMap<Integer, CrawlRuleAnalysisTypeBO>();

    private final Map<String,StorageTypeFieldTypeBO> fieldTypes=new HashMap<String,StorageTypeFieldTypeBO>();

    //内置参数
    private JSONObject fixedValues=JSONObject.fromObject("{}");


    public CrawlTaskParseJob(CrawlTaskBO crawlTaskBO,String flowDetailId,String projectId,CrawlSubTaskBO crawlSubTaskBO)
    {
        this.crawlTaskBO=crawlTaskBO;
        this.datasourceTypeId=crawlTaskBO.getDatasourceTypeId();
        this.flowDetailId=flowDetailId;
        this.projectId=projectId;
        this.crawlSubTaskBO=crawlSubTaskBO;
        initJob();
    }
    private void initJob()
    {
            crawlRuleService = SpringContextHolder.getBean("crawlRuleService");
            crawlRuleDetailService = SpringContextHolder
                    .getBean("crawlRuleDetailService");
            storageTypeService=SpringContextHolder.getBean("storageTypeService");
            storageTypeFieldService=SpringContextHolder.getBean("storageTypeFieldService");

            crawlRuleBO = crawlRuleService
                    .getCrawlRuleBOByDatasourceTypeId(datasourceTypeId);

            crawlRulePagePo=crawlRuleService.getCrawlRulePagePO(crawlRuleBO.getId());

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("ruleId", crawlRuleBO.getId());
            crawlRuleDetailBOs=crawlRuleDetailService.getCrawlRuleDetailList(param);
            List<CrawlRuleAnalysisTypeBO> crawlRuleAnalysisTypeBos=crawlRuleService.getCrawlRuleAnalysisTypList();
            for (CrawlRuleAnalysisTypeBO crawlRuleAnalysisTypeBO:crawlRuleAnalysisTypeBos){
                parseTypes.put(crawlRuleAnalysisTypeBO.getId(),crawlRuleAnalysisTypeBO);
            }
            storageTypeTable=storageTypeService.getStorageTypeByDatasourceTypeId(datasourceTypeId).getStorageTypeTable();

            List<StorageTypeFieldTypeBO> storageTypeFieldTypeBOs=storageTypeFieldService.getStorageTypeFieldTypeList();
            for (StorageTypeFieldTypeBO storageTypeFieldTypeBO:storageTypeFieldTypeBOs){
                fieldTypes.put(storageTypeFieldTypeBO.getId()+"",storageTypeFieldTypeBO);
            }

            //添加 抓取的URL。
            String paramValue=crawlSubTaskBO.getParamValue();
            JSONObject paramJson=JSONObject.fromObject(paramValue);
            Iterator<String> iterator=paramJson.keySet().iterator();
            while (iterator.hasNext()){
                String key=iterator.next();
                fixedValues.put("$$"+key+"$$",paramJson.getString(key));
            }
    }

    /**
     * 解析入口
     * @param htmlContent
     */
    public Map<String,Object> doJob(String htmlContent,List<String> exceptionMarks,String crawlUrl,
            Map<String,String> headMap,SiteProxyIp siteProxyIp) throws  Exception{
            fixedValues.put("_URL_",crawlUrl);
            fixedValues.put("_crawlTime_", DateUtil.formatDate(new Date()));
            fixedValues.put("_projectId_",projectId);

            LoggerUtil.debugTrace(loggerName, "进入doJob crawlUrl:" + crawlUrl);
        LoggerUtil.debugTrace(loggerName, "进入doJob fixedValues:" + JSONObject.fromObject(fixedValues));

            LoggerUtil.debugTrace(htmlContent);

            String taskJSONParam=crawlTaskBO.getJsonParam();
            JSONObject taskParamJSON=JSONObject.fromObject(taskJSONParam);
            String firstDetailId=taskParamJSON.containsKey("firstDetailId")?taskParamJSON.getString("firstDetailId"):"";
            LoggerUtil.debugTrace(loggerName, "进入doJob firstDetailId:" + firstDetailId);

            checkHtmlException(htmlContent, exceptionMarks);

            ProcessorUtil processorUtil=new ProcessorUtil();
            boolean isList=false;

                //最终的解析结果值
                JSONArray array=JSONArray.fromObject("[]");
                boolean flage=false;
                for (CrawlRuleDetailBO crawlRuleDetailBO:crawlRuleDetailBOs){
                    //前置处理器
                    Map<String,Object> preParamMap=new HashMap<String, Object>();
                    preParamMap.put("detailId",crawlRuleDetailBO.getId());
                    List<CrawlRuleDetailPreProcBO> crawlRuleDetailPreProcBOList=crawlRuleDetailService
                            .getCrawlRuleDetailPreProcBO(preParamMap);
                    if(crawlRuleDetailPreProcBOList!=null&&crawlRuleDetailPreProcBOList.size()>0){
                        JSONObject paramJSON=JSONObject.fromObject("{}");
                        paramJSON.put(ProcessorUtil.URL,crawlUrl);
                        paramJSON.put(ProcessorUtil.HOST,siteProxyIp==null?"":siteProxyIp.getHost());
                        paramJSON.put(ProcessorUtil.PORT,siteProxyIp==null?0:siteProxyIp.getPort());
                        paramJSON.put(ProcessorUtil.HEADMAP,headMap==null?null:JSONObject.fromObject(headMap).toString());
                        for (CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO:crawlRuleDetailPreProcBOList){
                            htmlContent=processorUtil.startRunProcessor(crawlRuleDetailPreProcBO.getProcessorId(),
                                    htmlContent,paramJSON,crawlRuleDetailPreProcBO.getProcessorValue());
                        }
                    }

                    LoggerUtil.debugTrace(loggerName, "进入doJob pageType:" + crawlRuleDetailBO.getPageType());

                    checkHtmlException(htmlContent, exceptionMarks);

                    //列表 pageType==1 ---> 列表
                    //    pageType==2 ----> 内容
                    List<String> procArrayList=new ArrayList<String>();
                    ParseUtil parseUtil=new ParseUtil();
                    if(crawlRuleDetailBO.getPageType()==1){
                        isList=true;
                        Map<String,Object> param=new HashMap<String, Object>();
                        param.put("detailId",crawlRuleDetailBO.getId());
                        List<CrawlRuleDetailParseBO> crawlRuleDetailParseBOList=crawlRuleDetailService
                                .getCrawlRuleDetailParseBo(param);

                        List<CrawlRuleDetailSuffProcBO> crawlRuleDetailSuffProcBOList=crawlRuleDetailService
                                .getCrawlRuleDetailSuffProcBos(preParamMap);


                        procArrayList= parseUtil.listParse(JSONArray.fromObject(crawlRuleDetailParseBOList),JSONArray.fromObject(crawlRuleDetailSuffProcBOList),
                                procArrayList,htmlContent,parseTypes,processorUtil,null);

                    }else{
                        procArrayList.add(htmlContent);
                    }


                    //字段
                    List<CrawlRuleDetailFieldPo> crawlRuleDetailFieldPOList=crawlRuleDetailService
                            .getCrawlRuleDetailFieldPos(preParamMap,datasourceTypeId);
                    if (procArrayList!=null&&procArrayList.size()>0){

                        for (String listParseResult:procArrayList){
                            JSONObject jsonObject=JSONObject.fromObject("{}");
                            Map<Long, Object> fields = new HashMap<>();
                            for (CrawlRuleDetailFieldPo crawlRuleDetailFieldPo:crawlRuleDetailFieldPOList){
                                try
                                {
                                    if(crawlRuleDetailFieldPo.getFieldParseBOs().size()<1&&crawlRuleDetailFieldPo.getFieldSuffProcBOs().size()<1)
                                        continue;
                                    JSONObject fieldJSON=JSONObject.fromObject("{}");
                                    StorageTypeFieldBO storageTypeFieldBO = crawlRuleDetailFieldPo
                                            .getStorageTypeFieldBO();
                                    fieldJSON.put("fieldParse",JSONArray.fromObject(crawlRuleDetailFieldPo.getFieldParseBOs()));
                                    fieldJSON.put("fieldSuffProc",JSONArray.fromObject(crawlRuleDetailFieldPo.getFieldSuffProcBOs()));
                                    fieldJSON.put("storageTypeFieldBo",JSONObject.fromObject(storageTypeFieldBO));

                                    Object parseResultStr= parseUtil.parseOneField(fieldJSON,listParseResult,parseTypes,processorUtil,fieldTypes,fixedValues);

                                    //将字段放置到结果集中
                                    String vale="";

                                    if (parseResultStr!=null)
                                    {
                                        //将字段装换成指定格式

                                        jsonObject.put(storageTypeFieldBO
                                                .getFieldEnName(), parseResultStr);
                                        fields.put(storageTypeFieldBO.getId(), parseResultStr);  //存放解析字段的id和解析数据

                                        if(parseResultStr instanceof Date){
                                            vale =DateUtil.formatDate(
                                                    (Date) parseResultStr);
                                        }else{
                                            vale=parseResultStr.toString().replaceAll("([\r\t\n]+)", "")
                                                    .replaceAll("[\"\']","");
                                            vale=!Validate.isEmpty(vale)?StringEscapeUtils.unescapeHtml(vale):"";
                                        }
                                    }else{
                                        //日志输出
                                        if(crawlRuleDetailFieldPo.getFieldParseBOs().size()>0||
                                                crawlRuleDetailFieldPo.getFieldSuffProcBOs().size()>0)
                                        {
                                            LoggerUtil.errorTrace(loggerName,
                                                    String.format(
                                                            "任务编号：%d,解析字段 %d (%s) 错误，结果为：%s",
                                                            crawlSubTaskBO
                                                                    .getId(),
                                                            storageTypeFieldBO
                                                                    .getId(),
                                                            storageTypeFieldBO
                                                                    .getFieldEnName(),
                                                            parseResultStr));
                                        }
                                    }
                                    /**
                                     * 添加参数与对应的值到内置处理器
                                     */

                                    fixedValues.put("$" + storageTypeFieldBO.getFieldEnName() + "$",vale);
                                }catch (Exception e){
                                    LoggerUtil.errorTrace(loggerName,"任务："+crawlSubTaskBO.getId()+e.getMessage(),e);
                                }
                            }
                            if(jsonObject.size()>=crawlRuleDetailFieldPOList.size()*0.5){
                                flage=true;
                            }

                            if(jsonObject.size()>0){
                                if(taskParamJSON.containsKey("batchNo")){
                                    jsonObject.put("batchNo",taskParamJSON.getString("batchNo"));
                                }
                                //添加dpm所需要的业务中转参数
                                StringBuilder md5sr= new StringBuilder(firstDetailId);
                                if(taskParamJSON.containsKey(WebUtil.TRAVEL_PARAMS)){
                                    String tranvelParams=taskParamJSON.getString(WebUtil.TRAVEL_PARAMS);
                                    try{
                                        JSONObject tranvelParamJSON=JSONObject.fromObject(tranvelParams);
                                        Iterator<String> iterator=tranvelParamJSON.keySet().iterator();
                                        while (iterator.hasNext()){
                                            String key=iterator.next();
                                            if(!jsonObject.containsKey(key)){
                                                jsonObject.put(key,tranvelParamJSON.get(key));
                                                md5sr.append(tranvelParamJSON.getString(key));
                                            }
                                        }
                                    }catch (Exception e){e.printStackTrace();}
                                }

                                if(jsonObject.containsKey("url")){
                                    String url=jsonObject.getString("url");
                                    md5sr.append(url);
                                }

                                Map<String, Object> param = new HashMap<>();
                                param.put("detailId", crawlRuleDetailBO.getId());

                                List<CrawlRuleDetailFieldBO> crawlRuleDetailFieldBOList = crawlRuleDetailService.getCrawlRuleDetailFieldBos(param);

                                int size = crawlRuleDetailFieldBOList.size();

                                if (size == 0) {
                                    jsonObject.put("uniqueValue", md5sr.toString());
                                } else {
                                    for (int i = 0; i < crawlRuleDetailFieldBOList.size(); i++) {
                                        CrawlRuleDetailFieldBO aCrawlRuleDetailFieldBO = crawlRuleDetailFieldBOList.get(i);
                                        //0-是去重标准, 1-不是去重标准
                                        if (aCrawlRuleDetailFieldBO.getIsUnique() != 0) continue;

                                        long fieldId = aCrawlRuleDetailFieldBO.getStorageTypeFieldId();

                                        for (Map.Entry<Long, Object> entry : fields.entrySet()) {
                                            if (entry.getKey() == fieldId) {
                                                md5sr.append(entry.getValue());
                                            }
                                        }
                                    }

                                    if (md5sr.length() > 1) {
                                        jsonObject.put("uniqueValue", MD5Util.encrypt(md5sr.toString()));
                                    }
                                }

                                LoggerUtil.debugTrace(loggerName, "进入doJob uniqueValue:" + jsonObject.get("uniqueValue"));

                                jsonObject.put("projectID",Long.parseLong(projectId));
                                jsonObject.put("detailId",flowDetailId);

                                array.add(jsonObject);
                            }
                        }
                    }
                    //解析结果值大于字段值总数的50% 停止规则解析。
                    if(flage){
                        break;
                    }
                }

            Map<String,Object> result=new HashMap<String, Object>();
            result.put("count",0);
            LoggerUtil.debugTrace("======================"+array.toString());
            if(array.size()>0&&!Validate.isEmpty(storageTypeTable)){
                String coprusInterfaceURI="/addDataInSearcher.json";
                Map<String,String> postParam=new HashMap<String, String>();
                postParam.put("dataType",storageTypeTable);
                postParam.put("dataJSON",array.toString());
                Object object=WebUtil.callRemoteService(WebUtil.getCorpusServerByEnv()+coprusInterfaceURI,"post",postParam);
                LoggerUtil.infoTrace(loggerName, "进入doJob corpus 数据:" + array.toString());
                String resultRoll=array.toString();
                try{
                    JSONObject resultJson=JSONObject.fromObject(object);

                    if(!isList)
                    {
                        resultRoll = resultJson.getString("dataList");
                    }else{
                        resultRoll = resultJson.getString("newDataList");
                    }
                    result.replace("count",JSONArray.fromObject(resultRoll).size());
                }catch (Exception e){
                    e.printStackTrace();
                    LoggerUtil.errorTrace(loggerName,"to corpus:"+e.getMessage(),e);
                }
                // dpmbs回调
           /*     LoggerUtil.infoTrace(loggerName,"dpmbs:"+resultRoll);*/
                remoteDpmbs(resultRoll,result,taskParamJSON);
            }
            return result;
    }

    /**
     * 异常检查
     */
    public void checkHtmlException(String htmlContent,
            List<String> exceptionMarks) throws Exception
    {
        String exception= ParseUtil
                .checkHtmlException(exceptionMarks,htmlContent);
        if(!Validate.isEmpty(exception)){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("mark",exception);
            jsonObject.put("html",htmlContent);
            throw new Exception("connectionException:"+jsonObject.toString());
        }
    }

    /**
     * 下一页的页码处理
     * @param currentPage 默认1
     * @param htmlContent
     */
    public String nextPageNum(String currentPage,String htmlContent,Map<String, String> crawlMap){
        try
        {
            //TODO 是否需要前置处理器
            CrawlRulePageBO crawlRulePageBO = crawlRulePagePo
                    .getCrawlRulePageBO();
            int calculation = crawlRulePageBO.getCalculation();

            LoggerUtil.debugTrace("=====nextPage: "+JSONObject.fromObject(crawlMap).toString());

            Long curentPageI=Long.parseLong(currentPage);
            //判定是否包含结束标示符。
            String endIdentifier = crawlRulePageBO.getEndPageIdentifier();
            if(endIdentifier.indexOf("{")>-1)
                endIdentifier = endIdentifier.replaceAll("\\{","\\\\{")
                        .replaceAll("}","\\\\}");
            if (!Validate.isEmpty(endIdentifier)&&htmlContent.indexOf(endIdentifier)>-1)
                return null;
            else if (!Validate.isEmpty(endIdentifier)&&htmlContent.matches(".*" + endIdentifier + ".*"))
            {
                return null;
            }

            if(crawlMap.containsKey("page")&&calculation==1){

                int stepLength = crawlRulePageBO.getStepLength();

                if(stepLength==0)
                    return null;

                //输入参数的时候
                String crawlPage=crawlMap.get("page");

                String [] pages=null;
                if(crawlPage.indexOf("page_")>-1){
                    pages=crawlPage.substring(crawlPage.indexOf("page_")+5).split("-");
                }else{
                    pages=crawlPage.split("-");
                }
                Integer startPage=Integer.parseInt(pages[0]);//1
                Integer endpage=Integer.parseInt(pages[1]);//3

                if (stepLength>1)
                    endpage=endpage*stepLength;

                if (curentPageI<startPage)
                    return startPage+"";
                else if(curentPageI>=startPage&&curentPageI<endpage){
                    Long nextpage=curentPageI+stepLength;
                    if (nextpage>endpage)
                        return null;
                    else
                        return nextpage+"";
                }else
                    return null;

            }else
            {
                List<String> result = null;

                ProcessorUtil processorUtil = new ProcessorUtil();
                JSONObject paramJSON = JSONObject.fromObject("{}");
                if (calculation == 2 || calculation == 3 || calculation == 4)
                {
                    List<CrawlRulePageParseBO> pageParseBOs = crawlRulePagePo
                            .getCrawlRulePageParseBO();
                    String pageResult = "";

                    for (CrawlRulePageParseBO crawlRulePageParseBO : pageParseBOs)
                    {
                        result = ParseUtil.parseValue(htmlContent,
                                crawlRulePageParseBO.getParseExpression(),
                                parseTypes.get(crawlRulePageParseBO
                                        .getParseType()).getParseName(),
                                fixedValues);
                        if (result == null || result.size() < 1)
                            continue;
                        else
                            pageResult = result.get(0);
                    }

                    List<CrawlRulePageSuffProcBO> pageSuffProcBOs = crawlRulePagePo
                            .getCrawlRulePageSuffProcBOs();

                    for (CrawlRulePageSuffProcBO crawlRulePageSuffProcBO : pageSuffProcBOs)
                    {
                        pageResult = processorUtil.startRunProcessor(
                                crawlRulePageSuffProcBO.getProcessorId(),
                                pageResult, paramJSON,
                                crawlRulePageSuffProcBO.getProcessorValue());

                    }
                    LoggerUtil.infoTrace(loggerName,"页码解析结果："+pageResult);
                   /* if (pageResult.matches("\\d+"))
                    {*/
                        if (calculation == 2)
                        { //总页码
                            if (curentPageI < Integer.parseInt(pageResult))
                                return (curentPageI + 1) + "";
                            else
                                return null;
                        }
                        else
                            if (calculation == 3)
                            {//总条数
                                int pageSize = crawlRulePageBO.getPageSize();
                                double numCount = Double
                                        .parseDouble(pageResult);
                                double count = Math.ceil(numCount / pageSize);
                                if (curentPageI < count)
                                    return (curentPageI + 1) + "";
                                else
                                    return null;
                            }
                            else
                            {//下一页
                                int maxnum=crawlRulePageBO.getMaxPage();
                                if (maxnum!=0&&Long.parseLong(pageResult) < Long.parseLong(maxnum+""))
                                    return pageResult;
                                else if(maxnum == 0)
                                    return pageResult;
                                else
                                    return null;
                            }
                    /*}
                    return null;*/
                }
                else
                {
                    //批量替换
                    int formatType = crawlRulePageBO.getFormatType();
                    String format = crawlRulePageBO.getFomat();
                    if(crawlRulePageBO.getStepLength()==0)
                        return null;

                    if (formatType == 2)
                    {
                        //格式为日期
                        return null;
                    }
                    else
                        if (formatType == 3)
                        {
                            //格式为字母
                            return null;
                        }
                        else
                        {
                            //格式为数字
                            int startPage = crawlRulePageBO.getStartPage();//开始页
                            int endPage = crawlRulePageBO.getEndPage();//结束页
                            int stepLength = crawlRulePageBO.getStepLength();//步长
                            if (Integer.parseInt(currentPage) >= endPage)
                            {
                                return null;
                            }
                            else
                            {
                                int currentP = Validate.isEmpty(currentPage) ||
                                        currentPage.equals("0") ?
                                        startPage :
                                        Integer.parseInt(currentPage);
                                int current = currentP + stepLength;
                                String curPage = "";
                                if ((current + "").length() < format.length())
                                {
                                    for (int i = 0; i < (format.length() -
                                            (current + "").length()); i++)
                                    {
                                        curPage += "0";
                                    }
                                }
                                String page = curPage + current;
                                if (Integer.parseInt(page)>10){
                                    String pages = null;
                                    if (page.startsWith("0")){
                                        pages = page.substring(1);
                                    }
                                    return pages;
                               }else {
                                    return page;
                                }

                            }
                        }
                }
            }
        }catch (Exception e){
            LoggerUtil.errorTrace(loggerName,"页码解析错误："+e.getMessage(),e);
            return null;
        }
    }

    /**
     * 调用dpm
     * 返回解析的数据量
     */
    private void remoteDpmbs(String resultRoll,Map result,JSONObject taskParamJSON){
        try{
            Map<String,String> postMap=new HashMap<String,String>();
            postMap.put("detailId",flowDetailId);
            postMap.put("projectId",projectId);
            postMap.put("batchNo",taskParamJSON.containsKey("batchNo")?taskParamJSON.getString("batchNo"):"");
            postMap.put("flowId",taskParamJSON.getString("flowId"));
            postMap.put("progress",(crawlTaskBO.getCompleteSubTaskNum()/crawlTaskBO.getSubTaskNum())*100+"");
            postMap.put("status","1");
            postMap.put("errorMessage",crawlTaskBO.getErrorMsg());
            postMap.put("dataJsonArray",resultRoll);
            postMap.put("num",result.get("count").toString());
            postMap.put("workFlowId",taskParamJSON.containsKey("workFlowId")?taskParamJSON.getString("workFlowId"):"0");
            postMap.put("travelParams",taskParamJSON.containsKey(WebUtil.TRAVEL_PARAMS)?taskParamJSON.getString(WebUtil.TRAVEL_PARAMS):"");
            String dpmURLCallBack="/acceptCallback.json";
            WebUtil.callRemoteService(WebUtil.getDpmbsServerByEnv()+dpmURLCallBack,"post",postMap);
            LoggerUtil.debugTrace(loggerName,"rollback dpm:"+JSONObject.fromObject(postMap));
        }catch (Exception e){
        }
    }

    public static void main(String[] args)
    {
        Long nextpage=2L;
        String t=nextpage+"".substring(0,(nextpage+"").indexOf("."));
        System.out.println(t);
    }
}
