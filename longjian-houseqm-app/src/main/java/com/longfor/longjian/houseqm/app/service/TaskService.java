package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskRspService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/15.
 */
@Repository
@Service
@Slf4j
public class TaskService {
    @Resource
    HouseQmCheckTaskRspService houseQmCheckTaskRspService;
    public HouseQmCheckTaskRspVo getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId) {
        HouseQmCheckTask houseQmCheckTask=houseQmCheckTaskRspService.getHouseQmCheckTaskByProjTaskId(projectId,taskId);
        HouseQmCheckTaskRspVo houseQmCheckTaskRspVo = new HouseQmCheckTaskRspVo();
        houseQmCheckTaskRspVo.setProjectId(houseQmCheckTask.getProjectId());
        houseQmCheckTaskRspVo.setTaskId(houseQmCheckTask.getTaskId());
                houseQmCheckTaskRspVo.setName(houseQmCheckTask.getName());
        houseQmCheckTaskRspVo.setStatus(houseQmCheckTask.getStatus());
                houseQmCheckTaskRspVo.setCategoryCls(houseQmCheckTask.getCategoryCls());
        houseQmCheckTaskRspVo.setRootCategoryKey(houseQmCheckTask.getRootCategoryKey());
                houseQmCheckTaskRspVo.setAreaIds(houseQmCheckTask.getAreaIds());
        houseQmCheckTaskRspVo.setAreaTypes(houseQmCheckTask.getAreaTypes());
        houseQmCheckTaskRspVo.setAreaIds(houseQmCheckTask.getAreaIds());
                houseQmCheckTaskRspVo.setPlanBeginOn(dateToInt(houseQmCheckTask.getPlanBeginOn()) );
        houseQmCheckTaskRspVo.setPlanEndOn(dateToInt(houseQmCheckTask.getPlanEndOn()));
                houseQmCheckTaskRspVo.setCreateAt(dateToInt(houseQmCheckTask.getCreateAt()));
        houseQmCheckTaskRspVo.setUpdateAt(dateToInt(houseQmCheckTask.getUpdateAt()));
                houseQmCheckTaskRspVo.setDeleteAt(dateToInt(houseQmCheckTask.getDeleteAt()));
                return houseQmCheckTaskRspVo;
    }

//date 转int
    private  Integer dateToInt(Date date){
        if(date==null){
            return  null;
        }
       return   (int) date.getTime() / 1000;
    }
}
