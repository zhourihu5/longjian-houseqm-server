package com.longfor.longjian.houseqm.domain.internalService.stat;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.stat.StatHouseQmProjectDailyStatMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2019/1/17 17:15
 */

@Service
@Slf4j
public class StatHouseQmProjectDailyStatService {


    @Resource
    StatHouseQmProjectDailyStatMapper statHouseQmProjectDailyStatMapper;


    /**
     *
     * @param categoryKey
     * @param timeFrameType
     * @param teamIds
     * @param timeFrameBegin
     * @param timeFrameEnd
     * @param timeFrameMax
     */
    @LFAssignDataSource("zhijian2_stat")
    public void searchStat(String categoryKey, String timeFrameType, List<Integer> teamIds, Date timeFrameBegin,
                           Date timeFrameEnd, Integer timeFrameMax){


        if(timeFrameMax == null){
            timeFrameMax =1 ;
        }

        //默认是昨天
        if(timeFrameEnd==null){
            timeFrameEnd = new Date();
        }



    }




}