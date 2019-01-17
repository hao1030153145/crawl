package com.transing.crawl.biz.service;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BizService;
import com.transing.crawl.integration.bo.CrawlInputAddOrEditPO;
import com.transing.crawl.integration.bo.CrawlInputBO;
import com.transing.crawl.web.po.CrawlInputParamPO;
import com.transing.crawl.web.po.DataSourceAndDataSourceTypePO;

import java.util.List;
import java.util.Map;

public interface CrawlInputParamService extends BizService {


    Map<Long,CrawlInputBO> getAllCrawlInputBo(long datasourceTypeId) throws BizException;


}
