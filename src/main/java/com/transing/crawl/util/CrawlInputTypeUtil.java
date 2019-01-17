package com.transing.crawl.util;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.httpclient.HttpResponse;
import com.jeeframework.util.json.JSONUtils;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.integration.bo.CrawlInputBO;
import com.transing.crawl.util.processor.ProcessorUtil;
import com.transing.crawl.web.controller.BaseReadExcelController;
import com.transing.crawl.web.controller.CrawlTaskController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.util.task
 * 源文件:CrawlInputTypeUtil.java
 *  输入参数输入值
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年08月29日
 */
public class CrawlInputTypeUtil
{
     private final String interfaceString = "/dataCrawl/getUploadFile.json";

    /**
     * 根据控件来判定不同的值的解析
     * @param crawlInputBO
     * @param inputParamJSON
     * @param dpmbsdir
     * @param readExcelController
     * @param crawlRuleRequestParam
     * @return key:输入参数名
     */
    public Map<String,String> InputParamSplit(CrawlInputBO crawlInputBO,
            JSONObject inputParamJSON,String dpmbsdir,
            BaseReadExcelController readExcelController,
            Map<String,JSONObject> crawlRuleRequestParam){

        Map<String,String> paramMap=new HashMap<String, String>();
        String[] key=doFilterInRequestParamBos(crawlInputBO.getId(),crawlRuleRequestParam);
        LoggerUtil.infoTrace(this.getClass().getName(),"inputParamJSON="+inputParamJSON.toString());
        try
        {
            for (String keys:key){
                String styleCode=inputParamJSON.getString("styleCode");
                String paramValue_="";
                LoggerUtil.infoTrace(this.getClass().getName(),"styleCode="+styleCode);
                try
                {
                    //前置处理器 (参数模板处理器)
                    JSONObject requestParam=crawlRuleRequestParam.get(keys);
                    JSONArray crawlRequestParamSuffProcs=requestParam.containsKey("requestParamSuffprocBO")?requestParam.getJSONArray("requestParamSuffprocBO"):null;
                    if(crawlRequestParamSuffProcs!=null&&crawlRequestParamSuffProcs.size()>0){
                        for (Object priffix:crawlRequestParamSuffProcs){
                            JSONObject paramSuffpro= (JSONObject)priffix;
                            if(paramSuffpro.getInt("processorId")==7){
                                ProcessorUtil processorUtil = new ProcessorUtil();
                                //通过反射调取参数模板处理器  processor表中,
                                //比如com.transing.crawl.util.processor.impl.paramProcessors.RedirectProcessor 重定向处理器
                                paramValue_ = processorUtil.startRunProcessor(paramSuffpro.getInt("processorId"), "",
                                        JSONObject.fromObject("{}"), paramSuffpro.getString("processorValue"));
                                if(!Validate.isEmpty(paramValue_)){
                                    paramValue_+="\\n";
                                }
                                break;
                            }
                        }

                    }
                }catch (Exception ex){}

                if (styleCode.equalsIgnoreCase("input")||styleCode.equalsIgnoreCase("datetime")){
                    // 文本输入 、时间控件
                    String paramValueinput_=inputParamJSON.getString("paramValue");
                    paramValueinput_=paramValueinput_.replaceAll(",", CrawlTaskController.keywordSplitChar);
                    paramValue_+= doRequestParamSufferProc(paramValueinput_,keys,crawlInputBO.getId(),crawlRuleRequestParam);

                }else if(styleCode.equalsIgnoreCase("checkbox")){
                     // 选择框
                    if(inputParamJSON.containsKey("paramValue")){
                        String paramValueinput_=inputParamJSON.getString("paramValue");
                        paramValue_+= doRequestParamSufferProc(paramValueinput_,keys,crawlInputBO.getId(),crawlRuleRequestParam);
                    }else{
                        return null;
                    }
                }else if(styleCode.equalsIgnoreCase("file")){
                     // 上传文件
                    String filePath=inputParamJSON.getString("paramValue");
                    String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                    Map<String, String> mapParams = new HashMap<String, String>();
                    mapParams.put("url",filePath);
                    File file = downLoadDpmbsServerFile(interfaceString,dpmbsdir, fileName, mapParams);
                    List list = ExcelUtil.readExcelInfos(file, readExcelController);
                    if (list != null)
                    {
                        StringBuffer sbuffer = new StringBuffer("");
                        for (Object object : list)
                        {
                            sbuffer.append("," + object.toString());
                        }
                       String  paramValueinput_ = sbuffer.toString().substring(1);
                        paramValueinput_=paramValueinput_.replaceAll(",", CrawlTaskController.keywordSplitChar);
                        paramValue_+= doRequestParamSufferProc(paramValueinput_,keys,crawlInputBO.getId(),crawlRuleRequestParam);
                    }

                }else if(styleCode.equalsIgnoreCase("input-file")){

                    // 文件上传组合控件(输入与上传,例如keyword,可以手动输入,也可以下载文件)
                    String paramValues=inputParamJSON.getString("paramValue");
                    if(JSONUtils.isJSONValid(paramValues))
                    {

                        JSONObject paramValue = JSONObject.fromObject(paramValues);
                        String value = paramValue.containsKey("value") ?
                                paramValue.getString("value") :
                                "";
                        LoggerUtil.infoTrace("value="+value);

                        String filePath = paramValue.containsKey("file") ?
                                paramValue.getString("file") :
                                "";
                        if (!Validate.isEmpty(filePath))
                        {
                            String fileName = filePath
                                    .substring(filePath.lastIndexOf("/") + 1);
                            Map<String, String> mapParams = new HashMap<String, String>();
                            mapParams.put("url", filePath);
                            File file = downLoadDpmbsServerFile(interfaceString,
                                    dpmbsdir, fileName, mapParams);
                            List list = ExcelUtil
                                    .readExcelInfos(file, readExcelController);
                            if (list != null)
                            {
                                StringBuffer sbuffer = new StringBuffer("");
                                for (Object object : list)
                                {
                                    sbuffer.append("," + object.toString());
                                }
                                if (!Validate.isEmpty(value))
                                {
                                    value += sbuffer.toString();
                                }
                                else
                                {
                                    value = sbuffer.substring(1);
                                }
                            }
                        }
                        LoggerUtil.infoTrace("value="+value);
                        value=value.replaceAll(",", CrawlTaskController.keywordSplitChar);
                        paramValue_+= doRequestParamSufferProc(value,keys,crawlInputBO.getId(),crawlRuleRequestParam);
                    }else{
                        paramValue_+=doRequestParamSufferProc(paramValues,keys,crawlInputBO.getId(),crawlRuleRequestParam);
                    }

                }else if(styleCode.equalsIgnoreCase("doubleTime")){
                    /**
                     * 组合时间控件
                     */
                    String paramValues=inputParamJSON.getString("paramValue");

                    JSONObject requestParamdt=crawlRuleRequestParam.get(keys);
                    JSONArray crawlRequestParamSuffProcsdt=requestParamdt.getJSONArray("requestParamSuffprocBO");
                    if(crawlRequestParamSuffProcsdt!=null&&crawlRequestParamSuffProcsdt.size()>0){
                        JSONObject paramSuffpro= (JSONObject) crawlRequestParamSuffProcsdt.get(0);
                        ProcessorUtil processorUtil = new ProcessorUtil();
                        paramValue_ = processorUtil.startRunProcessor(paramSuffpro.getInt("processorId"), paramValues,
                                        JSONObject.fromObject("{}"), paramSuffpro.getString("processorValue"));
                    }
                }else if(styleCode.equalsIgnoreCase("page")){
                    String paramValues=inputParamJSON.getString("paramValue");

                    if(!Validate.isEmpty(keys))
                    {
                        JSONObject pageJson = JSONObject
                                .fromObject(paramValues);
                        int start = pageJson.getInt("start");
                        int end = pageJson.getInt("end");


                        //页码的输入参数明显是page的。
                       /* if(keys.equalsIgnoreCase("page"))
                            paramValue_ += start + "-" + end;
                        else{*/
                            //页码的输入参数名不是page的，主要是提现在post提交
                            paramValue_+="page_";
                            paramValue_ += start + "-" + end;
                        //}
                    }else{
                        return null;
                    }
                }
                StringBuffer parvalue=new StringBuffer();
                String [] values=paramValue_.split(",");
                for (String st:values){
                    if (!Validate.isEmpty(st)){
                        parvalue.append(st).append(",");
                    }
                }
                paramValue_=parvalue.toString().endsWith(",")?parvalue.toString().substring(0,parvalue.toString().lastIndexOf(",")):parvalue.toString();
                paramMap.put(keys,paramValue_);
            }
        }catch (Exception e){
            LoggerUtil.errorTrace(getClass().getName(),e.getMessage(),e);
        /*    String paramValue=inputParamJSON.getString("paramValue");
            paramMap.put(paramKey, paramValue);*/
        }
        return paramMap;
    }


