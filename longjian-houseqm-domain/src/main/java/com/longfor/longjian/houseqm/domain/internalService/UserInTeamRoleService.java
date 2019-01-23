package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.UserInTeamRole;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/21.
 */
public interface UserInTeamRoleService {
    List<UserInTeamRole> selectByUserIdNotDel(Integer uid);
}
