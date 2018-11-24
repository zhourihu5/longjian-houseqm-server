package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;

import java.util.List;

public interface UserInHouseQmCheckTaskMapper extends LFMySQLMapper<UserInHouseQmCheckTask> {

    /**
     * 根据用户ID查
     *
     * @param userId
     * @return
     */
     List<UserInHouseQmCheckTask> selectByUserId(Integer userId);
}