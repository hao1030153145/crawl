package com.transing.crawl.util.processor.impl.preProcessors;

import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.processor.BaseProcessor;
import com.transing.crawl.util.processor.impl.autoHomeAnalysis.DownParseTTF;
import net.sf.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 包: com.transing.crawl.util.processor.impl
 * 源文件:AutoHomeProcessor.java
 * 汽车之家
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年08月16日
 */
public class AutoHomeProcessor extends BaseProcessor
{
    @Override
    public String runProcessor(String content, JSONObject param,
            String processorValue)
    {
        DownParseTTF downParseTTF = new DownParseTTF();
        content = downParseTTF.parseTTF(content);//解析汽车之家


        return content;
    }

    public Map<String,String> runjs(String text){
        try{
            String [] functions =funcSplit(text);
            String GLSpan=".*span.*?hs_.*?main.*?";
            String documentSeleGL=".*document\\.querySelectorAll.*";
            String windowsReturnGL=".*return\\s*?window.*";
            String pandinWindow=".*if\\(window\\.hs_fuckyou.*";
            String paramFile=".*createElement.*?sheet.*";
            String windColumn=".*return.*?this\\[.*?\\];.*";
            List<String> filterList=new ArrayList<String>();
            Set<String> set=new HashSet<String>();
            String funcNameFull="";
            for (int i=0;i<functions.length;i++){
                String str=functions[i];
                if (str.matches(GLSpan))
                {
                    String str2=str.substring(str.lastIndexOf("</span>")+6);
                    String name=regex(str2,"(\\w{2}_)\\(\\w{2}_,\\w{2}_\\);");
                    set.add(name);
                    String funcName = funcNameFromStr(str);
                    filterList.add(funcName);
                    functions[i]=funcName+"(){}"+str.substring(str.lastIndexOf("}")+1);
                }
                else
                    if (str.matches(documentSeleGL))
                    {
                        String text1=str.replaceAll("document\\.querySelectorAll\\(","").replaceAll("\\);",";");
                        functions[i]=text1;
                    }
                    else
                        if (str.matches(windowsReturnGL))
                        {
                            String [] wins=str.split("return window");
                            String end=wins[1].substring(wins[1].indexOf(";")+1);
                            functions[i]=wins[0]+end;
                        }else if(str.matches(paramFile)){
                            String [] vales=str.split("createElement");
                            String ts=vales[0].substring(0,vales[0].lastIndexOf("}")+1);
                            String ts_n=vales[0].substring(vales[0].lastIndexOf("}")+1);
                            ts+=ts_n.substring(0,ts_n.lastIndexOf("=")+1)+"this";
                            String ts1=vales[1].substring(vales[1].lastIndexOf("}")+1);
                            String [] s=ts1.split(";");
                            s[0]=";"+s[0].split("=")[0]+"=this";
                            for (String s1:s)
                            {
                                if(!Validate.isEmpty(s1))
                                {
                                    ts += s1 + ";";
                                }
                            }
                            if(ts.trim().endsWith("=;"))
                            {
                                functions[i] = ts
                                        .substring(0, ts.lastIndexOf(";"));
                            }else{
                                functions[i] = ts;
                            }
                        }else if(str.matches(windColumn)){
                            String currentStr=str.substring(0,str.indexOf("{")+1)+"return this;"+"}"+str.substring(str.lastIndexOf("}")+1);
                            functions[i]=currentStr;
                        }else if(str.matches(".*if.*undefined.*return.*true.*else.*return.*false.*")){
                            functions[i]=funcNameFromStr(str)+"(){return true;}"+str.substring(str.lastIndexOf("}")+1);
                        }else if(str.matches(pandinWindow)){
                            String funName=funcNameFromStr(str).trim();
                            if(!Validate.isEmpty(funName))
                            {
                                funcNameFull = funName;
                            }
                        }
            }
            List<String> list=new ArrayList<String>();
            for (int j=0;j<functions.length;j++)
            {
                String str=functions[j];
                if(!Validate.isEmpty(str.trim()))
                {
                    for (String st:set)
                    {
                        if(str.trim().startsWith(st)&&str.trim().matches(".*for.*")){
                            String [] curtext=str.split("for");
                            String val=curtext[0].substring(curtext[0].indexOf(",")+1,curtext[0].indexOf(")"));
                            String paramRegex="var\\s*(.*?)\\s*=";
                            String para=regexT(curtext[0],paramRegex);
                            String funcStr=curtext[0].substring(0,curtext[0].indexOf("{")+1)+curtext[0].substring(curtext[0].indexOf("var"));
                            String currentStr=funcStr+"attr["+para+"]="+val+";}"+str.substring(str.lastIndexOf("}")+1);
                            functions[j]=currentStr;
                            list.add(st);
                        }
                    }

                    if(str.matches(".*"+funcNameFull+"\\(\\);.*?")){
                        str=str.replaceAll(funcNameFull+"\\(\\);","");
                        functions[j]=str.replaceAll(funcNameFull+"();","");
                    }else if(str.matches(paramFile)){
                        String [] vales=str.split("createElement");
                        String ts=vales[0].substring(0,vales[0].lastIndexOf("}")+1);
                        String ts_n=vales[0].substring(vales[0].lastIndexOf("}")+1);
                        ts+=ts_n.substring(0,ts_n.lastIndexOf("=")+1)+"this";
                        String ts1=vales[1].substring(vales[1].lastIndexOf("}")+1);
                        String [] s=ts1.split(";");
                        s[0]=";"+s[0].split("=")[0]+"=this";
                        for (String s1:s)
                        {
                            if(!Validate.isEmpty(s1))
                            {
                                ts += s1 + ";";
                            }
                        }
                        if(ts.trim().endsWith("=;"))
                        {
                            functions[j] = ts
                                    .substring(0, ts.lastIndexOf(";"));
                        }else{
                            functions[j] = ts;
                        }
                    }

                }
            }
            StringBuffer buffer=new StringBuffer();
            for (String str:functions){
                buffer.append("function "+str);
            }
            String jstext=buffer.toString().replaceFirst("function","function mainDB(){");
            jstext+="}";
            String resultText="";
            if(jstext.indexOf("return this[")>0){
                String [] repl=jstext.split("return this");
                String t=repl[1].substring(repl[1].indexOf("];}")+3);
                resultText=repl[0]+"return this;}"+t;
            }else{
                resultText=jstext;
            }
            try
            {
                if (list.size() < set.size())
                {
                    String kw = nextFull(set, list);
                    String[] ts2 = resultText.split("function  " + kw);
                    String ts_1 = ts2[1].substring(1, ts2[1].indexOf(")"));
                    String ts_2 = ts2[1].substring(0,ts2[1].indexOf("}}}")+3);
                    String ts_3=ts2[1].substring(ts2[1].indexOf("}}}")+3);
                    String[] tss_1=ts_2.split("var");
                    String tr="var"+tss_1[1].substring(0,tss_1[1].indexOf("for"));
                    tr+="attr["+tr.substring(4,tr.indexOf("=")).trim()+"]="+ts_1.split(",")[1];
                    resultText=ts2[0]+"function "+kw+"("+ts_1+"){"+tr+";};"+ts_3;
                }
            }catch (Exception e){}
            if(resultText.indexOf("return false;")>0){
                String [] str=resultText.split("return false;");
                String str_1=str[0].substring(0,str[0].lastIndexOf("if ("));
                resultText=str_1+"return true;"+str[1].substring(1);
            }
            Map<String,String> resultMap=runJsString(resultText);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Map runJsString(String jstext){
        Map<String,String> valMap=new HashMap<String, String>();
        try
        {
            Context cx = Context.enter();
            Scriptable scope = cx.initStandardObjects();
            try
            {
                // Precompile source only once
                jstext = jstext.replaceAll("\r\n", "");
                Script script = cx.compileString(jstext, "js", 1, null);
                script.exec(cx, scope);
                Function func = (Function) scope.get("mainDB", scope);
                Object suTmp = func
                        .call(Context.getCurrentContext(), scope, func,
                                new Object[0]);
                scope = Context.toObject(suTmp, scope);
                Object[] s = scope.getIds();
                for (int i = 0; i < s.length; i++)
                {
                    // System.out.println(
                    //       s[i] + " " + scope.get(s[i].toString(), scope));
                    valMap.put(s[i].toString(),scope.get(s[i].toString(),scope).toString());
                }
            }
            finally
            {
                Context.exit();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return valMap;
    }
    private  String regexT(String text,String reg){
        try{
            Pattern pattern=Pattern.compile(reg);
            Matcher matcher=pattern.matcher(text);
            String text1="";
            if(matcher.find()){
                text1=matcher.group(1);
            }
            return text1;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String regex(String text,String reg){
        try{
            Pattern pattern=Pattern.compile(reg);
            Matcher matcher=pattern.matcher(text);
            String text1="";
            while(matcher.find()){
                text1=matcher.group(1);
            }
            return text1;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private  String[] funcSplit(String text){
        try{
            return text.split("function");
        }catch (Exception e){
            e.printStackTrace();
            return new String[0];
        }

    }

    private  String funcNameFromStr(String funstr){
        if(!Validate.isEmpty(funstr)){
            return funstr.substring(0,funstr.indexOf("("));
        }
        return "";
    }

    private String nextFull(Set<String> set,List<String> list){
        for (String st:set){
            if(!list.contains(st)){
                return st;
            }
        }
        return "";
    }
}
