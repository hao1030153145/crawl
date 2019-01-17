package com.transing.crawl.integration.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:DatasourceTypeBO.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月22日
 */
public class DatasourceTypeBO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;

    private long datasourceId;

    private String datasourceTypeName;

    private int storageTypeId;

    private int isNeedLogin;

    private int isNeedProxyIp;

    private Date createTime;

    private Date lastmodifyTime;

    private int status;

    private String exceptions;//异常处理类的集合
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getDatasourceId()
    {
        return datasourceId;
    }

    public void setDatasourceId(long datasourceId)
    {
        this.datasourceId = datasourceId;
    }

    public String getDatasourceTypeName()
    {
        return datasourceTypeName;
    }

    public void setDatasourceTypeName(String datasourceTypeName)
    {
        this.datasourceTypeName = datasourceTypeName;
    }

    public int getStorageTypeId()
    {
        return storageTypeId;
    }

    public void setStorageTypeId(int storageTypeId)
    {
        this.storageTypeId = storageTypeId;
    }

    public int getIsNeedLogin()
    {
        return isNeedLogin;
    }

    public void setIsNeedLogin(int isNeedLogin)
    {
        this.isNeedLogin = isNeedLogin;
    }

    public int getIsNeedProxyIp()
    {
        return isNeedProxyIp;
    }

    public void setIsNeedProxyIp(int isNeedProxyIp)
    {
        this.isNeedProxyIp = isNeedProxyIp;
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

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getExceptions()
    {
        return exceptions;
    }

    public void setExceptions(String exceptions)
    {
        this.exceptions = exceptions;
    }
}
