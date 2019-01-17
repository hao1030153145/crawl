package com.transing.crawl.integration.impl.ibatis;

import com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS;
import com.transing.crawl.integration.DatasourceDataService;
import com.transing.crawl.integration.bo.Datasource;
import com.transing.crawl.integration.bo.DatasourceTypeBO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * 包: com.transing.crawl.integration.impl.ibatis
 * 源文件:DatasourceDAOIbatis.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2018年01月10日
 */

@Scope("prototype")
@Repository("datasourceDataService")
public class DatasourceDAOIbatis extends BaseDaoiBATIS implements
        DatasourceDataService
{

    @Override
    public Datasource getDataSourceById(long id)
    {
        Datasource datasource=sqlSessionTemplate.selectOne("datasourceMapper.getDatasourceId",id);
        if(datasource==null){
            datasource=new Datasource();
            datasource.setId(id);
            datasource.setIsNeedProxyIp(0);
            datasource.setIsNeedLogin(0);
            addOrUpdate(datasource);
        }
        return datasource;
    }

    @Override
    public void addOrUpdate(Datasource datasource)
    {
        try{
            sqlSessionTemplate.insert("datasourceMapper.addDatasource",datasource);
        }catch (Exception e){
            sqlSessionTemplate.update("datasourceMapper.modifyDatasource",datasource);
        }
    }

    @Override
    public DatasourceTypeBO getDatasourceTypeBO(long id)
    {
        DatasourceTypeBO datasourceTypeBO=sqlSessionTemplate.selectOne("datasourceMapper.getDatasourceTypeId",id);
        return datasourceTypeBO;
    }

    @Override
    public void addOrUpdate(DatasourceTypeBO datasourceTypeBO)
    {
        try{
            sqlSessionTemplate.insert("datasourceMapper.addDatasourceType",datasourceTypeBO);
        }catch (Exception e){
            sqlSessionTemplate.update("datasourceMapper.modifyDatasourceType",datasourceTypeBO);
        }
    }
}
