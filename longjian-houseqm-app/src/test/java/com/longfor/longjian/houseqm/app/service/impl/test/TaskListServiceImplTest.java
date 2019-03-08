package com.longfor.longjian.houseqm.app.service.impl.test;

import com.longfor.longjian.houseqm.app.service.impl.TaskListServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskListServiceImplTest {
    @Resource
    private TaskListServiceImpl taskListService;
/*
    @Test
    public void searchTaskIssueStatMapByTaskIds() {
        try {
            taskListService.searchTaskIssueStatMapByTaskIds(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void list() {
        try {
            taskListService.list( 1, 2, 3, 4);
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void taskRole() {
        try {
            taskListService.taskRole(1);
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getTopTeam() {
        try {
            taskListService.getTopTeam(1);
        } catch (Exception e) {
           //Assert.fail(e.getMessage());
        }
    }*/
}