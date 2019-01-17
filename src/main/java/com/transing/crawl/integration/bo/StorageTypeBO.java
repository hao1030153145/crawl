package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:StorageTypeBO.java
 * 存储类型
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月20日
 */
public class StorageTypeBO
{
    private static final long serialVersionUID = 1L;

    private int id;

    private String storageTypeName; //存储类型名称

    private String storageTypeTable;//存储表名

    private Date createTime;

    private Date lastmodifyTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getStorageTypeName()
    {
        return storageTypeName;
    }

    public void setStorageTypeName(String storageTypeName)
    {
        this.storageTypeName = storageTypeName;
    }

    public String getStorageTypeTable()
    {
        return storageTypeTable;
    }

    public void setStorageTypeTable(String storageTypeTable)
    {
        this.storageTypeTable = storageTypeTable;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getLastmodifyTime()
    {
        return lastmodifyTime;
    }

    public void setLastmodifyTime(Date lastmodifyTime)
    {
        this.lastmodifyTime = lastmodifyTime;
    }
}
