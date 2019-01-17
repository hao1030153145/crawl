package com.transing.crawl.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.integration.bo.CrawlRuleAnalysisTypeBO;
import com.transing.crawl.integration.bo.CrawlRuleDetailPreProcBO;
import com.transing.crawl.integration.bo.StorageTypeFieldTypeBO;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.client.HttpResponse;
import com.transing.crawl.util.processor.ProcessorUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.StringEscapeUtils;
import org.htmlcleaner.CommentNode;
import org.htmlcleaner.EndTagToken;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;


/**
 * 包: com.transing.crawl.util
 * 源文件:XpathUtil.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月03日
 */
public class ParseUtil
{


    public static String getTagNodeHtmlString(Object tag2)
    {
        StringBuilder sb = new StringBuilder();

        if (tag2 instanceof TagNode)
        {
            TagNode tag = (TagNode) tag2;
            sb.append("<" + tag.getName());
            Map<String, String> attrs = tag.getAttributes();

            if (attrs != null && attrs.size() > 0)
            {
                for (String key : attrs.keySet())
                {
                    sb.append(" ").append(key).append("=\"").append(attrs.get(key)).append("\" ");
                }
            }

            sb.append(">");

            List child = tag.getChildren();

            for (Object c : child)
            {
                sb.append(getTagNodeHtmlString(c));
            }

            sb.append("</" + tag.getName() + ">");

        } else
        {
            sb.append(tag2.toString());
        }

        return sb.toString();
    }



    public static TagNode getTagNode(String html)
    {
        String content = html;

        content = content.replaceAll("<script [\\s|\\S]*? </scritp>", "");

        if (!content.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))
        {
            content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + content;
        }

        try
        {
            return new HtmlCleaner().clean(content);
        } catch (Exception e)
        {
        }

