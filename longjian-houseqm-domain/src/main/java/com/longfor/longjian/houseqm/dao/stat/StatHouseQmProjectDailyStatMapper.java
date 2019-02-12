package com.longfor.longjian.houseqm.dao.stat;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectDailyStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatHouseQmProjectDailyStatMapper extends LFMySQLMapper<StatHouseQmProjectDailyStat> {


    /**
     * SUM
     *
     * @param postfixDate
     * @param areaId
     * @param projectIds
     * @param categoryKey
     * @return
     */
    StatHouseQmProjectDailyStat selectSum(@Param("postfixDate") String postfixDate, @Param("areaId") Integer areaId,
                                          @Param("projectIds") List<Integer> projectIds,
                                          @Param("categoryKey") String categoryKey);

}