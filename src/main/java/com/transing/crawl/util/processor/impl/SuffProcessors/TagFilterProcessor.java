package com.transing.crawl.util.processor.impl.SuffProcessors;

import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.processor.BaseProcessor;
import com.wordnik.swagger.annotations.Api;
import net.sf.json.JSONObject;
import org.htmlcleaner.TagNode;

import java.util.*;

/**
 * 包: com.transing.crawl.util.processor.impl.SuffProcessors
 * 源文件:TagFilterProcessor.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年11月17日
 */
@Api(value = "标签过滤器",description = "去除内容中的标签\n,需要保留的标签在文本框中输入标签名称，ps:div,img")
public class TagFilterProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        List<String> tags=new ArrayList<String>();
        Map<String,String> tagToChanged= new HashMap<String,String>();

        if(!Validate.isEmpty(processorValue.trim())){
            String [] tagArrays=processorValue.trim().split(",");
            for (String tag:tagArrays){
                if(tag.indexOf("->")>0){
                    String [] tagsplits=tag.split("->");
                    tagToChanged.put(tagsplits[0],tagsplits[1]);
                    tags.add(tagsplits[0]);
                }else{
                    tags.add(tag);
                }
            }
        }
        content=content.replaceAll("([\r\t\n]+)","").trim();
        if(content.indexOf("&lt;")>-1)
            content=ParseUtil.getContentChangeHtmlElem(content);

        //获取所有的标签
        List<String> tagNameList= ParseUtil.getALLTagNodeName(content);
        if(tagNameList==null)
            return content;

        //筛选掉需要保留的标签。
        if(tags.size()>0)
            tagNameList.removeAll(tags);
        if(!tagNameList.contains("html"))
            tagNameList.add("html");

        TagNode tageNodeE=ParseUtil.getTagNode(content);
        content=tagNodeClear(tageNodeE,tagNameList);
        content =tagToChanges(content,tagToChanged);
        return content;
    }

    /**
     * 清洗标签
     * @param content 内容
     * @param tagnodes
     * @return
     */
    private String tagNodeClear(Object content,List<String> tagnodes){
        StringBuffer contentBuffer=new StringBuffer();
        if(content instanceof TagNode){
            TagNode tagNode= (TagNode) content;
            if (tagnodes.contains(tagNode.getName())){
                List child = tagNode.getChildren();

                for (Object c : child)
                    contentBuffer.append(tagNodeClear(c,tagnodes));
            }else{
                contentBuffer.append("<" + tagNode.getName());
                Map<String, String> attrs = tagNode.getAttributes();

                if (attrs != null && attrs.size() > 0)
                {
                    for (String key : attrs.keySet())
                        contentBuffer.append(" ").append(key).append("=\"").append(attrs.get(key)).append("\" ");
                }

                contentBuffer.append(">");
                List child = tagNode.getChildren();

                for (Object c : child)
                    contentBuffer.append(tagNodeClear(c,tagnodes));

                contentBuffer.append("</" + tagNode.getName() + ">");
            }

        }else
            contentBuffer.append(content.toString());
        return contentBuffer.toString();
    }

    /**
     * 将标签转换成指定的符号
     * @param content
     * @param tagNodeToChangess
     * @return
     */
    private String tagToChanges(String content,Map<String,String> tagNodeToChangess){
        if(tagNodeToChangess==null||tagNodeToChangess.size()==0)
            return content;
        Iterator<String> keys=tagNodeToChangess.keySet().iterator();
        while (keys.hasNext()){
            String tagKey=keys.next();
            String changedTags= tagNodeToChangess.get(tagKey);
            content =content.replaceAll("<"+tagKey+".*?>","");
            content =content.replaceAll("</"+tagKey+">",changedTags);
        }
        return content.trim();
    }



    public static void main(String[] args)
    {
        try
        {
            String url = "https://mp.weixin.qq.com/s/pCw4MbdGxy5zaSBcNFmrNQ";
            HttpClientHelper httpClientHelper = new HttpClientHelper();
            String test=httpClientHelper.doGet(url, "utf-8", "utf-8", null, null).getContent();
            //String test = "石家庄市启动重污染<font color=red>天气</font>Ⅱ级应急响应";
            //System.out.println(test);
            test=ParseUtil.parseValue(test,"//*[@id=\"js_content\"]","xpath",JSONObject.fromObject("{}")).get(0).toString();
            TagFilterProcessor tagFilterProcessor = new TagFilterProcessor();
            System.out.println(tagFilterProcessor
                    .runProcessor(test, JSONObject.fromObject("{}"), "p,br").trim());
            String t = tagFilterProcessor
                    .runProcessor(test, JSONObject.fromObject("{}"), "p->\\\\n,br->||");
            System.out.println(t.trim());
        }catch (Exception e){}
    }


}
