package com.transing.crawl.biz.service.impl.local;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.BaseService;
import com.jeeframework.logicframework.integration.dao.DAOException;
import com.transing.crawl.biz.service.UserService;
import com.transing.crawl.integration.UserDataService;
import com.transing.crawl.integration.bo.User;
import com.transing.crawl.web.filter.GetUsersFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanceyan
 * @version 1.0
 */
@Service("userService")
public class UserServicePojo extends BaseService implements UserService {
    @Resource
    private UserDataService userDataService;

    @Override
    public List<User> getUsers(GetUsersFilter getUsersFilter) {
        try {
            return userDataService.getUsers(getUsersFilter);
        } catch (DAOException e) {
            throw new BizException(e);

        }
    }

    @Override
    public long getUsersCount(GetUsersFilter getUsersFilter) {
        try {
            return userDataService.getUsersCount(getUsersFilter);
        } catch (DAOException e) {
            throw new BizException(e);

        }
    }

}