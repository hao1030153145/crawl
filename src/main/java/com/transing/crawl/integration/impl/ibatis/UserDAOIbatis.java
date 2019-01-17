package com.transing.crawl.integration.impl.ibatis;

import com.jeeframework.logicframework.integration.dao.DAOException;
import com.jeeframework.logicframework.integration.dao.ibatis.BaseDaoiBATIS;
import com.transing.crawl.integration.UserDataService;
import com.transing.crawl.integration.bo.User;
import com.transing.crawl.web.filter.GetUsersFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问对象
 *
 * @author lanceyan
 * @version 1.0
 */
@Scope("prototype")
@Repository("userDataService")
public class UserDAOIbatis extends BaseDaoiBATIS implements UserDataService {

    /**
     * 简单描述：根据userFilter返回用户对象列表
     * <p/>
     *
     * @param getUsersFilter
     * @
     */
    @Override
    public List<User> getUsers(GetUsersFilter getUsersFilter) {
        try {
            return sqlSessionTemplate.selectList("userMapper.getUsers", getUsersFilter);
        } catch (DataAccessException e) {
            throw new DAOException(e);

        }
    }

    @Override
    public long getUsersCount(GetUsersFilter getUsersFilter) {
        try {
            return sqlSessionTemplate.selectOne("userMapper.getUsersCount", getUsersFilter);
        } catch (DataAccessException e) {
            throw new DAOException(e);

        }
    }


}