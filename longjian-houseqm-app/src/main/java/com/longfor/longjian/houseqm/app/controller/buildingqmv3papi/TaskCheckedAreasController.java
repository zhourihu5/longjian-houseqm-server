package com.longfor.longjian.houseqm.app.controller.buildingqmv3papi;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.taskcheckedareas.CheckedAreasReq;
import com.longfor.longjian.houseqm.app.service.ITaskService;
import com.longfor.longjian.houseqm.app.vo.taskcheckedareas.CheckedAreasRsp;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @Resource
    private SessionInfo sessionInfo;

    /**
     * 获取任务区域信息
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "checked_areas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CheckedAreasRsp> doAction(HttpServletRequest request, @Valid CheckedAreasReq req) {
        LjBaseResponse<CheckedAreasRsp> taskResponse = new LjBaseResponse<>();
        try {
            ctrlTool.projPerm(request, "项目.工程检查.任务管理.查看");
            List<Integer> areaIds = taskService.getHouseqmCheckTaskCheckedAreas(req.getProject_id(), req.getTask_id());
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
        } catch (Exception e) {
            log.error(e.getMessage());
            taskResponse.setResult(1);
            taskResponse.setMessage(e.getMessage());
        }
        return taskResponse;
    }

}
