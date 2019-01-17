package com.transing.crawl.util.processor.impl.preProcessors;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.transing.crawl.util.processor.BaseProcessor;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * 包: com.transing.crawl.util.processor.impl
 * 源文件:WeiboProcessor.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年08月02日
 */
public class WeiboProcessor extends BaseProcessor
{

    private String regexHtml="view({";

    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        LoggerUtil.infoTrace("weibo","解析前："+content);
        StringBuilder sb = new StringBuilder();
        int i = 0, j;
       /* if(content.indexOf(regexHtml)>0){
        }else
        {
            regexHtml = "STK.pageletM.view({";
        }*/
        while ((i = content.indexOf(regexHtml, i + 1)) > 0)
        {

            j = content.indexOf("})</script>", i);
            String jsonHtml = content.substring(i + (regexHtml.length()-1), j + 1);
            boolean mayBeJSON = JSONUtils.mayBeJSON(jsonHtml);
            if (mayBeJSON)
            {
                JSONObject jsonObjec = null;
                try
                {
                    jsonObjec = JSONObject.fromObject(jsonHtml);

                    String html = jsonObjec.getString("html");
                    sb.append(html).append("\n\n\n");
                }
                catch (Exception e)
                {
                }
            }
        }

        LoggerUtil.infoTrace("weibo","解析后："+sb.toString());
        return sb.toString();
    }
}
