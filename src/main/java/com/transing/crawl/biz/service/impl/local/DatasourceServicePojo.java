package com.transing.crawl.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.transing.crawl.biz.service.DatasourceService;
import com.transing.crawl.integration.DatasourceDataService;
import com.transing.crawl.integration.bo.Datasource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 包: com.transing.crawl.biz.service.impl.local
 * 源文件:DatasourceServicePojo.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月20日
 */
@Service("datasourceService")
public class DatasourceServicePojo extends BaseService implements
        DatasourceService
{

    @Resource
    DatasourceDataService datasourceDataService;
    /**
     * 为最大限度的防止获取datasource信息卡顿或者断线
     * 现添加缓存池，先通过对缓存池获取数据，如果没有则
     * 远程获取并补充到缓存池中。
     * 为未能即时获取最新数据，每5分钟清空一次缓存池。
     */

    @Override
    public Datasource getDataSourceById(long id) throws BizException
    {
       return datasourceDataService.getDataSourceById(id);
    }

    @Override
    public void addOrUpdate(Datasource datasource) throws BizException
    {
        datasourceDataService.addOrUpdate(datasource);
    }

}
