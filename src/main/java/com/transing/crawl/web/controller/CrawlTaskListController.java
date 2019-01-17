package com.transing.crawl.web.controller;

import com.jeeframework.util.validate.Validate;
import com.jeeframework.webframework.exception.WebException;
import com.transing.crawl.biz.service.CrawlTaskService;
import com.transing.crawl.integration.bo.CrawlSubTaskBO;
import com.transing.crawl.integration.bo.CrawlTaskBO;
import com.transing.crawl.util.DateUtil;
import com.transing.crawl.web.exception.MySystemCode;
import com.transing.crawl.web.filter.CrawlTaskFilter;
import com.transing.crawl.web.po.CrawlTaskPO;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("crawlTaskListController")
@Api(value = "任务列表")
@RequestMapping(value = "/crawlTask")
public class CrawlTaskListController {

    @Resource
    private CrawlTaskService crawlTaskService;


    @RequestMapping(value = "/getCrawlTaskList.json", method = RequestMethod.POST)
    @ApiOperation(value = "任务列表")
    @ResponseBody
    public Map<String, Object> getCrawlTaskList(
            @RequestParam(value = "project", required = false) @ApiParam(value = "项目编号", required = false) String project,
            @RequestParam(value = "taskName", required = false) @ApiParam(value = "任务名称，默认null,支持模糊查询", required = false) String taskName,
            @RequestParam(value = "datasourceId", required = false) @ApiParam(value = "数据源，默认null,", required = false) String datasourceId,
            @RequestParam(value = "datasourceTypeId", required = false) @ApiParam(value = "数据源类型，默认null,", required = false) String datasourceTypeId,
            @RequestParam(value = "status", required = false) @ApiParam(value = "状态0-未启动，1-启动中，2-已完成，4-已停止，9-异常，默认空-全部,", required = false) String status,
            @RequestParam(value = "createdTime", required = false) @ApiParam(value = "创建时间，默认null", required = false) String createdTime,
            @RequestParam(value = "page", required = false) @ApiParam(value = "指定页码", required = false) String page,
            @RequestParam(value = "size", required = false) @ApiParam(value = "每页显示条数,默认“15”", required = false) String size,
            HttpServletRequest request) {
        long count = 0;
        List<CrawlTaskPO> crawlTaskPOList = new ArrayList<>();
        if (Validate.isEmpty(page)) {
            page = "1";
        }
        if (Validate.isEmpty(size)) {
            size = "15";
        }
        int startRow = (Integer.parseInt(page.trim()) - 1) * Integer.parseInt(size);


        CrawlTaskFilter crawlTaskFilter = new CrawlTaskFilter(project,taskName, datasourceId, datasourceTypeId, status, createdTime, startRow, Integer.valueOf(size));
        List<CrawlTaskBO> crawlTaskBOs = crawlTaskService.getCrawlTaskListByFilter(crawlTaskFilter);
        count = crawlTaskService.getCrawlTaskCountByFilter(crawlTaskFilter);
        for (CrawlTaskBO crawlTaskBO : crawlTaskBOs) {
            int dataNum = 0;
            List<Integer> integerList = crawlTaskService.getCrawlSubTaskListByTaskId(crawlTaskBO.getId());
            if (integerList != null) {
                for (Integer integer : integerList) {
                    dataNum += integer;
                }
                CrawlTaskPO crawlTaskPO = new CrawlTaskPO();
                crawlTaskPO.setTaskId(crawlTaskBO.getId());
                crawlTaskPO.setCompletedNum(crawlTaskBO.getCompleteSubTaskNum());
                if(crawlTaskBO.getLastmodifyTime()!=null) {
                    crawlTaskPO.setCompletedTime(DateUtil.formatDate(crawlTaskBO.getLastmodifyTime()));
                }
                if(crawlTaskBO.getCreateTime()!=null) {
                    crawlTaskPO.setCreateTime(DateUtil.formatDate(crawlTaskBO.getCreateTime()));
                }
                crawlTaskPO.setDataNum(dataNum);

                JSONObject jsonParam=JSONObject.fromObject(crawlTaskBO.getJsonParam());
                String projectId=jsonParam.getString("projectId");
                JSONObject param=jsonParam.getJSONObject("jsonParam");
                crawlTaskPO.setDatasourceType(param.getString("datasourceTypeName"));

                crawlTaskPO.setProjectId(projectId);
                crawlTaskPO.setStatus(crawlTaskBO.getTaskStatus());
                crawlTaskPO.setTaskName(crawlTaskBO.getTaskName());
                crawlTaskPO.setErrorMsg(crawlTaskBO.getErrorMsg());
                crawlTaskPO.setCountNum(integerList.size());
                if (crawlTaskPO.getStatus() == 0) {
                    crawlTaskPO.setStatusName("未启动");
                } else if (crawlTaskPO.getStatus() == 1) {
                    crawlTaskPO.setStatusName("启动中");
                } else if (crawlTaskPO.getStatus() == 2) {
                    crawlTaskPO.setStatusName("已完成");
                } else if (crawlTaskPO.getStatus() == 4) {
                    crawlTaskPO.setStatusName("已停止");
                } else if (crawlTaskPO.getStatus() == 9) {
                    crawlTaskPO.setStatusName("异常");
                }
                crawlTaskPOList.add(crawlTaskPO);
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("count", count);
        resultMap.put("datalist", crawlTaskPOList);
        return resultMap;
    }

    @RequestMapping(value = "/getCrawlSubTaskList.json",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取子任务的数据列表")
    public Map<String,Object> ggetCrawlSubTaskList(
            @RequestParam(value = "taskId", required = true) @ApiParam(value = "任务编号", required = true) String taskId,
            @RequestParam(value = "subTaskId", required = false) @ApiParam(value = "子任务编号", required = false) String subTaskId,
            @RequestParam(value = "taskStatus", required = false) @ApiParam(value = "任务状态", required = false) String taskStatus,
            @RequestParam(value = "startTime", required = false) @ApiParam(value = "开始时间", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) @ApiParam(value = "结束时间", required = false) String endTime,
            @RequestParam(value = "page", required = false,defaultValue = "1") @ApiParam(value = "指定页码", required = false) String page,
            @RequestParam(value = "size", required = false,defaultValue = "15") @ApiParam(value = "每页显示条数,默认“15”", required = false) String size)
    {



        int startRow = (Integer.parseInt(page.trim()) - 1) * Integer.parseInt(size);
        Map<String,Object> param=new HashMap<String, Object>();
        param.put("taskId",taskId);

        CrawlTaskBO taskBO=crawlTaskService.getCrawlTaskListById(param);
        if(taskBO==null)
            throw new WebException(MySystemCode.SYS_REQUEST_EXCEPTION);

        if(!Validate.isEmpty(taskStatus))
            param.put("status",taskStatus);
        if(!Validate.isEmpty(startTime))
            param.put("startTime",startTime);
        if(!Validate.isEmpty(endTime))
            param.put("endTime",endTime);
        if(!Validate.isEmpty(subTaskId))
            param.put("subTaskId",subTaskId);

        long count=crawlTaskService.getSubTaskCount(param);

        param.put("startRow",startRow);
        param.put("size",Integer.parseInt(size));

        Map<String,Object> resultMap=new HashMap<String, Object>();
        try
        {
            List<CrawlSubTaskBO> crawlSubTaskBOs = crawlTaskService
                    .getCrawlSubTaskBos(param);
            resultMap.put("dataList",crawlSubTaskBOs);
        }catch (Exception e){
            e.printStackTrace();
        }


        resultMap.put("count",count);
        resultMap.put("task",taskBO);
        return resultMap;
    }


}
