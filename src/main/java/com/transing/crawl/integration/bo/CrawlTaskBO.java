package com.transing.crawl.integration.bo;

import java.util.Date;

public class CrawlTaskBO {
    private long id;
    private String taskName;
    private long datasourceId;
    private long datasourceTypeId;
    private String errorMsg;
    private long taskStatus;
    private String jsonParam;
    private String detailId;
    private String projectId;
    private long completeSubTaskNum;
    private long subTaskNum;
    private Date createTime;
    private Date lastmodifyTime;
    private int discardNum;//丢弃数

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDetailId()
    {
        return detailId;
    }

    public void setDetailId(String detailId)
    {
        this.detailId = detailId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(long datasourceId) {
        this.datasourceId = datasourceId;
    }

    public long getDatasourceTypeId() {
        return datasourceTypeId;
    }

    public void setDatasourceTypeId(long datasourceTypeId) {
        this.datasourceTypeId = datasourceTypeId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public long getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(long taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getJsonParam() {
        return jsonParam;
    }

    public void setJsonParam(String jsonParam) {
        this.jsonParam = jsonParam;
    }

    public long getCompleteSubTaskNum() {
        return completeSubTaskNum;
    }

    public void setCompleteSubTaskNum(long completeSubTaskNum) {
        this.completeSubTaskNum = completeSubTaskNum;
    }

    public long getSubTaskNum() {
        return subTaskNum;
    }

    public void setSubTaskNum(long subTaskNum) {
        this.subTaskNum = subTaskNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastmodifyTime() {
        return lastmodifyTime;
    }

    public void setLastmodifyTime(Date lastmodifyTime) {
        this.lastmodifyTime = lastmodifyTime;
    }

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public int getDiscardNum()
    {
        return discardNum;
    }

    public void setDiscardNum(int discardNum)
    {
        this.discardNum = discardNum;
    }
}
