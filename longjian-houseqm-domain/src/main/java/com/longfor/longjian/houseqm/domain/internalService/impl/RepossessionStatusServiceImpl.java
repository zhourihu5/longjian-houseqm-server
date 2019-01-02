package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.RepossessionStatusMapper;
import com.longfor.longjian.houseqm.domain.internalService.RepossessionStatusService;
import com.longfor.longjian.houseqm.po.RepossessionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/29.
 */
@Service
@Slf4j
public class RepossessionStatusServiceImpl implements RepossessionStatusService {
    @Resource
    RepossessionStatusMapper repossessionStatusMapper;
    @Override
    @LFAssignDataSource("zhijian2")
    public List<RepossessionStatus> SearchByTaskIdAreaIdLike(Integer taskId, Integer areaId) {
        return repossessionStatusMapper.SearchByTaskIdAreaIdLike(taskId,areaId);
    }
}
