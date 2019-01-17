package com.transing.crawl.integration;

import com.jeeframework.logicframework.integration.DataService;
import com.transing.crawl.integration.bo.User;
import com.transing.crawl.web.filter.GetUsersFilter;

import java.util.List;

/**
 * 用户数据操作接口
 *
 * @author lanceyan
 * @version 1.0
 * @see
 */
public interface UserDataService extends DataService {

    /**
     * 简单描述：根据userFilter返回用户对象列表
     * <p>
     *
     * @param getUsersFilter
     * @
     */
    List<User> getUsers(GetUsersFilter getUsersFilter);

    /**
     * 简单描述：根据userFilter返回用户列表数量
     * <p>
     *
     * @param getUsersFilter
     * @
     */
    long getUsersCount(GetUsersFilter getUsersFilter);


}