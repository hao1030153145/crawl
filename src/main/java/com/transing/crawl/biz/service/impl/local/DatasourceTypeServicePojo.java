package com.transing.crawl.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.transing.crawl.biz.service.DatasourceTypeService;
import com.transing.crawl.integration.DatasourceDataService;
import com.transing.crawl.integration.bo.DatasourceTypeBO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 包: com.transing.crawl.biz.service.impl.local
 * 源文件:DatasourceTypeServicePojo.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月22日
 */
@Service("datasourceTypeService")
public class DatasourceTypeServicePojo extends BaseService implements
        DatasourceTypeService
{

    @Resource
    DatasourceDataService datasourceDataService;

    @Override
    public DatasourceTypeBO getDatasourceTypeBO(long id) throws BizException
    {
       return datasourceDataService.getDatasourceTypeBO(id);
    }

    @Override
    public void addOrUpdate(DatasourceTypeBO datasourceTypeBO)
            throws BizException
    {
        datasourceDataService.addOrUpdate(datasourceTypeBO);
    }

}
