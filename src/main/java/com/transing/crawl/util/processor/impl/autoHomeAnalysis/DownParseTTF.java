package com.transing.crawl.util.processor.impl.autoHomeAnalysis;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.transing.crawl.util.client.HttpClientHelper;
import com.transing.crawl.util.client.HttpResponse;
import org.apache.fontbox.ttf.CmapSubtable;
import org.apache.fontbox.ttf.GlyphData;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.http.HttpException;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 包:com.transing.crawl.util.processor.impl.preProcessors
 * 源文件:DownParseTTF
 * 汽车之家ttf文件解析
 * @author Leon  2018 成都创行, 2018年05月29日
 */
public class DownParseTTF {
    private static String loggerName="DownParseTTF";
    /*下载ttf文件*/
    public String parseTTF(String content)  {
        LoggerUtil.debugTrace(loggerName,"开始执行parseTTF");

        String analysis = null;
        if ( content!=null) {
            Pattern urlPatt = Pattern.compile("(\'//[^\\s]+.ttf*)");
            Matcher matcherPatt = urlPatt.matcher(content);
            String htmlsource = null;
            while (matcherPatt.find()){
                htmlsource = matcherPatt.group(1);
            }
            if (htmlsource==null){
                AutoHomeJsAly autoHomeJsAly = new  AutoHomeJsAly();
                content = autoHomeJsAly.jsAnalysis(content);
                return content;
            }
            LoggerUtil.debugTrace(loggerName,"URL:"+htmlsource);
            String ttfURL = htmlsource.replace("'","http:");
            File file = null;
            try {
                LoggerUtil.debugTrace(loggerName,"URL:"+ttfURL);
                URL url = new URL(ttfURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10 * 1000);
                conn.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
                InputStream inputStream = conn.getInputStream();
                byte[] getData = readInputStream(inputStream);
                String fileName = "campTTF";
                file = File.createTempFile("campTTF"+Thread.currentThread().getName(), ".ttf");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(getData);
                LoggerUtil.debugTrace(loggerName,"File:"+file.toString());
                /*调用ttf解析方法*/
                analysis = analysisTTF(file,"club.autohome.com.cn",content);
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                file.delete();
            }
        }
        return analysis;
    }

    /*读取文件*/
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        LoggerUtil.debugTrace(loggerName,"开始执行readInputStream");
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /*解析TTF 文件*/
    public static String analysisTTF(File file,String htmlsource,String content) {
        LoggerUtil.debugTrace(loggerName,"开始执行analysisTTF");
        int len;
        Map<String, String> campMap = new HashMap<String, String>();
        TrueTypeFont trueTypeFont = null;
        CmapSubtable cmapSubtable = null;
        TTFParser ttfParser = new TTFParser();
        Map<String,String> comp = new HashMap<String,String>();
        float y1 = 0;
        float y2 = 0;
        try {
            trueTypeFont = ttfParser.parse(file);
            len = trueTypeFont.getGlyph().getGlyphs().length;//得到ttf文件里面要解析的数的个数
            cmapSubtable = trueTypeFont.getCmap().getCmaps()[0];//得到所有字对应的16进制
            GlyphData[] glyphData = trueTypeFont.getGlyph().getGlyphs();//得到所有字的坐标
            for (int i = 1; i < len; i++) {
                List<Integer> list = cmapSubtable.getCharCodes(i);//得到字对应的16进制
                String str = Integer.toHexString(list.get(0));//将10进制转换成16进制
                str = "&#x" + str + ";";
                int pointCount = glyphData[i].getDescription().getPointCount();
                int contourCount = glyphData[i].getDescription().getContourCount();
                if((pointCount+"="+contourCount).equals("64=4")){
                        if(comp.get(pointCount+"="+contourCount) == null){
                            comp.put("64=4",str);
                            y1 = glyphData[i].getYMaximum();
                        }else{
                            if (y1>glyphData[i].getYMaximum()){
                                campMap.put(comp.get("64=4"),"64=4=1");
                                campMap.put(str,"64=4=2");
                            }else{
                                campMap.put(comp.get("64=4"),"64=4=2");
                                campMap.put(str,"64=4=1");
                            }
                        }
                }else if((pointCount+"="+contourCount).equals("12=1")){
                    if(comp.get(pointCount+"="+contourCount) == null){
                        comp.put("12=1",str);
                        y2 = glyphData[i].getYMinimum();
                    }else{
                        if (y2>glyphData[i].getYMinimum()){
                            campMap.put(comp.get("12=1"),"12=1=2");
                            campMap.put(str,"12=1=1");
                        }else{
                            campMap.put(comp.get("12=1"),"12=1=1");
                            campMap.put(str,"12=1=2");
                        }
                    }
                }else{
                    campMap.put(str, pointCount+"="+contourCount);
                }
            }
            LoggerUtil.debugTrace(loggerName,"campMap:"+campMap);
            AnalysisXML analysisXML = new AnalysisXML();
            Map<String, String> map = analysisXML.parseXML(htmlsource);
            Pattern contextPatt = Pattern.compile("(&#x[a-zA-Z0-9;]+)");
            Matcher matcher = contextPatt.matcher(content);
            while (matcher.find()) {
                String str = matcher.group(1);
                if (content.contains(str)) {
                    String replace = map.get(campMap.get(str));
                    LoggerUtil.debugTrace(loggerName,"replace:"+replace);
                    LoggerUtil.debugTrace(loggerName,"content:"+content);
                    if (replace!=null){
                        content = content.replace(str, replace);
                        LoggerUtil.debugTrace(loggerName,"content:"+content);
                        LoggerUtil.debugTrace(loggerName,"替换完毕");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

//    public static void main(String[] args) throws IOException, HttpException {
//        HttpClientHelper httpClientHelper = new HttpClientHelper();
//        HttpResponse httpResponse = httpClientHelper.doGetAndRetBytes("https://club.autohome.com.cn/bbs/thread/fbc927887d7e3a7f/73567790-1.html","GBK","GBK",null,null);
//        String str =  new String (httpResponse.getContentBytes(),"GBK");
//        DownParseTTF downParseTTF = new DownParseTTF();
//        downParseTTF.parseTTF(str);
//    }
}
