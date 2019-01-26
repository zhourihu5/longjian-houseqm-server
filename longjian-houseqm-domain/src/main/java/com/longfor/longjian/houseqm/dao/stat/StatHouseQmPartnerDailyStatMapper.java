package com.longfor.longjian.houseqm.dao.stat;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmPartnerDailyStat;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmPartnerDailyStatExample;
import java.util.List;

public interface StatHouseQmPartnerDailyStatMapper extends LFMySQLMapper<StatHouseQmPartnerDailyStat> {
    int countByExample(StatHouseQmPartnerDailyStatExample example);

    List<StatHouseQmPartnerDailyStat> selectByExample(StatHouseQmPartnerDailyStatExample example);
}