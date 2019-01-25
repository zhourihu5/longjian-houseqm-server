package com.longfor.longjian.houseqm.dao.stat;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmPartnerYearlyStat;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmPartnerYearlyStatExample;
import java.util.List;

public interface StatHouseQmPartnerYearlyStatMapper extends LFMySQLMapper<StatHouseQmPartnerYearlyStat> {
    int countByExample(StatHouseQmPartnerYearlyStatExample example);

    List<StatHouseQmPartnerYearlyStat> selectByExample(StatHouseQmPartnerYearlyStatExample example);
}