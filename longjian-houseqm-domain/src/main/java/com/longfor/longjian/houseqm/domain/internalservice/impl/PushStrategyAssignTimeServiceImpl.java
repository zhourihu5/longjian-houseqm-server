package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_notify.PushStrategyAssignTimeMapper;
import com.longfor.longjian.houseqm.domain.internalservice.PushStrategyAssignTimeService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyAssignTime;
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
 * @date 2018/12/13 0013 19:30
 */
@Transactional
@Service
@Slf4j
public class PushStrategyAssignTimeServiceImpl implements PushStrategyAssignTimeService {
    @Resource
    PushStrategyAssignTimeMapper pushStrategyAssignTimeMapper;

    /**
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2_notify")
    public List<PushStrategyAssignTime> searchByTaskIds(Set<Integer> taskIds) {
        return pushStrategyAssignTimeMapper.selectByTaskIds(taskIds, "false");
    }

    @LFAssignDataSource("zhijian2_notify")
    @Override
    public int add(PushStrategyAssignTime pushStrategyAssignTime) {
        pushStrategyAssignTime.setCreateAt(new Date());
        pushStrategyAssignTime.setUpdateAt(new Date());
        return pushStrategyAssignTimeMapper.insert(pushStrategyAssignTime);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public PushStrategyAssignTime selectByIdAndNotDel(Integer taskId) {
        Example example = new Example(PushStrategyAssignTime.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId).andIsNull("deleteAt");
        return pushStrategyAssignTimeMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int update(PushStrategyAssignTime dbConfigAssignTime) {
        dbConfigAssignTime.setUpdateAt(new Date());
        return pushStrategyAssignTimeMapper.updateByPrimaryKey(dbConfigAssignTime);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int delete(PushStrategyAssignTime dbConfigAssignTime) {
        List<PushStrategyAssignTime> assignTimes = pushStrategyAssignTimeMapper.select(dbConfigAssignTime);
        for (PushStrategyAssignTime assignTime : assignTimes) {
            assignTime.setUpdateAt(new Date());
            assignTime.setDeleteAt(new Date());
            pushStrategyAssignTimeMapper.updateByPrimaryKeySelective(assignTime);
        }
        return assignTimes.size();
    }

}
