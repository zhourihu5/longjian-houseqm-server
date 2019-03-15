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
    @Test
    public void testEdit() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/task/edit").header("token",TOKEN)
                        .param("category_cls","23").param("project_id","930")
                        .param("group_id","4").param("team_id","25")
                        .param("task_id","67716252").param("name","2019-3-8-1")
                        .param("area_ids","2952246").param("area_types","2,3,4")
                        .param("plan_begin_on"," 2019-03-11").param("plan_end_on","2019-03-31")
                        .param("repairer_ids","19959").param("checker_groups","[{\"name\":\"检查组1\",\"user_ids\":\"19970,19958\",\"approve_ids\":\"19970\",\"reassign_ids\":\"\",\"id\":6198,\"direct_approve_ids\":\"19970\"}]")
                        .param("repairer_follower_permission","10").param("repaired_picture_status","10")
                        .param("issue_desc_status","0").param("push_strategy_config","{}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void testAddDesc() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/issue/add_desc").header("token",TOKEN)
                        .param("issue_uuid","6032c4640393427b88942764f78675df").param("project_id","930")
                        .param("content","测试")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
    @Test
    public void testAddDescs() throws Exception {
        mockMvc.perform(
                post("/houseqm/v3/papi/issue/add_desc").header("token",TOKEN)
                        .param("issue_uuid","6032c4640393427b88942764f78675df").param("project_id","930")
                        .param("content","测试1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
    @Test
    public void testEditPlanEndOns() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/issue/edit_plan_end_on").header("token",TOKEN)
                        .param("issue_uuid","D217A7D9922B43F5B65F69DBE3C5CAF3").param("project_id","930")
                        .param("plan_end_on","1552492803")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
}
