package com.transing.crawl.job.server;

import com.jeeframework.jeetask.zookeeper.storage.NodePath;

/**
 * 包: com.transing.crawl.job.server
 * 源文件:WaitTaskNode.java
 * 等待抓取节点
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年12月22日
 */
public class WaitTaskNode
{
    public static final String ROOT = "waitTasks";

    private static final String TASKS = ROOT + "/%s/%s";

    private static final String HasConnection="hasConnection";
    private static final String waitConnection="waitConnection";

    private final NodePath nodePath;

    public WaitTaskNode() {
        nodePath = new NodePath();
    }

    public String getTasksWaitNode() {
        return String.format("%s/%s",ROOT,waitConnection);
    }

    public String getTasksRunNode() {
        return String.format("%s/%s",ROOT,HasConnection);
    }

    public static String getTaskIdWaitNode(final String task) {
        return String.format(TASKS,waitConnection,task);
    }
    public static String getTaskIdRunNode(final String task) {
        return String.format(TASKS,HasConnection,task);
    }
}
