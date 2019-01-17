package com.transing.crawl.biz.service;

import com.jeeframework.logicframework.biz.service.BizService;
import com.jeeframework.logicframework.integration.DataServiceException;
import com.transing.crawl.integration.bo.StorageTypeBO;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service
 * 源文件:StorageTypeService.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月20日
 */
public interface StorageTypeService extends BizService
{
    StorageTypeBO getStorageTypeByDatasourceTypeId(Long datasourceTypeId);

}
