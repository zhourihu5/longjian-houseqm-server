package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.dto.RepossessionStatusCompleteDailyCountDto;
import com.longfor.longjian.houseqm.po.zj2db.RepossessionStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Dongshun on 2018/12/29.
 */

public interface RepossessionStatusMapper extends LFMySQLMapper<RepossessionStatus> {
    List<RepossessionStatus> searchByTaskIdAreaIdLike(@Param("taskId") Integer taskId, @Param("areaId") Integer areaId);

    RepossessionStatusCompleteDailyCountDto selectByTaskIdInAndStatusAndNoDeletedOrStatusClientUpdateAt(Map<String, Object> condi);

    List<RepossessionStatusCompleteDailyCountDto> selectByTaskIdInAndStatusAndNoDeletedGroupByDateOrderByDateByPage(Map<String, Object> condi);
}