    /**
     * 下载dpmbs的Excel
     * @param url
     * @return
     */
    private File downLoadDpmbsServerFile(String url,String dirFile,String fileName,Map<String,String> param)
    {
        try
        {
            String dpmbsURI = WebUtil.getDpmbsServerByEnv();
            HttpClientHelper httpClientHelper = new HttpClientHelper();
            LoggerUtil.debugTrace("文件下载路径+" + dpmbsURI + url);
            HttpResponse httpResponse = httpClientHelper
                    .doPostAndRetBytes(dpmbsURI + url,param, "utf-8", "utf-8",
                            null, null);
            byte[] bytes = httpResponse.getContentBytes();

            File file = new File(dirFile + fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        }
        catch (Exception e)
        {
            LoggerUtil.errorTrace(getClass().getName(), "下载文件：" + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 通过输入参数的id查找对应的请求参数名称
     * @param crawlInputBoId
     * @param crawlRuleRequestParam
     */
    public String[] doFilterInRequestParamBos(long crawlInputBoId,Map<String,JSONObject> crawlRuleRequestParam){
        Iterator<String> keys=crawlRuleRequestParam.keySet().iterator();
        String keyValue="";
        while (keys.hasNext()){
            String key=keys.next();
            JSONObject valueJson= crawlRuleRequestParam.get(key);
            String paramType=valueJson.getString("inputType");
            if(paramType.equals("2")&&valueJson.getString("paramValue").equals(crawlInputBoId+"")){
                keyValue+=key+",";
            }
        }
        if(keyValue.endsWith(","))
            keyValue=keyValue.substring(0,keyValue.length()-1);
        return keyValue.split(",");
    }

    public String doRequestParamSufferProc(String value,String keys,long crawlInputBoId,Map<String,JSONObject> crawlRuleRequestParam){
        /**
         * 后置处理器
         */
        LoggerUtil.infoTrace(crawlInputBoId+"：后置处理器需要处理"+keys+"的内容："+value);
        StringBuffer stringBuffer=new StringBuffer();
        if(!Validate.isEmpty(keys)){
            JSONObject requestParam=crawlRuleRequestParam.get(keys);
            JSONArray crawlRequestParamSuffProcs=requestParam.getJSONArray("requestParamSuffprocBO");
            if(crawlRequestParamSuffProcs!=null&&crawlRequestParamSuffProcs.size()>0){
                String[] valueArray=value.split(CrawlTaskController.keywordSplitChar);
                for (String val:valueArray){
                    if(Validate.isEmpty(val.trim()))
                        break;
                    for (Object suffproc:crawlRequestParamSuffProcs){
                        JSONObject paramSuffpro= (JSONObject) suffproc;
                        ProcessorUtil processorUtil = new ProcessorUtil();
                        val = processorUtil.startRunProcessor(paramSuffpro.getInt("processorId"), val,
                                JSONObject.fromObject("{}"), paramSuffpro.getString("processorValue"));
                    }
                    LoggerUtil.infoTrace(crawlInputBoId+"：后置处理器处理结果："+val);
                    stringBuffer.append(val+CrawlTaskController.keywordSplitChar);
                }
                return stringBuffer.toString();
            }else{
                return value;
            }
        }else{
            return value;
        }
    }
}
