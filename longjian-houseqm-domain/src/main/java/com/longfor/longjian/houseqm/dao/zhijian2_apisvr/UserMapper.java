package com.longfor.longjian.houseqm.dao.zhijian2_apisvr;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends LFMySQLMapper<User> {

    List<User> selectByUserIdInAndNoDeleted(@Param("userIds") List<Integer> userIds,@Param("deleted") String deleted);

}