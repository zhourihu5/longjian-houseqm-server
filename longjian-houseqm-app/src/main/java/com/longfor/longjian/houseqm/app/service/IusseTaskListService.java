package com.longfor.longjian.houseqm.app.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskSimpleRspVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskVo;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Dongshun on 2018/12/13.
 */
@Repository
@Service
@Slf4j
public class IusseTaskListService {
@Resource
HouseQmCheckTaskService houseQmCheckTaskService;

    public List<HouseQmCheckTaskSimpleRspVo> selectByProjectIdAndCategoryCls(Integer projectId, Integer categoryCls) {
       List<HouseQmCheckTask> houseQmCheckTaskList=houseQmCheckTaskService.selectByProjectIdAndCategoryCls(projectId,categoryCls);
        ArrayList<HouseQmCheckTaskSimpleRspVo> hQmCheckTaskList = new ArrayList<HouseQmCheckTaskSimpleRspVo>();
        for (int i = 0; i <houseQmCheckTaskList.size() ; i++) {
            String[] split = houseQmCheckTaskList.get(i).getAreaTypes().split(",");
            HouseQmCheckTaskSimpleRspVo rspVo = new HouseQmCheckTaskSimpleRspVo();
            rspVo.setProjectId(houseQmCheckTaskList.get(i).getProjectId());
            rspVo.setTaskId(houseQmCheckTaskList.get(i).getTaskId());
           rspVo.setAreaTypes(Integer.parseInt(split[0]));
            rspVo.setName(houseQmCheckTaskList.get(i).getName());
            rspVo.setPlanEndOn(houseQmCheckTaskList.get(i).getPlanEndOn());
            rspVo.setCreateAt(houseQmCheckTaskList.get(i).getCreateAt());
            rspVo.setPlanBeginOn(houseQmCheckTaskList.get(i).getPlanBeginOn());
            hQmCheckTaskList.add(rspVo);
        }
        return hQmCheckTaskList;
    }
}
