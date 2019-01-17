package com.transing.crawl.web.controller;

import com.jeeframework.jeetask.startup.JeeTaskClient;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.json.JSONUtils;
import com.jeeframework.util.validate.Validate;
import com.jeeframework.webframework.exception.SystemCode;
import com.jeeframework.webframework.exception.WebException;
import com.transing.crawl.biz.service.CrawlInputParamService;
import com.transing.crawl.biz.service.CrawlRuleService;
import com.transing.crawl.biz.service.CrawlTaskService;
import com.transing.crawl.biz.service.DatasourceService;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.job.impl.CrawlSubTask;
import com.transing.crawl.util.CrawlInputTypeUtil;
import com.transing.crawl.util.ExcelUtil;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.WebUtil;
import com.transing.crawl.web.exception.MySystemCode;
import com.transing.crawl.web.filter.CrawlTaskFilter;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:CrawlTaskController.java
 * 任务的controller
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月29日
 */
@Controller
@Api(value = "抓取任务",position = 2)
@RequestMapping(value = "/crawlTask")
public class CrawlTaskController implements BaseReadExcelController
{

    private static String loggerName="crawlTask";

    private final static String JOB_CLAZZ="com.transing.crawl.job.SubTaskJob";

    @Resource
    private CrawlTaskService crawlTaskService;

    @Resource
    private CrawlRuleService crawlRuleService;

    @Resource
    private CrawlInputParamService crawlInputParamService;

    @Resource
    private DatasourceService datasourceService;

    @Resource
    private JeeTaskClient jeeTaskClient;


    private static final String  SplitChar="┃";

    public static final String keywordSplitChar="#@#";



    @RequestMapping(value = "/stopCrawl.json",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "停止抓取任务",position = 1)
    public Map<String,Object> executeCrawlTask(@RequestParam(value = "detailId",required = true)String detailId,
                                               HttpServletRequest request){

        if (Validate.isEmpty(detailId)){
            throw new WebException(SystemCode.SYS_REQUEST_EXCEPTION);
        }
        List<CrawlTaskBO> crawlTaskBOs = crawlTaskService.getCrawlTaskInfoByDetailId(detailId);
        for (CrawlTaskBO crawlTaskBO:crawlTaskBOs){
            Map<String,Object> param = new HashMap<>();
            param.put("id",crawlTaskBO.getId());
            param.put("taskStatus",4);
            crawlTaskService.modifyCrawlTaskInfoBO(param);
            param.clear();
            param.put("projectId",Integer.parseInt(crawlTaskBO.getProjectId()));
            param.put("task",crawlTaskBO.getId());
            param.put("detailId",detailId);
            List<CrawlSubTask> crawlSubTasks=crawlTaskService.getCrawlSubTask(param);
            for (CrawlSubTask crawlSubTask:crawlSubTasks){
                jeeTaskClient.stopTask(crawlSubTask.getId());
            }
            LoggerUtil.debugTrace("==============暂停任务："+crawlTaskBO.getId());
        }

        Map<String,Object> resultMap=new HashMap<String,Object>();

        return resultMap;
    }


