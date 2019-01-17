package com.transing.crawl.integration.bo;

import java.util.List;

public class CrawlInputAddOrEditPO {
    private String datasourceName;
    private String datasourceTypeId;
    private String datasourceTypeName;
    private List<CrawlInputBO> crawlInputParam;

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getDatasourceTypeId() {
        return datasourceTypeId;
    }

    public void setDatasourceTypeId(String datasourceTypeId) {
        this.datasourceTypeId = datasourceTypeId;
    }

    public String getDatasourceTypeName() {
        return datasourceTypeName;
    }

    public void setDatasourceTypeName(String datasourceTypeName) {
        this.datasourceTypeName = datasourceTypeName;
    }

    public List<CrawlInputBO> getCrawlInputParam() {
        return crawlInputParam;
    }

    public void setCrawlInputParam(List<CrawlInputBO> crawlInputParam) {
        this.crawlInputParam = crawlInputParam;
    }
}
