package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskSquadMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskSquadService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskSquad;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:37
 */
@Service
@Slf4j
public class HouseQmCheckTaskSquadServiceImpl implements HouseQmCheckTaskSquadService {


    @Resource
    HouseQmCheckTaskSquadMapper houseQmCheckTaskSquadMapper;


    /**
     *
     * @param taskIdList
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> selectByTaskIds(Set<Integer> taskIdList){

        return houseQmCheckTaskSquadMapper.selectByTaskIds(taskIdList,"false");
    }

    /**
     *
     * @param taskIdList
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList){

        return houseQmCheckTaskSquadMapper.selectByTaskIds(taskIdList,"true");
    }

}
