package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.core.bean.PageInfo;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.vo.IssueListVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

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

    @Resource
    private IIssueService iIssueService;

    /**
     * 问题检索
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param taskId
     * @param areaIds
     * @param statusIn
     * @param isOverDue
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<PageInfo<IssueListVo>> doAction(@RequestParam(value = "project_id") Integer projectId,@RequestParam(value = "category_cls") String categoryCls,
                                                        @RequestParam(value = "task_id",defaultValue ="0") Integer taskId,@RequestParam(value = "category_key",defaultValue = "") String categoryKey,
                                                        @RequestParam(value = "check_item_key",defaultValue = "") String checkItemKey,@RequestParam(value = "area_ids",defaultValue = "") String areaIds,
                                                        @RequestParam(value = "status_in",defaultValue = "") String statusIn,@RequestParam(value = "checker_id",defaultValue ="0") Integer checkerId,
                                                        @RequestParam(value = "repairer_id",defaultValue ="0")Integer repairerId,@RequestParam(value = "type",defaultValue ="0")Integer type,
                                                        @RequestParam(value = "condition",defaultValue ="0")Integer condition,@RequestParam(value = "key_word",defaultValue = "") String keyWord,
                                                        @RequestParam(value = "create_on_begin",defaultValue = "") String createOnBegin,@RequestParam(value = "create_on_end",defaultValue = "") String createOnEnd,
                                                        @RequestParam(value = "is_overdue",defaultValue = "false") Boolean isOverDue,
                                                        @RequestParam(value = "page_level") String pageLevel,
                                                        @ApiParam(value = "当前页码", required = false)
                                                        @Valid @Min(0)
                                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNum,
                                                        @ApiParam(value = "分页大小", required = false)
                                                        @Valid @Min(1)
                                                        @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize
    ) {

        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/

        PageInfo<IssueListVo> pageInfo = iIssueService.list(projectId,categoryCls,taskId,categoryKey,checkItemKey,
                                                            areaIds,statusIn,checkerId,repairerId,type,condition,keyWord,
                                                            createOnBegin,createOnEnd,isOverDue,pageNum,pageSize);
        TaskResponse<PageInfo<IssueListVo>> taskResponse = new TaskResponse<>();
        taskResponse.setData(pageInfo);
        return taskResponse;
    }
}
