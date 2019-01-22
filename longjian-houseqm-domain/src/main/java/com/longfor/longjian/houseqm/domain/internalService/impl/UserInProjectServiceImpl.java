package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.UserInProjectMapper;
import com.longfor.longjian.houseqm.domain.internalService.IUserInProjectService;
import com.longfor.longjian.houseqm.po.zj2db.UserInProject;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService.impl
 * @ClassName: UserInProjectServiceImpl
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 18:29
 */
@Service
@Slf4j
public class UserInProjectServiceImpl implements IUserInProjectService {

    @Resource
    private UserInProjectMapper userInProjectMapper;


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
