package com.longfor.longjian.houseqm.dao.stat;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmPartnerMonthlyStat;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmPartnerMonthlyStatExample;
import java.util.List;

public interface StatHouseQmPartnerMonthlyStatMapper extends LFMySQLMapper<StatHouseQmPartnerMonthlyStat> {
    int countByExample(StatHouseQmPartnerMonthlyStatExample example);

    List<StatHouseQmPartnerMonthlyStat> selectByExample(StatHouseQmPartnerMonthlyStatExample example);
}