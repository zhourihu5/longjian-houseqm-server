package com.longfor.longjian.houseqm.app.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskSimpleRspVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskVo;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
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
            HouseQmCheckTaskSimpleRspVo rspVo = new HouseQmCheckTaskSimpleRspVo();
            rspVo.setProject_id(houseQmCheckTaskList.get(i).getProjectId());
            rspVo.setTask_id(houseQmCheckTaskList.get(i).getTaskId());
            List<Integer> split = StringSplitToListUtil.splitToIdsComma(houseQmCheckTaskList.get(i).getAreaTypes(), ",");
            rspVo.setArea_types(split);
            rspVo.setName(houseQmCheckTaskList.get(i).getName());
            rspVo.setPlan_end_on(DateUtil.datetimeToTimeStamp( houseQmCheckTaskList.get(i).getPlanEndOn()));
            rspVo.setCreate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskList.get(i).getCreateAt()));
            rspVo.setPlan_begin_on(DateUtil.datetimeToTimeStamp(houseQmCheckTaskList.get(i).getPlanBeginOn()));
            hQmCheckTaskList.add(rspVo);
        }
        return hQmCheckTaskList;
    }
}
