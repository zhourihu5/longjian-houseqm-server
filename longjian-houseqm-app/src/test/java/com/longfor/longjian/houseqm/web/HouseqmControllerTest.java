package com.longfor.longjian.houseqm.web;

import com.longfor.longjian.common.filter.UrlFilter;
import com.longfor.longjian.houseqm.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Dongshun on 2019/3/8.
 */
@ActiveProfiles("sonar")
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // 指定我们SpringBoot工程的Application启动类
public class HouseqmControllerTest {
    private static final String TOKEN = "xF3fnRclIvv3KuIdaUpPnOgqkWnVzaIFBRO_ATlWz_0ljHf0b4jnce37_CaQkdeC";
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new UrlFilter()).build();
    }

    @Test
    public void testMyIssueLogList() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm/my_issue_log_list").header("token",TOKEN)
                        .param("task_id","67645644").param("last_id","0").param("timestamp","1541500296")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andReturn();
    }
    @Test
    public void testMyIssueList() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm/my_issue_list").header("token",TOKEN)
                        .param("task_id","67645644").param("last_id","0").param("timestamp","1541500296")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andReturn();
    }

    @Test
    public void testIssueMembers() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm/issue_members").header("token",TOKEN)
                        .param("task_id","67645644").param("last_id","0").param("timestamp","1541500296")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andReturn();
    }

    @Test
    public void testMyIssueAttachmentList() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm/my_issue_attachment_list").header("token",TOKEN)
                        .param("task_id","67645644").param("last_id","0").param("timestamp","1541500296")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andReturn();
    }


}
