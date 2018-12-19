package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Dongshun on 2018/12/15.
 */
@Service
@Slf4j
public class HouseQmCheckTaskRspService {
   @Resource
    HouseQmCheckTaskMapper houseQmCheckTaskMapper;
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId) {
      return   houseQmCheckTaskMapper.getHouseQmCheckTaskByProjTaskId( projectId,  taskId);
    }
}
