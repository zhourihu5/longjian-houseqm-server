package com.longfor.longjian.houseqm.dao.stat;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmPartnerWeeklyStat;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmPartnerWeeklyStatExample;
import java.util.List;

public interface StatHouseQmPartnerWeeklyStatMapper extends LFMySQLMapper<StatHouseQmPartnerWeeklyStat> {
    int countByExample(StatHouseQmPartnerWeeklyStatExample example);

    List<StatHouseQmPartnerWeeklyStat> selectByExample(StatHouseQmPartnerWeeklyStatExample example);
}