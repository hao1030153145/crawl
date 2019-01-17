package com.transing.crawl.util.processor;

import com.jeeframework.core.context.support.SpringContextHolder;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.biz.service.CrawlRuleService;
import com.transing.crawl.integration.bo.CrawlRuleProcessorBO;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.util.processor
 * 源文件:ProcessorUtil.java
 * 处理器的辅助类
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月03日
 */
public class ProcessorUtil
{
    private String loggerName="processorUtil";

    private CrawlRuleService crawlRuleService;


    public final static String URL="url";
    public final static String TYPE="type";
    public final static String HOST="host";
    public final static String PORT="port";
    public final static String HEADMAP="header";

    private  Map<String,CrawlRuleProcessorBO> map=new HashMap<String, CrawlRuleProcessorBO>();

    public ProcessorUtil()
    {
        init();
    }



    private void init(){
        this.crawlRuleService = SpringContextHolder.getBean("crawlRuleService");
        List<CrawlRuleProcessorBO> crawlRuleProcessorBOs=crawlRuleService.getProcessorList();
        for (CrawlRuleProcessorBO crawlRuleProcessorBO:crawlRuleProcessorBOs){
            map.put(crawlRuleProcessorBO.getId()+"",crawlRuleProcessorBO);
        }
    }

    /**
     * 调取处理器的入口
     * @param processorId 处理器的id
     * @param value 需要处理的值
     * @param paramObj 自定义的参数集合
     * @param processValue 后置处理器的表达式
     * @return
     */
    public String startRunProcessor(int processorId,String value, JSONObject paramObj,
            String processValue)
    {
        if(paramObj==null)
            paramObj=JSONObject.fromObject("{}");
        String classpath="com.transing.crawl.util.processor.impl.commonProcessors.BuiltInProcessor";
        try
        {
            CrawlRuleProcessorBO crawlRuleProcessorBO = map.get(processorId+"");
            if (!Validate.isEmpty(crawlRuleProcessorBO.getProcessorPath()))
            {
                classpath = crawlRuleProcessorBO.getProcessorPath();
            }
            paramObj.put(TYPE, crawlRuleProcessorBO.getProcessorName());
            BaseProcessor baseProcessor = (BaseProcessor) Class
                    .forName(classpath).newInstance();
            value = baseProcessor
                    .runProcessor(value, paramObj,processValue);
            return value;
        }catch (Exception e){
            LoggerUtil.errorTrace(loggerName,e.getMessage(),e);
        }
        return value;
    }


}
