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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HouseqmStatisticControllerTest {

    private static final String TOKEN = TokenGetUtil.getToken();
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new UrlFilter()).build();
    }

    @Test
    public void rhyfTaskStat() throws Exception {

            mockMvc.perform(
                    post("/v3/api/houseqm_statistic/rhyf_task_stat/").header("token",TOKEN)
                            .param("task_id","67645644")
                            .param("project_id","930")
                            .param("timestamp","1541500296")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("success"))
                    .andDo(MockMvcResultHandlers.print()).andReturn();

    }

    @Test
    public void taskStat() throws Exception {
            mockMvc.perform(
                    post("/v3/api/houseqm_statistic/task_stat/").header("token",TOKEN)
                            .param("task_id","67645644")
                            .param("project_id","930")
                            .param("area_id","2942699")
                            .param("timestamp","0")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("success"))
                    .andDo(MockMvcResultHandlers.print()).andReturn();

    }

    @Test
    public void getDaterangeOptions() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/get_daterange_options/").header("token",TOKEN)
                        .param("task_id","67645644")
                        .param("project_id","930")
                        .param("area_id","2942699")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskList() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/task_list/").header("token",TOKEN)
                        .param("project_id","930")
                        .param("source","gcgl")
                        .param("timestamp","0")
                        .param("area_id","2942699")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void projectIssueRepair() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/project_issue_repair/").header("token",TOKEN)
                        .param("source","gcgl")
                        .param("project_id","930")
                        .param("timestamp","0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskCheckitemStat() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/task_checkitem_stat/").header("token",TOKEN)
                        .param("project_id","930")
                        .param("task_id","67645644")
                        .param("area_id","2942699")
                        .param("begin_on","0")
                        .param("end_on","0")
                        .param("timestamp","0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskIssueRepair() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/task_issue_repair/").header("token",TOKEN)
                        .param("project_id","930")
                        .param("task_id","67645644")
                        .param("area_id","2942699")
                        .param("begin_on","0")
                        .param("end_on","0")
                        .param("timestamp","0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskIssueRepairList() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/task_issue_repair_list/").header("token",TOKEN)
                        .param("project_id","930")
                        .param("source","gcgl")
                        .param("plan_status","2")
                        .param("timestamp","0")
                        .param("page","1")
                        .param("page_size","20")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void categoryIssueList() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/category_issue_list/").header("token",TOKEN)
                        .param("project_id","930")
                        .param("category_key","1585_11_0")
                        .param("area_id","2942699")
                        .param("page","1")
                        .param("page_size","20")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void projectBuildingList() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/project_building_list/").header("token",TOKEN)
                        .param("project_id","930")
                        .param("timestamp","0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskBuildingList() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/task_building_list/").header("token",TOKEN)
                        .param("project_id","930")
                        .param("task_id","67645644")
                        .param("timestamp","0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void taskIssueStat() throws Exception {
        mockMvc.perform(
                post("/v3/api/houseqm_statistic/task_issue_stat/").header("token",TOKEN)
                        .param("project_id","930")
                        .param("task_id","67645644")
                        .param("area_id","2942699")
                        .param("timestamp","0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
}