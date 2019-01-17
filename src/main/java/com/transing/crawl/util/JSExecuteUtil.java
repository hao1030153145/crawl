package com.transing.crawl.util;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.validate.Validate;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 包: com.transing.connectionpool.util.common
 * 源文件:JSExecuteUtil.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年07月26日
 */
public class JSExecuteUtil
{
    public static final String logName = "JSExecuteUtil";

    protected Context cx;

    protected Scriptable scope;

    public JSExecuteUtil()
    {
        this.cx = Context.enter();
        this.scope = cx.initStandardObjects();
    }

    public Object runJavaScript(String filename)
    {
        String jsContent = this.getJsContent(filename);
        Object result = cx.evaluateString(scope, jsContent, filename, 1, null);

        return result;
    }

    public Object runJavaScript(String filename, InputStream is)
    {
        String jsContent = this.getJsContent(is);
        Object result = cx.evaluateString(scope, jsContent, filename, 1, null);
        return result;
    }

    public String getJsContent(InputStream is)
    {
        BufferedReader buf1 = new BufferedReader(new InputStreamReader(is));

        LineNumberReader reader1;
        try
        {
            reader1 = new LineNumberReader(buf1);
            String s = null;
            StringBuffer sb = new StringBuffer();
            while ((s = reader1.readLine()) != null)
            {
                sb.append(s).append("\n");
            }
            return sb.toString();
        } catch (Exception e)
        {
            return null;
        }
    }

    private String getJsContent(String filename)
    {
        LineNumberReader reader;
        try
        {
            reader = new LineNumberReader(new FileReader(filename));
            String s = null;
            StringBuffer sb = new StringBuffer();
            while ((s = reader.readLine()) != null)
            {
                sb.append(s).append("\n");
            }
            return sb.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Scriptable getScope()
    {
        return scope;
    }

    public static void main(String[] args)
    {
        try
        {
            // Precompile source only once

            String str1 = "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
            str1 = str1.trim();
            String source = "" + "$1 = 'sharedScope';\n" + "$1 += '    test';\n" + "";

            // source = "function test(" + "$test$" + "){ " + " $test$ = $test$
            // +1; " + "return " + "$test$" + ";} ";

            source = "function test(" + "$test$" + "){ " + " /(\\d+)/.test($test$);\n"
                    + "         test2 =  RegExp.$1 ; " + "return " + "test2" + ";} ";

            source = "function test($retweets$,retweets1){ " + " /(\\d+)/.test($retweets$);"
                    + "\r\n test2 =  RegExp.$1 ; if(test2==null || test2 ==\"\"){test2= 0;}" + "\r\n return test2;  }";

            // /(\d+)/.test(@retweets@);
            // test2 = RegExp.$1 ;
            // return test2;

            List<String> lists = new ArrayList<String>();
            lists.add("test1");
            lists.add("test2");

            System.out.println(executeJS(source, "test", new Object[] { " ,  " }));
        } finally
        {
        }
    }

    /**
     * 组合成function执行返回js解析后的数据表达式
     *
     * @param javascript
     * @param paramKey
     * @param paramValue
     * @return
     */
    public static String composeAndExecFunc(String javascript, String paramKey, String paramValue)
    {
        javascript = "function test(" + "$" + paramKey + "$" + "){ " + javascript + "  } ";
        Object retObj = executeJS(javascript, "test", new Object[] { paramValue });

        return String.valueOf(retObj);
    }

    /**
     * 组合成function执行返回js解析后的数据表达式
     *
     * @param javascript
     * @param jsParam
     * @return
     */
    public static String composeAndExecFunc(String javascript, Map jsParam)
    {
        StringBuilder sb = new StringBuilder("function test(){");

        if (jsParam != null && jsParam.size() > 0)
        {
            Set keySet = jsParam.keySet();

            for (Object key : keySet)
            {
                if (key != null && !"".equals(key.toString().trim()))
                {
                    sb.append("var $").append(key).append("$").append(" = ").append(" '").append(jsParam.get(key))
                            .append("'; ");
                }
            }
        }

        sb.append(javascript).append("  } ");

        LoggerUtil.debugTrace(logName, sb.toString());

        Object retObj = executeJS(sb.toString(), "test", null);

        return String.valueOf(retObj);
    }

    /**
     * 根据一组key和valueMap映射键值执行JS函数
     *
     * @param javascript
     * @param paramKeySet
     * @param paramValueMap
     * @return
     */
    public static String composeAndExecFunc(String javascript, Set<String> paramKeySet,
            Map<String, List<String>> paramValueMap)
    {

        List<String> paramKeyList = new ArrayList<String>();
        List<String> paramValueList = new ArrayList<String>();
        for (String paramKey : paramKeySet)
        {
            List<String> paramValueListTmp = paramValueMap.get(paramKey);
            if (!Validate.isNull(paramValueListTmp))
            {
                paramKeyList.add("$" + paramKey + "$");
                paramValueList.add(org.apache.commons.lang.StringUtils.join(paramValueListTmp, ","));
            } else
            {
                throw new BizException("paramKey 无对应的值");
            }
        }

        javascript = "function test(" + org.apache.commons.lang.StringUtils.join(paramKeyList, ",") + "){ " + javascript
                + "  } ";

        Object[] objectsArray = paramValueList.toArray(new Object[0]);
        Object retObj = executeJS(javascript, "test", objectsArray);

        return String.valueOf(retObj);
    }

    /**
     * 执行javascript 代码片段
     *
     * @param javascript
     * @param param
     * @return
     */
    public static String executeJS(String javascript, String funcName, Object[] param)
    {
        Context cx = Context.enter();
        Scriptable scope = cx.initStandardObjects();
        try
        {
            // Precompile source only once
            javascript = javascript.replaceAll("\r\n", "");
            Script script = compileJS(javascript, cx);

            execScripts(cx, scope, script);

            Function func = (Function) scope.get(funcName, scope);

            Object suTmp = func.call(Context.getCurrentContext(), scope, func, param);
            String retStr = Context.toString(suTmp);
            return retStr;
        } finally
        {
            Context.exit();
        }
    }

    /**
     * 编译javascript
     *
     * @param javascript
     * @param cx
     * @return
     */
    public static Script compileJS(String javascript, Context cx)
    {
        try
        {
            String source = javascript;
            return cx.compileString(source, "js", 1, null);
        } catch (Exception e)
        {
            throw new BizException("解析javascript出错", e);
        }
    }

    static Object execScripts(Context cx, Scriptable sharedScope, Script script)
    {

        Object obj = script.exec(cx, sharedScope);

        return obj;
    }
}
