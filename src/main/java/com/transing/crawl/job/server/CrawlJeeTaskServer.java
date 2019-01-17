package com.transing.crawl.job.server;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.jeeframework.jeetask.startup.JeeTaskServer;
import com.jeeframework.jeetask.zookeeper.election.LeaderService;
import com.jeeframework.jeetask.zookeeper.server.ServerService;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 包: com.transing.crawl.job.server
 * 源文件:CrawlJeeTaskServer.java
 *
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年12月21日
 */
@Slf4j
public class CrawlJeeTaskServer extends JeeTaskServer
{

    @Override
    protected LeaderService newLeaderService(String rolesTmp)
    {
        return new CrawlTaskService(regCenter,jobEventBus,context,serverService,rolesTmp);
    }


}
