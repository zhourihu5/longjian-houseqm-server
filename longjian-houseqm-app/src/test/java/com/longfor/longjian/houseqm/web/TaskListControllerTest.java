package com.longfor.longjian.houseqm.web;

import com.longfor.longjian.common.filter.UrlFilter;
import com.longfor.longjian.houseqm.Application;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskListControllerTest {

    private static final String TOKEN = "l8PLqmIPv8L-iQK85VMG03BPGsXa3FJRVdkHhTkgAUL5TQ0fODeug6IyrG9_f14g";
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new UrlFilter()).build();
    }

  /*  @Test
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

    }*/
}