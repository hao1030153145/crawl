package com.transing.crawl.crawlParse;

import com.jeeframework.core.context.support.SpringContextHolder;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.json.JSONUtils;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.biz.service.CrawlInputParamService;
import com.transing.crawl.biz.service.CrawlRuleService;
import com.transing.crawl.biz.service.CrawlTaskService;
import com.transing.crawl.biz.service.DatasourceService;
import com.transing.crawl.integration.bo.*;
import com.transing.crawl.util.CharsetUtils;
import com.transing.crawl.util.WebUtil;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.client.HttpResponse;
import com.transing.crawl.util.processor.ProcessorUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.http.HttpException;
import org.apache.http.TruncatedChunkException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;

public class CrawlThread
{

    private static String loggerName = "crawlThread";

    private final static String TASKWATTING = "task_waitting";

    private CrawlTaskBO crawlTaskBO;

    private long jeeTaskId;

    private CrawlSubTaskBO crawlSubTaskBO;

    private CrawlTaskService crawlTaskService;

    private CrawlRuleService crawlRuleService;

    private DatasourceService datasourceService;

    private CrawlInputParamService crawlInputParamService;

    private String flowDetailId;

    private String projectId;

    private String connectionId;

    private int reqInterval = 0;

    private SiteProxyIp siteProxyIp;

    private long crawlTaskId;

    private List<String> exceptionMarks = new ArrayList<String>();

    private CharsetUtils charsetUtils;




    public CrawlThread(CrawlSubTaskBO crawlSubTaskBO, long crawlTaskId,
            String flowDetailId, String projectId,long jeeTaskId)
    {
        this.crawlTaskId = crawlTaskId;
        this.crawlSubTaskBO = crawlSubTaskBO;
        this.flowDetailId = flowDetailId;
        this.projectId = projectId;
        this.jeeTaskId=jeeTaskId;
        init();
    }

    //启动线程执行子任务里的抓取任务
    //步骤: 1.更新子任务状态  2.利用taskid根据crawl_task_info获取datasource_id -> 通过crawl_rule表获取crawl_rule_id 通过crawl_rule_page获取first_url 3.获取头部信息 2.获取抓取url 3.抓取

    public boolean doJob() //throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskId", crawlTaskId);
        this.crawlTaskBO = crawlTaskService.getCrawlTaskListById(param);

        if (crawlTaskBO.getTaskStatus() == 4)
            return true;

        this.charsetUtils=new CharsetUtils();

        StringBuffer pageBuffer=new StringBuffer();

        updateSubTaskStatus(1, null);

        /**
         * Step1：获取CrawlRuleBO和CrawlRulePageBO
         */

        CrawlRuleBO crawlRuleBO = crawlRuleService
                .getCrawlRuleBOByDatasourceTypeId(
                        crawlTaskBO.getDatasourceTypeId());
        long crawlRuleId = crawlRuleBO.getId();
        CrawlRulePageBO crawlRulePageBO = crawlRuleService
                .getCrawlRulePageBO(crawlRuleId);
        String firstUrl = crawlRulePageBO.getFirstUrl();

        LoggerUtil.debugTrace(loggerName,"任务：" + crawlSubTaskBO.getId() + "任务开始运行。。。");
        LoggerUtil.debugTrace(loggerName,"crawlTask: "+JSONObject.fromObject(crawlTaskBO));


        /**
         *Step2:获取头部请求
         */

        Map<String, Object> paramHeader = new HashMap<>();
        paramHeader.put("ruleId", crawlRuleId);
        List<CrawlRuleRequestHeaderBO> crawlRuleRequestHeaderBOList = crawlRuleService
                .getCrawlRuleRequestHeaederList(paramHeader);
        Map<String, String> headerMap = new HashMap<String, String>();
        assembleHeadParam(headerMap,crawlRuleRequestHeaderBOList,crawlRuleId);
        LoggerUtil.infoTrace(loggerName,"任务：" + crawlSubTaskBO.getId() + "heade处理完成。。");
        /**
         * Step3:获取第一页的页码
         */

        String startPage = crawlRulePageBO.getStartPage()+"";
        String crawlJson = crawlSubTaskBO.getParamValue();
        Map<String, String> crawlMap = JSONObject.fromObject(crawlJson);
        startPage = getCrawlFirstPage(crawlMap, startPage);
        //pageBuffer.append(startPage);

