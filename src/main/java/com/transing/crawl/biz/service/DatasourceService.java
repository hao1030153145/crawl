package com.transing.crawl.biz.service;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BizService;
import com.jeeframework.logicframework.integration.DataServiceException;
import com.transing.crawl.integration.bo.Datasource;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service
 * 源文件:DatasourceService.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月20日
 */
public interface DatasourceService extends BizService
{


    /**
     * 根据id查询
     * @param id
     * @return
     */
    Datasource getDataSourceById(long id) throws
            BizException;

    /**
     * 添加或者修改数据源
     * @param datasource
     * @throws BizException
     */
    void addOrUpdate(Datasource datasource) throws BizException;

}
