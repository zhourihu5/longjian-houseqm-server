package com.longfor.longjian.houseqm.servicetest.implTest;

import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.app.service.impl.TaskListServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskListServiceImplTest {
    @Resource
    private TaskListServiceImpl taskListService;
    @Test
    public void searchTaskIssueStatMapByTaskIds() {
        try {
            taskListService.searchTaskIssueStatMapByTaskIds(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        } catch (Exception e) {
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void list() {
        try {
            taskListService.list( 1, 2, 3, 4);
        } catch (Exception e) {
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void taskRole() {
        try {
            taskListService.taskRole(1);
        } catch (Exception e) {
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getTopTeam() {
        try {
            taskListService.getTopTeam(1);
        } catch (Exception e) {
           Assert.fail(e.getMessage());
        }
    }
}