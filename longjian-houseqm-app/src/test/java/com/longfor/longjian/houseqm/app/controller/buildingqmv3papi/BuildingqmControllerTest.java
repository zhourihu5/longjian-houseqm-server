package com.longfor.longjian.houseqm.app.controller.buildingqmv3papi;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.UpdateDeviceReq;
import com.longfor.longjian.houseqm.app.req.buildingqm.MyIssuePatchListReq;
import com.longfor.longjian.houseqm.app.service.IBuildingqmService;
import com.longfor.longjian.houseqm.app.service.ICheckUpdateService;
import com.longfor.longjian.houseqm.app.vo.MyIssuePatchListVo;
import com.longfor.longjian.houseqm.app.vo.TaskIssueListVo;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.app.vo.TaskMemberListVo;
import com.longfor.longjian.houseqm.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildingqmControllerTest {

    @InjectMocks
    private IBuildingqmService buildingqmService;
    @InjectMocks
    private ICheckUpdateService iCheckUpdateService;

    private static final String PARAN = "yyyy-MM-dd HH:mm:ss";
    private static final String UTF = "UTF-8";
    @Mock
    private IBuildingqmService buildingqmServiceMock;
    @Mock
    private ICheckUpdateService iCheckUpdateServiceMock;

    @Test
    public void myTaskList() {
        Integer userId=9;
        LjBaseResponse<TaskListVo> response = new LjBaseResponse<>();

        try {
            when(buildingqmServiceMock.myTaskList(userId)).thenReturn(any(TaskListVo.class));
            TaskListVo vo = buildingqmService.myTaskList(userId);
            response.setData(vo);
        } catch (Exception e) {
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        Assert.assertNotNull(response.getData());
    }

    @Test
    public void check() {
        UpdateDeviceReq updateDeviceReq=new UpdateDeviceReq();
        updateDeviceReq.setIssue_log_update_time(DateUtil.datetimeToTimeStamp(new Date()));
        updateDeviceReq.setIssue_members_update_time(DateUtil.datetimeToTimeStamp(new Date()));
        updateDeviceReq.setIssue_update_time(DateUtil.datetimeToTimeStamp(new Date()));
        updateDeviceReq.setTask_update_time(DateUtil.datetimeToTimeStamp(new Date()));
        updateDeviceReq.setTask_members_update_time(DateUtil.datetimeToTimeStamp(new Date()));
        updateDeviceReq.setTask_id(1);
        Date taskUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getTask_update_time(), PARAN);
        Date issueUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getIssue_update_time(), PARAN);
        Date issueLogUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getIssue_log_update_time(), PARAN);
        Date taskMembersUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getTask_members_update_time(), PARAN);
        Date issueMembersUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getIssue_members_update_time(), PARAN);
        Integer userId=9;
        TaskIssueListVo taskIssueListVo = new TaskIssueListVo();
        TaskIssueListVo.TaskIussueVo item = taskIssueListVo.new TaskIussueVo();
        item.setTask(0);
        item.setIssue(0);
        item.setIssue_log(0);
        item.setTask_members(0);
        item.setIssue_members(0);

        LjBaseResponse<TaskIssueListVo> respone = new LjBaseResponse<>();
        try {
            when(iCheckUpdateServiceMock.getHouseqmCheckTaskLastUpdateAtByTaskId(any(Integer.class))).thenReturn(any(Date.class));

            Date lastUpdate = iCheckUpdateService.getHouseqmCheckTaskLastUpdateAtByTaskId(updateDeviceReq.getTask_id());
            Assert.assertNotNull(lastUpdate);
            if (lastUpdate != null && lastUpdate.after(taskUpdateTime)) {
                item.setTask(1);
            } else {
                item.setTask(0);
            }
            when(iCheckUpdateServiceMock.getHouseqmCheckTaskIssueLastId(userId, updateDeviceReq.getTask_id(), issueUpdateTime)).thenReturn(any(Integer.class));
            Integer newLastIssueId = iCheckUpdateService.getHouseqmCheckTaskIssueLastId(userId, updateDeviceReq.getTask_id(), issueUpdateTime);
            Assert.assertNotNull(newLastIssueId);
            if (newLastIssueId > 0) {
                item.setIssue(1);
            } else {
                item.setIssue(0);
            }
            when(iCheckUpdateServiceMock.getHouseQmCheckTaskIssueLogLastId(userId, updateDeviceReq.getTask_id(), issueLogUpdateTime)).thenReturn(any(Integer.class));
            Integer newLastLogId = iCheckUpdateService.getHouseQmCheckTaskIssueLogLastId(userId, updateDeviceReq.getTask_id(), issueLogUpdateTime);
            Assert.assertNotNull(newLastLogId);
            if (newLastLogId > 0) {
                item.setIssue_log(1);
            } else {
                item.setIssue_log(0);
            }
            item.setIssue_log(1);
            when(iCheckUpdateServiceMock.getHouseQmCheckTaskIssueUserLastUpdateTime(updateDeviceReq.getTask_id())).thenReturn(any(Date.class));
            Date issueLastUpdateTime = iCheckUpdateService.getHouseQmCheckTaskIssueUserLastUpdateTime(updateDeviceReq.getTask_id());
            Assert.assertNotNull(issueLastUpdateTime);
            when(iCheckUpdateServiceMock.getHouseQmCheckTaskLastUpdateTime(updateDeviceReq.getTask_id())).thenReturn(any(Date.class));
            Date taskLastUpdateTime = iCheckUpdateService.getHouseQmCheckTaskLastUpdateTime(updateDeviceReq.getTask_id());
            Assert.assertNotNull(taskLastUpdateTime);
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("exceptions thrown");
        }
    }

    @Test
    public void taskSquadsMembers() {
        String taskIds="1,2,3,4,5,6,7,8,9,10";
        LjBaseResponse<TaskMemberListVo> response = new LjBaseResponse<>();
        when(buildingqmServiceMock.taskSquadsMembers(taskIds)).thenReturn(any(TaskMemberListVo.class));
       TaskMemberListVo vo = buildingqmService.taskSquadsMembers(taskIds);
       response.setData(vo);
       Exception e=new Exception("mock exception");
       when(buildingqmServiceMock.taskSquadsMembers(any(String.class))).thenThrow(e);
       response.setResult(1);
       response.setMessage(e.getMessage());

    }

    @Test
    public void myIssuePatchList() {
        MyIssuePatchListReq req=new MyIssuePatchListReq();
        req.setLast_id(99);
        req.setTimestamp(DateUtil.datetimeToTimeStamp(new Date()));
        req.setTask_id(1);
        Integer userId=9;
        when(buildingqmService.myIssuePathList(any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(any(MyIssuePatchListVo.class));
        try {
            MyIssuePatchListVo miplv = buildingqmService.myIssuePathList(userId, req.getTask_id(), req.getTimestamp());
            Assert.assertNotNull(miplv);
        }catch (Exception e){
            Assert.fail("exception thrown");
        }
    }

    @Test
    public void create() {
    }

    @Test
    public void taskSquad() {
    }

    @Test
    public void edit() {
    }

    @Test
    public void issueLogInfo() {
    }

    @Test
    public void reportIssue() {
    }

    @Test
    public void issueStatisticExport() {
    }
}