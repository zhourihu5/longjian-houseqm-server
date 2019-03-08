package com.longfor.longjian.houseqm.web;

import com.longfor.longjian.common.filter.UrlFilter;
import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.util.TokenGetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Dongshun on 2019/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // 指定我们SpringBoot工程的Application启动类
public class BuildingqmControllerTest {
    private static final String TOKEN = TokenGetUtil.getToken();
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new UrlFilter()).build();
    }

    @Test
    public void testBuildingqmMyTaskList() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/buildingqm/my_task_list/").header("token",TOKEN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void testCheckUpdateCheck() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/check_update/check").header("token",TOKEN)
                        .param("issue_log_update_time","1541500296").param("issue_members_update_time","1541500295")
                        .param("issue_update_time","1541500295").param("task_members_update_time","76225907")
                        .param("task_update_time","1541501156").param("task_id","1541500299")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void testTaskSquadsMembers() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/buildingqm/task_squads_members").header("token",TOKEN)
                        .param("task_ids","67645752")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void testMyIssuePatchList() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/buildingqm/my_issue_patch_list/").header("token",TOKEN)
                        .param("task_id","67645644").param("timestamp","1541500295")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }


    @Test
    public void testTaskSquad() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/task/task_squad").header("token",TOKEN)
                        .param("task_id","67645644").param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
}
