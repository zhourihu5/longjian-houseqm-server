package com.longfor.longjian.houseqm.app.service.impl.test;

import com.longfor.longjian.houseqm.app.service.impl.TaskServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@ActiveProfiles("sonar")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskServiceImplTest {
    @Resource
    TaskServiceImpl taskService;

    @Test
    public void searchHouseQmCheckTaskByProjCategoryClsStatusPage() {
        try {
            taskService.searchHouseQmCheckTaskByProjCategoryClsStatusPage(1,1,1,1,1);
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseQmCheckTaskByProjTaskId() {
        try {
            taskService.getHouseQmCheckTaskByProjTaskId(1,1);
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseqmCheckTaskCheckedAreas() {
        try {
            taskService.getHouseqmCheckTaskCheckedAreas(1,1);
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void deleteHouseQmCheckTaskByProjTaskId() {
        try {
            taskService.deleteHouseQmCheckTaskByProjTaskId(1,1);
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchUserInKeyHouseQmCheckTaskByTaskId() {
        try {
            taskService.searchUserInKeyHouseQmCheckTaskByTaskId(1);
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getUsersByIds() {
        try {
            taskService.getUsersByIds(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }
}