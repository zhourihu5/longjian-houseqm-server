package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.houseqm.app.service.IusseTaskListService;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskSimpleRspVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/282  获取可用于检索的任务列表
 * http://192.168.37.159:3000/project/8/interface/api/1524 项目下季度检查获取任务列表
 * post请求，路径与get请求一致 参数不同 未找到py代码 搁置
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("houseqm/v3/papi/issue/")
@Slf4j
public class IusseTaskListController {

    @Resource
    private IusseTaskListService iusseTaskListService;

    /**
     * 获取可用于检索的任务列表
     *
     * @param projectId
     * @param categoryCls
     * @param
     * @return
     */
    @GetMapping(value = "task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<HouseQmCheckTaskSimpleRspVo> doAction(@RequestParam(value = "project_id") Integer
                                                                      projectId,
                                                              @RequestParam(value = "category_cls") Integer categoryCls/*,
                                                              @RequestParam(value = "page_level") String pageLevel,
                                                              @RequestParam(value = "group_id") String groupId,
                                                              @RequestParam(value = "team_id") String teamId*/
    ) {
       /* uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.移动验房.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')
        return
      */
        //获取sessionid
        //通过id projectId 判断
        TaskResponse<HouseQmCheckTaskSimpleRspVo> response = new TaskResponse<>();
        List<HouseQmCheckTaskSimpleRspVo> vos = iusseTaskListService.selectByProjectIdAndCategoryCls(projectId, categoryCls);
        response.setData(vos);
        response.setMessage("success");
        response.setMsg("success");
        return response;

    }
}
