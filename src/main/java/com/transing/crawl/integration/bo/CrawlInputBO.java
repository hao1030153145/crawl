package com.transing.crawl.integration.bo;

public class CrawlInputBO {
    private long id;
    private long datasourceId;
    private long datasourceTypeId;
    private String paramCnName;
    private String paramEnName;
    private String prompt;
    private long styleId;
    private String restrictions;
    private String isRequired;

    private String controlProp;//属性的值

    private String styleName;

    private String styleCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public long getStyleId() {
        return styleId;
    }

    public void setStyleId(long styleId) {
        this.styleId = styleId;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getControlProp()
    {
        return controlProp;
    }

    public void setControlProp(String controlProp)
    {
        this.controlProp = controlProp;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    public String getStyleCode()
    {
        return styleCode;
    }

    public void setStyleCode(String styleCode)
    {
        this.styleCode = styleCode;
    }
}
