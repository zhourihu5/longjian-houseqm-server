package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_notify.PushStrategyAssignTimeMapper;
import com.longfor.longjian.houseqm.domain.internalService.PushStrategyAssignTimeService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyAssignTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
     *
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2_notify")
    public List<PushStrategyAssignTime> searchByTaskIds(Set<Integer> taskIds){
        return pushStrategyAssignTimeMapper.selectByTaskIds(taskIds,"false");
    }
    @LFAssignDataSource("zhijian2_notify")
    @Override
    public int add(PushStrategyAssignTime pushStrategyAssignTime) {
        return pushStrategyAssignTimeMapper.insert(pushStrategyAssignTime);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public PushStrategyAssignTime selectByIdAndNotDel(Integer task_id) {
        Example example = new Example(PushStrategyAssignTime.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId",task_id).andIsNull("deleteAt");
        return pushStrategyAssignTimeMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int update(PushStrategyAssignTime dbConfigAssignTime) {
        return pushStrategyAssignTimeMapper.updateByPrimaryKey(dbConfigAssignTime);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public int delete(PushStrategyAssignTime dbConfigAssignTime) {
        return pushStrategyAssignTimeMapper.delete(dbConfigAssignTime);
    }

}
