package com.transing.crawl.web.controller;

;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.testframework.AbstractSpringBaseControllerTest;
import com.jeeframework.util.encrypt.MD5Util;
import com.jeeframework.util.httpclient.HttpClientHelper;
import com.jeeframework.util.httpclient.HttpResponse;
import com.jeeframework.util.httpclient.proxy.SiteProxyIp;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.crawlParse.CrawlTaskParseJob;
import com.transing.crawl.crawlParse.CrawlThread;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.integration.bo.CrawlTaskBO;
import com.transing.crawl.util.*;
import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.impl.commonProcessors.BuiltInProcessor;
import com.transing.crawl.util.processor.impl.SuffProcessors.TagFilterProcessor;
import com.transing.crawl.util.processor.impl.preProcessors.WeiboProcessor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 包: com.transing.crawl.web.controller
 * 源文件:CrawlTaskInfoTest.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月07日
 */
public class CrawlTaskInfoTest extends AbstractSpringBaseControllerTest
{

    @Test
    public void crawlTask() throws Exception{
        String url="/crawlTask/executeCrawl.json";
        String param="{\"count\":0,\"inputParams\":\"[[{\\\"paramValue\\\":\\\"4222184219642477\\\",\\\"id\\\":333},{\\\"paramValue\\\":\\\"\\\",\\\"id\\\":334},{\\\"paramValue\\\":\\\"https://weibo.com/1845864154/G9j6ZEsrP?type=comment\\\",\\\"id\\\":335}],[{\\\"paramValue\\\":\\\"\\\",\\\"id\\\":333},{\\\"paramValue\\\":\\\"\\\",\\\"id\\\":334},{\\\"paramValue\\\":\\\"https://weibo.com/1845864154/G9j6ZEsrP?type=comment\\\",\\\"id\\\":335}]]\",\"jsonParam\":{\"datasourceId\":\"29\",\"datasourceName\":\"微博\",\"datasourceTypeId\":\"227\",\"datasourceTypeName\":\"微博视频评论\",\"inputParamArray\":[[{\"paramValue\":\"4222184219642477\",\"id\":333},{\"paramValue\":\"\",\"id\":334},{\"paramValue\":\"https://weibo.com/1845864154/G9j6ZEsrP?type=comment\",\"id\":335}],[{\"paramValue\":\"\",\"id\":333},{\"paramValue\":\"\",\"id\":334},{\"paramValue\":\"https://weibo.com/1845864154/G9j6ZEsrP?type=comment\",\"id\":335}]],\"status\":0,\"taskName\":\"流程抓取微博视频评论\",\"workFlowTemplateId\":0},\"status\":0,\"taskName\":\"流程抓取微博视频评论\",\"workFlowTemplateId\":0,\"flowId\":\"336\",\"flowDetailId\":\"3578\",\"typeNo\":\"dataCrawl\",\"paramType\":\"0\",\"projectId\":\"401\",\"firstDetailId\":\"3577\"}";
        MvcResult mvcResult=this.mockMvc.perform(
                (MockMvcRequestBuilders.post(url)
                        .param("jsonParam", param)
                        .param("flowId", "0")
                        .param("flowDetailId", "2004")
                        .param("typeNo", "dataCrawl")
                        .param("paramType", "0")
                        .param("projectId", "288"))
                        .param("workFlowId","0")
                        .param("batchNo","")
                        .param("travelParams","")
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String content=mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(content));
        assertTrue(JSONObject.fromObject(content).getInt("code")==0);
    }


