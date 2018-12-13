package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.PushStrategyAssignTime;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PushStrategyAssignTimeMapper extends LFMySQLMapper<PushStrategyAssignTime> {

    /**
     *
     * @param taskIds
     * @return
     */
    public List<PushStrategyAssignTime> selectByTaskIds(@Param("taskIds") Set<Integer> taskIds,@Param("deleted") String deleted);

}