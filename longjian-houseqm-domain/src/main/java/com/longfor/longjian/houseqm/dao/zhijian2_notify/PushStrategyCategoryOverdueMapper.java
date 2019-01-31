package com.longfor.longjian.houseqm.dao.zhijian2_notify;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryOverdue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PushStrategyCategoryOverdueMapper extends LFMySQLMapper<PushStrategyCategoryOverdue> {

    /**
     *
     * @param taskIds
     * @param deleted
     * @return
     */
    public List<PushStrategyCategoryOverdue> selectByTaskIds(@Param("taskIds") Set<Integer> taskIds, @Param("deleted") String deleted);
}