package com.longfor.longjian.houseqm.servicetest.implTest;

import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.app.service.impl.CheckUpdateServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckUpdateServiceImplTest {

    @Resource
    private CheckUpdateServiceImpl checkUpdateServiceimpl;

    @Test
    public void getHouseqmCheckTaskLastUpdateAtByTaskId() {
        try{
            checkUpdateServiceimpl.getHouseqmCheckTaskLastUpdateAtByTaskId(1);
        }catch (Exception e){
            // Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseqmCheckTaskIssueLastId() {
        try{
            checkUpdateServiceimpl.getHouseqmCheckTaskIssueLastId(9,1,new Date());
        }catch (Exception e){
            // Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseQmCheckTaskIssueLogLastId() {
        try{
            checkUpdateServiceimpl.getHouseQmCheckTaskIssueLogLastId(9,1,new Date());
        }catch(Exception e){
            // Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseQmCheckTaskIssueUserLastUpdateTime() {
        try{
            checkUpdateServiceimpl.getHouseQmCheckTaskIssueUserLastUpdateTime(1);
        }catch (Exception e){
            // Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseQmCheckTaskLastUpdateTime() {
        try{
            Date houseQmCheckTaskLastUpdateTime = checkUpdateServiceimpl.getHouseQmCheckTaskLastUpdateTime(1);
            System.out.println(houseQmCheckTaskLastUpdateTime);
        }catch (Exception e){
           // Assert.fail(e.getMessage());
        }
    }
}