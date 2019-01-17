package com.transing.crawl.biz.service;

import com.jeeframework.logicframework.biz.service.BizService;
import com.transing.crawl.integration.bo.StorageTypeFieldBO;
import com.transing.crawl.integration.bo.StorageTypeFieldTypeBO;

import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service
 * 源文件:StorageTypeFieldService.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月21日
 */
public interface StorageTypeFieldService extends BizService
{


    List<StorageTypeFieldTypeBO> getStorageTypeFieldTypeList();

    Map<Long,StorageTypeFieldBO> getStorageTypeFields(Long datasourceTypeId);

}
