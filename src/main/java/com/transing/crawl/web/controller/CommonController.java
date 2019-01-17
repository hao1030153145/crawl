package com.transing.crawl.web.controller;

import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.util.ExcelUtil;
import com.transing.crawl.util.WebUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Controller("commonController")
@Api(value = "公共接口", position = 0)
public class CommonController {

    /**
     * 下载模板文件
     * @param templateName
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getTemplateFile.json",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "下载模板")
    public void getTemplateFileByte(
            @RequestParam(value = "templateName", required = false, defaultValue = "keyword") @ApiParam(value = "模板名称", required = false, defaultValue = "keyword") String templateName,
            HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String fileName = templateName+".xls";
            response.reset();
            OutputStream os = response.getOutputStream();
            response.setHeader("Content-type", "application/xls");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder
                    .encode("关键词.xls", "UTF-8"));
            File outFile = ExcelUtil.getTemplateFile(fileName);
            byte[] buff = new byte[512];
            int n = 0;
            FileInputStream is = new FileInputStream(outFile);
            while ((n = is.read(buff)) > 0)
            {
                os.write(buff, 0, n);
            }
            is.close();
            os.flush();
            os.close();
        } catch (Exception e)
        {
            LoggerUtil.errorTrace(getClass().getName(),"模板下载错误："+e.toString(), e);
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/getFixedValues.json",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取自定义参数的解析字段")
    public List<String> getAllFixedValues(
            @RequestParam(value = "flag",required = false)@ApiParam(value = "参数标示",required = false) String flag){
        List<String> list=new ArrayList<String>();
        if(!Validate.isEmpty(flag)){
            //内置参数
            if(flag.equalsIgnoreCase("_")){
                list.add("_crawlTime_");
                list.add("_URL_");
                list.add("_projectId_");
            }
        }
        return list;
    }

    @RequestMapping(value = "/getProcessorDesc.json",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取处理器的注释")
    public Map<String,String> getProcessorDesc(
            @RequestParam(value = "processPaths",required = true)@ApiParam(value = "处理器路径",required = true) String processPaths)
            throws ClassNotFoundException
    {
        Map<String,String> result=new HashMap<String, String>();
        String [] paths=processPaths.split(",");
        for (String path:paths){
            if(!Validate.isEmpty(path))
            {
                Class c = Class.forName(path);
                Annotation classApi = c.getAnnotation(
                        com.wordnik.swagger.annotations.Api.class);
                com.wordnik.swagger.annotations.Api element = (com.wordnik.swagger.annotations.Api) classApi;
                String desc="";
                try
                {
                     desc = element.description();
                    if(Validate.isEmpty(desc))
                        desc="";

                }catch (Exception e){
                    desc="";
                }
                result.put(path, desc);
            }
        }
        return result;
    }
}
