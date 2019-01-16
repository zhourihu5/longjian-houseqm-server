package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.PushStrategyCategoryOverdueMapper;
import com.longfor.longjian.houseqm.domain.internalService.PushStrategyCategoryOverdueService;
import com.longfor.longjian.houseqm.po.PushStrategyCategoryOverdue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/13 0013 19:46
 */
@Transactional
@Service
@Slf4j
@LFAssignDataSource("zhijian2_notify")
public class PushStrategyCategoryOverdueServiceImpl implements PushStrategyCategoryOverdueService {

    @Resource
    PushStrategyCategoryOverdueMapper pushStrategyCategoryOverdueMapper;
    /**
     *
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2_notify")
    public List<PushStrategyCategoryOverdue> searchByTaskIds(Set<Integer> taskIds){
        return pushStrategyCategoryOverdueMapper.selectByTaskIds(taskIds,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int add(PushStrategyCategoryOverdue pushStrategyCategoryOverdue) {
        return pushStrategyCategoryOverdueMapper.insert(pushStrategyCategoryOverdue);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public PushStrategyCategoryOverdue selectByTaskIdAndNotDel(Integer task_id) {
        Example example = new Example(PushStrategyCategoryOverdue.class);
        example.createCriteria().andEqualTo("taskId",task_id).andIsNull("deleteAt");
        return pushStrategyCategoryOverdueMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int update(PushStrategyCategoryOverdue dbConfigCategoryOverdue) {
        return pushStrategyCategoryOverdueMapper.updateByPrimaryKey(dbConfigCategoryOverdue);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int delete(PushStrategyCategoryOverdue dbConfigCategoryOverdue) {
        return pushStrategyCategoryOverdueMapper.delete(dbConfigCategoryOverdue);
    }
}