        /**
         * Step4：获取连接
         * 需要代理ip的则需要获取连接
         */
        long datasourceId = crawlTaskBO.getDatasourceId();
        long datasourceTypeId = crawlTaskBO.getDatasourceTypeId();
        String cookie = getConnection(datasourceId, datasourceTypeId);

        /**
         * 检测 连接获取是否超时
         */
        if (!Validate.isEmpty(cookie)&&cookie.equals(TASKWATTING)){
            return false;
        }

        if (!Validate.isEmpty(cookie))
            headerMap.put("Cookie", cookie);
        else
            siteProxyIp = null;
        LoggerUtil.infoTrace(loggerName,"任务：" + crawlSubTaskBO.getId() + "获取连接操作完成。。");

        /**
         * Step4:执行抓取
         */
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        httpClientHelper.setConnectionTimeout(30000);
        String exptonstatus = WebUtil.CONNECTION_NORMAL;

        CrawlTaskParseJob crawlTaskParseJob = new CrawlTaskParseJob(
                crawlTaskBO, flowDetailId, projectId,
                crawlSubTaskBO);


        //判定请求方法
        if (crawlRuleBO.getRequestMethod().equalsIgnoreCase(WebUtil.GET_METHED))
        {
            Boolean x = methodGETCrawl(pageBuffer, crawlRuleBO, crawlRulePageBO,
                    firstUrl, headerMap,
                    startPage, crawlMap, datasourceId, datasourceTypeId,
                    httpClientHelper,
                    exptonstatus, crawlTaskParseJob);
            if (x != null)
                return x;

        }else if (crawlRuleBO.getRequestMethod().equalsIgnoreCase(WebUtil.POST_METHED))
        {

            Boolean x = methodPOSTCrawl(pageBuffer, crawlRuleBO, firstUrl,
                        headerMap, startPage,
                        crawlMap, datasourceId, datasourceTypeId,
                        httpClientHelper,
                        exptonstatus, crawlTaskParseJob);
            if (x != null)
                return x;
        }
        return true;
    }

    /**
     * post抓取
     * @param pageBuffer 页码统计
     * @param crawlRuleBO 抓取规则
     * @param firstUrl 抓取首页地址
     * @param headerMap  访问头信息
     * @param startPage  开始页码
     * @param crawlMap  输入参数集合
     * @param datasourceId  数据源编号
     * @param datasourceTypeId 数据源类型id
     * @param httpClientHelper
     * @param exptonstatus  异常标示
     * @param crawlTaskParseJob  抓取解析对象
     * @return
     */
    private Boolean methodPOSTCrawl(StringBuffer pageBuffer,
            CrawlRuleBO crawlRuleBO, String firstUrl,
            Map<String, String> headerMap, String startPage,
            Map<String, String> crawlMap, long datasourceId,
            long datasourceTypeId, HttpClientHelper httpClientHelper,
            String exptonstatus, CrawlTaskParseJob crawlTaskParseJob)
    {
        String mark;
        String html;
        HttpResponse httpResponse = null;
        Map<String, String> postData = new HashMap<>();

        String pageValue = "";
        String pageKey = "";
        for (String key : crawlMap.keySet())
        {
            String value = crawlMap.get(key);
            postData.put(key, value);
            //输入页码的定义
            if (value.indexOf("page_") > -1 ||
                    key.equalsIgnoreCase("page"))
            {
                pageKey = key;
                pageValue = value;
            }
            else if (value.indexOf("@page@") > -1)    //内部变量
                pageKey = key;
        }
        String start = startPage;

        if (!Validate.isEmpty(pageValue) &&
                pageValue.indexOf("page_") > -1)
        {
            try
            {
                String startRow = pageValue
                        .substring(pageValue.indexOf("page_") + 5);
                String[] pages = startRow.split("-");
                start = pages[0];
                crawlMap.put("page", startRow);
            }
            catch (Exception e){}
        }
        if (postData.containsKey(pageKey))
            postData.replace(pageKey, start + "");

        try
        {
            httpResponse = serverDoExcete(firstUrl, WebUtil.POST_METHED,
                    httpClientHelper, crawlRuleBO.getRequestEncoding(),
                    crawlRuleBO.getResponseEncoding(), headerMap,
                    postData);
            String httpContent = new String(
                    httpResponse.getContentBytes(),
                    crawlRuleBO.getResponseEncoding());
            pageBuffer.append(",").append(start);

            Map<String, Object> resultMap = crawlTaskParseJob
                    .doJob(httpContent, exceptionMarks, firstUrl,
                            headerMap, siteProxyIp);
            int count = Integer
                    .parseInt(resultMap.get("count").toString());
            updateCrawlNum(count);
            String page = crawlTaskParseJob.nextPageNum(start + "", httpContent, crawlMap);
            Map<String, Object> paramBo = new HashMap<String, Object>();
            paramBo.put("taskId", crawlTaskBO.getId());
            LoggerUtil.infoTrace(loggerName,String.format("任务编号:%d,抓取路径：%s ,翻页次数 %s",crawlSubTaskBO.getId(),firstUrl,page));

            while (page != null)
            {
                try
                {
                    Thread.sleep(reqInterval);
                }
                catch (Exception e)
                {
                }
                //更新crawlTask 查看是否停止
                crawlTaskBO = crawlTaskService
                        .getCrawlTaskListById(paramBo);
                if (crawlTaskBO.getTaskStatus() == 4)
                    return true;
                String crawlUrl = crawlSubTaskBO.getCrawlUrl();

                if (postData.containsKey(pageKey))
                    postData.replace(pageKey, page);

                try
                {
                    httpResponse = serverDoExcete(crawlUrl,
                            WebUtil.POST_METHED, httpClientHelper,
                            crawlRuleBO.getRequestEncoding(),
                            crawlRuleBO.getResponseEncoding(),
                            headerMap, postData);

                    httpContent = new String(
                            httpResponse.getContentBytes(),
                            crawlRuleBO.getResponseEncoding());

                    resultMap = crawlTaskParseJob
                            .doJob(httpContent, exceptionMarks,
                                    crawlUrl, headerMap, siteProxyIp);
                    int count2 = Integer.parseInt(
                            resultMap.get("count").toString());

                    updateCrawlNum(count2);

                    pageBuffer.append(",").append(page);
                    page = crawlTaskParseJob.nextPageNum(page, httpContent, crawlMap);
                    LoggerUtil.infoTrace(loggerName,String.format("任务编号:%d,抓取路径：%s ,翻页次数 %s",crawlSubTaskBO.getId(),crawlUrl,page));
                }
                catch (Exception e)
                {
                    String mesg = e.getMessage();
                    if (!Validate.isEmpty(mesg)&&mesg.startsWith("connectionException"))
                    {
                        headerMap = errorConnection(exptonstatus, mesg,
                                datasourceId, datasourceTypeId,
                                headerMap);
                        if(headerMap.containsKey("Cookie")&&headerMap.get("Cookie")!=null)
                        {
                            if (headerMap.get("Cookie").equals(TASKWATTING))
                                return false;
                        }
                        continue;
                    }
                    else
                        break;
                }

            }
            crawlTaskBO = crawlTaskService
                    .getCrawlTaskListById(paramBo);

            updateSubTaskStatus(2, null);
            return true;
        }
        catch (Exception e)
        {
            LoggerUtil.errorTrace(loggerName, e.getMessage(), e);
            updateSubTaskStatus(9, e.toString());
            updateTaskInfo(9,e.toString());
            return true;
        }
        finally
        {
            rollBackDpm();
            updateTaskInfo();
            updateSubTaskCrawlPage(pageBuffer.toString());

            if (!Validate.isEmpty(connectionId))
            {
                exptonstatus = WebUtil.CONNECTION_NORMAL;
                html = mark = "";
                WebUtil.doRollBackConnection(datasourceId,
                        datasourceTypeId, connectionId,
                        exptonstatus, html, mark);
            }
        }
    }

    /**
     *  GET 访问处理方式
     * @param pageBuffer  页码统计
     * @param crawlRuleBO  解析规则
     * @param crawlRulePageBO 页码解析对象
     * @param firstUrl   抓取首页地址
     * @param headerMap  抓取头信息
     * @param startPage  抓取首页
     * @param crawlMap   输入参数集合
     * @param datasourceId 数据编号
     * @param datasourceTypeId  数据源类型
     * @param httpClientHelper 抓取采集对象
     * @param exptonstatus  异常编码
     * @param crawlTaskParseJob 解析对象
     * @return
     */
    private Boolean methodGETCrawl(StringBuffer pageBuffer,
            CrawlRuleBO crawlRuleBO, CrawlRulePageBO crawlRulePageBO,
            String firstUrl, Map<String, String> headerMap, String startPage,
            Map<String, String> crawlMap, long datasourceId,
            long datasourceTypeId, HttpClientHelper httpClientHelper,
             String exptonstatus,CrawlTaskParseJob crawlTaskParseJob)
    {
        String html = "";
        String mark = "";
        HttpResponse httpResponse = null;
        try
        {
            //组装url
            /**
             * A、替换页码配置中首页地址的输入参数
             * ps:首页地址中应该是不包含页码的输入参数(即页码很明确)
             */
            String page = startPage + "";
            String httpContent="";
            Map<String, Object> resultMap = null;

            //如果配置的页码大于页码配置中默认的起始页，则跳过对首页的抓取
            if (crawlRulePageBO.getStartPage() >= Long.parseLong(startPage))
            {
                for (String key : crawlMap.keySet())
                    firstUrl = firstUrl.replaceAll("@" + key + "@", crawlMap.get(key));

                LoggerUtil.infoTrace(loggerName,String.format("任务编号：%d ，数据源：%d ，获取Cookie:%s，代理IP:%s; 使用端口：%d",
                        crawlSubTaskBO.getId(),datasourceId,headerMap.get("Cookie"),(siteProxyIp != null ?siteProxyIp.getHost() :""),
                        (siteProxyIp != null ?siteProxyIp.getPort():0)));
                LoggerUtil.infoTrace(loggerName,String.format("任务编号:%d,抓取路径：%s ,翻页次数 %s",crawlSubTaskBO.getId(),firstUrl,page));

                httpResponse = serverDoExcete(firstUrl, WebUtil.GET_METHED,
                        httpClientHelper, crawlRuleBO.getRequestEncoding(),
                        crawlRuleBO.getResponseEncoding(), headerMap, null);


                httpContent=charsetUtils.CharsetCheckEntrance(httpResponse.getContentBytes(),
                        crawlRuleBO.getResponseEncoding(),httpResponse.getResponseEncode());

                pageBuffer.append(crawlRulePageBO.getStartPage()).append(",");


                /**
                 * B、翻页的过程中出现异常。
                 * 返回异常连接，并获取新的连接继续抓取
                 */
                int count = 0;

                boolean flag = true;//是否继续循环获取。
                while (flag)
                {
                    try
                    {
                        resultMap = crawlTaskParseJob
                                .doJob(httpContent, exceptionMarks,
                                        firstUrl, headerMap, siteProxyIp);
                        count = Integer.parseInt(
                                resultMap.get("count").toString());
                        flag = false;
                    }
                    catch (Exception e)
                    {
                        LoggerUtil.errorTrace(loggerName, e.getMessage(), e);
                        String mesg = e.getMessage();
                        flag = false;
                        if (!Validate.isEmpty(mesg)&&mesg.startsWith("connectionException"))
                        {

                            headerMap = errorConnection(exptonstatus,
                                    mesg, datasourceId,
                                    datasourceTypeId, headerMap);
                            if(headerMap.containsKey("Cookie")&&headerMap.get("Cookie")!=null)
                            {
                                if (headerMap.get("Cookie").equals(TASKWATTING))
                                {
                                    return false;
                                }
                            }

                            //获取有用连接再次补抓
                            httpResponse = serverDoExcete(firstUrl,
                                    WebUtil.GET_METHED,
                                    httpClientHelper,
                                    crawlRuleBO.getRequestEncoding(),
                                    crawlRuleBO.getResponseEncoding(),
                                    headerMap, null);

                            httpContent = charsetUtils.CharsetCheckEntrance(
                                    httpResponse.getContentBytes(),
                                    crawlRuleBO.getResponseEncoding(),
                                    httpResponse.getResponseEncode());
                            flag = true;
                        }
                    }
                }

                /**
                 * C、更新抓取数量
                 */
                updateCrawlNum(count);
                /**
                 * D、获取下一页的页码
                 */
                page = crawlTaskParseJob.nextPageNum(startPage + "", httpContent, crawlMap);
            }

            Map<String, Object> paramBo = new HashMap<String, Object>();
            paramBo.put("taskId", crawlTaskBO.getId());
            while (page != null)
            {
                /**
                 * E、休眠到连接指定的抓取间隔
                 */
                try
                {
                    Thread.sleep(reqInterval);
                }
                catch (Exception e){}

                /**
                 * F、更新crawlTask 查看是否停止
                 */
                crawlTaskBO = crawlTaskService
                        .getCrawlTaskListById(paramBo);
                if (crawlTaskBO.getTaskStatus() == 4)
                    return true;
                /**
                 * G、更新抓取的页码
                 */
                String crawlUrl = crawlSubTaskBO.getCrawlUrl();
                crawlUrl = crawlUrl.replaceAll("@page@", page);

                LoggerUtil.infoTrace(loggerName,String.format("任务编号:%d,抓取路径：%s ,翻页次数 %s",crawlSubTaskBO.getId(),crawlUrl,page));

                httpResponse = serverDoExcete(crawlUrl, WebUtil.GET_METHED,
                        httpClientHelper,
                        crawlRuleBO.getRequestEncoding(),
                        crawlRuleBO.getResponseEncoding(),
                        headerMap, null);


                httpContent=charsetUtils.CharsetCheckEntrance(httpResponse.getContentBytes(),
                        crawlRuleBO.getResponseEncoding(),httpResponse.getResponseEncode());

                try
                {
                    resultMap = crawlTaskParseJob
                            .doJob(httpContent, exceptionMarks, crawlUrl,
                                    headerMap, siteProxyIp);
                    int count2 = Integer
                            .parseInt(resultMap.get("count").toString());
                    updateCrawlNum(count2);
                    //页码执行记录
                    pageBuffer.append(page).append(",");

                    page = crawlTaskParseJob.nextPageNum(page, httpContent, crawlMap);
                }
                catch (Exception e)
                {
                    String mesg = e.getMessage();
                    if (!Validate.isEmpty(mesg)&&mesg.startsWith("connectionException"))
                    {
                        headerMap = errorConnection(exptonstatus, mesg,
                                datasourceId, datasourceTypeId,
                                headerMap);
                        if(headerMap.containsKey("Cookie")&&headerMap.get("Cookie")!=null)
                        {
                            if (headerMap.get("Cookie").equals(TASKWATTING))
                                return false;
                        }
                        continue;
                    }
                    else
                        break;
                }
            }

            updateSubTaskStatus(2, null);
            return true;
        }
        catch (Exception e)
        {
            LoggerUtil.errorTrace(loggerName,
                    "任务：" + crawlSubTaskBO.getId() + e.getMessage(), e);

            updateSubTaskStatus(9, e.toString());

            updateTaskInfo(9,e.toString());

            String mesg = e.getMessage();

            if (!Validate.isEmpty(mesg)&&mesg.startsWith("connectionException"))
            {
                exptonstatus = WebUtil.CONNECTION_EXCEPTION;
                String str = mesg.substring(mesg.indexOf(":") + 1);
                JSONObject jsonObject = JSONObject.fromObject(str);
                html = jsonObject.getString("html");
                mark = jsonObject.getString("mark");
            }
            return true;
        }
        finally
        {
            rollBackDpm();
            updateTaskInfo();
            updateSubTaskCrawlPage(pageBuffer.toString());
            if (!Validate.isEmpty(connectionId))
                WebUtil.doRollBackConnection(datasourceId, datasourceTypeId,
                        connectionId,
                        exptonstatus, html, mark);
        }
    }

    public void init()
    {
        crawlTaskService = (CrawlTaskService) SpringContextHolder
                .getBean("crawlTaskService");
        crawlRuleService = (CrawlRuleService) SpringContextHolder
                .getBean("crawlRuleService");
        datasourceService = (DatasourceService) SpringContextHolder
                .getBean("datasourceService");
        crawlInputParamService=(CrawlInputParamService) SpringContextHolder
                .getBean("crawlInputParamService");
    }

    /**
     * 组装head
     * @param headerMap
     */
    public void assembleHeadParam(Map<String,String> headerMap,List<CrawlRuleRequestHeaderBO> crawlRuleRequestHeaderBOList,Long crawlRuleId){
        if (crawlRuleRequestHeaderBOList != null &&
                crawlRuleRequestHeaderBOList.size() > 0)
        {
            Map<Long,CrawlInputBO> crawlInputBOMap= null;

            for (CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBo : crawlRuleRequestHeaderBOList)
            {
                try
                {
                    if (crawlRuleRequestHeaderBo.getInputType() == 2 &&
                            crawlInputBOMap == null)
                        crawlInputBOMap = crawlInputParamService
                                .getAllCrawlInputBo(
                                        crawlTaskBO.getDatasourceTypeId());
                    LoggerUtil.infoTrace("==headType="+(crawlRuleRequestHeaderBo.getInputType()==2&&crawlInputBOMap==null)+"crawInput:"+crawlInputBOMap.size());
                }catch (Exception e){
                    e.printStackTrace();
                }

                Map<String, Object> paramHeaderSufferProc = new HashMap<>();
                paramHeaderSufferProc.put("requestHeaderId", crawlRuleRequestHeaderBo.getId());
                List<CrawlRuleRequestHeaderSufferProcBO> crawlRuleRequestHeaderSufferProcBOList = crawlRuleService
                        .getCrawlRuleRequestHeaderSufferProcBO(paramHeaderSufferProc);


                String headerValue = getHeaderValue(crawlRuleRequestHeaderBo,crawlInputBOMap);
                String  headerName = crawlRuleRequestHeaderBo.getHeaderName();
                //有后置处理器则处理
                if (crawlRuleRequestHeaderSufferProcBOList != null &&
                        crawlRuleRequestHeaderSufferProcBOList.size() > 0)
                {
                    for (CrawlRuleRequestHeaderSufferProcBO crawlRuleRequestHeaderSufferProcBO : crawlRuleRequestHeaderSufferProcBOList)
                    {
                        //后置处理器处理value
                        JSONObject jsonObject = JSONObject.fromObject("{}");
                        ProcessorUtil processorUtil = new ProcessorUtil();
                        headerValue = processorUtil.startRunProcessor(
                                crawlRuleRequestHeaderSufferProcBO
                                        .getProcessorId(), headerValue,
                                jsonObject, crawlRuleRequestHeaderSufferProcBO
                                        .getProcessorValue());
                    }
                }
                //利用firstUrl进行http请求
                headerMap.put(headerName, headerValue);
            }
        }
    }

    /**
     * 获取head的值
     * @return
     */
    private String getHeaderValue(CrawlRuleRequestHeaderBO crawlRuleRequestHeaderBo,Map<Long,CrawlInputBO> crawlInputBOMap){
        int inputType=crawlRuleRequestHeaderBo.getInputType();
        if (inputType == 1)
            //   1为输入值
            return crawlRuleRequestHeaderBo.getHeaderValue()==null?
                    "":crawlRuleRequestHeaderBo.getHeaderValue();
        else if (inputType == 2)
        {//2为选择输入参数
            if(crawlInputBOMap==null)
                return "";
            String headValueId=crawlRuleRequestHeaderBo.getHeaderValue();
            if(crawlInputBOMap.containsKey(Long.parseLong(headValueId))){
                CrawlInputBO crawlInputBO =crawlInputBOMap.get(Long.parseLong(headValueId));
                String paramEnName=crawlInputBO.getParamEnName();
                JSONObject jsonObject=JSONObject.fromObject(crawlSubTaskBO.getParamValue());
                if(jsonObject.containsKey(paramEnName))
                    return jsonObject.getString(paramEnName);
            }
            return "";
        }
        else
                //3为内部变量
                //将url中的内部变量的@page@ 替换成$page$并记录
          return "page";
    }

    synchronized public void updateCrawlNum(int count)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskId", crawlTaskBO.getId());
        param.put("subId", crawlSubTaskBO.getId());
        crawlSubTaskBO = crawlTaskService
                .getCrawlSubTaskListByTaskIdAndSubId(param);
        long crawlDataNum = crawlSubTaskBO.getCrawlDataNum();
        param.clear();
        param.put("crawlDataNum", crawlDataNum + count);
        param.put("id", crawlSubTaskBO.getId());
        crawlTaskService.modifyCrawlSubTaskInfo(param);
        crawlSubTaskBO.setCrawlDataNum(crawlDataNum + count);
    }

    private void updateSubTaskStatus(int status, String errorMsg)
    {
        Map<String, Object> paramBo = new HashMap<String, Object>();
        if (!Validate.isEmpty(errorMsg))
            paramBo.put("errorMsg", errorMsg);
        paramBo.put("taskStatus", status);
        paramBo.put("id", crawlSubTaskBO.getId());
        crawlTaskService.modifyCrawlSubTaskInfo(paramBo);
    }

    /**
     * 访问请求
     *
     * @param url
     * @param method
     * @param httpClientHelper
     * @param requestEncoding
     * @param responseEncoding
     * @param headerMap
     * @param postParam
     * @return
     * @throws IOException
     * @throws HttpException
     */
    private HttpResponse serverDoExcete(String url, String method,
            HttpClientHelper httpClientHelper, String requestEncoding,
            String responseEncoding, Map<String, String> headerMap,
            Map<String, String> postParam)
            throws IOException, HttpException
    {
        HttpResponse httpResponse = null;
        for (int i = 0; i < 3; i++)
        {
            try
            {
                if (method.equalsIgnoreCase(WebUtil.GET_METHED))
                    httpResponse = httpClientHelper
                            .doGetAndRetBytes(url, requestEncoding,
                                    responseEncoding, headerMap, siteProxyIp);
                else
                    httpResponse = httpClientHelper
                            .doPostAndRetBytes(url, postParam, requestEncoding,
                                    responseEncoding, headerMap, siteProxyIp);

                if(postParam!=null)
                    LoggerUtil.debugTrace(loggerName,String.format("子任务编号：%d ,抓取地址：%s，访问参数：%s",
                            crawlSubTaskBO.getId(),url, JSONObject.fromObject(postParam).toString()));
                //状态编码不是正确的 200 ，则日志输出//TODO 访问错误的应对措施
                if(httpResponse.getStatusCode()!= 200){
                    LoggerUtil.errorTrace(loggerName,String.format("子任务编号：%d ,抓取地址：%s，访问编码：%d",
                            crawlSubTaskBO.getId(),url,httpResponse.getStatusCode()));
                }
                break;
            }
            catch (SocketTimeoutException e){}
            catch (TruncatedChunkException e){}
            catch (ConnectTimeoutException e){}
            catch (ClientProtocolException e){
                LoggerUtil.errorTrace(loggerName,"ClientProtocolException: url= "+url,e);
            }catch (IOException e){}
            try
            {
                Thread.sleep(60000);
            }catch (Exception ext){}
        }
        if(httpResponse==null)
            httpResponse=new HttpResponse();
        return httpResponse;
    }
    /**
     * dpbm的回调
     *
     */
    public void rollBackDpm(){

        Map<String, String> postMap = new HashMap<String, String>();
        postMap.put("detailId",flowDetailId);
        postMap.put("finishNum","1");
        postMap.put("resultNum",crawlSubTaskBO.getCrawlDataNum()+"");
        postMap.put("projectId",crawlTaskBO.getProjectId());
        JSONObject taskParamJSON=JSONObject.fromObject(crawlTaskBO.getJsonParam());
        postMap.put("workFlowId",taskParamJSON.containsKey("workFlowId")?taskParamJSON.getString("workFlowId"):"0");
        postMap.put("travelParams",taskParamJSON.containsKey(WebUtil.TRAVEL_PARAMS)?taskParamJSON.getString(WebUtil.TRAVEL_PARAMS):"");
        String dpmURLCallBack = "/updateResultNumAndFinishNum.json";
        WebUtil.callRemoteService(
                WebUtil.getDpmbsServerByEnv() + dpmURLCallBack, "post",
                postMap);
        LoggerUtil.debugTrace(loggerName,"dpm回调：任务："+crawlSubTaskBO.getId()+" ,项目："+projectId+":"
                +JSONObject.fromObject(postMap).toString());

    }


    /**
     * 出现连接异常，汇报并重新获取连接
     *
     * @param exptonstatus
     * @param mesg
     * @param datasourceId
     * @param datasourceTypeId
     * @param headerMap
     * @return
     */
    private Map<String, String> errorConnection(String exptonstatus,
            String mesg, long datasourceId, long datasourceTypeId,
            Map<String, String> headerMap)
    {
        exptonstatus = WebUtil.CONNECTION_EXCEPTION;
        String str = mesg.substring(mesg.indexOf(":") + 1);
        JSONObject jsonObject = JSONObject.fromObject(str);
        String html = jsonObject.getString("html");
        String mark = jsonObject.getString("mark");
        if (!Validate.isEmpty(connectionId))
        {
            WebUtil.doRollBackConnection(datasourceId, datasourceTypeId,
                    connectionId, exptonstatus, html, mark);
            connectionId = null;
        }
        String cookie = getConnection(datasourceId, datasourceTypeId);


        if (!Validate.isEmpty(cookie))
        {
            headerMap.replace("Cookie", cookie);
        }
        return headerMap;
    }

    /**
     * 获取连接
     *
     * 当该类型的连接等待时间大于5分钟，
     * 则将当前任务放置到等待队列中。
     *
     * @param datasourceId
     * @return
     */
    private String getConnection(long datasourceId, long datasourceTypeId)
    {
        Datasource datasource = datasourceService
                .getDataSourceById(datasourceId);
        String exceptionmark = null;
        LoggerUtil.infoTrace(loggerName,
                "任务:" + crawlSubTaskBO.getId() + "获取对应的datasource的是否需要代理ip"+(datasource.getIsNeedProxyIp() == 1));
        if (datasource.getIsNeedProxyIp() == 1)
        {
            Map<String, String> connectionParam = null;

            long startTime=System.currentTimeMillis();

            while (connectionParam == null)
            {
                connectionParam = WebUtil
                        .doGetConnection(datasource.getId(), datasourceTypeId);
                if (connectionParam == null)
                {
                    try
                    {
                        //当获取连接大于 5分钟 则 返回
                        long endTime=System.currentTimeMillis();
                        long waitTime=endTime-startTime;
                        if(waitTime/(1000*60) > WebUtil.getConnectionWaitTime())
                            return TASKWATTING;

                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
            LoggerUtil.debugTrace(loggerName,
                    "获取连接：" + connectionParam.get(WebUtil.CONNECTION_ID));

            String host = connectionParam.get(WebUtil.CONNECTION_HOST);
            String port = connectionParam.get(WebUtil.CONNECTION_PORT);
            this.reqInterval = Integer.parseInt(
                    connectionParam.get(WebUtil.CONNECTION_REQINTERVAL));
            this.connectionId = connectionParam.get(WebUtil.CONNECTION_ID);
            if (!Validate.isEmpty(host) && port.matches("\\d+"))
            {
                siteProxyIp = new SiteProxyIp(host, Integer.parseInt(port));
            }
            exceptionmark = connectionParam
                    .get(WebUtil.CONNECTION_EXCEPTIONMARK);
            if (!Validate.isEmpty(exceptionmark) &&
                    JSONUtils.isJSONValid(exceptionmark))
            {
                try
                {
                    JSONArray jsonArray = JSONArray.fromObject(exceptionmark);
                    LoggerUtil.debugTrace("markList=" + jsonArray.toString());
                    exceptionMarks = JSONArray
                            .toList(jsonArray, new String(), new JsonConfig());
                }
                catch (Exception e)
                {
                }
            }
            String cookie = connectionParam.get(WebUtil.CONNECTION_COOKIE);
            return cookie;

        }
        return null;
    }

    /**
     * 获取首页地址
     *
     * @param crawlMap
     * @param startPage
     * @return
     */
    private String getCrawlFirstPage(Map<String, String> crawlMap, String startPage)
    {
        if (crawlMap != null)
        {
            Iterator<String> values = crawlMap.values().iterator();
            while (values.hasNext())
            {
                String value = values.next();
                if (value.indexOf("page_")>-1)
                {
                    String[] pages = value.substring(value.indexOf("page_")+5).split("-");
                    startPage = pages[0];
                    return startPage;
                }
            }
        }
        return startPage;
    }


    /**
     * 更新主任务状态
     */
    private void updateTaskInfo(){
        Map<String,Object> paramBo=new HashMap<String, Object>();
        paramBo.clear();
        paramBo.put("taskId", crawlTaskBO.getId());
        paramBo.put("status", 2);
        List list = crawlTaskService.getCrawlSubTaskBos(paramBo);
        paramBo.replace("status", 9);
        List errorlist = crawlTaskService.getCrawlSubTaskBos(paramBo);
        paramBo.clear();
        paramBo.put("id", crawlTaskBO.getId());
        paramBo.put("completeSubTaskNum", list.size());
        if ((list.size()+errorlist.size()) == crawlTaskBO.getSubTaskNum()&&errorlist.size()==0)
            paramBo.put("taskStatus", 2);
        crawlTaskService.modifyCrawlTaskInfoBO(paramBo);

        LoggerUtil.debugTrace("任务进度：当前完成数：" + list.size() + "，总数：" +
                crawlTaskBO.getSubTaskNum());
    }

    private void updateTaskInfo(int status,String errorMsg){
        Map<String,Object> paramBo=new HashMap<String, Object>();
        paramBo.clear();
        paramBo.put("id", crawlTaskBO.getId());
        paramBo.put("errorMsg", errorMsg);
        paramBo.put("taskStatus", 9);
        crawlTaskService.modifyCrawlTaskInfoBO(paramBo);
    }

    /**
     * 更新任务执行的页码数据
     * @param page
     */
    private void updateSubTaskCrawlPage(String page){
        Map<String,Object> paramBo=new HashMap<String, Object>();
        paramBo.put("id",crawlSubTaskBO.getId());
        paramBo.put("complatePage",page);
        crawlTaskService.modifyCrawlSubTaskInfo(paramBo);
    }


    public static void main(String[] args)
    {
        System.out.println(3000000/(1000*60) > 5);
    }

}
