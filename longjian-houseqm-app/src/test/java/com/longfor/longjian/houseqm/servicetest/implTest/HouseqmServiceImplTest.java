package com.longfor.longjian.houseqm.servicetest.implTest;

import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.service.impl.HouseqmServiceImpl;
import com.longfor.longjian.houseqm.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HouseqmServiceImplTest {

    @Resource
    private HouseqmServiceImpl houseqmServiceImpl;

    @Test
    public void searchHouseQmApproveUserIdInMyCheckSquad() {
        try{
            houseqmServiceImpl.searchHouseQmApproveUserIdInMyCheckSquad(9,1);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void myIssueLogList() {
        try{
            DeviceReq deviceReq=new DeviceReq();
            deviceReq.setLast_id(1);
            deviceReq.setTask_id(1);
            deviceReq.setTimestamp(DateUtil.datetimeToTimeStamp(new Date()));
            HttpServletRequest mockReq=new MockHttpServletRequest();
            houseqmServiceImpl.myIssueLogList(deviceReq,mockReq);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void myIssueList() {
        try {
            DeviceReq deviceReq = new DeviceReq();
            deviceReq.setLast_id(1);
            deviceReq.setTask_id(1);
            deviceReq.setTimestamp(DateUtil.datetimeToTimeStamp(new Date()));
            HttpServletRequest mockReq = new MockHttpServletRequest();
            houseqmServiceImpl.myIssueList(deviceReq, mockReq);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void issueMembers() {
        try{
            DeviceReq deviceReq = new DeviceReq();
            deviceReq.setLast_id(1);
            deviceReq.setTask_id(1);
            deviceReq.setTimestamp(DateUtil.datetimeToTimeStamp(new Date()));
            houseqmServiceImpl.issueMembers(deviceReq);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void myIssueAttachementList() {
        try{
            DeviceReq deviceReq = new DeviceReq();
            deviceReq.setLast_id(1);
            deviceReq.setTask_id(1);
            deviceReq.setTimestamp(DateUtil.datetimeToTimeStamp(new Date()));
            HttpServletRequest mockReq = new MockHttpServletRequest();
            houseqmServiceImpl.myIssueAttachementList(deviceReq,mockReq);
        }catch(Exception e){
            Assert.fail(e.getMessage());
        }
    }
//java.lang.NullPointerException
    @Test
    public void searchTargetAreaByTaskId() {
        try{
            houseqmServiceImpl.searchTargetAreaByTaskId(3,290);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}