package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.PushStrategyCategoryThresholdMapper;
import com.longfor.longjian.houseqm.domain.internalService.PushStrategyCategoryThresholdService;
import com.longfor.longjian.houseqm.po.PushStrategyCategoryThreshold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2_notify")
    public List<PushStrategyCategoryThreshold> searchByTaskIds(Set<Integer> taskIds){
       return pushStrategyCategoryThresholdMapper.selectByTaskIds(taskIds,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int add(PushStrategyCategoryThreshold pushStrategyCategoryThreshold) {
        return pushStrategyCategoryThresholdMapper.insert(pushStrategyCategoryThreshold);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public PushStrategyCategoryThreshold selectTaskIdAndNotDel(Integer task_id) {
        Example example = new Example(PushStrategyCategoryThreshold.class);
        example.createCriteria().andEqualTo("taskId",task_id).andIsNull("deleteAt");
        return pushStrategyCategoryThresholdMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int update(PushStrategyCategoryThreshold dbConfigCategoryThreshold) {
        return pushStrategyCategoryThresholdMapper.updateByPrimaryKey(dbConfigCategoryThreshold);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int delete(PushStrategyCategoryThreshold dbConfigCategoryThreshold) {
        return pushStrategyCategoryThresholdMapper.delete(dbConfigCategoryThreshold);
    }
}
