package com.longfor.longjian.houseqm.web;

import com.longfor.longjian.common.filter.UrlFilter;
import com.longfor.longjian.houseqm.Application;
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
public class IusseTaskListControllerTest {
    private static final String TOKEN = "7gaxyW9RW9VrALW1dC9cdHn7ISufeyz1MBNd3hMzbZkEQoU89Boq35hh1xRLAT_y";
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new UrlFilter()).build();
    }
    @Test
    public void testTaskList() throws Exception {
        mockMvc.perform(
                post("/houseqm/v3/papi/issue/task_list/").header("token",TOKEN)
                        .param("project_id","930").param("category_cls","23")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(
                post("/houseqm/v3/papi/stat_houseqm/get_acceptanceitems_setting/").header("token",TOKEN)
                        .param("project_ids","10").param("timestamp","1453376610")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
    @Test
    public void testTeamsAndProjects() throws Exception {
        mockMvc.perform(
                post("/houseqm/v3/papi/mine/teams_and_projects").header("token",TOKEN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
    @Test
    public void testIssueList() throws Exception {
        mockMvc.perform(
                post("/houseqm/v3/papi/issue/configs").header("token",TOKEN)
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
    @Test
    public void testIssueDetailBase() throws Exception {
        mockMvc.perform(
                post("/houseqm/v3/papi/issue/detail_base").header("token",TOKEN)
                        .param("project_id","628").param("issue_uuid","3914e20f5a33462aab2ad67aaa16172a")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void testIssueDetailLog() throws Exception {
        mockMvc.perform(
                post("/houseqm/v3/papi/issue/detail_log").header("token",TOKEN)
                        .param("project_id","628").param("issue_uuid","3914e20f5a33462aab2ad67aaa16172a")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void testIssueDetailRepairLog() throws Exception {
        mockMvc.perform(
                post("/houseqm/v3/papi/issue/detail_repair_log").header("token",TOKEN)
                        .param("project_id","930").param("issue_uuid","72232bbead6545248a6cd7a80e2e2acf")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
}
