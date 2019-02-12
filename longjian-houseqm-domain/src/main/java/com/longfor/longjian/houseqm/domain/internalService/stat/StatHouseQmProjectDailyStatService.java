package com.longfor.longjian.houseqm.domain.internalService.stat;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.common.time.TimeFrame;
import com.longfor.longjian.common.util.DateUtil;
import com.longfor.longjian.houseqm.dao.stat.StatHouseQmProjectDailyStatMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectDailyStat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     // stat_house_qm_project_daily_stat_20181116
     // stat_house_qm_project_weekly_stat_201846
     // stat_house_qm_project_monthly_stat_201811
     // stat_house_qm_project_yearly_stat_2018
     // stat_house_qm_project_quarterly_stat_20191
     *
     * @param timeFrame
     * @param categoryKey
     * @param projectIds
     * @return
     */
    @LFAssignDataSource("zhijian2_stat")
    public StatHouseQmProjectDailyStat query(TimeFrame timeFrame,String categoryKey, List<Integer> projectIds){

        /**
         * 防止天表没生成
         */
        if(timeFrame.getBeginOn().after(DateUtil.yesterdayZeroDate())){
            return null;
        }

        String postFixDate  = timeFrame.getTableIdx();
        Integer areaId = null;


        StatHouseQmProjectDailyStat stat =  statHouseQmProjectDailyStatMapper.selectSum(postFixDate, areaId,
                projectIds, categoryKey);

        log.debug("StatHouseQmProjectDailyStat query:{}, result is null :{}", postFixDate, stat==null);

        return stat;
    }

}
