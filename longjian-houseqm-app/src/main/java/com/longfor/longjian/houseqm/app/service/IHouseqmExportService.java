package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.export.ProjectOrdersVo;

import java.util.Date;
import java.util.List;


public interface IHouseqmExportService {

    List<ProjectOrdersVo> searchProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(Integer projectId, Integer taskId, List<Integer> areaIds, Integer repairerId, Date beginOn, Date endOn, Integer categoryCls);

    String exportProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(Integer projectId, Integer taskId, List<Integer> areaIds, Integer repairerId, Date beginOn, Date endOn, Integer categoryCls, boolean withRule);
}
