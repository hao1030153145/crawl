package com.transing.crawl.integration.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 包: com.transing.crawl.integration.bo
 * 源文件:Datasource.java
 * 数据源
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年06月20日
 */
public class Datasource implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;

    private String datasourceName;

    private int isNeedLogin;

    private String loginClazz;

    private int status=1;

    private int isNeedProxyIp;

    private Date createTime;

    private Date lastmodifyTime;

    private String exceptionClazz;

    public Date getLastmodifyTime()
    {
        return lastmodifyTime;
    }

    public void setLastmodifyTime(Date lastmodifyTime)
    {
        this.lastmodifyTime = lastmodifyTime;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getDatasourceName()
    {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName)
    {
        this.datasourceName = datasourceName;
    }

    public int getIsNeedLogin()
    {
        return isNeedLogin;
    }

    public void setIsNeedLogin(int isNeedLogin)
    {
        this.isNeedLogin = isNeedLogin;
    }

    public String getLoginClazz()
    {
        return loginClazz;
    }

    public void setLoginClazz(String loginClazz)
    {
        this.loginClazz = loginClazz;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
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

    public String getExceptionClazz()
    {
        return exceptionClazz;
    }

    public void setExceptionClazz(String exceptionClazz)
    {
        this.exceptionClazz = exceptionClazz;
    }
}
