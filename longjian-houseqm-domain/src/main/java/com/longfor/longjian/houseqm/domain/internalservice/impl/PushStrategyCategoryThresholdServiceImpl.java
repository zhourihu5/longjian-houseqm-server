package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_notify.PushStrategyCategoryThresholdMapper;
import com.longfor.longjian.houseqm.domain.internalservice.PushStrategyCategoryThresholdService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryThreshold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/13 0013 19:56
 */
@Transactional
@Service
@Slf4j
@LFAssignDataSource("zhijian2_notify")
public class PushStrategyCategoryThresholdServiceImpl implements PushStrategyCategoryThresholdService {

    @Resource
    PushStrategyCategoryThresholdMapper pushStrategyCategoryThresholdMapper;

    /**
     * 取未删除的数据
     *
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2_notify")
    public List<PushStrategyCategoryThreshold> searchByTaskIds(Set<Integer> taskIds) {
        return pushStrategyCategoryThresholdMapper.selectByTaskIds(taskIds, "false");
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int add(PushStrategyCategoryThreshold pushStrategyCategoryThreshold) {
        pushStrategyCategoryThreshold.setUpdateAt(new Date());
        pushStrategyCategoryThreshold.setCreateAt(new Date());
        return pushStrategyCategoryThresholdMapper.insert(pushStrategyCategoryThreshold);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public PushStrategyCategoryThreshold selectTaskIdAndNotDel(Integer taskId) {
        Example example = new Example(PushStrategyCategoryThreshold.class);
        example.createCriteria().andEqualTo("taskId", taskId).andIsNull("deleteAt");
        return pushStrategyCategoryThresholdMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int update(PushStrategyCategoryThreshold dbConfigCategoryThreshold) {
        dbConfigCategoryThreshold.setUpdateAt(new Date());
        return pushStrategyCategoryThresholdMapper.updateByPrimaryKey(dbConfigCategoryThreshold);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int delete(PushStrategyCategoryThreshold dbConfigCategoryThreshold) {
        List<PushStrategyCategoryThreshold> thresholds = pushStrategyCategoryThresholdMapper.select(dbConfigCategoryThreshold);
        for (PushStrategyCategoryThreshold threshold : thresholds) {
            threshold.setUpdateAt(new Date());
            threshold.setDeleteAt(new Date());
            pushStrategyCategoryThresholdMapper.updateByPrimaryKeySelective(threshold);
        }
        return thresholds.size();
    }
}
