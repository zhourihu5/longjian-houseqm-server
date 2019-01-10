package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.Task;

public interface TaskMapper extends LFMySQLMapper<Task> {
    int add(Task task);
}