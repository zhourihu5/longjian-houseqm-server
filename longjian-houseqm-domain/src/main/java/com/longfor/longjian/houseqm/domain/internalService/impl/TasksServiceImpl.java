package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.TaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.TasksService;
import com.longfor.longjian.houseqm.po.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Dongshun on 2019/1/8.
 */
@Service
@Slf4j
@Transactional
public class TasksServiceImpl implements TasksService {
    @Resource
    TaskMapper taskMapper;
    @LFAssignDataSource("zhijian2")
    @Override
    public int add(Task task) {
        taskMapper.insertSelective(task);

     return    task.getId();
    }


}