    /**
     * {
     "crawlFreq": "单次抓取",
     "datasourceName": "凤凰",
     "datasourceTypeName": "凤凰咨询",
     "inputParams": "关键词暴雨,湖南\n",
     "jsonParam": {
     "crawlFreqType": "1",
     "datasourceId": "8",
     "datasourceName": "凤凰",
     "datasourceTypeId": "6",
     "datasourceTypeName": "凤凰咨询",
     "inputParamArray": [
     {
     "id": "11",
     "isRequired": "0",
     "paramCnName": "关键词",
     "paramEnName": "keyword",
     "paramValue": "暴雨,湖南",
     "prompt": "请输入关键词",
     "restrictions": " ",
     "styleCode": "input"
     }
     ],
     "quartzTime": "",
     "status": 0,
     "storageTypeTable": "ifeng_news",
     "taskName": "凤凰咨询"
     },
     "status": 0,
     "taskName": "凤凰咨询"
     }
     */
    @RequestMapping(value = "/executeCrawl.json",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "抓取任务的创建",position = 1)
    public Map<String,Object> executeCrawlTask(
            @RequestParam(value = "jsonParam",required = true) String jsonParam,
            @RequestParam(value = "flowId",required = true)String flowId,
            @RequestParam(value = "flowDetailId",required = true)String flowDetailId,
            @RequestParam(value = "firstDetailId",required = true) String firstDetailId,
            @RequestParam(value = "typeNo",required = true)String typeNo,
            @RequestParam(value = "paramType",required = true)String paramType,
            @RequestParam(value = "projectId",required = true)String projectId,
            @RequestParam(value = "workFlowId",required = true)String workFlowId,
            @RequestParam(value = "batchNo",required = false)String batchNo,
            @RequestParam (value = "travelParams",required = false)@ApiParam(value = "系统中转参数 JSON",required = false) String travelParams,
            HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap<String,Object>();
        try
        {
            if (!JSONUtils.isJSONValid(jsonParam))
            {
                throw new WebException(MySystemCode.SYS_REQUEST_EXCEPTION);
            }
            JSONObject param=JSONObject.fromObject(jsonParam);

            LoggerUtil.infoTrace(loggerName,"======任务参数: "+ jsonParam + ", flowId: " + flowId +
                    ", flowDetailId: " + flowDetailId + ", firstDetailId: " + firstDetailId + ", typeNo: " + typeNo
                    + ", paramType: " + paramType + ", projectId: " + projectId + ", workFlowId: " + workFlowId
                    + ", batchNo" + batchNo + "travelParams:"+travelParams);

            JSONObject taskParamJSON=param.getJSONObject("jsonParam");

            String taskName= taskParamJSON.getString("taskName");
            String datasourceId= taskParamJSON.getString("datasourceId");

            Datasource datasource=datasourceService.getDataSourceById(Long.parseLong(datasourceId));
            if(datasource.getStatus()==0){
                throw new WebException(MySystemCode.BIZ_TASK_EXCEPTION);
            }
            /**
             * 循环查看输入参数是否空的
             */
            JSONArray inputParamArrays=taskParamJSON.getJSONArray("inputParamArray");
            int canUseparams=0;
            for (Object obj:inputParamArrays){
                JSONArray array= (JSONArray) obj;
                for(Object json:array)
                {
                    JSONObject inputVal = (JSONObject) json;
                    String inputValue = inputVal.containsKey("paramValue")?inputVal.getString("paramValue") :"";
                    inputValue = inputValue.replaceAll(",", "").trim();
                    if (!Validate.isEmpty(inputValue))
                    {
                        canUseparams++;
                    }
                }
            }
            if(canUseparams==0)
            {
                //如果所有的任务都不符合任务组成标准
                //则通知工作流，标示为 已完成
                Map<String,String> params=new HashMap<String, String>();
                params.put("detailId",flowDetailId);
                params.put("projectId",projectId);
                params.put("finishNum",inputParamArrays.size()+"");
                params.put("resultNum","0");
                params.put("workFlowId",workFlowId);
                params.put("travelParams",travelParams);
                String dpmURLCallBack = "/updateResultNumAndFinishNum.json";
                WebUtil.callRemoteService(
                        WebUtil.getDpmbsServerByEnv() + dpmURLCallBack, "post",
                        params);
                //throw new WebException(MySystemCode.BIZ_TASK_EXCEPTION);
                return resultMap;
            }

            String datasourceTypeId = taskParamJSON.getString("datasourceTypeId");
            CrawlTaskBO crawlTaskBO=new CrawlTaskBO();
            crawlTaskBO.setDatasourceId(Long.parseLong(datasourceId));
            crawlTaskBO.setDatasourceTypeId(Long.parseLong(datasourceTypeId));
            crawlTaskBO.setTaskName(taskName);
            crawlTaskBO.setCompleteSubTaskNum(0);
            crawlTaskBO.setSubTaskNum(0);
            crawlTaskBO.setTaskStatus(0);
            crawlTaskBO.setDetailId(flowDetailId);
            param.put("flowId",flowId);
            param.put("flowDetailId",flowDetailId);
            param.put("typeNo",typeNo);
            param.put("paramType",paramType);
            param.put("projectId",projectId);
            param.put("firstDetailId",firstDetailId);
            param.put("workFlowId",workFlowId);
            param.put("batchNo",batchNo);
            //dpm 需要的业务参数
            try{
                if(!Validate.isEmpty(travelParams)){
                    if (!param.containsKey(WebUtil.TRAVEL_PARAMS)){
                        param.put(WebUtil.TRAVEL_PARAMS,travelParams);
                    }else{
                        param.replace(WebUtil.TRAVEL_PARAMS,travelParams);
                    }
                }
            }catch (Exception e){e.printStackTrace();}
            LoggerUtil.infoTrace("==crawlTaskController: "+param );
            crawlTaskBO.setProjectId(projectId);
            crawlTaskBO.setJsonParam(param.toString());
            crawlTaskService.saveCrawlTaskInfoBo(crawlTaskBO);
            //分解参数

            /**
             * 查询数据源下的规则
             */
            CrawlRuleBO crawlRuleBO=crawlRuleService.getCrawlRuleBOByDatasourceTypeId(crawlTaskBO.getDatasourceTypeId());
            Map<String,Object> filter=new HashMap<String, Object>();
            filter.put("ruleId",crawlRuleBO.getId());
            /**
             * 规则的请求参数
             * crawlRuleRequestParam 装所有的请求参数的处理。
             */
            List<CrawlRuleRequestParamBO> crawlRuleRequestParamBOList=crawlRuleService.getCrawlRuleRequestParamList(filter);
            Map<String,JSONObject> crawlRuleRequestParam=new HashMap<String, JSONObject>();

            if(crawlRuleRequestParamBOList !=null){
                JSONObject requestParamObj=null;
                for (CrawlRuleRequestParamBO crawlRuleRequestParamBO:crawlRuleRequestParamBOList){
                    requestParamObj=JSONObject.fromObject(crawlRuleRequestParamBO);

                    filter.clear();
                    filter.put("requestParamId",crawlRuleRequestParamBO.getId());
                    List<CrawlRuleRequestParamSufferProcBO> crawlRuleRequestParamSufferProcBOList=
                            crawlRuleService.getCrawlRuleRequestParamSuffProcList(filter);
                    requestParamObj.put("requestParamSuffprocBO",crawlRuleRequestParamSufferProcBOList);
                    crawlRuleRequestParam.put(crawlRuleRequestParamBO.getParamName(),requestParamObj);
                }
            }


            List<Map<String,String>> paramList=new ArrayList<Map<String, String>>();

            JSONArray inputParamArray=taskParamJSON.getJSONArray("inputParamArray");
            CrawlInputTypeUtil crawlInputTypeUtil=new CrawlInputTypeUtil();

            String pageValues="";
            String crawlUrl=crawlRuleBO.getCrawlUrl();

            Map<Long,CrawlInputBO> crawlInputBOMap=crawlInputParamService.getAllCrawlInputBo(crawlTaskBO.getDatasourceTypeId());

            //传递的任务的总数
            int taskCount=inputParamArray.size();
            //装载所有源输入的输入参数
            JSONObject oldAllInput=JSONObject.fromObject("{}");
            //如果出现同一个输入参数在两个不同的地方引用
            //这个时候在使用关键词的时候应该当一条任务。
            Map<String,String[]> valueEqualKey=new HashMap<String,String[]>();

            for(Object inputParamObj:inputParamArray){
                JSONArray array= (JSONArray) inputParamObj;


                Map<String,String> paramMapJ=new HashMap<String, String>();
                for (Object json:array){
                    JSONObject inputParamJSON= (JSONObject) json;

                    long inputParamId=inputParamJSON.getLong("id");
                    CrawlInputBO crawlInputBO=crawlInputBOMap.get(inputParamId);
                    inputParamJSON.put("styleCode",crawlInputBO.getStyleCode());

                if(inputParamJSON.containsKey("paramValue"))
                {
                    if (Validate.isEmpty(inputParamJSON.getString("paramValue"))
                            ||inputParamJSON.getString("paramValue").equalsIgnoreCase("null"))
                        break;
                    oldAllInput.put(crawlInputBO.getParamEnName(),
                            inputParamJSON.getString("paramValue"));
                }

                    /**
                     * 输入参数的类型判定
                     */

                    Map<String,String> inputMap=crawlInputTypeUtil.InputParamSplit(crawlInputBO,inputParamJSON,request.getRealPath("/"),this,crawlRuleRequestParam);
                    if(inputMap!=null&&inputMap.size()>0){
                        Iterator<String> inputKey=inputMap.keySet().iterator();
                        while (inputKey.hasNext()){
                            String key=inputKey.next();
                            LoggerUtil.debugTrace("=======key="+key+";value="+inputMap.get(key));
                            //替换模板输出的值
                            String value=inputMap.get(key);
                            if(!Validate.isEmpty(value)&&!Validate.isEmpty(key)&&!key.equalsIgnoreCase("null")){
                                if(value.indexOf("\\n")>0){
                                    String[] replaceUrlArray=value.split("\\\\n");
                                    String replaceUrls=replaceUrlArray[0];
                                    crawlUrl=crawlUrl.replaceAll("@"+key+"@",replaceUrls);
                                    value=replaceUrlArray.length==2?replaceUrlArray[1]:"";
                                }
                                if(key.equalsIgnoreCase("page")){
                                    paramMapJ.put(key, "\\$page\\$");
                                    pageValues=value;
                                }else
                                {
                                    paramMapJ.put(key, value);
                                }
                            }

                            //同一个输入参数对应多个请求参数
                            if(inputMap.size()>1)
                            {
                                String[] values = value.split(keywordSplitChar);
                                valueEqualKey.put(key,values);
                            }
                        }
                    }
                }
                paramList.add(paramMapJ);
            }

            int subTaskCount=0;

            for (Map<String,String> paramMap:paramList)
            {
                Iterator<String> iterator = paramMap.keySet().iterator();
                LinkedList<String[]> linkedList = new LinkedList<String[]>();
                String[] keys = new String[paramMap.size()];
                int count = 0;
                while (iterator.hasNext())
                {
                    String key = iterator.next();
                    keys[count] = key;
                    String value = paramMap.get(key);
                    String[] values = value.split(keywordSplitChar);
                    LoggerUtil.infoTrace(loggerName,"数据源："+datasourceId+"=="+value);
                    linkedList.add(count, values);
                    count++;
                }
                List<String> descartResult = new ArrayList<String>();
                LoggerUtil.infoTrace(loggerName,"======descartResult: " + linkedList.size());
                descart(linkedList, descartResult, 0, "", keys);

                //创建子任务
                CrawlSubTaskBO crawlSubTaskBO = null;
                LoggerUtil.infoTrace(loggerName,"任务id"+crawlTaskBO.getId()+" : 任务数："+taskCount +"< " +descartResult.size()+" = "+(taskCount<descartResult.size()));
                //读取最大的任务数
                if(taskCount<descartResult.size())
                    taskCount=descartResult.size();
                LoggerUtil.infoTrace(loggerName,"任务id"+crawlTaskBO.getId()+" : 任务数："+taskCount );

                for (String result : descartResult)
                {
                    LoggerUtil.infoTrace(loggerName,"数据源："+datasourceId+":"+result);
                    String[] resultArry = result.split(SplitChar);
                    String taskURl = crawlUrl;
                    JSONObject paramJSON = JSONObject.fromObject("{}");
                    for (String str : resultArry)
                    {
                        if (str.split("@").length > 1)
                        {
                            String key = str.split("@")[0];
                            String value = str.split("@")[1];
                            paramJSON.put(key, value);
                        }
                    }

                    if(!decideKeyValue(paramJSON,valueEqualKey)){
                        continue;
                    }

                    /**
                     * 获取内部变量或者输入值
                     */
                    JSONObject SubtaskParamJSON = JSONObject.fromObject("{}");

                    for (CrawlRuleRequestParamBO requestParamBO : crawlRuleRequestParamBOList)
                    {
                        String paramName = requestParamBO.getParamName();
                        LoggerUtil.debugTrace(loggerName,"数据源："+datasourceId+" == 参数："+paramName);
                        long paramTypes = requestParamBO.getParamType();
                        long inputType = requestParamBO.getInputType();
                        String valueSub = paramJSON.containsKey(paramName) ?
                                paramJSON.getString(paramName) :
                                null;
                        LoggerUtil.debugTrace(loggerName,"数据源："+datasourceId+" == 参数："+paramName+" value:"+valueSub);
                        if (paramTypes == 1)
                        {//url
                            if (inputType == 1)
                            {
                                //   1为输入值
                                String paramValue = requestParamBO
                                        .getParamValue();
                                taskURl = taskURl.replaceAll("@" + paramName + "@",paramValue);
                                SubtaskParamJSON.put(paramName, paramValue);
                            }
                            else
                                if (inputType == 2)
                                {
                                    //2为选择输入参数
                                    if (!Validate.isEmpty(valueSub))
                                    {
                                        if (paramName.equalsIgnoreCase("page"))
                                        {
                                            taskURl = taskURl.replaceAll("@" + paramName +"@",valueSub);
                                            SubtaskParamJSON.put(paramName, pageValues);
                                        }
                                        else
                                        {
                                            taskURl = taskURl.replaceAll("@" + paramName +"@",valueSub);
                                            SubtaskParamJSON.put(paramName, valueSub);
                                        }
                                    }
                                }
                                else
                                {
                                    //3为内部变量
                                    //将url中的内部变量的@page@ 替换成$page$并记录
                                    taskURl = taskURl.replaceAll("@" + paramName + "@","\\$" + paramName + "\\$");
                                    Map<String,Object> filterMap=new HashMap<String, Object>();
                                    filterMap.put("requestParamId",requestParamBO.getId());
                                    List<CrawlRuleRequestParamSufferProcBO> crawlRuleRequestParamSufferProcBOList=
                                            crawlRuleService.getCrawlRuleRequestParamSuffProcList(filterMap);
                                    if(crawlRuleRequestParamSufferProcBOList!=null&&crawlRuleRequestParamSufferProcBOList.size()>0){
                                        String content= ParseUtil.getSuffProcResult(valueSub,JSONArray.fromObject(crawlRuleRequestParamSufferProcBOList),SubtaskParamJSON);
                                        SubtaskParamJSON.put(paramName,"page_"+content);
                                    }
                                }
                        }
                        else
                        {
                            //form
                            if (inputType == 1)
                            {
                                //   1为输入值
                                String paramValue = requestParamBO
                                        .getParamValue();
                                SubtaskParamJSON.put(paramName, paramValue);
                            }
                            else
                                if (inputType == 2 &&
                                        !Validate.isEmpty(valueSub))
                                {
                                    //2为选择输入参数
                                    if (paramName.equalsIgnoreCase("page"))
                                    {
                                        SubtaskParamJSON.put(paramName, pageValues);
                                    }
                                    else
                                    {
                                        SubtaskParamJSON.put(paramName, valueSub);
                                    }
                                }
                                else if (inputType==3)
                                {
                                    //3为内部变量
                                    SubtaskParamJSON.put(paramName, "@" +requestParamBO.getParamValue() + "@");
                                }
                        }
                    }
                    //清除所有的@@
                    if (taskURl.indexOf("@") > 0)
                    {
                        taskURl = taskURl.replaceAll("@.*?@", "");
                    }
                    //将内部变量的表达式还原成@@
                    taskURl = taskURl.replaceAll("\\$", "@");

                    SubtaskParamJSON = filterOldCrawlInputParam(oldAllInput,
                            SubtaskParamJSON);

                    crawlSubTaskBO = new CrawlSubTaskBO();
                    crawlSubTaskBO.setParamValue(SubtaskParamJSON.toString());
                    crawlSubTaskBO.setTaskId(crawlTaskBO.getId());
                    crawlSubTaskBO.setCrawlDataNum(0);
                    crawlSubTaskBO.setCrawlUrl(taskURl);
                    crawlSubTaskBO.setTaskProgress(0);
                    crawlSubTaskBO.setTaskStatus(0);
                    crawlTaskService.saveCrawlSubTaskInfo(crawlSubTaskBO);
                    subTaskCount++;

                    //往任务调度上传任务
                    CrawlSubTask crawlSubTask = new CrawlSubTask();
                    crawlSubTask.setName(crawlTaskBO.getTaskName());
                    crawlSubTask.setParam(
                            JSONObject.fromObject(crawlSubTaskBO).toString());
                    crawlSubTask.setJobClass(JOB_CLAZZ);
                    crawlSubTask.setFlowdetailId(Long.parseLong(flowDetailId));
                    crawlSubTask.setProjectId(Long.parseLong(projectId));
                    crawlSubTask.setTaskId(crawlTaskBO.getId());
                    crawlSubTask.setDatasourceId(crawlTaskBO.getDatasourceId());

                    jeeTaskClient.submitTask(crawlSubTask);
                }
            }

            if(subTaskCount<1)
            {
                crawlTaskService.delCrawlTaskInfo(crawlTaskBO.getId());
                return resultMap;
            }else{
                int discardTaskNum=taskCount-subTaskCount;
                LoggerUtil.infoTrace(loggerName,"项目"+crawlTaskBO.getId()+",总数："+taskCount+" ,分解后任务数："+subTaskCount+",丢弃 "+discardTaskNum);
                crawlTaskBO.setSubTaskNum(subTaskCount);
                Map<String,Object> crawlTaskMap=new HashMap<String, Object>();
                crawlTaskMap.put("id",crawlTaskBO.getId());
                crawlTaskMap.put("discardNum",discardTaskNum);
                crawlTaskMap.put("subTaskNum",subTaskCount);
                crawlTaskService.modifyCrawlTaskInfoBO(crawlTaskMap);

                //通知dpm
                try{
                    String url="/updateTotalTaskNum.json";
                    Map<String,String> dpm=new HashMap<String, String>();
                    dpm.put("detailId",crawlTaskBO.getDetailId());
                    dpm.put("totalNum",taskCount+"");//（包含丢弃任务）
                    dpm.put("projectId",crawlTaskBO.getProjectId());
                    dpm.put("workFlowId",workFlowId);
                    dpm.put("travelParams",travelParams);
                    WebUtil.callRemoteService(WebUtil.getDpmbsServerByEnv()+url,"post",dpm);

                    //如果包含丢弃任务，先把丢弃的任务当成已完成任务通知dpm
                    if(discardTaskNum>0){
                        dpm.remove("totalNum");
                        dpm.put("finishNum",discardTaskNum+"");
                        dpm.put("resultNum","0");
                        dpm.put("workFlowId",workFlowId);
                        dpm.put("travelParams",travelParams);
                        String dpmURLCallBack = "/updateResultNumAndFinishNum.json";
                        WebUtil.callRemoteService(
                                WebUtil.getDpmbsServerByEnv() + dpmURLCallBack, "post",
                                dpm);
                    }

                }catch (Exception e){
                    LoggerUtil.errorTrace(loggerName,"rollback dpm is error :"+e.getMessage());
                }

            }

            //更新任务数已经状态
            Map<String,Object> crawlTaskMap=new HashMap<String, Object>();
            crawlTaskMap.put("id",crawlTaskBO.getId());
            crawlTaskMap.put("taskStatus",1);
            crawlTaskService.modifyCrawlTaskInfoBO(crawlTaskMap);

        }catch (Exception e){
            e.printStackTrace();
            LoggerUtil.errorTrace(loggerName,"projectId="+projectId+",detailId="+flowDetailId+" ;"+e.getMessage(),e);
            throw new WebException(SystemCode.SYS_REQUEST_EXCEPTION);
        }
        return resultMap;
    }

