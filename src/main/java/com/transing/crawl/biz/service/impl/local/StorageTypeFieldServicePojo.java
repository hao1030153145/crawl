package com.transing.crawl.biz.service.impl.local;

import com.jeeframework.logicframework.biz.service.BaseService;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.transing.crawl.biz.service.StorageTypeFieldService;
import com.transing.crawl.integration.bo.StorageTypeFieldBO;
import com.transing.crawl.integration.bo.StorageTypeFieldTypeBO;
import com.transing.crawl.util.WebUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包: com.transing.crawl.biz.service.impl.local
 * 源文件:StorageTypeFieldServicePojo.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月21日
 */
@Service("storageTypeFieldService")
public class StorageTypeFieldServicePojo extends BaseService implements
        StorageTypeFieldService {

    @Override
    public List<StorageTypeFieldTypeBO> getStorageTypeFieldTypeList()
    {
        List<StorageTypeFieldTypeBO> storageTypeFieldTypeBOs=new ArrayList<StorageTypeFieldTypeBO>();
        String url="/common/getFieldTypeList.json";
        Object object= WebUtil.callRemoteService(WebUtil.getBasebsByEnv()+url,"get",null);
        JSONArray array=JSONArray.fromObject(object);
        StorageTypeFieldTypeBO storageTypeFieldTypeBO=null;
        for (Object type:array){
            JSONObject typeJson= (JSONObject) type;
            storageTypeFieldTypeBO=new StorageTypeFieldTypeBO();
            storageTypeFieldTypeBO.setId(typeJson.getInt("id"));
            storageTypeFieldTypeBO.setFieldType(typeJson.getString("fieldType"));
            storageTypeFieldTypeBOs.add(storageTypeFieldTypeBO);
        }
        return storageTypeFieldTypeBOs;
    }

    @Override
    public Map<Long, StorageTypeFieldBO> getStorageTypeFields(Long datasourceTypeId)
    {
        Map<Long, StorageTypeFieldBO> storageTypeFieldBOMap=new HashMap<Long,StorageTypeFieldBO>();
        String url="/common/getStorageTypeFieldList.json?datasourceTypeId="+datasourceTypeId;
        Object object= WebUtil.callRemoteService(WebUtil.getBasebsByEnv()+url,"get",null);
        JSONArray array=JSONArray.fromObject(object);
        StorageTypeFieldBO storageTypeFieldBO=null;
        for (Object type:array){
            JSONObject jsonObject= (JSONObject) type;
            storageTypeFieldBO=new StorageTypeFieldBO();
            storageTypeFieldBO.setId(jsonObject.getInt("id"));
            storageTypeFieldBO.setFieldLength(jsonObject.getInt("fieldLength"));
            storageTypeFieldBO.setFieldType(jsonObject.getString("fieldTypeId"));
            storageTypeFieldBO.setDecimalLength(jsonObject.getInt("decimalLength"));
            storageTypeFieldBO.setFieldCnName(jsonObject.getString("fieldCnName"));
            storageTypeFieldBO.setFieldDesc(jsonObject.getString("fieldDesc"));
            storageTypeFieldBO.setFieldEnName(jsonObject.getString("fieldEnName"));
            storageTypeFieldBO.setStorageTypeId(jsonObject.getLong("storageTypeId"));
            storageTypeFieldBOMap.put(storageTypeFieldBO.getId(),storageTypeFieldBO);
        }
        return storageTypeFieldBOMap;
    }
}
