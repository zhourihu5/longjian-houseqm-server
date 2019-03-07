package com.longfor.longjian.houseqm.domain.internalservice.impl.test;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_notify.PushStrategyCategoryOverdueMapper;
import com.longfor.longjian.houseqm.domain.internalservice.PushStrategyCategoryOverdueService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryOverdue;
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
 * @date 2018/12/13 0013 19:46
 */
@Transactional
@Service
@Slf4j
@LFAssignDataSource("zhijian2_notify")
public class PushStrategyCategoryOverdueServiceImpl implements PushStrategyCategoryOverdueService {

    @Resource
    PushStrategyCategoryOverdueMapper pushStrategyCategoryOverdueMapper;

    @LFAssignDataSource("zhijian2_notify")
    public List<PushStrategyCategoryOverdue> searchByTaskIds(Set<Integer> taskIds) {
        return pushStrategyCategoryOverdueMapper.selectByTaskIds(taskIds, "false");
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int add(PushStrategyCategoryOverdue pushStrategyCategoryOverdue) {
        pushStrategyCategoryOverdue.setUpdateAt(new Date());
        pushStrategyCategoryOverdue.setCreateAt(new Date());
        return pushStrategyCategoryOverdueMapper.insert(pushStrategyCategoryOverdue);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public PushStrategyCategoryOverdue selectByTaskIdAndNotDel(Integer taskId) {
        Example example = new Example(PushStrategyCategoryOverdue.class);
        example.createCriteria().andEqualTo("taskId", taskId).andIsNull("deleteAt");
        return pushStrategyCategoryOverdueMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int update(PushStrategyCategoryOverdue dbConfigCategoryOverdue) {
        dbConfigCategoryOverdue.setUpdateAt(new Date());
        return pushStrategyCategoryOverdueMapper.updateByPrimaryKey(dbConfigCategoryOverdue);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int delete(PushStrategyCategoryOverdue dbConfigCategoryOverdue) {
        List<PushStrategyCategoryOverdue> overdues = pushStrategyCategoryOverdueMapper.select(dbConfigCategoryOverdue);
        for (PushStrategyCategoryOverdue overdue : overdues) {
            overdue.setUpdateAt(new Date());
            overdue.setDeleteAt(new Date());
            pushStrategyCategoryOverdueMapper.updateByPrimaryKeySelective(overdue);
        }
        return overdues.size();
    }
}
