package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.houseqm.app.req.taskcheckedareas.CheckedAreasReq;
import com.longfor.longjian.houseqm.app.service.ITaskService;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import com.longfor.longjian.houseqm.app.vo.taskcheckedareas.CheckedAreasRsp;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/272 获取任务区域信息
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("buildingqm/v3/papi/task/")
@Slf4j
public class TaskCheckedAreasController {

    @Resource
    private ITaskService taskService;
    @Resource
    private CtrlTool ctrlTool;

    /**
     * 获取任务区域信息
     *
     * @param req
     * @return
     */
    @GetMapping(value = "checked_areas/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<CheckedAreasRsp> doAction(HttpServletRequest request, @Valid CheckedAreasReq req) {

        //todo session uid  权限验证 uid = session['uid']
        int uid = 7566;
        /*
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.任务管理.查看')
       */
        try {
            ctrlTool.projPerm(request,"项目.工程检查.任务管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Integer> areaIds = taskService.getHouseqmCheckTaskCheckedAreas(req.getProject_id(), req.getTask_id());
        TaskResponse<CheckedAreasRsp> taskResponse = new TaskResponse<>();
        String checkAreaIds = null;
        if (areaIds.isEmpty()) {
            checkAreaIds = "";
        } else {
            //对areaIds进行排序并转换成字符 元素间加逗号隔开
            Collections.sort(areaIds);
            checkAreaIds = StringSplitToListUtil.dataToString(areaIds, ",");
        }
        CheckedAreasRsp data = new CheckedAreasRsp();
        data.setCheck_area_ids(checkAreaIds);
        taskResponse.setData(data);
        return taskResponse;
    }

}
