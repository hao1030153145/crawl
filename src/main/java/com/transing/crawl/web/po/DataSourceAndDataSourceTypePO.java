package com.transing.crawl.web.po;

import com.transing.crawl.integration.bo.DataSourceAndDataSourceTypeBO;
import com.transing.crawl.integration.bo.DatasourceTypeBO;

import java.util.List;

public class DataSourceAndDataSourceTypePO {
    private String datasourceId;
    private String datasourceName;
    private List<DataSourceAndDataSourceTypeBO> datasourceTypes;

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public List<DataSourceAndDataSourceTypeBO> getDatasourceTypes() {
        return datasourceTypes;
    }

    public void setDatasourceTypes(List<DataSourceAndDataSourceTypeBO> datasourceTypes) {
        this.datasourceTypes = datasourceTypes;
    }
}
