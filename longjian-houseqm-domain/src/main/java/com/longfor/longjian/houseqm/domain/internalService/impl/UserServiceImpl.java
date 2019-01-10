package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.google.common.collect.Maps;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.UserMapper;
import com.longfor.longjian.houseqm.domain.internalService.UserService;
import com.longfor.longjian.houseqm.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Houyan
 * @date 2018/12/15 0015 13:20
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

    /**
     *
     * @param users
     * @return
     */
    @LFAssignDataSource("zhijian2_apisvr")
    public Map<Integer, User> selectByIds(List<Integer> users){
        List<User> userList = userMapper.selectByUserIds(users);
        HashMap<Integer, User> map = Maps.newHashMap();
        for (User user : userList) {
            map.put(user.getUserId(),user);
        }
        return map;
    }

    @Override
    @LFAssignDataSource("zhijian2_apisvr")
    public List<User> searchByUserIdInAndNoDeleted(List<Integer> userIds) {
        return userMapper.selectByUserIdInAndNoDeleted(userIds,"false");
    }

    @Override
    public User selectByUserIdAndNotDelete(Integer senderId) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNull("deleteAt");
        criteria.andEqualTo("userId",senderId);

        return userMapper.selectOneByExample(example);
    }

}
