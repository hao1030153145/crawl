package com.transing.crawl.util.processor.impl.autoHomeAnalysis;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 包:com.transing.crawl.util.processor.impl.preProcessors
 * 源文件:AnalysisXML
 * ttf的xml文件解析
 * @author Leon  2018 成都创行, 2018年05月29日
 */
public class AnalysisXML {
    private static String loggerName="DownParseTTF";
    public Map<String,String> parseXML(String isURL){
        LoggerUtil.debugTrace(loggerName,"开始执行AnalysisXML");
        Map<String,String> map = new HashMap<String,String>();
        //SAXReader saxReader = new SAXReader();
        try{
//            String str = System.getProperty("user.dir");
//            Document document = saxReader.read(new File(str+File.separator+"ttf.xml"));
//            LoggerUtil.debugTrace(loggerName,"document:"+document);
//            Element rootElement = document.getRootElement();
//            Element fatherElement = rootElement.element(isURL);
//            String value = fatherElement.element("value").getText();
//            String boundingBox = fatherElement.element("boundingBox").getText();
            String value = "少/低/的/八/多/得/长/好/了/十/左/二/右/四/大/短/下/地/矮/上/近/一/三/七/着/和/高/呢/六/坏/更/远/是/不/九/很/小/五";
            String boundingBox = "[32.0,-275.0,2026.0,1691.0]/[12.0,-275.0,2036.0,1707.0]/[114.0,-235.0,1940.0,1703.0]/[18.0,-237.0,2024.0,1623.0]/[62.0,-285.0,1926.0,1701.0]/\n" +
                    "[26.0,-267.0,2020.0,1707.0]/[54.0,-267.0,2024.0,1701.0]/[36.0,-237.0,2020.0,1703.0]/[106.0,-217.0,1884.0,1553.0]/[50.0,-265.0,2000.0,1691.0]/\n" +
                    "[22.0,-147.0,2000.0,1705.0]/[60.0,-33.0,1982.0,1445.0]/[38.0,-257.0,2000.0,1709.0]/[186.0,-257.0,1908.0,1555.0]/[28.0,-263.0,2028.0,1691.0]/\n" +
                    "[20.0,-267.0,2006.0,1709.0]/[52.0,-255.0,1996.0,1537.0]/[14.0,-195.0,2040.0,1697.0]/[22.0,-279.0,2016.0,1703.0]/[52.0,-145.0,1998.0,1667.0]/\n" +
                    "[36.0,-215.0,2020.0,1689.0]/[32.0,689.0,2020.0,865.0]/[60.0,-115.0,1992.0,1501.0]/[52.0,-205.0,1992.0,1687.0]/[24.0,-263.0,1980.0,1717.0]/\n" +
                    "[40.0,-251.0,2008.0,1729.0]/[40.0,-251.0,2008.0,1729.0]/[132.0,-269.0,2002.0,1591.0]/[54.0,-225.0,2002.0,1689.0]/[38.0,-263.0,2026.0,1683.0]/\n" +
                    "[34.0,-277.0,2024.0,1605.0]/[24.0,-239.0,2016.0,1665.0]/[14.0,-285.0,2030.0,1625.0]/[12.0,-277.0,2024.0,1527.0]/[32.0,-255.0,2020.0,1693.0]/\n" +
                    "[32.0,-279.0,2022.0,1701.0]/[36.0,-239.0,2022.0,1679.0]/[40.0,-161.0,2018.0,1563.0]";
            String[] valueArray = value.split("/");
            String[] boundingBoxArray = boundingBox.split("/");
            for(int i=0;i<valueArray.length;i++){
                map.put(boundingBoxArray[i].trim(),valueArray[i].trim());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
