package com.longfor.longjian.houseqm.dao.stat;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectWeeklyStat;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectWeeklyStatExample;
import java.util.List;

public interface StatHouseQmProjectWeeklyStatMapper extends LFMySQLMapper<StatHouseQmProjectWeeklyStat> {
    int countByExample(StatHouseQmProjectWeeklyStatExample example);

    List<StatHouseQmProjectWeeklyStat> selectByExample(StatHouseQmProjectWeeklyStatExample example);
}