package com.transing.crawl.web.controller;


import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jeeframework.testframework.AbstractSpringBaseControllerTest;
import com.transing.crawl.util.CharsetUtils;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.client.HttpResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.http.HttpException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CrawlRuleTest extends AbstractSpringBaseControllerTest {

    //测试url中带问号,解析错误
    //例如 http://www.newzhongke.com/News/Detail.asp?44.html  http://www.cn-em.com/news/?2977.htm
    @Test
    public void testUrlExistQuestionMark() throws Exception {
        HttpClientHelper httpClientHelper = new HttpClientHelper();

        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");

        HttpResponse response = httpClientHelper.doGetAndRetBytes("http://www.newzhongke.com/News/Detail.asp?44.html",
                "utf-8", "utf-8", header, null);
        String s = new String(response.getContentBytes(), "utf-8");

        HttpResponse response2 = httpClientHelper.doGetAndRetBytes("http://www.cn-em.com/news/?2977.htm",
                "utf-8", "utf-8", header, null);
        String s2 = new String(response2.getContentBytes(), "gbk");

        System.out.println(s);
        System.out.println(s2);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response2.getStatusCode(), 200);
    }

    @Test
    public void testJsonArray() throws Exception {
        JSONArray fieldArray = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("1", "$title$");
        obj.put("2", "abc");

        JSONObject obj2 = new JSONObject();
        obj2.put("1", "$url$");
        obj2.put("2", "abc");

        fieldArray.add(obj);
        fieldArray.add(obj2);

        String ss = "\\$(.*?)\\$";
        String temp;
        Pattern pa = Pattern.compile(ss);
        Matcher ma;
        ma = pa.matcher(fieldArray.toString());

        while (ma.find()) {
            temp = ma.group();
            if (temp != null) {
                System.out.println("temp: " + temp);
            }
        }

        System.out.println(fieldArray.toString());
        Assert.assertTrue(fieldArray.toString().contains("$"));
    }


    @Test
    public void JsonObjectTest() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("1", "$title$");
        obj.put("2", "abc");

        obj.put("3", "$url$");
        obj.put("4", "def");


        System.out.println(obj.toString());
    }

    //无$的字段解析
    @Test
    @Rollback(false)
    public void crawlRuleFieldNoTest() throws Exception {
        String testUrl = "http://finance.sina.com.cn/chanjing/gsnews/2018-03-22/doc-ifysnevk5700537.shtml\n" +
                "https://finance.sina.cn/chanjing/gdxw/2018-04-09/detail-ifyuwqez7084188.d.html";
        String param = "{}";
        String headers = "[]";
        String crawlMethod = "GET";
        String requestEncoding = "UTF-8";
        String responseEncoding = "UTF-8";
        String detailPreProc = "[]";
        String detailParse = "[]";
        String detailSuffProc = "[]";
        String pageType = "2";
        String datasourceId = "51";
        String field = "[{\"crawlRuleDetailId\":73,\"id\":409,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":135,\"storageTypeFieldId\":16,\"fieldEnName\":\"title\",\"fieldDesc\":\"列表标题\",\"fieldLength\":40,\"fieldCnName\":\"列表标题\",\"fieldTypeName\":\"text\",\"fieldParse\":[{\"createTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":409,\"id\":222,\"lastmodifyTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"NewsTitle\",\"parseType\":\"3\",\"$$hashKey\":\"object:24\"}],\"fieldSuffProc\":[{\"createTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":409,\"id\":33,\"lastmodifyTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118}}]}]";

        String requestURI = "/crawlRule/testDetailRuleFieldProcessor.json";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(requestURI).param("testInLogin", "no")
                .param("param", param).param("headers", headers).param("crawlMethod", crawlMethod)
                .param("requestEncoding", requestEncoding).param("responseEncoding", responseEncoding)
                .param("detailPreProc", detailPreProc).param("detailParse", detailParse)
                .param("detailSuffProc", detailSuffProc).param("pageType", pageType).param("datasourceId", datasourceId)
                .param("field", field).param("testUrl", testUrl)
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(content));
        assertTrue(JSONObject.fromObject(content).getInt("code") == 0);
    }


    //有$的字段解析
    @Test
    @Rollback(false)
    public void parseValueTest() throws Exception {
        String testUrl = "http://www.askci.com/Home/AddMoreNewsList";
        String param = "{}";
        String headers = "[]";
        String crawlMethod = "GET";
        String requestEncoding = "UTF-8";
        String responseEncoding = "UTF-8";
        String detailPreProc = "[]";
        String detailParse = "[{\"crawlRuleDetailId\":73,\"createTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"id\":34,\"lastmodifyTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$..listNews[:2]\",\"parseType\":\"3\",\"$$hashKey\":\"object:18\"}]";
        String detailSuffProc = "[]";
        String pageType = "1";
        String datasourceId = "59";
        String field = "[{\"crawlRuleDetailId\":73,\"id\":409,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":135,\"storageTypeFieldId\":16,\"fieldEnName\":\"title\",\"fieldDesc\":\"列表标题\",\"fieldLength\":40,\"fieldCnName\":\"列表标题\",\"fieldTypeName\":\"text\",\"fieldParse\":[{\"createTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":409,\"id\":222,\"lastmodifyTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$..NewsTitle\",\"parseType\":\"3\",\"$$hashKey\":\"object:24\"}],\"fieldSuffProc\":[{\"createTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":409,\"id\":33,\"lastmodifyTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"processorId\":\"2\",\"processorValue\":\"var title = \\\"@title@\\\";\\nreturn \\\"123\\\" + title;\",\"processorId1\":\"\",\"$$hashKey\":\"object:26\"}],\"$$hashKey\":\"object:4\",\"storageTypeFieldBo\":{\"createTime\":{\"date\":26,\"day\":2,\"hours\":23,\"minutes\":26,\"month\":8,\"seconds\":50,\"time\":1506439610000,\"timezoneOffset\":-480,\"year\":117},\"decimalLength\":0,\"fieldCnName\":\"列表标题\",\"fieldDesc\":\"列表标题\",\"fieldEnName\":\"title\",\"fieldLength\":40,\"fieldType\":\"text\",\"id\":16,\"lastmodifyTime\":{\"date\":29,\"day\":2,\"hours\":22,\"minutes\":41,\"month\":4,\"seconds\":6,\"time\":1527604866000,\"timezoneOffset\":-480,\"year\":118},\"storageTypeId\":9}},{\"crawlRuleDetailId\":73,\"id\":410,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":135,\"storageTypeFieldId\":17,\"fieldEnName\":\"url\",\"fieldDesc\":\"网站链接\",\"fieldLength\":200,\"fieldCnName\":\"网站链接\",\"fieldTypeName\":\"text\",\"fieldParse\":[{\"createTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":410,\"id\":223,\"lastmodifyTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$title$\",\"parseType\":\"4\",\"$$hashKey\":\"object:31\"}],\"fieldSuffProc\":[],\"$$hashKey\":\"object:5\"},{\"crawlRuleDetailId\":73,\"id\":411,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":135,\"storageTypeFieldId\":18,\"fieldEnName\":\"summary\",\"fieldDesc\":\"内容摘要\",\"fieldLength\":200,\"fieldCnName\":\"内容摘要\",\"fieldTypeName\":\"text\",\"fieldParse\":[],\"fieldSuffProc\":[{\"createTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":411,\"id\":34,\"lastmodifyTime\":{\"date\":29,\"day\":2,\"hours\":14,\"minutes\":45,\"month\":4,\"seconds\":32,\"time\":1527576332000,\"timezoneOffset\":-480,\"year\":118},\"processorId\":\"2\",\"processorValue\":\"return \\\"456\\\" + $title$;\",\"processorId1\":\"\",\"$$hashKey\":\"object:35\"}],\"$$hashKey\":\"object:6\"},{\"crawlRuleDetailId\":73,\"id\":412,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":135,\"storageTypeFieldId\":19,\"fieldEnName\":\"publishtime\",\"fieldDesc\":\"发布时间\",\"fieldLength\":40,\"fieldCnName\":\"发布时间\",\"fieldTypeName\":\"datetime\",\"fieldParse\":[],\"fieldSuffProc\":[],\"$$hashKey\":\"object:7\"},{\"crawlRuleDetailId\":73,\"id\":413,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":135,\"storageTypeFieldId\":29,\"fieldEnName\":\"content\",\"fieldDesc\":\"内容\",\"fieldLength\":10000,\"fieldCnName\":\"内容\",\"fieldTypeName\":\"text\",\"fieldParse\":[],\"fieldSuffProc\":[],\"$$hashKey\":\"object:8\"},{\"crawlRuleDetailId\":73,\"id\":414,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":135,\"storageTypeFieldId\":46,\"fieldEnName\":\"author\",\"fieldDesc\":\"作者\",\"fieldLength\":255,\"fieldCnName\":\"作者\",\"fieldTypeName\":\"text\",\"fieldParse\":[],\"fieldSuffProc\":[],\"$$hashKey\":\"object:9\"},{\"crawlRuleDetailId\":73,\"id\":415,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":135,\"storageTypeFieldId\":47,\"fieldEnName\":\"source\",\"fieldDesc\":\"来源\",\"fieldLength\":255,\"fieldCnName\":\"来源\",\"fieldTypeName\":\"text\",\"fieldParse\":[],\"fieldSuffProc\":[],\"$$hashKey\":\"object:10\"}]";

        String requestURI = "/crawlRule/testDetailRuleFieldProcessor.json";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(requestURI).param("testInLogin", "no")
                .param("param", param).param("headers", headers).param("crawlMethod", crawlMethod)
                .param("requestEncoding", requestEncoding).param("responseEncoding", responseEncoding)
                .param("detailPreProc", detailPreProc).param("detailParse", detailParse)
                .param("detailSuffProc", detailSuffProc).param("pageType", pageType).param("datasourceId", datasourceId)
                .param("field", field).param("testUrl", testUrl)
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(content));
        assertTrue(JSONObject.fromObject(content).getInt("code") == 0);
    }

    @Test
    @Rollback(false)
    public void jsonPathTest() throws Exception {
        String content = "{\"rv\":{\"Flag\":1,\"ReturnMsg\":\"\"},\"listNews\":[{\"NewsId\":\"NI000000000001123898\",\"CreateDate\":\"\\/Date(1527497596137)\\/\",\"StrCreateDate\":\"2018-05-28 16:53:16\",\"NewsTitle\":\"2018年第17周彩电畅销机型排行榜分析：海信品牌智能电视最畅销（附榜单）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/574e573e-83c3-4fc0-bb1a-dd60527f0d59-366x248.png\",\"NewsSavePath\":\"http://s.askci.com/news/chanxiao/20180528/1653161123898.shtml\",\"NewsContent\":\"\\t中商情报网讯：2018年第17周全国彩电畅销机型排行榜已揭晓，据数据统计显示：第十七周（2018年4月23日2018年4月29日）液晶电视畅销机型前十名分别是：海信LED55N3000U、长虹55U1、海信LED55MU7...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"家电\",\"TagUrl\":\"/News/List/tag-家电\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"智能电视\",\"TagUrl\":\"/News/List/tag-智能电视\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"彩电\",\"TagUrl\":\"http://www.askci.com/News/List/tag-彩电\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"液晶电视\",\"TagUrl\":\"http://www.askci.com/News/List/tag-液晶电视\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123897\",\"CreateDate\":\"\\/Date(1527497020473)\\/\",\"StrCreateDate\":\"2018-05-28 16:43:40\",\"NewsTitle\":\"2018年第一季度兰州各区GDP排行榜：七里河突破百亿 榆中增速最高（附榜单）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/cce2c5e4-d48f-41cf-a64d-fced9b99843e-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/finance/20180528/1643411123897.shtml\",\"NewsContent\":\"\\t中商情报网讯：2018年第一季度兰州经济呈现稳中向好的发展态势，主要指标增速保持在合理区间，实现了建设美丽幸福新城关的良好开局。兰州实现地区生产总值533.75亿元，同比增长7.4%。其中：第...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"金融\",\"TagUrl\":\"http://www.askci.com/News/List/tag-金融\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"经济\",\"TagUrl\":\"http://www.askci.com/News/List/tag-经济\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123895\",\"CreateDate\":\"\\/Date(1527496020523)\\/\",\"StrCreateDate\":\"2018-05-28 16:27:00\",\"NewsTitle\":\"2018年第一季度甘肃各市GDP排行榜：兰州总量第一 甘南增速第一（附榜单）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/c4fb2e6b-0c45-4f0f-94e0-8d5da6326e20-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/finance/20180528/1627011123895.shtml\",\"NewsContent\":\"\\t中商情报网讯：2018年一季度甘肃经济开局平稳向好，质量效益提升。一季度甘肃生产总值1575.8亿元，同比增长5.3%，增速居全国第26位。其中，第一产业增加值117.4亿元，增长4.0%。第二产业增加值...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"金融\",\"TagUrl\":\"http://www.askci.com/News/List/tag-金融\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"经济\",\"TagUrl\":\"http://www.askci.com/News/List/tag-经济\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123894\",\"CreateDate\":\"\\/Date(1527495742777)\\/\",\"StrCreateDate\":\"2018-05-28 16:22:22\",\"NewsTitle\":\"娃哈哈给员工六一放假回家陪孩子！这么有爱 娃哈哈到底是一家怎样的公司？\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/4c646fad-53f2-476d-902f-910a399a98b7-366x248.png\",\"NewsSavePath\":\"http://s.askci.com/news/qiye/20180528/1622241123894.shtml\",\"NewsContent\":\"\\t中商情报网讯：“六一”儿童节马上就要到了，根据国家规定，6月1日当天不满14周岁的少年儿童放假1天，可当天是工作日，家长却需要上班，没法陪孩子。\\t然而，近日娃哈哈的一则放假通知火爆朋友...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"含乳饮料\",\"TagUrl\":\"http://www.askci.com/News/List/tag-含乳饮料\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"娃哈哈\",\"TagUrl\":\"http://www.askci.com/news/list/tag-娃哈哈\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"六一儿童节\",\"TagUrl\":\"http://www.askci.com/news/list/tag-六一儿童节/\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"饮料\",\"TagUrl\":\"http://www.askci.com/News/List/tag-饮料\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123893\",\"CreateDate\":\"\\/Date(1527495686113)\\/\",\"StrCreateDate\":\"2018-05-28 16:21:26\",\"NewsTitle\":\"2018年5月单周电影票房排行榜：《超时空同居》票房反超《复联3》夺冠（5.21-5.27）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/1bf0f470-2fd2-4dbc-8d2d-6f0e257a6af4-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/chanye/20180528/1621271123893.shtml\",\"NewsContent\":\"\\t中商情报网讯：2018年第21周（5月21日至5月27日），全国票房继续下滑。全国单周电影票房为67947万元，环比下滑37%，共计2014979放映场次，环比上涨5%，上周观影人次1990万，环比下降34%。\\t上周...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"电影\",\"TagUrl\":\"http://www.askci.com/News/List/tag-电影\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"票房\",\"TagUrl\":\"http://www.askci.com/News/List/tag-票房\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123891\",\"CreateDate\":\"\\/Date(1527494566077)\\/\",\"StrCreateDate\":\"2018-05-28 16:02:46\",\"NewsTitle\":\"两张图看懂在线教育市场及发展趋势：2018年市场规模将有望突破3000亿元\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/eb25e26c-ded8-4a2e-9f18-2fd8e43d10bc-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/chanye/20180528/1602461123891.shtml\",\"NewsContent\":\"\\t中商情报网讯：随着互联网和信息技术的快速发展，人们获取知识的方式和途径发生了巨大的变化，特别是从互联网到移动互联网，创造了跨时空的生活、工作和学习方式，使知识获取的方式发生了根本...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"互联网\",\"TagUrl\":\"http://www.askci.com/News/List/tag-互联网\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"在线教育\",\"TagUrl\":\"http://www.askci.com/News/List/tag-在线教育\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123890\",\"CreateDate\":\"\\/Date(1527494284930)\\/\",\"StrCreateDate\":\"2018-05-28 15:58:04\",\"NewsTitle\":\"2018年第一季度15个副省级城市社会消费品零售总额排行榜：武汉不敌成都\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/06247483-8fae-49d7-99c5-4d69ccb487f1-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/finance/20180528/1558051123890.shtml\",\"NewsContent\":\"\\t中商情报网讯：一季度，全国社会消费品零售总额90275亿元，同比增长9.8%，增速比1-2月份加快0.1个百分点，比上年同期回落0.2个百分点。按经营单位所在地分，城镇消费品零售额77096亿元，增长9....\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"经济\",\"TagUrl\":\"http://www.askci.com/News/List/tag-经济\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"金融\",\"TagUrl\":\"http://www.askci.com/News/List/tag-金融\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123888\",\"CreateDate\":\"\\/Date(1527493464653)\\/\",\"StrCreateDate\":\"2018-05-28 15:44:24\",\"NewsTitle\":\"2018年5月电影市场周报：新片票房表现乏力  大盘进一步下跌37% （5.21-5.27）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/13fab52b-b1b6-425b-813b-35f30320b9e9-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/chanye/20180528/1544251123888.shtml\",\"NewsContent\":\"\\t中商情报网讯：2018年第21周（5月21日至5月27日），全国票房继续下滑。全国单周电影票房为67947万元，环比下滑37%，共计2014979放映场次，环比上涨5%，上周观影人次1990万，环比下降34%。\\t一周...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"电影\",\"TagUrl\":\"http://www.askci.com/News/List/tag-电影\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"票房\",\"TagUrl\":\"http://www.askci.com/News/List/tag-票房\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123886\",\"CreateDate\":\"\\/Date(1527491512687)\\/\",\"StrCreateDate\":\"2018-05-28 15:11:52\",\"NewsTitle\":\"南京和苏州哪一个城市工资高？六张图带你看懂江苏各市平均工资情况（图）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/99d84807-f24c-46e9-8f18-a19f2f7f25dc-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/finance/20180528/1511541123886.shtml\",\"NewsContent\":\"\\t中商情报网讯：2017年，江苏省城镇非私营单位就业人员年平均工资为78267元，与2016年相比名义增长9.4%。城镇私营单位就业人员年平均工资为49345元，名义增长4.6%。\\t非私营单位就业人员年平均工...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"经济\",\"TagUrl\":\"http://www.askci.com/News/List/tag-经济\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"金融\",\"TagUrl\":\"http://www.askci.com/News/List/tag-金融\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]},{\"NewsId\":\"NI000000000001123885\",\"CreateDate\":\"\\/Date(1527490837770)\\/\",\"StrCreateDate\":\"2018-05-28 15:00:37\",\"NewsTitle\":\"2018年5月中国食品饮料行业周报：2018年营养获取指数发布 雀巢第一（5.21-5.25）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/03/9ca8ecd3-e15f-41f8-857a-24bb56d3aa3e-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/chanye/20180528/1500381123885.shtml\",\"NewsContent\":\"\\t一、食品饮料行业板块走势\\t上周（5.21-5.25）食品饮料指数下跌2.22%，与沪深300持平，跑输上证综指0.6个百分点，板块日均成交额155.9亿元。食品饮料子行业中其他酒类、啤酒上涨幅度最大，分别...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"食品\",\"TagUrl\":\"http://www.askci.com/News/List/tag-食品\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"乳制品\",\"TagUrl\":\"http://www.askci.com/News/List/tag-乳制品\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"葡萄酒\",\"TagUrl\":\"http://www.askci.com/News/List/tag-葡萄酒\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"饮料\",\"TagUrl\":\"http://www.askci.com/News/List/tag-饮料\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]}],\"lastDate\":\"2018-05-28 15:00:37\"}";

        ReadContext ctx = JsonPath.parse(content);
        List<Object> list = ctx.read("$..listNews[2]");

        System.out.println("content " + content);

        List<String> list2 = new ArrayList<>();


        Object json = new JSONTokener(list.toString()).nextValue();

        if (json instanceof JSONArray){
            Object json2 = new JSONTokener(((JSONArray) json).get(0).toString()).nextValue();

            if (json2 instanceof JSONArray) {
                JSONArray jsonArray = JSONArray.fromObject(list.get(0));

                if (jsonArray != null && jsonArray.size() > 0) {
                    for (Object object : jsonArray) {
                        list2.add(object.toString());
                    }
                }
            } else {
                JSONArray jsonArray = JSONArray.fromObject(list);

                if (jsonArray != null && jsonArray.size() > 0) {
                    for (Object object : jsonArray) {
                        list2.add(object.toString());
                    }
                }
            }
        } else {
            list2.add(list.toString());
        }

        System.out.println("list: " + list2);
        System.out.println(list2.size());
    }

    @Test
    @Rollback(false)
    public void jsonPathTest2() throws Exception {
        String content = "{\"NewsId\":\"NI000000000001123902\",\"CreateDate\":\"/Date(1527499959743)/\",\"StrCreateDate\":\"2018-05-28 17:32:39\",\"NewsTitle\":\"2018年第一季度全国各省会城市GDP排行榜：成都突破3千亿 广州失落（附榜单）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/fc9e7147-6379-4254-b631-484e308827ca-366x248.png\",\"NewsSavePath\":\"http://www.askci.com/news/finance/20180528/1732401123902.shtml\",\"NewsContent\":\"\\t中商情报网讯：目前，除合肥和拉萨外，其他省会城市均公布了2018年第一季度经济数据。广州经济依旧霸榜，GDP总量逼近5千亿，达到4954.02亿元，同比增长4.3%。成都第二，GDP总量突破3千亿，达到...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"金融\",\"TagUrl\":\"http://www.askci.com/News/List/tag-金融\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"经济\",\"TagUrl\":\"http://www.askci.com/News/List/tag-经济\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]}";
        ReadContext ctx = JsonPath.parse(content);

        List<Object> authorsOfBooksWithISBN = ctx.read("$..NewsTitle");

        System.out.println("authorsOfBooksWithISBN: " + authorsOfBooksWithISBN.get(0));

    }

    @Test
    @Rollback(false)
    public void parseValueTest2() throws Exception {
        String testUrl = "http://www.askci.com/Home/AddMoreNewsList";
        String param = "{}";
        String headers = "[]";
        String crawlMethod = "GET";
        String requestEncoding = "UTF-8";
        String responseEncoding = "UTF-8";
        String detailPreProc = "[]";
        String detailParse = "[{\n" +
                "\t\t\t\"crawlRuleDetailId\": 73,\n" +
                "\t\t\t\"createTime\": {\n" +
                "\t\t\t\t\"date\": 29,\n" +
                "\t\t\t\t\"day\": 2,\n" +
                "\t\t\t\t\"hours\": 23,\n" +
                "\t\t\t\t\"minutes\": 1,\n" +
                "\t\t\t\t\"month\": 4,\n" +
                "\t\t\t\t\"seconds\": 32,\n" +
                "\t\t\t\t\"time\": 1527606092000,\n" +
                "\t\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\t\"year\": 118\n" +
                "\t\t\t},\n" +
                "\t\t\t\"id\": 34,\n" +
                "\t\t\t\"lastmodifyTime\": {\n" +
                "\t\t\t\t\"date\": 29,\n" +
                "\t\t\t\t\"day\": 2,\n" +
                "\t\t\t\t\"hours\": 23,\n" +
                "\t\t\t\t\"minutes\": 1,\n" +
                "\t\t\t\t\"month\": 4,\n" +
                "\t\t\t\t\"seconds\": 32,\n" +
                "\t\t\t\t\"time\": 1527606092000,\n" +
                "\t\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\t\"year\": 118\n" +
                "\t\t\t},\n" +
                "\t\t\t\"parseExpression\": \"$..listNews\",\n" +
                "\t\t\t\"parseType\": \"3\",\n" +
                "\t\t\t\"$$hashKey\": \"object:18\"\n" +
                "\t\t}]";
        String detailSuffProc = "[]";
        String pageType = "1";
        String datasourceId = "59";
        String field = "[{\n" +
                "\t\"crawlRuleDetailId\": 73,\n" +
                "\t\"id\": 409,\n" +
                "\t\"isNull\": \"0\",\n" +
                "\t\"isUnique\": \"0\",\n" +
                "\t\"ruleId\": 135,\n" +
                "\t\"storageTypeFieldId\": 16,\n" +
                "\t\"fieldEnName\": \"title\",\n" +
                "\t\"fieldDesc\": \"列表标题\",\n" +
                "\t\"fieldLength\": 40,\n" +
                "\t\"fieldCnName\": \"列表标题\",\n" +
                "\t\"fieldTypeName\": \"text\",\n" +
                "\t\"fieldParse\": [{\n" +
                "\t\t\"createTime\": {\n" +
                "\t\t\t\"date\": 29,\n" +
                "\t\t\t\"day\": 2,\n" +
                "\t\t\t\"hours\": 23,\n" +
                "\t\t\t\"minutes\": 1,\n" +
                "\t\t\t\"month\": 4,\n" +
                "\t\t\t\"seconds\": 32,\n" +
                "\t\t\t\"time\": 1527606092000,\n" +
                "\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\"year\": 118\n" +
                "\t\t},\n" +
                "\t\t\"detailFieldId\": 409,\n" +
                "\t\t\"id\": 222,\n" +
                "\t\t\"lastmodifyTime\": {\n" +
                "\t\t\t\"date\": 29,\n" +
                "\t\t\t\"day\": 2,\n" +
                "\t\t\t\"hours\": 23,\n" +
                "\t\t\t\"minutes\": 1,\n" +
                "\t\t\t\"month\": 4,\n" +
                "\t\t\t\"seconds\": 32,\n" +
                "\t\t\t\"time\": 1527606092000,\n" +
                "\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\"year\": 118\n" +
                "\t\t},\n" +
                "\t\t\"parseExpression\": \"$..NewsTitle\",\n" +
                "\t\t\"parseType\": \"3\",\n" +
                "\t\t\"$$hashKey\": \"object:24\"\n" +
                "\t}],\n" +
                "\t\"fieldSuffProc\": [],\n" +
                "\t\"$$hashKey\": \"object:4\"\n" +
                "}, {\n" +
                "\t\"crawlRuleDetailId\": 73,\n" +
                "\t\"id\": 410,\n" +
                "\t\"isNull\": \"0\",\n" +
                "\t\"isUnique\": \"0\",\n" +
                "\t\"ruleId\": 135,\n" +
                "\t\"storageTypeFieldId\": 17,\n" +
                "\t\"fieldEnName\": \"url\",\n" +
                "\t\"fieldDesc\": \"网站链接\",\n" +
                "\t\"fieldLength\": 200,\n" +
                "\t\"fieldCnName\": \"网站链接\",\n" +
                "\t\"fieldTypeName\": \"text\",\n" +
                "\t\"fieldParse\": [{\n" +
                "\t\t\"createTime\": {\n" +
                "\t\t\t\"date\": 29,\n" +
                "\t\t\t\"day\": 2,\n" +
                "\t\t\t\"hours\": 23,\n" +
                "\t\t\t\"minutes\": 1,\n" +
                "\t\t\t\"month\": 4,\n" +
                "\t\t\t\"seconds\": 32,\n" +
                "\t\t\t\"time\": 1527606092000,\n" +
                "\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\"year\": 118\n" +
                "\t\t},\n" +
                "\t\t\"detailFieldId\": 410,\n" +
                "\t\t\"id\": 223,\n" +
                "\t\t\"lastmodifyTime\": {\n" +
                "\t\t\t\"date\": 29,\n" +
                "\t\t\t\"day\": 2,\n" +
                "\t\t\t\"hours\": 23,\n" +
                "\t\t\t\"minutes\": 1,\n" +
                "\t\t\t\"month\": 4,\n" +
                "\t\t\t\"seconds\": 32,\n" +
                "\t\t\t\"time\": 1527606092000,\n" +
                "\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\"year\": 118\n" +
                "\t\t},\n" +
                "\t\t\"parseExpression\": \"\",\n" +
                "\t\t\"parseType\": \"4\",\n" +
                "\t\t\"$$hashKey\": \"object:28\"\n" +
                "\t}],\n" +
                "\t\"fieldSuffProc\": [],\n" +
                "\t\"$$hashKey\": \"object:5\"\n" +
                "}, {\n" +
                "\t\"crawlRuleDetailId\": 73,\n" +
                "\t\"id\": 411,\n" +
                "\t\"isNull\": \"0\",\n" +
                "\t\"isUnique\": \"0\",\n" +
                "\t\"ruleId\": 135,\n" +
                "\t\"storageTypeFieldId\": 18,\n" +
                "\t\"fieldEnName\": \"summary\",\n" +
                "\t\"fieldDesc\": \"内容摘要\",\n" +
                "\t\"fieldLength\": 200,\n" +
                "\t\"fieldCnName\": \"内容摘要\",\n" +
                "\t\"fieldTypeName\": \"text\",\n" +
                "\t\"fieldParse\": [],\n" +
                "\t\"fieldSuffProc\": [{\n" +
                "\t\t\"createTime\": {\n" +
                "\t\t\t\"date\": 29,\n" +
                "\t\t\t\"day\": 2,\n" +
                "\t\t\t\"hours\": 23,\n" +
                "\t\t\t\"minutes\": 1,\n" +
                "\t\t\t\"month\": 4,\n" +
                "\t\t\t\"seconds\": 32,\n" +
                "\t\t\t\"time\": 1527606092000,\n" +
                "\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\"year\": 118\n" +
                "\t\t},\n" +
                "\t\t\"detailFieldId\": 411,\n" +
                "\t\t\"id\": 34,\n" +
                "\t\t\"lastmodifyTime\": {\n" +
                "\t\t\t\"date\": 29,\n" +
                "\t\t\t\"day\": 2,\n" +
                "\t\t\t\"hours\": 23,\n" +
                "\t\t\t\"minutes\": 1,\n" +
                "\t\t\t\"month\": 4,\n" +
                "\t\t\t\"seconds\": 32,\n" +
                "\t\t\t\"time\": 1527606092000,\n" +
                "\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\"year\": 118\n" +
                "\t\t},\n" +
                "\t\t\"processorId\": \"2\",\n" +
                "\t\t\"processorValue\": \"return \\\"456\\\";\",\n" +
                "\t\t\"processorId1\": \"\",\n" +
                "\t\t\"$$hashKey\": \"object:32\"\n" +
                "\t}],\n" +
                "\t\"$$hashKey\": \"object:6\"\n" +
                "}, {\n" +
                "\t\"crawlRuleDetailId\": 73,\n" +
                "\t\"id\": 412,\n" +
                "\t\"isNull\": \"0\",\n" +
                "\t\"isUnique\": \"0\",\n" +
                "\t\"ruleId\": 135,\n" +
                "\t\"storageTypeFieldId\": 19,\n" +
                "\t\"fieldEnName\": \"publishtime\",\n" +
                "\t\"fieldDesc\": \"发布时间\",\n" +
                "\t\"fieldLength\": 40,\n" +
                "\t\"fieldCnName\": \"发布时间\",\n" +
                "\t\"fieldTypeName\": \"datetime\",\n" +
                "\t\"fieldParse\": [],\n" +
                "\t\"fieldSuffProc\": [],\n" +
                "\t\"$$hashKey\": \"object:7\"\n" +
                "}, {\n" +
                "\t\"crawlRuleDetailId\": 73,\n" +
                "\t\"id\": 413,\n" +
                "\t\"isNull\": \"0\",\n" +
                "\t\"isUnique\": \"0\",\n" +
                "\t\"ruleId\": 135,\n" +
                "\t\"storageTypeFieldId\": 29,\n" +
                "\t\"fieldEnName\": \"content\",\n" +
                "\t\"fieldDesc\": \"内容\",\n" +
                "\t\"fieldLength\": 10000,\n" +
                "\t\"fieldCnName\": \"内容\",\n" +
                "\t\"fieldTypeName\": \"text\",\n" +
                "\t\"fieldParse\": [],\n" +
                "\t\"fieldSuffProc\": [],\n" +
                "\t\"$$hashKey\": \"object:8\"\n" +
                "}, {\n" +
                "\t\"crawlRuleDetailId\": 73,\n" +
                "\t\"id\": 414,\n" +
                "\t\"isNull\": \"0\",\n" +
                "\t\"isUnique\": \"0\",\n" +
                "\t\"ruleId\": 135,\n" +
                "\t\"storageTypeFieldId\": 46,\n" +
                "\t\"fieldEnName\": \"author\",\n" +
                "\t\"fieldDesc\": \"作者\",\n" +
                "\t\"fieldLength\": 255,\n" +
                "\t\"fieldCnName\": \"作者\",\n" +
                "\t\"fieldTypeName\": \"text\",\n" +
                "\t\"fieldParse\": [],\n" +
                "\t\"fieldSuffProc\": [],\n" +
                "\t\"$$hashKey\": \"object:9\"\n" +
                "}, {\n" +
                "\t\"crawlRuleDetailId\": 73,\n" +
                "\t\"id\": 415,\n" +
                "\t\"isNull\": \"0\",\n" +
                "\t\"isUnique\": \"0\",\n" +
                "\t\"ruleId\": 135,\n" +
                "\t\"storageTypeFieldId\": 47,\n" +
                "\t\"fieldEnName\": \"source\",\n" +
                "\t\"fieldDesc\": \"来源\",\n" +
                "\t\"fieldLength\": 255,\n" +
                "\t\"fieldCnName\": \"来源\",\n" +
                "\t\"fieldTypeName\": \"text\",\n" +
                "\t\"fieldParse\": [],\n" +
                "\t\"fieldSuffProc\": [],\n" +
                "\t\"$$hashKey\": \"object:10\"\n" +
                "}]";
        String requestURI = "/crawlRule/testDetailRuleFieldProcessor.json";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(requestURI).param("testInLogin", "no")
                .param("param", param).param("headers", headers).param("crawlMethod", crawlMethod)
                .param("requestEncoding", requestEncoding).param("responseEncoding", responseEncoding)
                .param("detailPreProc", detailPreProc).param("detailParse", detailParse)
                .param("detailSuffProc", detailSuffProc).param("pageType", pageType).param("datasourceId", datasourceId)
                .param("field", field).param("testUrl", testUrl)
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(content));
        assertTrue(JSONObject.fromObject(content).getInt("code") == 0);
    }

    @Test
    @Rollback(false)
    public void jsonPathTest3() throws Exception {
        String content = "{\"NewsId\":\"NI000000000001123898\",\"CreateDate\":\"/Date(1527497596137)/\",\"StrCreateDate\":\"2018-05-28 16:53:16\",\"NewsTitle\":\"2018年第17周彩电畅销机型排行榜分析：海信品牌智能电视最畅销（附榜单）\",\"NewsLogo\":\"http://image1.askci.com/images/2018/05/28/574e573e-83c3-4fc0-bb1a-dd60527f0d59-366x248.png\",\"NewsSavePath\":\"http://s.askci.com/news/chanxiao/20180528/1653161123898.shtml\",\"NewsContent\":\"\\t中商情报网讯：2018年第17周全国彩电畅销机型排行榜已揭晓，据数据统计显示：第十七周（2018年4月23日2018年4月29日）液晶电视畅销机型前十名分别是：海信LED55N3000U、长虹55U1、海信LED55MU7...\",\"ListTag\":[{\"TagId\":null,\"TagName\":\"彩电\",\"TagUrl\":\"http://www.askci.com/News/List/tag-彩电\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"家电\",\"TagUrl\":\"/News/List/tag-家电\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"智能电视\",\"TagUrl\":\"/News/List/tag-智能电视\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]},{\"TagId\":null,\"TagName\":\"液晶电视\",\"TagUrl\":\"http://www.askci.com/News/List/tag-液晶电视\",\"TagStatus\":null,\"CreateDate\":null,\"EditDate\":null,\"CreateBy\":null,\"EditBy\":null,\"NewsReportTag\":[],\"NewsTag\":[]}]}";
        ReadContext ctx = JsonPath.parse(content);

        List<String> authorsOfBooksWithISBN = ctx.read("$..NewsTitle");

        System.out.println("authorsOfBooksWithISBN: " + authorsOfBooksWithISBN.get(0));

    }

    @Test
    @Rollback(false)
    public void matchesTest() throws Exception {
        String content = "1234$$hashKey567$123$";
        String content2 = "1234$$hashKey567$123";

        String s = content.replace("$$hashKey", "");
        String s2 = content2.replace("$$hashKey", "");

        Assert.assertTrue(s.length() > s.replaceAll("\\$(.*?)\\$", "").length());
        Assert.assertFalse(s2.length() > s2.replaceAll("\\$(.*?)\\$", "").length());
    }

    @Test
    public void JsonObjectTest2() throws Exception{

        System.out.println(System.getProperty("user.dir"));
    }
    @Test
    public void CharseUtils() throws IOException, HttpException {
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        HttpResponse response = httpClientHelper.doGetAndRetBytes("http://news.sina.com.cn/o/2018-06-10/doc-ihcufqif4470554.shtml","utf-8","utf-8",null,null);
        String str = new String(response.getContentBytes(),"utf-8");
        CharsetUtils charseUtils = new CharsetUtils();
        //str = charseUtils.clearContent(str);
        System.out.println(str);
    }

    @Test
    @Rollback(false)
    public void parseValueTest3() throws Exception {
        String testUrl = "https://www.toutiao.com/search_content/?offset=1&format=json&keyword=汽车 限购&autoload=true&count=20&cur_tab=1\n" +
                "http://www.toutiao.com/search_content/?offset=1&format=json&keyword=排放标准&autoload=true&count=20&cur_tab=1";
        String param = "{}";
        String headers = "[]";
        String crawlMethod = "GET";
        String requestEncoding = "UTF-8";
        String responseEncoding = "UTF-8";
        String detailPreProc = "[]";
        String detailParse = "[{\"crawlRuleDetailId\":142,\"createTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":28,\"time\":1537329388000,\"timezoneOffset\":-480,\"year\":118},\"id\":72,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":28,\"time\":1537329388000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$.data\",\"parseType\":\"3\",\"$$hashKey\":\"object:18\"}]";
        String detailSuffProc = "[]";
        String pageType = "1";
        String datasourceId = "83";
        String field = "[{\"crawlRuleDetailId\":142,\"id\":932,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":189,\"storageTypeFieldId\":16,\"fieldEnName\":\"title\",\"fieldDesc\":\"列表标题\",\"fieldLength\":40,\"fieldCnName\":\"列表标题\",\"fieldTypeName\":\"text\",\"fieldParse\":[{\"createTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":28,\"time\":1537329388000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":932,\"id\":467,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":28,\"time\":1537329388000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"return _projectId_;\",\"parseType\":\"4\",\"$$hashKey\":\"object:24\"}],\"fieldSuffProc\":[],\"$$hashKey\":\"object:4\"},{\"crawlRuleDetailId\":142,\"id\":933,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":189,\"storageTypeFieldId\":17,\"fieldEnName\":\"url\",\"fieldDesc\":\"网站链接\",\"fieldLength\":200,\"fieldCnName\":\"网站链接\",\"fieldTypeName\":\"text\",\"fieldParse\":[{\"createTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":28,\"time\":1537329388000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":933,\"id\":468,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":28,\"time\":1537329388000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$..title[*]\",\"parseType\":\"3\",\"$$hashKey\":\"object:28\"}],\"fieldSuffProc\":[],\"$$hashKey\":\"object:5\"},{\"crawlRuleDetailId\":142,\"id\":934,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":189,\"storageTypeFieldId\":18,\"fieldEnName\":\"summary\",\"fieldDesc\":\"内容摘要\",\"fieldLength\":200,\"fieldCnName\":\"内容摘要\",\"fieldTypeName\":\"text\",\"fieldParse\":[{\"createTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":29,\"time\":1537329389000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":934,\"id\":469,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":29,\"time\":1537329389000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$..image_list[1]\",\"parseType\":\"3\",\"$$hashKey\":\"object:32\"}],\"fieldSuffProc\":[],\"$$hashKey\":\"object:6\"},{\"crawlRuleDetailId\":142,\"id\":935,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":189,\"storageTypeFieldId\":19,\"fieldEnName\":\"publishtime\",\"fieldDesc\":\"发布时间\",\"fieldLength\":40,\"fieldCnName\":\"发布时间\",\"fieldTypeName\":\"datetime\",\"fieldParse\":[{\"createTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":29,\"time\":1537329389000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":935,\"id\":470,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":29,\"time\":1537329389000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$..image_list[0].url\",\"parseType\":\"3\",\"$$hashKey\":\"object:36\"}],\"fieldSuffProc\":[],\"$$hashKey\":\"object:7\"},{\"crawlRuleDetailId\":142,\"id\":936,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":189,\"storageTypeFieldId\":29,\"fieldEnName\":\"content\",\"fieldDesc\":\"内容\",\"fieldLength\":10000,\"fieldCnName\":\"内容\",\"fieldTypeName\":\"text\",\"fieldParse\":[{\"createTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":29,\"time\":1537329389000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":936,\"id\":471,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":29,\"time\":1537329389000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$.title\",\"parseType\":\"3\",\"$$hashKey\":\"object:40\"}],\"fieldSuffProc\":[],\"$$hashKey\":\"object:8\"},{\"crawlRuleDetailId\":142,\"id\":937,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":189,\"storageTypeFieldId\":46,\"fieldEnName\":\"author\",\"fieldDesc\":\"作者\",\"fieldLength\":255,\"fieldCnName\":\"作者\",\"fieldTypeName\":\"text\",\"fieldParse\":[{\"createTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":29,\"time\":1537329389000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":937,\"id\":472,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":11,\"minutes\":56,\"month\":8,\"seconds\":29,\"time\":1537329389000,\"timezoneOffset\":-480,\"year\":118},\"parseExpression\":\"$url$\",\"parseType\":\"4\",\"$$hashKey\":\"object:44\"}],\"fieldSuffProc\":[],\"$$hashKey\":\"object:9\",\"storageTypeFieldBo\":{\"createTime\":{\"date\":19,\"day\":2,\"hours\":0,\"minutes\":7,\"month\":11,\"seconds\":21,\"time\":1513613241000,\"timezoneOffset\":-480,\"year\":117},\"decimalLength\":0,\"fieldCnName\":\"作者\",\"fieldDesc\":\"作者\",\"fieldEnName\":\"author\",\"fieldLength\":255,\"fieldType\":\"text\",\"id\":46,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":19,\"minutes\":38,\"month\":8,\"seconds\":31,\"time\":1537357111000,\"timezoneOffset\":-480,\"year\":118},\"storageTypeId\":9,\"usedNum\":\"66\"}},{\"crawlRuleDetailId\":142,\"id\":938,\"isNull\":\"0\",\"isUnique\":\"0\",\"ruleId\":189,\"storageTypeFieldId\":47,\"fieldEnName\":\"source\",\"fieldDesc\":\"来源\",\"fieldLength\":255,\"fieldCnName\":\"来源\",\"fieldTypeName\":\"text\",\"fieldParse\":[],\"fieldSuffProc\":[{\"createTime\":{\"date\":19,\"day\":3,\"hours\":14,\"minutes\":18,\"month\":8,\"seconds\":29,\"time\":1537337909000,\"timezoneOffset\":-480,\"year\":118},\"detailFieldId\":938,\"id\":99,\"lastmodifyTime\":{\"date\":19,\"day\":3,\"hours\":14,\"minutes\":18,\"month\":8,\"seconds\":29,\"time\":1537337909000,\"timezoneOffset\":-480,\"year\":118},\"processorId\":\"2\",\"processorValue\":\"var source = '@source@';\\nreturn $summary$ + \\\"aaa\\\" + source;\",\"processorId1\":\"\",\"$$hashKey\":\"object:50\"}],\"$$hashKey\":\"object:10\"}]";

        String requestURI = "/crawlRule/testDetailRuleFieldProcessor.json";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(requestURI).param("testInLogin", "no")
                .param("param", param).param("headers", headers).param("crawlMethod", crawlMethod)
                .param("requestEncoding", requestEncoding).param("responseEncoding", responseEncoding)
                .param("detailPreProc", detailPreProc).param("detailParse", detailParse)
                .param("detailSuffProc", detailSuffProc).param("pageType", pageType).param("datasourceId", datasourceId)
                .param("field", field).param("testUrl", testUrl)
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(com.jeeframework.util.json.JSONUtils.isJSONValid(content));
        assertTrue(JSONObject.fromObject(content).getInt("code") == 0);
    }

}
