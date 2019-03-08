package com.longfor.longjian.houseqm.web;

import com.longfor.longjian.common.filter.UrlFilter;
import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.util.TokenGetUtil;
import org.junit.Assert;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HouseqmStatControllerTest {

    private static final String TOKEN = TokenGetUtil.getToken();
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new UrlFilter()).build();
    }

    @Test
    public void categoryStat() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat/category_stat/").header("token",TOKEN)
                        .param("task_id","67645644")
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void inspectionSituationSearch() throws Exception {
        try {
            mockMvc.perform(
                    post("/oapi/v3/houseqm/stat/inspection_situation_search/").header("token",TOKEN)
                            .param("task_id","67645644")
                            .param("project_id","930")
                            .param("issue_status","2")
                            .param("page","1")
                            .param("page_size","20")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("success"))
                    .andDo(MockMvcResultHandlers.print()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void checkerStat() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat_houseqm/checker_stat/").header("token",TOKEN)
                        .param("task_ids","67645644")
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskSituationDaily() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat/task_situation_daily/").header("token",TOKEN)
                        .param("task_ids","67645644")
                        .param("project_id","930")
                        .param("page","1")
                        .param("page_size","20")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskSituationOverall() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat/task_situation_overall/").header("token",TOKEN)
                        .param("task_ids","67645644")
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskAreaList() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat/task_area_list/").header("token",TOKEN)
                        .param("task_id","67645644")
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void areaSituationTaskList() {
    }

    @Test
    public void taskDetail() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat/task_detail/").header("token",TOKEN)
                        .param("task_id","67645644")
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskSituationRepairStat() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat/task_situation_repair_stat/").header("token",TOKEN)
                        .param("task_id","67645644")
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskSituationMembersChecker() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat/task_situation_members_checker/").header("token",TOKEN)
                        .param("task_id","67645644")
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskSituationMembersRepairer() throws Exception {
        mockMvc.perform(
                post("/oapi/v3/houseqm/stat/task_situation_members_repairer/").header("token",TOKEN)
                        .param("task_id","67645644")
                        .param("project_id","930")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void areaSituation() {
    }
}