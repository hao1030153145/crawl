package com.transing.crawl.web.po;

public class CrawlInputParamPO
{
    private long id;
    private String datasourceName;
    private String datasourceTypeName;
    private String paramCnName;
    private String paramEnName;
    private String style;
    private String isRequired;
    private String promptx;

    private String controlProp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getDatasourceTypeName() {
        return datasourceTypeName;
    }

    public void setDatasourceTypeName(String datasourceTypeName) {
        this.datasourceTypeName = datasourceTypeName;
    }

    public String getParamCnName() {
        return paramCnName;
    }

    public void setParamCnName(String paramCnName) {
        this.paramCnName = paramCnName;
    }

    public String getParamEnName() {
        return paramEnName;
    }

    public void setParamEnName(String paramEnName) {
        this.paramEnName = paramEnName;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getPromptx() {
        return promptx;
    }

    public void setPromptx(String promptx) {
        this.promptx = promptx;
    }

    public String getControlProp()
    {
        return controlProp;
    }

    public void setControlProp(String controlProp)
    {
        this.controlProp = controlProp;
    }
}
