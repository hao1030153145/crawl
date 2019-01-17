package com.transing.crawl.util.processor.impl.commonProcessors;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.processor.BaseProcessor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 包: com.transing.crawl.util.processor.impl.commonProcessors
 * 源文件:JSONProcessor.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年03月06日
 */
public class JSONProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        try
        {
            if (!Validate.isEmpty(content)){
                int  type=getFormat(content);
                if(type == 0){
                    return null;
                }
                content=substringContent(content,type);
                if(!Validate.isEmpty(content)){
                    if(!Validate.isEmpty(processorValue)){
                        String [] splits=processorValue.split("\\.");
                        List<String> resultArray=new ArrayList<String>();
                        ParseUtil.JSONReadArray(content,splits,0,resultArray);
                        StringBuffer stringBuffer=new StringBuffer();
                        for (String string:resultArray){
                            stringBuffer.append(string);
                        }
                        return stringBuffer.toString();
                    }else{
                        if(type == 1)
                            return JSONObject.fromObject(content).toString();
                        else
                            return JSONArray.fromObject(content).toString();
                    }
                }
            }
        }
        catch (Exception e)
        {
            LoggerUtil.errorTrace(loggerName,"JSONProcessor:"+content+" message:"+e.getMessage(),e);
        }
        return null;
    }

    private int getFormat(String content){
        int arrayFormatPosition=content.indexOf("[");
        int objForamtPosition=content.indexOf("{");
        if((arrayFormatPosition==-1&&objForamtPosition!=-1)||objForamtPosition<arrayFormatPosition){
                //JSONObject
            return 1;
        }else if((objForamtPosition==-1&&arrayFormatPosition!=-1)||objForamtPosition>arrayFormatPosition){
                //JSONARRAY
            return 2;
        }else{
            return 0;
        }
    }

    private String substringContent(String content,int type){
        if(type==1){
            content = content.substring(content.indexOf("{"),content.lastIndexOf("}")+1);
        }else{
            content = content.substring(content.indexOf("["),content.lastIndexOf("]")+1);
        }
        return content;
    }

    public static void main(String[] args)
    {
        String url="https://www.huxiu.com/v2_action/article_list?huxiu_hash_code=b3877ba209da3b1b5195f935b061dcfd&last_dateline=1520246730";
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        try
        {
            String cotent=httpClientHelper.doGet(url,"utf-8","utf-8",new HashMap<String, String>(),null).getContent();
            JSONProcessor jsonProcessor=new JSONProcessor();
            jsonProcessor.runProcessor(cotent,null,"data");
        }
        catch (HttpException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