    @Override
    public List read2003Data(List<HSSFRow> list)
    {
        List<String> resultList=new ArrayList<String>();
        for (int rows=0;rows<list.size();rows++){
            HSSFRow row=list.get(rows);
            HSSFCell keywordCell=row.getCell(0);
            String keyword= ExcelUtil.getHssfValue(keywordCell);
            resultList.add(keyword);
        }
        return resultList;
    }

    @Override
    public List read2007Data(List<XSSFRow> list)
    {
        List<String> resultList=new ArrayList<String>();
        for (int rows=0;rows<list.size();rows++){
            XSSFRow xssfRow=list.get(rows);
            XSSFCell keywordXSSCell=xssfRow.getCell(0);
            String keyword=ExcelUtil.getXssfValue(keywordXSSCell);
            resultList.add(keyword);
        }
        return resultList;
    }

    /**
     * 在同一个输入参数对应多个请求参数的情况下
     * 判定每个请求参数是否正确
     * @return
     */
    private boolean decideKeyValue(JSONObject subTaskParamJSON,Map<String,String[]> valueEqualKey){
        if(subTaskParamJSON==null||valueEqualKey==null||valueEqualKey.size()==0)
            return true;
        if(valueEqualKey.size()>0){
            List<Integer> positionArray=new ArrayList<Integer>();
            Iterator<String> key=subTaskParamJSON.keySet().iterator();
            while (key.hasNext()){
                String keys=key.next();
                String value=subTaskParamJSON.getString(keys);
                if (valueEqualKey.containsKey(keys)){
                    String[] valueArray=valueEqualKey.get(keys);
                    for (int i=0;i<valueArray.length;i++){
                        if(valueArray[i].equals(value)){
                            positionArray.add(i);
                            break;
                        }
                    }
                }
            }
            if(positionArray.size()>0){
                for (int i=1;i<positionArray.size();i++){
                    if(positionArray.get(i-1)!=positionArray.get(i)){
                        return false;
                    }
                }
            }
        }
        return true;
    }



