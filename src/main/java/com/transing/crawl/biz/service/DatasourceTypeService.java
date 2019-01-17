package com.transing.crawl.biz.service;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BizService;
import com.transing.crawl.integration.bo.Datasource;
import com.transing.crawl.integration.bo.DatasourceTypeBO;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service
 * 源文件:DatasourceTypeService.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月22日
 */
public interface DatasourceTypeService extends BizService
{

    /**
     * 查询数据源类型
     * @param id
     * @return
     * @throws BizException
     */
    DatasourceTypeBO getDatasourceTypeBO(long id) throws BizException;


    /**
     * 添加或者修改数据源类型
     * @param datasourceTypeBO
     * @throws BizException
     */
    void addOrUpdate(DatasourceTypeBO datasourceTypeBO) throws BizException;

}
