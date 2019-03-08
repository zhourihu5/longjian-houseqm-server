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
public class TaskControllerTest {
    private static final String TOKEN = TokenGetUtil.getToken();
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new UrlFilter()).build();
    }
    @Test
    public void testTaskView() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/task/view/").header("token",TOKEN)
                        .param("project_id","3").param("task_id","291")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                 .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
    @Test
    public void testListInfo() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/task/list_info/").header("token",TOKEN)
                        .param("project_id","930").param("category_cls","23").param("status","1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
    @Test
    public void testTaskRole() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/task/task_role/").header("token",TOKEN)
                        .param("project_id","854").param("task_id","79319153")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
}
