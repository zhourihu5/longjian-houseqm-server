package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.UserInProject;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/22.
 */
public interface UserInProjectService {
    List<UserInProject> selectByUserIdNotDel(Integer uid);
}
