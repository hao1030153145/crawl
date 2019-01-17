package com.transing.crawl.integration.bo;

import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:StorageTypeFieldBO.java
 * 存储类型字段
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月21日
 */
public class StorageTypeFieldBO
{

    private long id;

    private long storageTypeId;

    private String fieldCnName;

    private String fieldEnName;

    private String fieldDesc;

    private String fieldType;

    private int fieldLength;

    private int decimalLength;

    private Date createTime;

    private Date lastmodifyTime;

    public long getStorageTypeId()
    {
        return storageTypeId;
    }

    public void setStorageTypeId(long storageTypeId)
    {
        this.storageTypeId = storageTypeId;
    }

    public String getFieldCnName()
    {
        return fieldCnName;
    }

    public void setFieldCnName(String fieldCnName)
    {
        this.fieldCnName = fieldCnName;
    }

    public String getFieldEnName()
    {
        return fieldEnName;
    }

    public void setFieldEnName(String fieldEnName)
    {
        this.fieldEnName = fieldEnName;
    }

    public String getFieldDesc()
    {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc)
    {
        this.fieldDesc = fieldDesc;
    }

    public String getFieldType()
    {
        return fieldType;
    }

    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }

    public int getFieldLength()
    {
        return fieldLength;
    }

    public void setFieldLength(int fieldLength)
    {
        this.fieldLength = fieldLength;
    }

    public int getDecimalLength()
    {
        return decimalLength;
    }

    public void setDecimalLength(int decimalLength)
    {
        this.decimalLength = decimalLength;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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
