package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.TaskList2Vo;

public interface ITaskListService {
    TaskList2Vo list(int teamId, int projectId, int categoryCls, int status);


}
