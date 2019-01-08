package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.RepossessionStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/29.
 */

public interface RepossessionStatusMapper extends LFMySQLMapper<RepossessionStatus> {
    List<RepossessionStatus> searchByTaskIdAreaIdLike(@Param("taskId") Integer taskId, @Param("areaId") Integer areaId);
}