    /**
     * 笛卡尔 算法
     * a=[a,b];
     * b=[1,2];
     * result=a1,a2,b1,b2;
     * @param linkedList
     * @param result
     * @param layer
     * @param curstring
     * @param keyb
     */

    public static void descart(LinkedList<String[]> linkedList,List<String> result,int layer,String curstring,String [] keyb){
        if(linkedList.size()-1> layer){
            if(linkedList.get(layer).length==0){
                descart(linkedList,result,layer+1,curstring,keyb);
            }else{
                for (int i=0;i<linkedList.get(layer).length;i++){
                    StringBuilder s1 = new StringBuilder();
                    s1.append(keyb[layer]+"@"+curstring + SplitChar);
                    s1.append(keyb[layer]+"@"+linkedList.get(layer)[i]);
                    descart(linkedList,result,layer + 1,s1.toString(),keyb);
                }
            }
        }else if(linkedList.size()-1==layer){
            if (linkedList.get(layer).length == 0)
            {
                 //result.add(curstring);
            }else
            {
                for (int i = 0; i < linkedList.get(layer).length; i++)
                {
                    String rsu=curstring+SplitChar+keyb[layer]+"@"+linkedList.get(layer)[i];
                    String resu=rsu.substring(rsu.indexOf(SplitChar)+1);
                    result.add(resu);
                }
            }
        }
    }