        return null;
    }

    /**
     * 正则处理
     * @param regx
     * @param html
     * @return
     */
    public static List<Object> regexMatcher(String regx, String html){
        List<Object> ret=new ArrayList<>();
        Integer curPattern = Pattern.DOTALL;
        Pattern p = Pattern.compile(regx, curPattern);

        Matcher m = p.matcher(html);// 开始编译
        while (m.find())
        {
            String retContent = m.group(1);
            if (Validate.isEmpty(retContent)){
                break;
            }else {
                ret.add(retContent);
            }
        }
        return ret;
    }

    /**
     * json结构数据的读取
     * @param content
     * @return
     */
    public static  void JSONReadArray(String content,String [] keys,int layer,List resultArray)throws Exception
    {
        if(Validate.isEmpty(content))
            return;

        if(layer<keys.length){
            try
            {
                JSONObject contentJSON = JSONObject.fromObject(content);
                content = contentJSON.get(keys[layer]).toString();
                JSONReadArray(content, keys, layer + 1, resultArray);
            }catch (Exception e){
                JSONArray jsonArray=JSONArray.fromObject(content);
                for (int i = 0; i < jsonArray.size(); i++){
                     JSONReadArray(jsonArray.getString(i), keys, layer + 1, resultArray);
                }
            }
        }else{
            try
            {
                JSONArray contentArray = JSONArray.fromObject(content);
                for (int i = 0; i < contentArray.size(); i++)
                {
                    resultArray.add(contentArray.get(i));
                }
            }catch (Exception e)
            {
                try
                {
                    JSONObject jsonObject = JSONObject.fromObject(content);
                    resultArray.add(jsonObject.getString(keys[layer - 1]));
                }
                catch (Exception ex)
                {
                    resultArray.add(content);
                }
            }
        }
    }

    /**
     * 测试的前置处理器
     * @param detailPreProcArray
     * @param processorUtil
     * @param content
     * @return
     */
    public  String proprecParse(JSONArray detailPreProcArray,ProcessorUtil processorUtil,String content,JSONObject paramJSON){
        List<CrawlRuleDetailPreProcBO> crawlRuleDetailPreProcBOList = new ArrayList<CrawlRuleDetailPreProcBO>();
        for (Object procObj : detailPreProcArray)
        {
            JSONObject procJSON = (JSONObject) procObj;
            String processorId=procJSON.containsKey("processorId")?procJSON.getString("processorId"):"";
            if (Validate.isEmpty(processorId)||processorId.equals("0"))
            {
                if(procJSON.containsKey("processorId1"))
                    processorId = procJSON.getString("processorId1");
                procJSON.replace("processorId",processorId);
            }


            procJSON.remove("$$hashKey");
            try
            {
                if(procJSON.size()>0)
                {
                    CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO = (CrawlRuleDetailPreProcBO) JSONObject
                            .toBean(procJSON);
                    crawlRuleDetailPreProcBOList
                            .add(crawlRuleDetailPreProcBO);
                }
            }
            catch (Exception e)
            {
                if(procJSON.containsKey("processorId")){
                    CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO=new CrawlRuleDetailPreProcBO();
                    crawlRuleDetailPreProcBO.setProcessorId(Integer.parseInt(procJSON.getString("processorId")));
                    if(procJSON.containsKey("processorValue")){
                        crawlRuleDetailPreProcBO.setProcessorValue(procJSON.getString("processorValue"));
                    }
                    crawlRuleDetailPreProcBOList.add(crawlRuleDetailPreProcBO);
                }
            }
        }
        if (crawlRuleDetailPreProcBOList.size() > 0)
        {
            for (CrawlRuleDetailPreProcBO crawlRuleDetailPreProcBO : crawlRuleDetailPreProcBOList)
            {
                content = processorUtil.startRunProcessor(
                        crawlRuleDetailPreProcBO.getProcessorId(),
                        content, paramJSON,
                        crawlRuleDetailPreProcBO.getProcessorValue());
            }
        }
        return content;
    }

    /**
     * 测试的列表解析
     * @param detailParseArray
     * @param detailSuffProcArray
     * @param parseResult
     * @param content
     * @param parseTypes
     * @param processorUtil
     */

    public  List<String> listParse(JSONArray detailParseArray,JSONArray detailSuffProcArray,
            List<String> parseResult,String content,Map<Integer,CrawlRuleAnalysisTypeBO> parseTypes,ProcessorUtil processorUtil,JSONObject fieldMJson){
        String text=content;
        List<String> list = new ArrayList<String>();
        for (Object obj : detailParseArray)
        {
            try
            {
                JSONObject detailParseJSON = (JSONObject) obj;

                  parseResult = parseValue(text,
                                detailParseJSON.getString("parseExpression"),
                                parseTypes.get(Integer.parseInt(detailParseJSON.getString("parseType")))
                                        .getParseName(),fieldMJson);
                if (parseResult == null||parseResult.size()<1)
                {
                    continue;
                }

                text="";
                for (String t:parseResult){
                    list.add(t);
                    text+=t;
                }
                if(Validate.isEmpty(text)){
                    text=content;
                }

            }catch (Exception e){e.printStackTrace();}
        }

        List<String> procArrayList=new ArrayList<String>();
        if(list!=null)
        {
            if (list.size() > 0 && detailSuffProcArray.size() > 0)
            {
                for (String parseResultObj : list)
                {
                    for (Object detailSuffProcOBj : detailSuffProcArray)
                    {
                        try
                        {
                            JSONObject objects = (JSONObject) detailSuffProcOBj;
                            String processorId=objects.containsKey("processorId")?objects.getString("processorId"):"";
                            if (Validate.isEmpty(processorId)||processorId.equals("0"))
                            {
                                if(objects.containsKey("processorId1"))
                                    processorId = objects.getString("processorId1");
                                objects.replace("processorId",processorId);
                            }

                            if (!Validate.isEmpty(objects.getString("processorId")))
                            {
                                parseResultObj = processorUtil
                                        .startRunProcessor(Integer.parseInt(
                                                objects.getString(
                                                        "processorId")),
                                                parseResultObj,
                                                JSONObject.fromObject("{}"),
                                                objects.getString(
                                                        "processorValue"));
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    procArrayList.add(parseResultObj);
                }
            }
            if (procArrayList.size() > 0)
            {
                list.removeAll(procArrayList);
            }
        }
        return list;
    }

//    /**
//     * 字段的测试
//     * @param fieldArray
//     * @param listItem
//     * @param parseTypes
//     * @throws Exception
//     */
//    public Map<String,Object> fieldParse(JSONArray fieldArray, String listItem, Map<Integer, CrawlRuleAnalysisTypeBO> parseTypes,
//                                         ProcessorUtil processorUtil, Map<String, StorageTypeFieldTypeBO> fieldTypes, JSONObject fieldMJson) throws Exception {
//        Map<String, Object> itemMap= new HashMap<>();
//
//        for (Object fieldObj : fieldArray) {
//            JSONObject fieldJson = (JSONObject) fieldObj;
//
//            String textValue = parseOneField(fieldJson, listItem, parseTypes, processorUtil, fieldTypes, fieldMJson).toString();
//            itemMap.put(fieldJson.getString("fieldEnName"), textValue);
//        }
//
//        return itemMap;
//    }

    /**
     * 字段的测试
     * @param fieldArray
     * @param listItem
     * @param parseTypes
     * @throws Exception
     */
    public Map<String,Object> fieldParse(JSONArray fieldArray, String listItem, Map<Integer, CrawlRuleAnalysisTypeBO> parseTypes,
            ProcessorUtil processorUtil, Map<String, StorageTypeFieldTypeBO> fieldTypes, JSONObject fieldMJson) throws Exception {
        Map<String, Object> itemMap= new HashMap<>();

        //找出parseExpression中所有被$$包裹的field
        Set<String> allSet = new HashSet<>();

        for (Object aFieldArray : fieldArray) {
            Set<String> set = getVariableName(aFieldArray.toString());
            allSet.addAll(set);
        }

        //如果全部解析都不带$,就按以前处理,并返回处理后的itemMap
        if (allSet.size() == 0) {
            for (Object fieldObj : fieldArray) {
                JSONObject fieldJson = (JSONObject) fieldObj;

                String textValue = parseOneField(fieldJson, listItem, parseTypes, processorUtil, fieldTypes, fieldMJson).toString();
                textValue = org.apache.commons.lang.StringEscapeUtils.unescapeHtml(textValue);
                itemMap.put(fieldJson.getString("fieldEnName"), textValue);
            }

            return itemMap;
        }

        //每个字段的解析方式和后置处理器,只要类型是js或者自定义参数,包含$field$, 中间的字段不能互相调用以及调死(比如 $A$ -> $B$ -> $C$ -> $A$)
        for (Object fieldObj : fieldArray) {
            JSONObject fieldJson = (JSONObject) fieldObj;

            String s = fieldJson.toString().replace("$$hashKey", "").replace("$$", "");
            if (s.length() > s.replaceAll("\\$(.*?)\\$", "").length()) continue;

            //找出fieldArray里面没有$引用的字段
            fieldTextValue(fieldObj, allSet, fieldMJson, listItem, parseTypes, processorUtil, fieldTypes, itemMap);
        }

        JSONArray finalFieldArray = new JSONArray();

        for (Object fieldObj : fieldArray) {
            JSONObject fieldJson = (JSONObject) fieldObj;

            String fieldEnName = fieldJson.getString("fieldEnName");

            if (itemMap.get(fieldEnName) == null) {
                finalFieldArray.add(fieldJson);
            }
        }

        int max = finalFieldArray.size();         //防止死循环
        int arrayCount;

        do {
            JSONArray array = recursion(finalFieldArray, allSet, fieldMJson, listItem, parseTypes, processorUtil, fieldTypes, itemMap, fieldArray);
            arrayCount = array.size();
            max--;
        } while (arrayCount > 0 && max > 0);

        return itemMap;
    }

    private JSONArray recursion(JSONArray finalFieldArray, Set<String> allSet, JSONObject fieldMJson, String listItem,
                                Map<Integer, CrawlRuleAnalysisTypeBO> parseTypes, ProcessorUtil processorUtil,
                                Map<String, StorageTypeFieldTypeBO> fieldTypes, Map<String, Object> itemMap, JSONArray fieldArray) throws Exception {
        for (Object fieldObj : finalFieldArray) {
            JSONObject fieldJson = (JSONObject) fieldObj;

            //单个field中的所有带$,引用的其他字段 找出fieldArray里面没有$引用的字段 中全部找出来了
            Set<String> fieldSet = getVariableName(fieldJson.toString());

            if (fieldSet == null || fieldSet.size() < 1) {
                continue;
            }

            List<String> list = new ArrayList<>();

            for (Object object : fieldArray) {
                list.add(JSONObject.fromObject(object).getString("fieldEnName"));
            }

            Set<String> finalSet = new HashSet<>();

            for (String field : fieldSet) {
                if (list.stream().anyMatch(it -> it.equals(field))) {
                    finalSet.add(field);
                }
            }

            long count = finalSet.stream().filter(it -> fieldMJson.getString("$" + it + "$") != null).count();

            //表示该field中用$引用的其他字段已经在
            if (count == finalSet.size()) {
                fieldTextValue(fieldObj, allSet, fieldMJson, listItem, parseTypes, processorUtil, fieldTypes, itemMap);
            }
        }

        return finalFieldArray;
    }

    private void fieldTextValue(Object fieldObj, Set<String> allSet, JSONObject fieldMJson,
                                String listItem, Map<Integer, CrawlRuleAnalysisTypeBO> parseTypes, ProcessorUtil processorUtil,
                                Map<String, StorageTypeFieldTypeBO> fieldTypes, Map<String, Object> itemMap) throws Exception {

        JSONObject fieldJson = (JSONObject) fieldObj;

        String textValue = parseOneField(fieldJson, listItem, parseTypes, processorUtil, fieldTypes, fieldMJson).toString();
        String fieldEnName = fieldJson.getString("fieldEnName");

        if (allSet.contains(fieldEnName)) {
            fieldMJson.put("$" + fieldEnName + "$", textValue);
//            allSet.remove(fieldEnName);
        }

        textValue = org.apache.commons.lang.StringEscapeUtils.unescapeHtml(textValue);
        itemMap.put(fieldJson.getString("fieldEnName"), textValue);
    }

    //获取所有$包裹的变量的名称
    private Set<String> getVariableName(String content) {

        Set<String> set = new HashSet<>();

        try {
            String ss = "\\$(.*?)\\$";
            String temp;
            Pattern pa = Pattern.compile(ss);
            Matcher ma;

            //在字段的测试的时候,json参数中混入了$$hashKey,影响判断
            content = content.replace("$$hashKey", "");
            ma = pa.matcher(content);

            while (ma.find()) {
                temp = ma.group();
                if (temp != null) {
                    temp = temp.replace("$", "");

                    if (!Validate.isEmpty(temp)) {
                        set.add(temp);
                    }
                }
            }
            return set;
        } catch (Exception e) {
            return set;
        }

    }


    /**
     * 单个字段的解析
     * @param Fieldjson
     * @param listItem
     * @param parseTypes
     * @param processorUtil
     * @return
     * @throws Exception
     */
    public Object parseOneField(JSONObject Fieldjson,String listItem,
            Map<Integer,CrawlRuleAnalysisTypeBO> parseTypes,
            ProcessorUtil processorUtil,Map<String,StorageTypeFieldTypeBO> fieldTypes,JSONObject fieldMJson)throws Exception{

        JSONArray fieldParses = Fieldjson
                .getJSONArray("fieldParse");
        JSONArray fieldSuffProc = Fieldjson
                .getJSONArray("fieldSuffProc");
        String textValue="";
        for (Object obj : fieldParses)
        {
            JSONObject fieldParseobj = (JSONObject) obj;
            List<String> parseResultw = parseValue(listItem,
                    fieldParseobj.getString(
                            "parseExpression"),
                    parseTypes.get(Integer.parseInt(
                            fieldParseobj.getString(
                                    "parseType")))
                            .getParseName(),fieldMJson);

            if (parseResultw == null||parseResultw.size()<1)
            {
                continue;
            }
            else
            {
                StringBuffer stringBuffer = new StringBuffer();
                for (String t : parseResultw)
                {
                    stringBuffer.append(t);
                    stringBuffer.append(";");
                }
                textValue = stringBuffer.toString().substring(0,
                        stringBuffer.length() - 1);
                break;
            }
        }
        for (int i=0;i<fieldSuffProc.size();i++)
        {
            JSONObject fieldSuffProcObj = (JSONObject) fieldSuffProc.get(i);

            String processorId=fieldSuffProcObj.containsKey("processorId")?fieldSuffProcObj.getString("processorId"):"";
            if (Validate.isEmpty(processorId)||processorId.equals("0"))
            {
                if(fieldSuffProcObj.containsKey("processorId1"))
                    processorId = fieldSuffProcObj.getString("processorId1");
                fieldSuffProcObj.replace("processorId",processorId);
            }

           String textValueSupart = processorUtil
                    .startRunProcessor(Integer.parseInt(fieldSuffProcObj.getString("processorId")),
                            textValue,fieldMJson,fieldSuffProcObj.getString("processorValue"));
            if(i==fieldSuffProc.size()-1){
                textValue=textValueSupart;
                break;
            }
            if(!Validate.isEmpty(textValueSupart))
            {
                textValue=textValueSupart;
                break;
            }
        }

        textValue = org.apache.commons.lang.StringEscapeUtils.unescapeHtml(textValue);

        if(!Validate.isEmpty(textValue)){
            JSONObject filedBOJSON=Fieldjson.getJSONObject("storageTypeFieldBo");
            String fieldTypeId=(filedBOJSON!=null&&filedBOJSON.containsKey("fieldType"))?filedBOJSON.getString("fieldType"):"text";
            Object result= changeFiledType(fieldTypeId,textValue,fieldTypes);
            return result;
        }else{
            return "";
        }
    }

    /**
     * 转换字段的格式
     * @param fieldTypeId 当前字段类型的id
     * @param value
     * @return
     */
    private Object changeFiledType(String fieldTypeId,
            String value,Map<String,StorageTypeFieldTypeBO> fieldTypes)
    {
        if(fieldTypes.containsKey(fieldTypeId)){
            StorageTypeFieldTypeBO storageTypeFieldTypeBO=fieldTypes.get(fieldTypeId);

            if(storageTypeFieldTypeBO.getFieldType().equalsIgnoreCase("int")){
                try{
                    Integer val=Integer.parseInt(value);
                    return val;
                }catch (Exception e){
                    LoggerUtil.errorTrace(e.getMessage(),e);
                    String val= RegexUtil.matchRegxWithPrefix(value,"(\\d+)",true);
                    return !Validate.isEmpty(val)?Integer.parseInt(val):0;
                }
            }else if(storageTypeFieldTypeBO.getFieldType().equalsIgnoreCase("float")){
                try{
                    Float val=Float.parseFloat(value);
                    return val;
                }catch (Exception e){
                    String val= RegexUtil.matchRegxWithPrefix(value,"[\\d]+\\.[\\d]+",true);
                    return !Validate.isEmpty(val)?Float.parseFloat(val):0F;
                }
            }else if(storageTypeFieldTypeBO.getFieldType().equalsIgnoreCase("datetime")){
                return DateUtil.parseDate(value);
            }else{
                value = value.replaceAll("([\r\t\n]+)", " ").replace("[\r\t\n]","");
                value = value.replaceAll("([ ]+)"," ");
            }
        }
        return value;
    }

    /**
     * 判定页面中是否包含exception 的mark
     * @param exceptionMark
     * @param html
     * @return
     */
    public static String checkHtmlException(List<String> exceptionMark,String html){
        if(exceptionMark!=null&&exceptionMark.size()>0){
            for (String mark:exceptionMark){
                LoggerUtil.debugTrace("mark="+mark);
                if(html.indexOf(mark)>0){
                    LoggerUtil.debugTrace("mark=="+mark);
                    return mark;
                }
            }
        }
        return null;
    }


    /**
     * 字段的解析
     * @param content 内容
     * @param parseExpress
     * @return
     * @throws Exception
     */
    public static List<String> parseValue(String content,
                                          String parseExpress, String analysisType, JSONObject fixedJson)
            throws Exception{

        List<String> parseResult=null;

        if(analysisType.equalsIgnoreCase("xpath")){
            //xpath
            String [] xpathResultArray=xpathParse(content,parseExpress);
            if(xpathResultArray!=null&&xpathResultArray.length>0){
                parseResult= Arrays.asList(xpathResultArray);
                return parseResult;
            }else{
                return null;
            }
        }else if(analysisType.equalsIgnoreCase("regex")){
            //regex
            String [] regexResultArray=listtoArray(ParseUtil
                    .regexMatcher(parseExpress,content));
            LoggerUtil.debugTrace("parseUtil","regex : "+
                    parseExpress +" result="+JSONArray.fromObject(regexResultArray));
            if (regexResultArray!=null&&regexResultArray.length>0){
                parseResult = Arrays.asList(regexResultArray);
                return parseResult;
            }else{
                return null;
            }
        } else if (analysisType.equalsIgnoreCase("json")){
           JsonPathSelector jsonPathSelector = new JsonPathSelector(parseExpress);
           return jsonPathSelector.selectList(content);
        } else {
            //固定值

            if(fixedJson!=null&&fixedJson.size()>0){
                Iterator<String> keys=fixedJson.keySet().iterator();
                StringBuffer jsStr=new StringBuffer("function processor(){");
                while (keys.hasNext()){
                    String key=keys.next();
                    Object value=fixedJson.get(key);
                    value=contentScape(value.toString());
                    if(key.indexOf("\\.")>-1)
                        key=key.replaceAll("\\.","_");
                    jsStr.append("var "+key+"=\""+value+"\"; ");
                }
                if (parseExpress.indexOf("return")<0)
                    parseExpress="return "+parseExpress;
                if(!parseExpress.endsWith(";"))
                    parseExpress+=";";
                jsStr.append(parseExpress);
                jsStr.append("}");
                String jsResult=jsParse(jsStr.toString(),"processor");
                List<String> list = new ArrayList<String>();
                list.add(jsResult);
                return list;
            }else {
                return null;
            }
        }
    }

    /**
     * Xpath 解析
     * @param xpath 表达式
     * @param xpath 被解析内容
     */
    private static String[] xpathParse(String content,String xpath) throws Exception
    {
        TagNode doc= ParseUtil.getTagNode(content);
        Object[] nodeList =  doc.evaluateXPath(xpath);

        if (nodeList == null || nodeList.length < 1)
        {
            return null;
        }

        String[] ret = new String[nodeList.length];

        for (int i = 0; i < nodeList.length; i++)
        {
            ret[i] = ParseUtil.getTagNodeHtmlString(nodeList[i]);
        }

        return ret;
    }

    /**
     * 将list 转成成string[]
     * @param list
     * @return
     */
    private static   String[] listtoArray (List<Object> list) throws Exception
    {
        if (list == null || list.size() == 0)
        {
            return null;
        }

        String[] ret = new String[list.size()];

        for (int i = 0; i < list.size(); i++)
        {
            ret[i] = String.valueOf(list.get(i));
        }

        return ret;
    }

    /**
     * js解析
     * @param processorValue
     * @param methodName
     * @return
     */
    public static String jsParse(String processorValue,String methodName){
        Context cx = Context.enter();
        Scriptable scope = cx.initStandardObjects();
        LoggerUtil.infoTrace("parseUtils",processorValue);
        try
        {
            Script script = cx
                    .compileString(processorValue, "js", 1, null);
            script.exec(cx, scope);
            Function func = (Function) scope.get(methodName, scope);
            Object suTmp = func
                    .call(Context.getCurrentContext(), scope, func,
                            new Object[0]);
            scope = Context.toObject(suTmp, scope);
            Object[] s = scope.getIds();
            if(s.length>0)
            {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < s.length; i++)
                {
                    stringBuffer
                            .append(scope.get(s[i].toString(), scope)
                                    .toString());
                    stringBuffer.append(" ");
                }
                return stringBuffer.toString()
                        .substring(0,
                                stringBuffer.toString().length() - 1);
            }else{
                return scope.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally
        {
            Context.exit();
        }
    }

    /**
     * 获取列表中所有的标签名称
     * @param html
     * @return
     */
    public static List<String> getALLTagNodeName(String html){
        List<String> nodeNames=new ArrayList<String>();

        TagNode tagNode=getTagNode(html);
        if (tagNode==null)
            return null;
        TagNode[] tagNodes=tagNode.getAllElements(true);

        for (TagNode node:tagNodes){
            String tagName=node.getName();
            if(!nodeNames.contains(tagName))
                nodeNames.add(tagName);
        }
        return nodeNames;
    }

    /**
     * 转义
     * 将文本中的&lt;等转换成html标签
     * @param content
     * @return
     */
    public static String getContentChangeHtmlElem(String content){
        return StringEscapeUtils.unescapeHtml3(content);
    }

    /**
     * 内容编码
     * 将文本中的“等转换成字符串
     * @param content
     * @return
     */
    public static String contentScape(String content){
        content=content.replaceAll("([\r\n\t]+)","");
        return StringEscapeUtils.escapeHtml4(content);
    }

    /**
     * 后置处理器的运行
     * @param content
     * @param suffProcArray
     * @return
     */
    public static String getSuffProcResult(String content,JSONArray suffProcArray,JSONObject fieldMJson){
        ProcessorUtil processorUtil=new ProcessorUtil();
        for (Object obj : suffProcArray)
        {
            JSONObject SuffProcObj = (JSONObject) obj;

            String processorId=SuffProcObj.containsKey("processorId")?SuffProcObj.getString("processorId"):"";
            if (Validate.isEmpty(processorId)||processorId.equals("0"))
            {
                if(SuffProcObj.containsKey("processorId1"))
                    processorId = SuffProcObj.getString("processorId1");
                SuffProcObj.replace("processorId",processorId);
            }

            String textValueSupart = processorUtil
                    .startRunProcessor(Integer.parseInt(SuffProcObj.getString("processorId")),
                            content,fieldMJson,SuffProcObj.getString("processorValue"));
            /*if(!Validate.isEmpty(textValueSupart))
            {*/
                content=textValueSupart;
            //}
        }
        return content;
    }

    public static TagNode getContentTag(String content){
        TagNode tagNode = new HtmlCleaner().clean(content);
        //tagNode = (TagNode) tagNode.getChildTagList().get(1);
        /*if (tagNode.getChildTagList().size() > 0)
            tagNode = (TagNode) tagNode.getChildTagList().get(0);*/
        return tagNode;
    }

    /**
     * 过滤内容中的指定标签
     * @param tag2
     * @param deleteTags
     * @return
     */
    public static String getTagNodeHtmlString(Object tag2,List<String> deleteTags){
        StringBuilder sb = new StringBuilder();

        if (tag2 instanceof TagNode)
        {
            TagNode tag = (TagNode) tag2;
            if(!deleteTags.contains(tag.getName()))
            {
                sb.append("<" + tag.getName());
                Map<String, String> attrs = tag.getAttributes();

                if (attrs != null && attrs.size() > 0)
                {
                    for (String key : attrs.keySet())
                    {
                        sb.append(" ").append(key).append("=\"")
                                .append(attrs.get(key)).append("\" ");
                    }
                }
                sb.append(">");

                List child = tag.getChildren();

                for (Object c : child)
                {
                    if (!(c instanceof EndTagToken)&&!(c instanceof CommentNode))
                    {
                        sb.append(getTagNodeHtmlString(c,deleteTags));
                    }
                }
                if (!((attrs == null || attrs.size() == 0) &&
                        child.size() == 0))
                {
                    sb.append("</" + tag.getName() + ">");
                }
            }
        } else
        {
            sb.append(tag2.toString());
        }

        return sb.toString();
    }



    public static void main(String[] args)
    {
        String value="";
        try
        {
            HttpClientHelper httpClientHelper=new HttpClientHelper();
            HttpResponse httpResponse=httpClientHelper.doGet("http://www.laibin.gov.cn/LBFront/jrlb/20171228/004005001_b0e43f71-cbb7-494c-97f7-03454d7c4cbf.htm","utf-8","utf-8",null,null);
            System.out.println(httpResponse.getContent());

            /*HttpClient httpClient=new HttpClient();
            GetMethod get=new GetMethod("http://www.ec.org.cn/?channel-67.html=&page=2");
            httpClient.executeMethod(get);
            byte [] contentByte=get.getResponseBody();
            System.out.println("--------------------------------------------");
            String content = new String(contentByte,"GBK");
            System.out.println(content);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
