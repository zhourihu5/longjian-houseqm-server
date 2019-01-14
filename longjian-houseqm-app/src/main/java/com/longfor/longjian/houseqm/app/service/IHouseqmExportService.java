package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.req.ProjectOrdersReq;
import com.longfor.longjian.houseqm.app.vo.export.ProjectOrdersVo;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.service
 * @ClassName: IHouseqmExportService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 10:07
 */
public interface IHouseqmExportService {

    List<ProjectOrdersVo> searchProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(Integer project_id, Integer task_id, List<Integer> areaIds, Integer repairer_id, Date begin_on, Date end_on, Integer category_cls);

    String exportProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(Integer project_id, Integer task_id, List<Integer> areaIds, Integer repairer_id, Date begin_on, Date end_on, Integer category_cls, boolean withRule);
}
