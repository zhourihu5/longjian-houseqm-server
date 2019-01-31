package com.longfor.longjian.houseqm.dao.zhijian2_apisvr;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import org.apache.ibatis.annotations.Param;

public interface TeamMapper extends LFMySQLMapper<Team> {
    Team selectByTeamId(@Param("teamId") int teamId, @Param("deleted") String deleted);
}