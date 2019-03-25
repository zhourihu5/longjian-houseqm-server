package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.TaskMapper;
import com.longfor.longjian.houseqm.domain.internalservice.TasksService;
import com.longfor.longjian.houseqm.po.zj2db.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Dongshun on 2019/1/8.
 */
@Service
@Slf4j
@Transactional
public class TasksServiceImpl implements TasksService {
    @Resource
    private TaskMapper taskMapper;

    @LFAssignDataSource("zhijian2")
    @Override
    public int add(Task task) {
        task.setCreateAt(new Date());
        task.setUpdateAt(new Date());
        taskMapper.insertSelective(task);
        return task.getId();
    }


}
