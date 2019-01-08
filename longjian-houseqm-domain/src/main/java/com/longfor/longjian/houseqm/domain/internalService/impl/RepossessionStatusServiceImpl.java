package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.RepossessionStatusMapper;
import com.longfor.longjian.houseqm.domain.internalService.RepossessionStatusService;
import com.longfor.longjian.houseqm.po.RepossessionStatus;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
    public List<RepossessionStatus> searchByTaskIdAreaIdLike(Integer taskId, Integer areaId) {
        return repossessionStatusMapper.searchByTaskIdAreaIdLike(taskId,areaId);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<RepossessionStatus> searchRepossessionStatusByTaskIdAreaIdLike(Integer taskId, Integer areaId) {
        Example example = new Example(RepossessionStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId",taskId).andLike("areaPathAndId","%/"+areaId+"/%");
        ExampleUtil.addDeleteAtJudge(example);
        return repossessionStatusMapper.selectByExample(example);
    }
}
