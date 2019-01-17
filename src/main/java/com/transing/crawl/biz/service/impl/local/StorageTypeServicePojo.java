package com.transing.crawl.biz.service.impl.local;

import com.jeeframework.logicframework.biz.service.BaseService;
import com.jeeframework.logicframework.integration.DataServiceException;
import com.transing.crawl.biz.service.StorageTypeService;
import com.transing.crawl.integration.bo.StorageTypeBO;
import com.transing.crawl.util.WebUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service.impl.local
 * 源文件:StorageTypeServicePojo.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月20日
 */
@Service("storageTypeService")
public class StorageTypeServicePojo extends BaseService implements
        StorageTypeService
{

    @Override
    public StorageTypeBO getStorageTypeByDatasourceTypeId(
            Long datasourceTypeId)
    {
        StorageTypeBO storageTypeBO=new StorageTypeBO();
        String url="/common/getDataSourceTypeAndTableName.json?datasourceTypeId="+datasourceTypeId;
        Object object= WebUtil
                .callRemoteService(WebUtil.getBasebsByEnv()+url,"get",null);
        JSONObject storageJSON=JSONObject.fromObject(object);
        storageTypeBO.setId(storageJSON.getInt("storageTypeId"));
        storageTypeBO.setStorageTypeTable(storageJSON.getString("storageTypeTable"));
        storageTypeBO.setStorageTypeName(storageJSON.getString("storageTypeName"));
        return storageTypeBO;
    }
}
