package com.transing.crawl.integration.bo;

public class CrawlInputParamBO {
    private String id;
    private String datasourceId;
    private String datasourceTypeId;
    private String paramCnName;
    private String paramEnName;
    private String prompt;
    private String styleName;
    private String styleCode;
    private String restrictions;
    private String isRequired;

    private String controlProp;

    public String getControlProp()
    {
        return controlProp;
    }

    public void setControlProp(String controlProp)
    {
        this.controlProp = controlProp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
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
}
