package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.Team;
import org.apache.ibatis.annotations.Param;

public interface TeamMapper extends LFMySQLMapper<Team> {
    public Team selectByTeamId(@Param("teamId") int teamId, @Param("deleted") String deleted);
}