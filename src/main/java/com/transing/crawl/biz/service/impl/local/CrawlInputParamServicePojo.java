package com.transing.crawl.biz.service.impl.local;

import com.alibaba.druid.support.logging.Log;
import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.transing.crawl.biz.service.CrawlInputParamService;
import com.transing.crawl.integration.bo.CrawlInputBO;
import com.transing.crawl.integration.bo.CrawlInputParamBO;
import com.transing.crawl.util.WebUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("crawlInputParamService")
public class CrawlInputParamServicePojo extends BaseService implements
        CrawlInputParamService {

    @Override
    public Map<Long, CrawlInputBO> getAllCrawlInputBo(long datasourceTypeId)
            throws BizException
    {
        Map<Long, CrawlInputBO> crawlInputParamBOMap=new HashMap<Long, CrawlInputBO>();
        String url="/common/getCrawlInputParamsByDatasourceType.json?datasourceTypeId="+datasourceTypeId;
        Object object= WebUtil
                .callRemoteService(WebUtil.getBasebsByEnv()+url,"get",null);
        JSONArray crawlInputBOArray=JSONArray.fromObject(object);
        LoggerUtil.infoTrace("crawlInputParam",datasourceTypeId+"输入参数："+crawlInputBOArray.toString());
        CrawlInputBO crawlInputParamBO =null;
        for (Object inputParamBo:crawlInputBOArray){
            JSONObject crawlInputBO= (JSONObject) inputParamBo;
            crawlInputParamBO = new CrawlInputBO();
            crawlInputParamBO.setId(crawlInputBO.getLong("id"));
            crawlInputParamBO.setDatasourceId(crawlInputBO.getLong("datasourceId"));
            crawlInputParamBO.setDatasourceTypeId(crawlInputBO.getLong("datasourceTypeId"));
            crawlInputParamBO.setIsRequired(crawlInputBO.getString("isRequired"));
            crawlInputParamBO.setParamCnName(crawlInputBO.getString("paramCnName"));
            crawlInputParamBO.setParamEnName(crawlInputBO.getString("paramEnName"));
            crawlInputParamBO.setPrompt(crawlInputBO.getString("prompt"));
            crawlInputParamBO.setRestrictions(crawlInputBO.getString("restrictions"));
            crawlInputParamBO.setStyleId(crawlInputBO.getLong("styleId"));
            crawlInputParamBO.setStyleCode(crawlInputBO.getString("styleCode"));
            crawlInputParamBO.setStyleName(crawlInputBO.getString("styleName"));
            crawlInputParamBOMap.put(crawlInputParamBO.getId(),crawlInputParamBO);
        }
        return crawlInputParamBOMap;
    }
}
