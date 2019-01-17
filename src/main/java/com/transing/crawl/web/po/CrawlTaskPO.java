package com.transing.crawl.web.po;

import java.util.Date;

public class CrawlTaskPO {


    private long taskId;
    private String taskName;
    private String errorMsg;
    private String statusName;
    private String datasourceType;
    private long status;
    private long completedNum;
    private long countNum;
    private long dataNum;
    private String projectId;
    private String createTime;
    private String completedTime;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDatasourceType()
    {
        return datasourceType;
    }

    public void setDatasourceType(String datasourceType)
    {
        this.datasourceType = datasourceType;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getCompletedNum() {
        return completedNum;
    }

    public void setCompletedNum(long completedNum) {
        this.completedNum = completedNum;
    }

    public long getDataNum() {
        return dataNum;
    }

    public void setDataNum(long dataNum) {
        this.dataNum = dataNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public long getCountNum() {
        return countNum;
    }

    public void setCountNum(long countNum) {
        this.countNum = countNum;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }
}
