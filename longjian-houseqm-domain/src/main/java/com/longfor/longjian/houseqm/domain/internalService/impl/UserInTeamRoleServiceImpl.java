package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.UserInTeamRoleMapper;
import com.longfor.longjian.houseqm.domain.internalservice.UserInTeamRoleService;
import com.longfor.longjian.houseqm.po.zj2db.UserInTeamRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/21.
 */
@Transactional
@Slf4j
@Service
public class UserInTeamRoleServiceImpl implements UserInTeamRoleService {
    @Resource
    UserInTeamRoleMapper userInTeamRoleMapper;
    @Override
    @LFAssignDataSource("zhijian2_apisvr")
    public List<UserInTeamRole> selectByUserIdNotDel(Integer uid) {
        Example example = new Example(UserInTeamRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",uid).andIsNull("deleteAt");
        return userInTeamRoleMapper.selectByExample(example);
    }
}