    @Test
    public void crawlTask2() throws Exception{
        String url="/crawlTask/executeCrawl.json";
        String param="{\"count\":0,\"inputParams\":\"[[{\\\"paramValue\\\":\\\"4222184219642477\\\",\\\"id\\\":333},{\\\"paramValue\\\":\\\"\\\",\\\"id\\\":334},{\\\"paramValue\\\":\\\"https://weibo.com/1845864154/G9j6ZEsrP?type=comment\\\",\\\"id\\\":335}],[{\\\"paramValue\\\":\\\"\\\",\\\"id\\\":333},{\\\"paramValue\\\":\\\"\\\",\\\"id\\\":334},{\\\"paramValue\\\":\\\"https://weibo.com/1845864154/G9j6ZEsrP?type=comment\\\",\\\"id\\\":335}]]\",\"jsonParam\":{\"datasourceId\":\"29\",\"datasourceName\":\"微博\",\"datasourceTypeId\":\"227\",\"datasourceTypeName\":\"微博视频评论\",\"inputParamArray\":[[{\"paramValue\":\"4222184219642477\",\"id\":333},{\"paramValue\":\"\",\"id\":334},{\"paramValue\":\"https://weibo.com/1845864154/G9j6ZEsrP?type=comment\",\"id\":335}],[{\"paramValue\":\"\",\"id\":333},{\"paramValue\":\"\",\"id\":334},{\"paramValue\":\"https://weibo.com/1845864154/G9j6ZEsrP?type=comment\",\"id\":335}]],\"status\":0,\"taskName\":\"流程抓取微博视频评论\",\"workFlowTemplateId\":0},\"status\":0,\"taskName\":\"流程抓取微博视频评论\",\"workFlowTemplateId\":0,\"flowId\":\"336\",\"flowDetailId\":\"3578\",\"typeNo\":\"dataCrawl\",\"paramType\":\"0\",\"projectId\":\"401\",\"firstDetailId\":\"3577\"}";
        MvcResult mvcResult=this.mockMvc.perform(
                (MockMvcRequestBuilders.post(url).param("jsonParam", param)
                        .param("flowId", "0").param("flowDetailId", "2004")
                        .param("typeNo", "dataCrawl").param("paramType", "0")
                        .param("projectId", "288")).param("firstDetailId","1120").param("workFlowId","0"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String content=mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(content));
        assertTrue(JSONObject.fromObject(content).getInt("code")==0);
    }



    @Test
    public void CrawlTaskConnectionTest() throws Exception{
       // String getConnection="http://localhost:8080/connection/get.json?datasourceId=13";
            /*//JSONObject json= (JSONObject) WebUtil.callRemoteService(getConnection,"get",null);
            String cookie=json.getString("cookie");
            String host=json.getString("host");
            int port=json.getInt("port");
            String connectionId=json.getString("connectionId");
            JSONArray jsonArray=json.getJSONArray("exceptionMark");
            List<String> marks=JSONArray.toList(jsonArray,new String(),new JsonConfig());*/

            //String cookie="ALC=ac%3D27%26bt%3D1511320228%26cv%3D5.0%26et%3D1542856228%26ic%3D1992198656%26scf%3D%26uid%3D6358197659%26vf%3D1%26vs%3D0%26vt%3D4%26es%3D1d5c1a2e74aa4930a42e86cc07517f1c;ALF=1542856228;ALF=1542856228;LT=1511320228;SCF=Atba5E7zzU6ima-6nDMMFb1z37PBd1tkhLb3tmEp6Bc7hdROJUg-RIFColCi5ngUDdILweATNMN4vjWBxRAY-EU.;SCF=Atba5E7zzU6ima-6nDMMFb1z37PBd1tkhLb3tmEp6Bc7hdROJUg-RIFColCi5ngUDeV3Z0jfmEGqgschXj5CHBg.;SUB=_2A253EJr0DeRhGeBN7loQ-SnKzjWIHXVUZ4s8rDV_PUNbm9BeLXegkW9NHetkTy-rgyF2lbKwPx8HK0MiS7-YnUsP;SUB=_2A253EJr0DeRhGeBN7loQ-SnKzjWIHXVUZ4s8rDV8PUNbktANLUL7kW9NHetkT2hxq5FlGnqtYmox0_d2haPC3kIa;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5ZgeSw4A-2R37LohKuP1sN5NHD95Qce0-ReK.NSo-4Ws4DqcjDi--ci-z7i-zXi--4iKLWiK.7i--Xi-zRiKLhi--RiKLFi-z7P0Wbdh5t;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5ZgeSw4A-2R37LohKuP1sN5JpX5K2hUgL.Foq0SKnp1KMcSK.2dJLoIE.LxKqLBo5LBoBLxK.L1-2L1K5LxKBLBonL1-eLxKnL1-zLBoWkesije5tt;SUHB=0M2FeuoPmA7MmZ;YF-Ugrow-G0=57484c7c1ded49566c905773d5d00f82;YF-V5-G0=c6a30e994399473c262710a904cc33c5;httpsupgrade_ab=SSL;sso_info=v02m6alo5qztKWRk5ilkKOApZCjkKWRk6SljpSEpY6TgKWRk5ClkKOgpY6UjKWRk6CljpSIpZCjgZ-MhomsjIKZtZqWkL2No4y1joOEuY2zmLWOkMDA;tgc=TGT-NjM1ODE5NzY1OQ==-1511320228-gz-B93783F6543FED20DF340EE996F0143C-1";
            SiteProxyIp siteProxyIp=null;//new SiteProxyIp("118.190.138.155",9001);
            String weiboURl="http://snapshot.sogoucdn.com/websnapshot?ie=utf8&url=http%3A%2F%2Ffinance.sina.com.cn%2Fstock%2Fsnipe%2F2018-08-02%2Fdoc-ihhehtqf5385681.shtml&did=6eca4c3d7807437c-a15a5304896cf7f6-9206d6a330edfe1d82df701c95379806&k=2594e1c8faa05e1f426108b208615b33&encodedQuery=%22%E5%B9%BF%E5%B7%9E%E6%B8%AF%22&query=%22%E5%B9%BF%E5%B7%9E%E6%B8%AF%22&&p=40040108&dp=1&w=01020400&m=0&st=1";
            Map<String,String> headerMap=new HashMap<String, String>();
            //headerMap.put("Cookie",cookie);
            HttpClientHelper httpClientHelper=new HttpClientHelper();
            boolean flag=true;
           /* while (flag)
            {*/
                byte[] content = httpClientHelper
                        .doGetAndRetBytes(weiboURl, "utf-8", "utf-8", headerMap,
                                siteProxyIp).getContentBytes();
                /*BaseProcessor processor = new WeiboProcessor();
                content=processor.runProcessor(content, null, null);*/
                /*String mark=ParseUtil.checkHtmlException(marks,content);
                if(!Validate.isEmpty(mark)){
                    reollback(connectionId,WebUtil.CONNECTION_EXCEPTION,content,mark);
                    break;
                }*/
                System.out.println(new String(content,"GBK"));

          /*  }*/
    }

    public void reollback(String connectionId,String status,String mesg,String mark){
        Map<String,String> postParma=new HashMap<String, String>();
        postParma.put("connectionId",connectionId);
        postParma.put("datasourceId","13");
        postParma.put("status",status);
        postParma.put("html",mesg);
        postParma.put("mark",mark);
        String rollBack="http://localhost:8080/connection/recovery.json";
        Object object=WebUtil.callRemoteService(rollBack,"post",postParma);
        System.out.println(object.toString());
    }

    @Test
    public void testHttpClient() throws Exception{
        String url = "http://weixin.sogou.com/weixin?query=国II&_sug_type_=&s_from=input&_sug_=n&type=2&page=1&ie=utf8";
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        Map<String, String> headerMap = new HashMap<String, String>();
       // headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        //BIGipServerpool-c26-xcar-data-80=2378764042.20480.0000; _fwck_www=7aa4fb553b000d25f05b1dda9413e0c7; _appuv_www=62871d1b30bcadb0163a74a3e854b6e2; ad__city=386; _Xdwnewuv=1; uv_firstv_refers=http%3A//www.xcar.com.cn/; _PVXuv=5a0bb46d678a1; _fwck_tools=a441b651f00cd921441f27c0baf50466; _appuv_tools=7c61092b7b8dd97cbe16a4b475ed081d; _Xdwuv=5107165103862; _fwck_club=5c83daca3f9ca3afa09d1716dfbd6b87; _appuv_club=33bc05576383d1c5d17ea166961e4db7; place_prid=17; place_crid=386; place_ip=125.71.76.80_1; _locationInfo_=%7Burl%3A%22http%3A%2F%2Fchengdu.xcar.com.cn%2F%22%2Ccity_id%3A%22386%22%2Cprovince_id%3A%2217%22%2C%20city_name%3A%22%25E6%2588%2590%25E9%2583%25BD%22%7D; Hm_lvt_53eb54d089f7b5dd4ae2927686b183e0=1508292609,1510716527; Hm_lpvt_53eb54d089f7b5dd4ae2927686b183e0=1510716548; _Xdwstime=1510716648
        //headerMap.put("Cookie","SINAGLOBAL=9224376492407.246.1507878549189; httpsupgrade_ab=SSL; un=15008473372; wvr=6; SSOLoginState=1510904920; SCF=AjUhYtfZSIwcP410tjHmbVh1qIC9X1macQublRS8NoJny5H8DXHqU3dLVL-qwNl3X8WaHbME5hx9cCmSFacixp0.; SUB=_2A253CuQJDeRhGeRJ6VUY8CnIyzuIHXVUflLBrDV8PUNbmtBeLUvhkW8xxfdIV3BvpVMZtHYKBy339VODtQ..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWMXaVFLFTWb0K81w_uUAgR5JpX5KMhUgL.FozNeoM4ehMXehM2dJLoI7pydspLdLvaUKMReK-t; SUHB=0tKrIVGwdpGQE0; ALF=1542440919; _s_tentry=-; UOR=,,tech.ifeng.com; SWB=usrmdinst_15; Apache=375240778994.1814.1510905814749; ULV=1510905814756:24:13:4:375240778994.1814.1510905814749:1510722554493; WBStorage=82ca67f06fa80da0|undefined");
        //headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        // headerMap.put("Cookie","_xsrf=487d06dad7270e1d0b538342633a0245;aliyungf_tc=AQAAAOwRUgK99QcAsou+dslrJ9BOqEZx;cap_id=\"MGRjMThiZDc0ZjdkNDMzODk0ZmQ2ZjAzMmU4M2VmZDE=|1512370063|dbf4e1a38db4bfedb7687b19280d7e407e64f3fd\";l_cap_id=\"NWQ1MGMwOTg0YzcxNDE0YWExNGNkODJmOWU0YTg3NzY=|1512370063|a980353d5531f4721dda889634db12e8243ddf9e\";l_n_c=1;n_c=1;q_c1=78639a7867ee48ef9332f89b0c6247ce|1512370063000|1512370063000;r_cap_id=\"YzdiODI4ZWQwMzJjNGU3OWEzNDcxYThhN2FjNzc4NWI=|1512370063|9269096ee63f432385bbe20920085a204b4d4e70\";z_c0=\"MS4xanJOZ0JRQUFBQUFYQUFBQVlRSlZUWjA5RWx2Yk15NjBUeW5tV0RaN2p3endwX3FTdFNlX3hBPT0=|1512370077|f7205e83f08b42a08da8f424afabec8a307a0304\"");
         /*  headerMap.put("Cookie","aliyungf_tc=AQAAAParX1QahQ0Aj6zT3q2sJWsiUcHg; " +
                   //"d_c0=\"AAACTCbwxwyPTgjS-f3gR6oF-0CCNDQje0I=|1512356047\"; " +
                   //"q_c1=6ead4d0fa163454db8b62f8a168554c3|1512356049000|1512356049000; " +
                   //"_zap=2f1b037a-67ef-41f9-b83b-7626c62cf65d; " +
                   //"capsion_ticket=\"2|1:0|10:1512356053|14:capsion_ticket|44:MGJhMDMxZDAwMWZkNDdiYmFjN2VjZDMwNjA4YzJiZjg=|88c0ba62de2657583a334f7e77f1f350bef995bf3233fb1321c9f19f92fae199\";" +
                   " z_c0=\"2|1:0|10:1512356082|4:z_c0|92:Mi4xNTZob0FnQUFBQUFBQUFKTUp2REhEQ1lBQUFCZ0FsVk44Z1lTV3dBT1dfYlk3YnA3aDA3NjZPZTllM2ZUZU1UcTRn|7731bb6958959dbcc67a85c6a6e7f40c60638437f42c409678a53f8d10f8ba41\"; " +
                   //"z_c0=\"MS4xNTZob0FnQUFBQUFYQUFBQVlRSlZUY3NxRFZ2LXlNZHJUSXh6YmlOdEdSNVhYbW43dDBGV2l3PT0=|1512037579|311b04103312a190685a551fce5aa2ace5c7549b\""+
                   "_xsrf=54c8978d-09b9-48ea-8272-a4620f75e836");*/
        ////headerMap.put("Cookie","aliyungf_tc=AQAAAD25634NYg0Aj6zT3qWBByy39JO7;
        //  q_c1=6de769386f24487e930e5263e564e0a6|1512027305000|1512027305000;
        //// _xsrf=6afe00dd6729b5a86575ce49895f7741;
        //  r_cap_id=\"NjY3Yzc4MTgyNTBmNGQ5OWE3ZmY5NmQyYTYxYWIyM2M=|1512027305|9371c5982e8835d8a1e9e3b3f571bb8a90ee8b41\";
        // cap_id=\"ODk5Yzg2NDZhYmMxNDkwNWIyM2EwZGI3ZTkyNGNhZGM=|1512027305|20d4c90ef95dc536767a1719ded6a74cd252f4ba\";
        //                 d_c0=\"AEACrBoKwwyPTgx4goC2BS5F3C2e_zRAtqk=|1512027307\";
        //// l_n_c=1;
        //                 z_c0=Mi4xRXVQakF3QUFBQUFBUUFLc0dnckREQmNBQUFCaEFsVk52Z0lOV3dCTGhoWXBwQUdDaDVuM01Hb0dzblBKdF9Yb0dR|1512027326|a86134d63c028ff554141c799a2def037a4741da;
        //// _xsrf=6afe00dd6729b5a86575ce49895f7741");
        SiteProxyIp siteProxyIp =null;//new SiteProxyIp("61.134.25.106",3128);
        //headerMap.put("Host","search.58che.com");
        HttpResponse response = httpClientHelper
                .doGetAndRetBytes(url, "utf-8", "utf-8", headerMap, siteProxyIp);
        //LoggerUtil.infoTrace(response.getContent());
        String cont=new String(response.getContentBytes(),"utf-8");

        System.out.println(cont);
        //String contn1=new String(response.getContentBytes(),"GBK");
        //CharsetUtils charsetUtils=new CharsetUtils();

        //String content=charsetUtils.CharsetCheckEntrance(response.getContentBytes(),"utf-8","utf-8");
        //System.out.println(content);
    }

    @Test
    public void postClient() throws Exception{
        String url="http://47.92.21.32/heartbeat?f=030002010051D1520163B708C3F0B11276D40E-C80F-7D9A-33EF-3D38B011B653";
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        Map<String, String> headerMap = new HashMap<String, String>();
        //headerMap.put("Referer","http://v.youku.com/v_show/id_XNTc3Njc4NjY4.html?spm=a2h0k.8191407.0.0&from=s1.8-1-1.2&f=19073660&debug=flv");
        Map<String, String> postMap = new HashMap<String, String>();
        JSONObject ojc=JSONObject.fromObject("{\"vid\":\"144419667\",\"pid\":\"283b0aba3383918cf50eba5c9b331e8a93c335dbd5927568156cc994e8205e6b\",\"dma\":\"4134\",\"useArea\":0,\"bitmap\":[{\"num\":0,\"has\":true,\"fileid\":\"030002010051D1520163B708C3F0B11276D40E-C80F-7D9A-33EF-3D38B011B653\"}],\"type\":\"flv0\",\"ct\":2,\"dbd\":{\"pc\":0,\"pmps\":16},\"pos\":0,\"hint\":2,\"load\":0,\"area\":\"510100\",\"sid\":\"5a07f782eb3a427071be28d0f6380f21\",\"fid\":\"030002010051D1520163B708C3F0B11276D40E-C80F-7D9A-33EF-3D38B011B653\"}");
        postMap.put(ojc.toString(),"");
        HttpResponse response=httpClientHelper.doPost(url,postMap,"utf-8","utf-8",headerMap,null);
        String content=response.getContent();
        List list= ParseUtil.parseValue(content,"//*[@id=\"container\"]","xpath",null);

    }

    @Test
    public  void main()
    {

        long houses=60*60*1000L;
        long day=houses*24;
        long week=day*7;


        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date date=format.parse("2017-08-20 10:00:00");
            Long dateLo=date.getTime();
            Long newTime=dateLo+day*13;
            System.out.println(DateUtil.formatDate(new Date(newTime)));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


    }
    @Test
    public void test() throws Exception{
        String md5sr="99"+"http://www.aqsiq.gov.cn/zjxw/dfzjxw/dfftpxw/201707/t20170705_492475.htm";
        System.out.println(MD5Util.encrypt(md5sr+"17"));
        System.out.println(MD5Util.encrypt("9917"));
    }

    @Test
    public void postTest() throws Exception{

        String url="http://search.people.com.cn/cnpeople/search.do";
        Map<String,String> header=new HashMap<String, String>();
        header.put("Referer","http://search.people.com.cn/cnpeople/news/getNewsResult.jsp");
        header.put("Content-Type","application/x-www-form-urlencoded");

//keyword=&pageNum=1&siteName=news&facetFlag=true&nodeType=belongsId&nodeId=0&pageCode=&originName=
        Map<String,String> param=new HashMap<String, String>();
        param.put("siteName","news");
        param.put("pageNum","1");
        param.put("facetFlag","null");
        param.put("nodeType","belongsId");
        param.put("nodeId","0");
        param.put("pageCode","");
        param.put("originName","");
        param.put("keyword","奥迪");

        HttpClientHelper httpClientHelper=new HttpClientHelper();
        byte[] conby=httpClientHelper.doPostAndRetBytes(url,param,"GBK","GBK",header,null).getContentBytes();
        String content=new String(httpClientHelper.doPostAndRetBytes(url,param,"GBK","GBK",header,null)
                .getContentBytes(),"GBK");

        List list= ParseUtil.parseValue(content,"//body//text()","xpath",null);
        char[] chars=list.get(0).toString().replaceAll("([\r\n\t]+)","").toCharArray();
        for (char c:chars){
            System.out.println(c);
        }

        //System.out.println(isMessyCode(list.get(0).toString()));
    }


    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = "";//after.replaceAll("p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    System.out.println(c);
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


    @Test
    public void duanxinTest() throws Exception
    {
       /* String url="http://121.40.57.61:8080/smsApi!mt";
        Map<String,String> postMap=new HashMap<String, String>();
        postMap.put("userAccount","18101871288");
        postMap.put("sign", MD5Util.encrypt("44626"+"c6ddecf7513417c9e9a4547604ff398f").toLowerCase());
        postMap.put("cpmId","123152");
        postMap.put("mobile","13208467564");
        postMap.put("message", URLDecoder.decode("测试","utf-8"));
        postMap.put("codeNo","10690090166");
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        String content=httpClientHelper.doPost(url,postMap,"utf-8","utf-8",null,new SiteProxyIp("118.190.138.0",9001)).getContent();
        System.out.println(content);*/

        try {
            HttpClientHelper httpClientHelper = new HttpClientHelper();
            Map<String, String> postDatarMapQuokka = new HashMap<String, String>();


            postDatarMapQuokka.put("userAccount", "18101871288");
            postDatarMapQuokka.put("sign",  MD5Util.encrypt("44626"+"c6ddecf7513417c9e9a4547604ff398f").toLowerCase());

            postDatarMapQuokka.put("cpmId", "222221");
            postDatarMapQuokka.put("mobile", "13208467473");
            postDatarMapQuokka.put("message", "983634");
            postDatarMapQuokka.put("codeNo", "10690090166");

            SiteProxyIp proxyIp =null; //new SiteProxyIp("118.190.138.0", 80);

            Map<String,String> header=new HashMap<String, String>();
            header.put("Content-type","text/html");

            HttpResponse htmlContentQuokka = httpClientHelper.doPost("http://121.40.57.61:8080/smsApi!mt",
                    postDatarMapQuokka, "utf-8", "utf-8", header, proxyIp);

            String quokkaUserStr = htmlContentQuokka.getContent();
            System.out.println("发送验证码的状态:" + htmlContentQuokka.getContent());


        } catch (Exception e) {
            System.out.println("cl发送验证码出错!!"+ e);
        }

    }

    @Test
    public void testtes(){
        String tcon="{\"code\":200,\"content\":{\"recommendation\":null,\"keyword\":\"国VI\",\"sortField\":\"0\",\"optionsSearchTypes\":null,\"curPage\":1,\"results\":[{\"code\":\"1011111011100111011100000001110001101000110000000000000000000000\",\"contentId\":\"3e218e5dae1249bf8566d15af8ddc176\",\"des\":\"近日，由国家发改委委托，国家能源局、省经信委以及中国国际工程咨询公司相关负责人来我市对国六标准油品升级工作开展督查。检查组重点检查了国六标准成品油保供方案和应急预案落实情况，包括油品来源、调运、置换和应急保障等内容，部门联合监管与执法情况，市级开展联合检查情况，取缔非法加油站点及流动加油车情况进行了督查。\",\"imgUrl\":null,\"keyword\":\"加油站;汽柴油;油品;承诺书;成品油\",\"listResult\":null,\"pubtime\":\"2017-09-13 11:04:01\",\"sitename\":\"山东频道\",\"title\":\"济宁加油站签承诺书 月底全面升级国VI汽柴油\",\"url\":\"http://www.sd.xinhuanet.com/sd/jin/2017-09/13/c_1121655710.htm\"},{\"code\":null,\"contentId\":\"333179806112081121322895\",\"des\":\"今年9月底前，济南、淄博、济宁、德州、聊城、滨州、菏泽7市要全部供应符合国VI标准的汽柴油，禁止销售普通柴油。\",\"imgUrl\":null,\"keyword\":\"山东;普通柴油;全省加油站;滨州;菏泽\",\"listResult\":null,\"pubtime\":\"2017-07-15 08:27:17\",\"sitename\":\"新华网\",\"title\":\"9月底前山东七市全部供应符合国VI标准的汽柴油\",\"url\":\"www.sd.xinhuanet.com/news/2017-07/15/c_1121322895.htm\"},{\"code\":null,\"contentId\":\"3243367681197129092575\",\"des\":\"国家能源局6月23日对外发布第六阶段车用汽油和车用柴油两项国家强制性标准的征求意见稿。根据征求意见稿，第六阶段车用汽油标准降低了车用汽油的烯烃含量、芳烃含量、苯含量，硫含量限值与第五阶段标准相同。\",\"imgUrl\":null,\"keyword\":\"车用柴油;汽柴油;车用汽油;VI;欧洲标准\",\"listResult\":null,\"pubtime\":\"2016-06-27 09:40:50\",\"sitename\":\"新华网\",\"title\":\"国VI车用汽柴油将达到甚至优于欧洲标准\",\"url\":\"news.xinhuanet.com/fortune/2016-06/27/c_129092575.htm\"},{\"code\":null,\"contentId\":\"32426148611971119100448\",\"des\":\"国家能源局２３日对外发布第六阶段车用汽油和车用柴油两项国家强制性标准的征求意见稿。\",\"imgUrl\":null,\"keyword\":\"车用;柴油;含量;汽油;炼油\",\"listResult\":null,\"pubtime\":\"2016-06-23 16:11:06\",\"sitename\":\"新华网\",\"title\":\"国ＶＩ车用汽柴油将达到甚至优于欧洲标准\",\"url\":\"news.xinhuanet.com/fortune/2016-06/23/c_1119100448.htm\"},{\"code\":null,\"contentId\":\"324261481112861119100448\",\"des\":\"国家能源局２３日对外发布第六阶段车用汽油和车用柴油两项国家强制性标准的征求意见稿。\",\"imgUrl\":null,\"keyword\":\"车用;柴油;含量;汽油;炼油\",\"listResult\":null,\"pubtime\":\"2016-06-23 16:11:06\",\"sitename\":\"新华网\",\"title\":\"国ＶＩ车用汽柴油将达到甚至优于欧洲标准\",\"url\":\"news.xinhuanet.com/2016-06/23/c_1119100448.htm\"},{\"code\":null,\"contentId\":\"321368572112251118279651\",\"des\":\"国家能源局副局长王晓林8日表示，成品油国VI标准有望2019年实施。在推动电动汽车充电基础设施建设方面，王晓林说，推动电动汽车，困难在于充电桩欠缺，目前正在全力推进电动汽车充电桩建设，在人口密集地区要按一定比例建充电桩。\",\"imgUrl\":null,\"keyword\":\"成品油;国家能源;王晓林;VI;2019年\",\"listResult\":null,\"pubtime\":\"2016-03-09 15:24:05\",\"sitename\":\"新华网\",\"title\":\"国家能源局：成品油国VI标准有望2019年实施\",\"url\":\"www.hn.xinhuanet.com/Business/car/2016-03/09/c_1118279651.htm\"},{\"code\":null,\"contentId\":\"321356169112071118275309\",\"des\":\"国家能源局副局长王晓林８日表示，成品油国ＶＩ标准有望２０１９年实施。\",\"imgUrl\":null,\"keyword\":\"王晓林;大气污染;副局长;充电;成品油\",\"listResult\":null,\"pubtime\":\"2016-03-09 10:17:37\",\"sitename\":\"新华网\",\"title\":\"国家能源局：成品油国ＶＩ标准有望2019年实施\",\"url\":\"www.tj.xinhuanet.com/tt/jcdd/2016-03/09/c_1118275309.htm\"},{\"code\":null,\"contentId\":\"321351034112261118273845\",\"des\":\"国家能源局副局长王晓林８日表示，成品油国ＶＩ标准有望２０１９年实施。\",\"imgUrl\":null,\"keyword\":\"王晓林;大气污染;副局长;充电;成品油\",\"listResult\":null,\"pubtime\":\"2016-03-09 09:22:06\",\"sitename\":\"新华网\",\"title\":\"国家能源局：成品油国ＶＩ标准有望２０１９年实施\",\"url\":\"www.jx.xinhuanet.com/news/24rdxw/2016-03/09/c_1118273845.htm\"},{\"code\":null,\"contentId\":\"32134834811188135169703\",\"des\":\"国家能源局副局长王晓林８日表示，成品油国ＶＩ标准有望２０１９年实施。在推动电动汽车充电基础设施建设方面，王晓林说，推动电动汽车，困难在于充电桩欠缺，目前正在全力推进电动汽车充电桩建设，在人口密集地区要按一定比例建充电桩。\",\"imgUrl\":null,\"keyword\":\"成品油;国家能源;王晓林;ＶＩ;２０１９年\",\"listResult\":null,\"pubtime\":\"2016-03-09 08:51:50\",\"sitename\":\"新华网\",\"title\":\"国家能源局：成品油国VI标准有望2019年实施\",\"url\":\"sh.xinhuanet.com/2016-03/09/c_135169703.htm\"},{\"code\":null,\"contentId\":\"321347037112261118272646\",\"des\":\"国家能源局副局长王晓林8日表示,成品油国ＶＩ标准有望２０１９年实施。目前，力争让农村老百姓烧比较高质量的煤，终极目标是让他们用电和气。\",\"imgUrl\":null,\"keyword\":\"成品油;王晓林;能源局;电动汽车;充电\",\"listResult\":null,\"pubtime\":\"2016-03-09 08:35:11\",\"sitename\":\"新华网\",\"title\":\"成品油国VI标准有望2019年实施\",\"url\":\"www.jx.xinhuanet.com/news/fmbd/2016-03/09/c_1118272646.htm\"}],\"pageCount\":2,\"resultCount\":18}}";
        String expressionste="content.results";
        String [] expressions=expressionste.split("\\.");
        if(Validate.isEmpty(expressions)){
            expressions=new String[0];
        }else{
            if(tcon.indexOf("{")>-1)
                tcon=tcon.substring(tcon.indexOf("{"),tcon.lastIndexOf("}")+1);
        }

        List<String> resultList=new ArrayList<String>();
        try
        {
            ParseUtil.JSONReadArray(tcon,expressions,0,resultList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testHttp() throws Exception{
        String url="http://search.cctv.com/search.php?qtext=汽车&type=web&page=1&datepid=1&vtime=-1&sort=date";
        Map<String,String> head=new HashMap<String, String>();
        //head.put("Cookie","UM_distinctid=160c02ef7e2594-09f2fb647c1a3c-19107055-1fa400-160c02ef7e36f; CNZZDATA1975683=cnzz_eid%3D1056749093-1515051794-http%253A%252F%252Fsearch.chinadaily.com.cn%252F%26ntime%3D1515051794; __asc=c2b15a55160c02ef800a28e8c33; __auc=c2b15a55160c02ef800a28e8c33");
       // head.put("Cookie","web_search_key=%E5%A4%A7%E4%BC%97");
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        byte [] bytes=httpClientHelper.doGetAndRetBytes(url,"utf-8","utf-8",head,null).getContentBytes();
        String content=new String(bytes,"utf-8");
        List<String>contentlist=ParseUtil.parseValue(content,"//div[@class=\"center_side\"]/div[@class=\"list\"]","xpath",JSONObject.fromObject("{}"));
        LoggerUtil.debugTrace(content);
        content=ParseUtil.getContentChangeHtmlElem(content);
        LoggerUtil.debugTrace("===="+content);//_PVXuv=123456789
    }

    @Test
    public void getPage()throws Exception{
        CrawlTaskBO crawlTaskBO=new CrawlTaskBO();
        crawlTaskBO.setDatasourceTypeId(44);
        CrawlSubTaskBO crawlSubTaskBO=new CrawlSubTaskBO();
        CrawlTaskParseJob crawlTaskParseJob=new CrawlTaskParseJob(crawlTaskBO,"","",crawlSubTaskBO);
        String currentPage=System.currentTimeMillis()+"";
        String url="https://www.huxiu.com/v2_action/article_list?huxiu_hash_code=b3877ba209da3b1b5195f935b061dcfd&last_dateline="+currentPage;
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        String content=httpClientHelper.doGet(url,"utf-8","utf-8",new HashMap<String, String>(),null).getContent();
        Map<String,String> crawlMap=new HashMap<String, String>();
        //{"keyword":"b3877ba209da3b1b5195f935b061dcfd","type":"javascript","page":"page_1520843965000","huxiuhas
        //hcode":"b3877ba209da3b1b5195f935b061dcfd"}
        crawlMap.put("keyword","");
        crawlMap.put("type","javascript");
        crawlMap.put("page","01");
        crawlMap.put("huxiuhashcode","b3877ba209da3b1b5195f935b061dcfd");
        crawlTaskParseJob.nextPageNum(currentPage,content,crawlMap);
    }

    @Test
    public void crawlInputMarge() throws Exception{
        JSONObject oldJSON=JSONObject.fromObject("{}");
        oldJSON.put("url","http://www.iqiyi.com");
        JSONObject newJSON=JSONObject.fromObject("{}");
        newJSON.put("url","http://www.iqiyi.com");
        CrawlTaskController crawlTaskController=new CrawlTaskController();
        JSONObject object=crawlTaskController.filterOldCrawlInputParam(oldJSON,newJSON);
        System.out.println(object);
    }

    @Test
    public void testDoGet()throws Exception{
        String url="http://snapshot.sogoucdn.com/websnapshot?ie=utf8&url=http://www.tianyancha.com/company/196449350?hi=s&did=3a5003723b86a70f-a575be80819d7640-b857821ce6322a6dae6a7e4c9b22aa76&k=d8a1cdada14d81e1d21b4e35e154a437&encodedQuery=\"艾芬达\"+&query=\"艾芬达\"+&&w=01020400&m=0&st=0";
        Map<String,String> headMap=new HashMap<String, String>();
        HttpClientHelper httpClientHelper=new HttpClientHelper();
        SiteProxyIp siteProxyIp=new SiteProxyIp("118.190.82.249",9001);
        String content=httpClientHelper.doGet(url,"utf-8","gbk",headMap,siteProxyIp).getContent();
        System.out.println(content);
    }

    @Test
    public void doJob()throws Exception{
            CrawlSubTaskBO crawlSubTaskBO = new CrawlSubTaskBO();
            crawlSubTaskBO.setComplatePage("");
            crawlSubTaskBO.setCrawlDataNum(0);
            crawlSubTaskBO.setCrawlUrl("http://www.cn-em.com/news/?2977.htm");
            crawlSubTaskBO.setId(273683);
            crawlSubTaskBO.setParamValue("{\"url\":\"http://www.cn-em.com/news/?2977.htm\"}");
        new CrawlThread(crawlSubTaskBO, 239011, 1 + "", 1 + "", 273683).doJob();
    }


}


