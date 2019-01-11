package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.Project;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService
 * @ClassName: ProjectService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:29
 */
public interface ProjectService {

    Project getOneByProjId(int projId);
}
