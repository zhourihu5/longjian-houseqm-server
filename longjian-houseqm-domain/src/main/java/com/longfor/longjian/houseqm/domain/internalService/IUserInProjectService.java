package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.UserInProject;

import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService
 * @ClassName: IUserInProjectService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 18:29
 */
public interface IUserInProjectService {

    List<UserInProject> searchByUserId(int uid);
}