    /**
     * 对输入参数的组装，目的是存储所有的输入参数(包括在组件taskUrl没有用到的输入参数)
     * @param oldInput
     * @param subTaskParam
     * @return
     */
    public JSONObject filterOldCrawlInputParam(JSONObject oldInput,JSONObject subTaskParam){
        try
        {
            Iterator<String> oldKeys = oldInput.keySet().iterator();
            Iterator<String> subTaskKeys = subTaskParam.keySet().iterator();
            Iterator<String> valueOld = oldInput.values().iterator();
            int position = -1;
            while (subTaskKeys.hasNext())
            {
                String key = subTaskKeys.next();
                String value = subTaskParam.getString(key);
                while (valueOld.hasNext())
                {
                    String oldValue = valueOld.next();
                    if (oldValue.indexOf(value) > -1)
                    {
                        String[] valu = oldValue.split(",");
                        for (int i = 0; i < valu.length; i++)
                        {
                            if (valu[i].indexOf(value) > -1)
                            {
                                position = i;
                                break;
                            }
                        }
                        break;
                    }
                }
            }

            while (oldKeys.hasNext())
            {
                String key = oldKeys.next();
                if (!subTaskParam.containsKey(key))
                {
                    String value = oldInput.getString(key);
                    if (position != -1)
                    {
                        String[] vals = value.split(",");
                        subTaskParam.put(key, vals[position]);
                    }
                    else
                    {
                        subTaskParam.put(key, value);
                    }
                }
            }
            return subTaskParam;
        }catch (Exception e){
            //LoggerUtil.errorTrace(loggerName,e.getMessage(),e);
            return subTaskParam;
        }
    }

