package com.transing.crawl.util.processor.impl.preProcessors;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.JSExecuteUtil;
import com.transing.crawl.util.ParseUtil;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.ProcessorUtil;
import com.wordnik.swagger.annotations.Api;
import net.sf.json.JSONObject;
import org.apache.http.cookie.Cookie;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.util.processor.impl.preProcessors
 * 源文件:VkuProcessor.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年04月03日
 */
@Api(value = "优酷视频的评论前置处理器",description = "url中应该包括 appKey以及data")
public class VkuProcessor extends BaseProcessor
{
    public static final String vku_JS = "com/transing/crawl/util/processor/impl/preProcessors/Vkuprocessor.js";

    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        try
        {
            String appid="";
            String appkey="24679788";

            SiteProxyIp siteProxyIp=null;
            if(param.containsKey(ProcessorUtil.HOST))
            {
                String proxyIp = param.getString(ProcessorUtil.HOST);
                Integer port = param.getInt(ProcessorUtil.PORT);
                if (!Validate.isEmpty(proxyIp))
                {
                    siteProxyIp = new SiteProxyIp(proxyIp, port);
                }
            }
            String url ="";
            if(param.containsKey(ProcessorUtil.URL))
            {
               url= param.getString(ProcessorUtil.URL);
            }


            List list= ParseUtil.regexMatcher("id=(\\d+)",url);
            if(list.size()>0)
                appid =list.get(0).toString();

            String datastr="data={\"liveId\":\""+appid+"\"}";

            LoggerUtil.debugTrace(loggerName,"vku url="+url);
            HttpClientHelper httpClientHelper = new HttpClientHelper();
            Map<String, String> headMap = new HashMap<String, String>();
            long t=System.currentTimeMillis();
            String sign=getSU("",t,datastr.substring(datastr.indexOf("=")+1));
            url="http://acs.youku.com/h5/mtop.youku.live.livetemplate/1.0/?jsv=2.4.2&appKey="+appkey+"&t="+t+"&sign="+sign+"&api=mtop.youku.live.liveTemplate&v=1.0&type=jsonp&dataType=jsonp&callback=mtopjsonp1&data={\"liveId\":\""+appid+"\",\"sdkVersion\":\"\",\"app\":\"\"}";
            httpClientHelper.doGet(url, "utf-8", "utf-8", headMap, siteProxyIp).getContent();
            List<Cookie> cookies=httpClientHelper.getCookies();
            String token="";
            for (Cookie cookie:cookies){
                if(cookie.getName().equals("_m_h5_tk")){
                    String value=cookie.getValue();
                    token=value.split("_")[0];
                }
            }
            t=System.currentTimeMillis();

            sign=getSU(token,t,datastr.substring(datastr.indexOf("=")+1));

            url="http://acs.youku.com/h5/mtop.youku.live.pc.livefullinfo/2.0/?jsv=2.4.2&&appKey="+appkey+"&t="+t+"&sign="+sign+"&api=mtop.youku.live.pc.livefullinfo&v=2.0&type=jsonp&dataType=jsonp&callback=mtopjsonp2&"+datastr;
            LoggerUtil.debugTrace(loggerName,"=== vkuProcessorUrl:"+url);
            String contents=httpClientHelper.doGet(url,"utf-8","utf-8",headMap,siteProxyIp).getContent();
            return contents;
        }catch (Exception e){
            LoggerUtil.errorTrace(loggerName,"vkuprocessors:"+e.getMessage(),e);
            return "";}
    }


    public String getSU(String token,long tims,String data)
    {
        String su = "";
        try
        {
            DefaultResourceLoader drl = new DefaultResourceLoader();

            Resource src1 = drl.getResource(vku_JS);
            JSExecuteUtil jsExecuteUtil=new JSExecuteUtil();

            jsExecuteUtil.runJavaScript(vku_JS, src1.getInputStream());

            Scriptable scope = jsExecuteUtil.getScope();

            Function getSU = (Function) scope.get("test", scope);

            Object suTmp = getSU.call(Context.getCurrentContext(), scope, getSU, new Object[] { token,tims,data });

            su = Context.toString(suTmp);

        } catch (Exception e)
        {

            throw new BizException("tsinaLogin.js 不存在");

        }
        return su;
    }

    public static void main(String[] args)
    {
        VkuProcessor vkuProcessor=new VkuProcessor();
        JSONObject object=JSONObject.fromObject("{}");
        object.put(ProcessorUtil.TYPE,"http://acs.youku.com/h5/mtop.youku.live.pc.livefullinfo/2.0/?jsv=2.4.2&appKey=24679788&t=1522745582138&sign=bf4801179122c55e168c29f6b5649b7b&api=mtop.youku.live.pc.livefullinfo&v=2.0&type=jsonp&dataType=jsonp&callback=mtopjsonp2&data={\"liveId\":\"14833\"}");
        vkuProcessor.runProcessor("",object,"");

    }
}
