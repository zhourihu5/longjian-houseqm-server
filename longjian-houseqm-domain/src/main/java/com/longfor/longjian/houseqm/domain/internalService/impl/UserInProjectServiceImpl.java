package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.UserInProjectMapper;
import com.longfor.longjian.houseqm.domain.internalService.UserInProjectService;
import com.longfor.longjian.houseqm.po.zj2db.UserInProject;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.longfor.longjian.houseqm.domain.internalService.IUserInProjectService;
import com.longfor.longjian.houseqm.po.zj2db.UserInProject;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/22.
 */

@Transactional
@Service
@Slf4j
public class UserInProjectServiceImpl implements UserInProjectService {
    @Resource
    UserInProjectMapper userInProjectMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInProject> selectByUserIdNotDel(Integer uid) {
        Example example = new Example(UserInProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", uid).andIsNull("deleteAt");
        return userInProjectMapper.selectByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInProject> searchByUserId(int uid) {
        Example example = new Example(UserInProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",uid);
        ExampleUtil.addDeleteAtJudge(example);

        return userInProjectMapper.selectByExample(example);
    }
}