    @RequestMapping(value = "/submitTwo.json",method = RequestMethod.GET)
    @ResponseBody
    public String crawlTaskSubmit(@RequestParam(value = "projectId",required = true)String projectId,
            @RequestParam(value = "status",required = true) String status,
            @RequestParam(value = "subTaskStatus",required = true) String subTaskStatus){

        CrawlTaskFilter filter=new CrawlTaskFilter();
        filter.setProject(projectId);
        filter.setStatus(status);
        List<CrawlTaskBO> crawlTaskBOs=crawlTaskService.getCrawlTaskListByFilter(filter);
        int i=0;
        for (CrawlTaskBO crawlTaskBO:crawlTaskBOs){
            Map<String,Object> param=new HashMap<String, Object>();
            param.put("status",Integer.parseInt(subTaskStatus));
            param.put("taskId",crawlTaskBO.getId());
           List<CrawlSubTaskBO> crawlSubTaskBOs=crawlTaskService.getCrawlSubTaskBos(param);
            for (CrawlSubTaskBO crawlSubTaskBO:crawlSubTaskBOs){
                try
                {
                    CrawlSubTask crawlSubTask = new CrawlSubTask();
                    crawlSubTask.setName(crawlTaskBO.getTaskName());
                    crawlSubTask.setParam(
                            JSONObject.fromObject(crawlSubTaskBO).toString());
                    crawlSubTask.setJobClass(JOB_CLAZZ);
                    crawlSubTask.setFlowdetailId(
                            Long.parseLong(crawlTaskBO.getDetailId()));
                    crawlSubTask.setProjectId(Long.parseLong(projectId));
                    crawlSubTask.setTaskId(crawlTaskBO.getId());
                    crawlSubTask.setDatasourceId(crawlTaskBO.getDatasourceId());
                    jeeTaskClient.submitTask(crawlSubTask);
                    i++;
                }catch (Exception e){e.printStackTrace();}
            }
        }
        return String.format("共计重新拉去%d条任务",i);
    }

    public static void main(String[] args)
    {
        String t="大众,@宝马,@te,st";
        String ts=t.replaceAll(",","#@#");
        System.out.println(ts);
    }
}
