package com.transing.crawl.integration;

import com.jeeframework.logicframework.integration.DataService;
import com.transing.crawl.integration.bo.Datasource;
import com.transing.crawl.integration.bo.DatasourceTypeBO;

/**
 * 包: com.transing.crawl.integration
 * 源文件:DatasourceDataService.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年01月10日
 */
public interface DatasourceDataService extends DataService
{
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Datasource getDataSourceById(long id) ;
    /**
     * 添加或者修改数据源
     * @param datasource
     */
    void addOrUpdate(Datasource datasource) ;



    /**
     * 查询数据源类型
     * @param id
     * @return
     */
    DatasourceTypeBO getDatasourceTypeBO(long id) ;


    /**
     * 添加或者修改数据源类型
     * @param datasourceTypeBO
     */
    void addOrUpdate(DatasourceTypeBO datasourceTypeBO) ;
}
