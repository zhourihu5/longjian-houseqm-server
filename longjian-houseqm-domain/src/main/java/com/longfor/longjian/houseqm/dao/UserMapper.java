package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends LFMySQLMapper<User> {

    List<User> selectByUserIds(@Param("users") List<Integer> users);

    List<User> selectByUserIdInAndNoDeleted(@Param("userIds") List<Integer> userIds,@Param("deleted") String deleted);

}