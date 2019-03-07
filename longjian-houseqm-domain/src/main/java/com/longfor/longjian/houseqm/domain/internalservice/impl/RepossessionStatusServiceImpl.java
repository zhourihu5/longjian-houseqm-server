package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.common.util.DateUtil;
import com.longfor.longjian.houseqm.dao.zj2db.RepossessionStatusMapper;
import com.longfor.longjian.houseqm.domain.internalservice.RepossessionStatusService;
import com.longfor.longjian.houseqm.dto.RepossessionStatusCompleteDailyCountDto;
import com.longfor.longjian.houseqm.po.zj2db.RepossessionStatus;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public List<RepossessionStatus> searchByTaskIdAreaIdLike(Integer taskId, Integer areaId) {
        return repossessionStatusMapper.searchByTaskIdAreaIdLike(taskId, areaId);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<RepossessionStatus> searchRepossessionStatusByTaskIdAreaIdLike(Integer taskId, Integer areaId) {
        Example example = new Example(RepossessionStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId).andLike("areaPathAndId", "%/" + areaId + "/%");
        ExampleUtil.addDeleteAtJudge(example);
        return repossessionStatusMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public RepossessionStatusCompleteDailyCountDto searchByTaskIdInAndStatusAndNoDeletedOrStatusClientUpdateAt(Map<String, Object> condi) {
        return repossessionStatusMapper.selectByTaskIdInAndStatusAndNoDeletedOrStatusClientUpdateAt(condi);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<RepossessionStatusCompleteDailyCountDto> searchByTaskIdInAndStatusAndNoDeletedGroupByDateOrderByDateByPage(Map<String, Object> condi) {
        return repossessionStatusMapper.selectByTaskIdInAndStatusAndNoDeletedGroupByDateOrderByDateByPage(condi);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<RepossessionStatus> searchByTaskIdAndStatusInAndStatusClientUpdateAt(int taskId, List<Integer> repossStatuses, Date startTime, Date endTime) {
        Example example = new Example(RepossessionStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        if (CollectionUtils.isNotEmpty(repossStatuses)) criteria.andIn("status", repossStatuses);

        if (!(DateUtil.datetimeZero(startTime))) {
            criteria.andGreaterThanOrEqualTo("statusClientUpdateAt", startTime);
        }
        if (!(DateUtil.datetimeZero(endTime))) {
            criteria.andLessThanOrEqualTo("statusClientUpdateAt", endTime);
        }
        ExampleUtil.addDeleteAtJudge(example);
        return repossessionStatusMapper.selectByExample(example);
    }
}
