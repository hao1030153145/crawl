package com.transing.crawl.integration.bo;

import java.util.Date;

public class CrawlSubTaskBO {
    private long id;
    private long taskId;
    private String crawlUrl;
    private long taskProgress;
    private long taskStatus;
    private String complatePage;//抓取页数
    private String errorMsg;
    private String host;
    private String paramValue;
    private long crawlDataNum;
    private Date createTime;
    private Date lastmodifyTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getCrawlUrl() {
        return crawlUrl;
    }

    public void setCrawlUrl(String crawlUrl) {
        this.crawlUrl = crawlUrl;
    }

    public long getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(long taskProgress) {
        this.taskProgress = taskProgress;
    }

    public long getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(long taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getErrorMsg() {
        return errorMsg==null?"":errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getCrawlDataNum() {
        return crawlDataNum;
    }

    public void setCrawlDataNum(long crawlDataNum) {
        this.crawlDataNum = crawlDataNum;
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

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getComplatePage()
    {
        return complatePage==null?"":complatePage;
    }

    public void setComplatePage(String complatePage)
    {
        this.complatePage = complatePage;
    }
}
