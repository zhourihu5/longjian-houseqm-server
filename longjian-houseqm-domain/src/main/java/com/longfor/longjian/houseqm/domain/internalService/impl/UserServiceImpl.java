package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_apisvr.UserMapper;
import com.longfor.longjian.houseqm.domain.internalService.UserService;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
     * go 代码 unscoped = true
     * getUsersByIds()
     * @param users
     * @return
     */
    @LFAssignDataSource("zhijian2_apisvr")
    public Map<Integer, User> selectByIds(List<Integer> users){
        if (users==null||users.size()<=0)return Maps.newHashMap();
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("userId",users);
        List<User> userList= userMapper.selectByExample(example);
        Map<Integer, User> map = userList.stream().collect(Collectors.toMap(User::getUserId, u -> u));
        return map;
    }

    @Override
    @LFAssignDataSource("zhijian2_apisvr")
    public List<User> searchByUserIdInAndNoDeleted(List<Integer> userIds) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("userId",userIds);
        ExampleUtil.addDeleteAtJudge(example);
        return userMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2_apisvr")
    public User selectByUserIdAndNotDelete(Integer senderId) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",senderId);
        criteria.andIsNull("deleteAt");
        return userMapper.selectOneByExample(example);
    }

}
