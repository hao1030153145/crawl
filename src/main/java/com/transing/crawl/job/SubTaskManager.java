package com.transing.crawl.job;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.jeeframework.jeetask.task.Task;
import com.jeeframework.jeetask.zookeeper.storage.NodeStorage;
import com.jeeframework.jeetask.zookeeper.task.TaskNode;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.util.validate.Validate;
import com.transing.crawl.job.server.WaitTaskNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 包: com.transing.crawl.job
 * 源文件:SubTaskManager.java
 * 任务缓冲队列
 * @author Allen  Copyright 2016 成都创行, Inc. All rights reserved.2017年12月21日
 */
public class SubTaskManager
{
    private  NodeStorage nodeStorage;
    private WaitTaskNode waitTaskNode;
    private static SubTaskManager subTaskManager=null;
    private static CoordinatorRegistryCenter regCenter=null;


    private SubTaskManager(){
        nodeStorage = new NodeStorage(regCenter);
        waitTaskNode=new WaitTaskNode();
    }


    /**
     * 提交任务到缓冲任务队列中
     */
    public void submitWaitTask(Task task){
        try
        {
            LoggerUtil.debugTrace(
                    "===========================================任务 " +
                            task.getId() + " ,放入缓冲任务队列中。。。。");

             this.nodeStorage.fillNode(waitTaskNode
                .getTaskIdWaitNode(String.valueOf(task.getId())),task.toZk());
        }catch (Exception e){
            e.getMessage();
        }
    }


    /**
     * 获取所有的等待任务的队列
     * @return
     */
    public List<String> getAllWaitNodeTask(){
        if(!nodeStorage.isNodeExisted(waitTaskNode.getTasksWaitNode()))
        {
            return new ArrayList<>();
        }
        return nodeStorage.getNodeChildrenKeys(waitTaskNode.getTasksWaitNode());
    }

    /**
     * 获取等待任务中的队列
     * @param node
     * @return
     */
    public Task getWaitNodeTask(String node){
        String nodeTaskStr= nodeStorage.getNodeData(WaitTaskNode
                .getTaskIdWaitNode(node));
        Task task=null;
        try{
            task=Task.fromZk(nodeTaskStr);
        }catch (Exception e){}
        return task;
    }

    /**
     * 获取所有有连接的任务
     * @return
     */
    public List<String> getAllHasConnectionNodeTask(){
        try
        {
            if (!nodeStorage.isNodeExisted(waitTaskNode.getTasksRunNode()))
                return new ArrayList<>();
            return nodeStorage
                    .getNodeChildrenKeys(waitTaskNode.getTasksRunNode());
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    /**
     * 获取有连接的任务
     * @param node
     * @return
     */
    public void removeHasConnectionNodeTask(String node){
        nodeStorage.removeNode(waitTaskNode.getTaskIdRunNode(node));
    }

    /**
     * 是否没有该任务
     * @param node
     * @return
     */
    public boolean isNotRunHasConnectionTask(String node){
        try
        {
            String taskstr = nodeStorage
                    .getNodeData(TaskNode.getTaskIdNode(node));
            if (!Validate.isEmpty(taskstr))
                return true;
        }catch (Exception e){
        }
        return false;
    }

    /**
     * 将任务从等待连接的数据中移动到有连接的任务队列中
     * @param task
     */
    public void changeTaskWaitQ2RunQ(Task task){
        nodeStorage.fillNode(waitTaskNode.getTaskIdRunNode(String.valueOf(task.getId())),task.getId());
        nodeStorage.fillNode(TaskNode.getTaskIdNode(String.valueOf(task.getId())), task.toZk());
        nodeStorage.removeNode(waitTaskNode
                .getTaskIdWaitNode(String.valueOf(task.getId())));
    }


    public static SubTaskManager newInstace(final CoordinatorRegistryCenter regCentere){
        if(regCenter==null&&regCentere!=null)
            regCenter=regCentere;

        if(subTaskManager==null){
            subTaskManager=new SubTaskManager();
        }
        return subTaskManager;
    }


}
