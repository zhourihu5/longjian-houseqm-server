package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.core.bean.PageInfo;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.houseqm.app.vo.IssueListVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * http://192.168.37.159:3000/project/8/interface/api/286 问题检索
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("buildingqm/v3/papi/issue/")
@Slf4j
public class IssueListController {


    /**
     * 问题检索
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @return
     */
    @MockOperation
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<PageInfo<IssueListVo>> doAction(@RequestParam(value="project_id" ) Integer projectId,
                                                    @RequestParam(value="category_cls") String categoryCls,
                                                    @RequestParam(value="page_level") String pageLevel,
                                                    @RequestParam(value="task_id") String taskId,
                                                    @RequestParam(value="area_ids") String areaIds,
                                                    @RequestParam(value="status_in") String statusIn,
                                                    @RequestParam(value="is_overdue") String isOverDue,
                                                    @RequestParam(value="key_word") String keyWord,
                                                    @ApiParam(value = "当前页码", required = false)
                                                   @Valid @Min(0)
                                                   @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNum,
                                                    @ApiParam(value = "分页大小", required = false)
                                                   @Valid @Min(1)
                                                   @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize
                                         ){


        return null;
    }
}
