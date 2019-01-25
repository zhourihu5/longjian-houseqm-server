package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.service.IusseTaskListService;
import com.longfor.longjian.houseqm.app.vo.ApiMineMsg;
import com.longfor.longjian.houseqm.app.vo.ApiStatHouseqmMeterSettingMsgVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskSimpleRspVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import com.longfor.longjian.houseqm.domain.internalService.RepossessionMeterSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("houseqm/v3/papi/")
@Slf4j
public class IusseTaskListController {

    @Resource
    private IusseTaskListService iusseTaskListService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;

    /**
     * 获取可用于检索的任务列表
     *
     * @param projectId
     * @param categoryCls
     * @param
     * @return
     */
    @GetMapping(value = "issue/task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<HouseQmCheckTaskSimpleRspVo.TaskList> doAction(HttpServletRequest request, @RequestParam(value = "project_id") Integer
            projectId,
                                                                       @RequestParam(value = "category_cls") Integer categoryCls/*,
                                                              @RequestParam(value = "page_level") String pageLevel,
                                                              @RequestParam(value = "group_id") String groupId,
                                                              @RequestParam(value = "team_id") String teamId*/
    ) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.移动验房.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通过id projectId 判断
        TaskResponse<HouseQmCheckTaskSimpleRspVo.TaskList> response = new TaskResponse<>();
        List<HouseQmCheckTaskSimpleRspVo> vos = iusseTaskListService.selectByProjectIdAndCategoryCls(projectId, categoryCls);
        HouseQmCheckTaskSimpleRspVo.TaskList taskList = new HouseQmCheckTaskSimpleRspVo().new TaskList();
        taskList.setTask_list(vos);
        response.setData(taskList);
        return response;

    }

    /**
     * http://192.168.37.159:3000/project/8/interface/api/3304  获取验房验收项目配置列表
     */

    @GetMapping(value = "stat_houseqm/get_acceptanceitems_setting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public LjBaseResponse<ApiStatHouseqmMeterSettingMsgVo.HouseqmMeterSetting> getAcceptanceitemsSetting(@RequestParam(name = "project_ids", required = true) String projectIds,
                                                                                                         @RequestParam(name = "timestamp", required = false, defaultValue = "0") Integer timestamp) {
        log.info("get_acceptanceitems_setting, project_ids=" + projectIds + ", timestamp=" + timestamp + "");
        List<ApiStatHouseqmMeterSettingMsgVo> acceptanceItems = iusseTaskListService.getAcceptanceitemsSetting(projectIds, timestamp);
        LjBaseResponse<ApiStatHouseqmMeterSettingMsgVo.HouseqmMeterSetting> response = new LjBaseResponse<>();
        ApiStatHouseqmMeterSettingMsgVo.HouseqmMeterSetting setting = new ApiStatHouseqmMeterSettingMsgVo().new HouseqmMeterSetting();
        setting.setAcceptance_items(acceptanceItems);
        response.setData(setting);
        return response;
    }

    /**
     * http://192.168.37.159:3000/project/8/interface/api/3308 获取“我”的公司与项目列表
     *
     * @param categorys
     * @return
     */
    @GetMapping(value = "mine/teams_and_projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public LjBaseResponse<ApiMineMsg> teamsAndProjects(@RequestParam(name = "categorys", required = false, defaultValue = "26,28") String categorys) {
        log.info("teams_and_projects, categorys=" + categorys + "");
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        ApiMineMsg apiMineMsg = iusseTaskListService.teamsAndProjects(userId, categorys);

        LjBaseResponse<ApiMineMsg> response = new LjBaseResponse<>();
        response.setData(apiMineMsg);
        return response;
    }

}
