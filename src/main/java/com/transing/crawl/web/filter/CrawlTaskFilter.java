package com.transing.crawl.web.filter;

public class CrawlTaskFilter {
    private String project;
    private String taskName;
    private String datasourceId;
    private String datasourceTypeId;
    private String status;
    private String createdTime;
    private String createTimeBefore;
    private Integer startRow;
    private Integer size;

    public CrawlTaskFilter(String project,String taskName, String datasourceId, String datasourceTypeId, String status, String createdTime, Integer startRow, Integer size) {
        this.taskName = taskName;
        this.datasourceId = datasourceId;
        this.datasourceTypeId = datasourceTypeId;
        this.status = status;
        this.createdTime = createdTime;
        this.startRow = startRow;
        this.size = size;
        this.project=project;
    }

    public CrawlTaskFilter()
    {
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDatasourceTypeId() {
        return datasourceTypeId;
    }

    public void setDatasourceTypeId(String datasourceTypeId) {
        this.datasourceTypeId = datasourceTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getProject()
    {
        return project;
    }

    public void setProject(String project)
    {
        this.project = project;
    }

    public String getCreateTimeBefore()
    {
        return createTimeBefore;
    }

    public void setCreateTimeBefore(String createTimeBefore)
    {
        this.createTimeBefore = createTimeBefore;
    }
}
