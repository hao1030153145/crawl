package com.transing.crawl.integration.bo;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:StorageTypeFieldTypeBO.java
 * 存储字段类型
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月21日
 */
public class StorageTypeFieldTypeBO
{
    private static final long serialVersionUID = 1L;
    private int id;
    private String fieldType;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFieldType()
    {
        return fieldType;
    }

    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }
}
