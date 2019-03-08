package com.longfor.longjian.houseqm.app.controller.buildingqmv3papi;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskListControllerTest {

    private static final String TOKEN = "xF3fnRclIvv3KuIdaUpPnOgqkWnVzaIFBRO_ATlWz_0ljHf0b4jnce37_CaQkdeC";
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new UrlFilter()).build();
    }

    @Test
    public void list() throws Exception {
        mockMvc.perform(
                post("/buildingqm/v3/papi/task/list/").header("token",TOKEN)
                        .param("team_id","25")
                        .param("project_id","930")
                        .param("category_cls","23")
                        .param("status","1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andReturn();

    }
}