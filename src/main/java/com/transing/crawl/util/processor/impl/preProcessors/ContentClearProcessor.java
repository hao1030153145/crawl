package com.transing.crawl.util.processor.impl.preProcessors;

import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.client.HttpResponse;
import com.transing.crawl.util.processor.BaseProcessor;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 包: com.transing.crawl.util.processor.impl.preProcessors
 * 源文件:ContentClearProcessor.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年07月20日
 */
public class ContentClearProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        if(!Validate.isEmpty(processorValue)){
            try{
                ParseUtil parseUtil=new ParseUtil();
                String [] deleteTags = processorValue.split(",");
                TagNode node=parseUtil.getContentTag(content);
                List<String> tags= Arrays.asList(deleteTags);
                content = parseUtil.getTagNodeHtmlString(node,tags);
                content = StringEscapeUtils.unescapeHtml(content);
            }catch (Exception e){
            }
        }
        return content;
    }

    public static void main(String[] args)
    {
        try
        {
        String url="http://snapshot.sogoucdn.com/websnapshot?ie=utf8&url=https://www.tianyancha.com/company/900609850&did=3a5003723b86a70f-eb422bacbd261a7c-95c63397489d015249f42b8d0fa4e146&k=05f97befae4e395657570cd43e2ee7dc&encodedQuery=金峰环宇+建筑工程&query=金峰环宇+建筑工程&&w=01020400&m=0&st=0";
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        HttpResponse response= httpClientHelper.doGetAndRetBytes(url,"GBK","GBK",new HashMap<>(),null);
            System.out.println(response.getContent());
        ContentClearProcessor contentClearProcessor=new ContentClearProcessor();
        contentClearProcessor.runProcessor(new String(response.getContentBytes(),"GBK"),JSONObject.fromObject("{}"),"script,link,style");
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
