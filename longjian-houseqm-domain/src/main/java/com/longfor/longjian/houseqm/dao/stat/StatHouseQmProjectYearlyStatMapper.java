package com.longfor.longjian.houseqm.dao.stat;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectYearlyStat;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectYearlyStatExample;
import java.util.List;

public interface StatHouseQmProjectYearlyStatMapper extends LFMySQLMapper<StatHouseQmProjectYearlyStat> {
    int countByExample(StatHouseQmProjectYearlyStatExample example);

    List<StatHouseQmProjectYearlyStat> selectByExample(StatHouseQmProjectYearlyStatExample example);
}