package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.houseqm.app.service.ITaskService;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    ITaskService taskService;

    /**
     * 获取任务区域信息
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param groupId
     * @param teamId
     * @param taskId
     * @return
     */
    @GetMapping(value = "checked_areas/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<String> doAction(@RequestParam(value = "project_id") Integer projectId,
                                         @RequestParam(value = "category_cls") String categoryCls,
                                         @RequestParam(value = "page_level") String pageLevel,
                                         @RequestParam(value = "group_id") String groupId,
                                         @RequestParam(value = "team_id") String teamId,
                                         @RequestParam(value = "task_id") Integer taskId) {

        //todo session uid  权限验证
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.任务管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/

        List<Integer> areaIds = taskService.getHouseqmCheckTaskCheckedAreas(projectId, taskId);
        //数据处理成
        TaskResponse<String> taskResponse = new TaskResponse<>();
        String checkAreaIds = null;
        if (areaIds.isEmpty()) {
            checkAreaIds = "";
        }else{
            //对areaIds进行排序并转换成字符 元素间加逗号隔开
            Collections.sort(areaIds);
            checkAreaIds=StringSplitToListUtil.dataToString(areaIds, ",");
        }

        taskResponse.setData(checkAreaIds);

        return taskResponse;
    }

}
