package com.longfor.longjian.houseqm.app.service.test;

import com.longfor.longjian.houseqm.app.service.IusseTaskListService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Dongshun on 2019/3/7.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("sonar")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IusseTaskListServiceTest {
    @Resource
    IusseTaskListService iusseTaskListService;
    @Test
    public void selectByProjectIdAndCategoryCls(){
        try {
            iusseTaskListService.selectByProjectIdAndCategoryCls(1,1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void getAcceptanceitemsSetting(){
        try {
            iusseTaskListService.getAcceptanceitemsSetting("1,2,3,45",1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void teamsAndProjects(){
        try {
            iusseTaskListService.teamsAndProjects(1,"1");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void datetimeZero(){
        try {
            iusseTaskListService.datetimeZero(new Date());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void createTeamsMap(){
        try {

            iusseTaskListService.createTeamsMap( Arrays.asList(1, 2, 3, 4));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
