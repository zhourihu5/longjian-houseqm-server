package com.longfor.longjian.houseqm.dao.stat;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectMonthlyStat;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectMonthlyStatExample;
import java.util.List;

public interface StatHouseQmProjectMonthlyStatMapper extends LFMySQLMapper<StatHouseQmProjectMonthlyStat> {
    int countByExample(StatHouseQmProjectMonthlyStatExample example);

    List<StatHouseQmProjectMonthlyStat> selectByExample(StatHouseQmProjectMonthlyStatExample example);
}