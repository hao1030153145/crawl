package com.transing.crawl.util.processor.impl.commonProcessors;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.ProcessorUtil;
import net.sf.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

/**
 * 包: com.transing.crawl.util.processor.impl
 * 源文件:charsEncoding.java
 * 内置处理器
 * 包含对正则，js的处理。
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月03日
 */
public class BuiltInProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,String processorValue)
    {
        try
        {

            if (param.containsKey(ProcessorUtil.TYPE))
            {
                if (param.getString(ProcessorUtil.TYPE)
                        .equalsIgnoreCase("regex") ||
                        param.getString(ProcessorUtil.TYPE)
                                .equalsIgnoreCase("正则处理器"))
                {
                    if(Validate.isEmpty(content)){
                        return content;
                    }
                    List list = ParseUtil.regexMatcher(processorValue, content);
                    StringBuffer matchStr = new StringBuffer();
                    for (Object obj : list)
                    {
                        matchStr.append(obj);
                        matchStr.append(" ");
                    }
                    if(matchStr.toString().endsWith(" ")){
                        return matchStr.toString()
                                .substring(0, matchStr.toString().length() - 1);
                    }
                    return matchStr.toString();
                }
                else
                    if (param.getString(ProcessorUtil.TYPE)
                            .equalsIgnoreCase("javascript"))
                    {
                        if (!processorValue.trim().startsWith("function"))
                        {
                            processorValue = processorValue
                                    .replaceAll("@.*?@", content)
                                    .replaceAll("([\r\n\t]+)", "");
                            if(param.size()>0){
                                StringBuffer javascriptMethod=new StringBuffer();
                                Iterator<String> paramKey= param.keySet().iterator();
                                while (paramKey.hasNext()){
                                    String key = paramKey.next();
                                    if(!ProcessorUtil.TYPE.equalsIgnoreCase(key))
                                    {
                                        String value = param.getString(key);
                                        if (key.indexOf("\\.")>0)
                                            key=key.replaceAll("\\.","_");
                                        value = ParseUtil.contentScape(value);
                                        javascriptMethod.append("var ")
                                                .append(key)
                                                .append(" = \"").append(value)
                                                .append("\";");
                                    }
                                }
                                processorValue = javascriptMethod+processorValue;
                            }
                            processorValue =
                                    "function processor(){" + processorValue +
                                            "}";
                        }
                        String methodName = processorValue
                                .substring(8, processorValue.indexOf("("))
                                .trim();
                        return ParseUtil.jsParse(processorValue, methodName);
                    }
            }
            
            return content;
        }catch (Exception e){
            LoggerUtil.errorTrace(loggerName,e.getMessage(),e);
            throw e;
        }
    }
}
