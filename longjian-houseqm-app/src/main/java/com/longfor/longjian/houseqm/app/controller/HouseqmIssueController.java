package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchAppointReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueExportPdfReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmIssueService;
import com.longfor.longjian.houseqm.app.vo.IssueBatchAppointRspVo;
import com.longfor.longjian.houseqm.consts.ErrorEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import com.longfor.longjian.houseqm.app.vo.url.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.controller
 * @ClassName: HouseqmIssueController
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:02
 */
@RestController
@RequestMapping("oapi/v3/houseqm/issue/")
@Slf4j
public class HouseqmIssueController {

    @Resource
    private IHouseqmIssueService iHouseqmIssueService;
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;

    @Value("${stat_export_server_addr}")
    String statExportServerAddr;

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse
     * @Author hy
     * @Description 项目下问题导出PDF到任务
     * http://192.168.37.159:3000/project/8/interface/api/3356
     * @Date 17:13 2019/1/10
     * @Param [req]
     **/
    @PostMapping(value = "export_pdf/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse exportPdf(@Valid IssueExportPdfReq req) {
        //todo 鉴权 _, _, err = ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.问题管理.查看", "项目.工程检查.问题管理.查看"})
        Project proj = iHouseqmIssueService.getProjectByProjId(req.getProject_id());
        if (proj == null)
            throw new LjBaseRuntimeException(ErrorEnum.DB_ITEM_UNFOUND.getCode(), ErrorEnum.DB_ITEM_UNFOUND.getMessage());

        List<String> uuids = null;
        if (req.getUuids().trim().length() > 0) {
            uuids = StringSplitToListUtil.splitToStringComma(req.getUuids(), ",");
        }

        String taskName = "";
        List<HouseQmCheckTask> taskList = Lists.newArrayList();
        //优先看有没有传task_id
        if (req.getTask_id() > 0) {
            HouseQmCheckTask houseQmCheckTask = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(req.getProject_id(), req.getTask_id());
            taskList.add(houseQmCheckTask);
        } else if (req.getUuids().length() > 0) {
            // 按uuid提取
            List<HouseQmCheckTaskIssue> issueList = iHouseqmIssueService.searchHouseQmIssueListByProjUuidIn(req.getProject_id(), uuids);
            HashMap<Integer, Boolean> taskMap = Maps.newHashMap();
            issueList.forEach(issue -> taskMap.put(issue.getTaskId(), true));
            List<Integer> taskIds = taskMap.keySet().stream().collect(Collectors.toList());
            if (taskIds.size() > 0) {
                taskList = houseQmCheckTaskService.searchHouseQmCheckTaskByTaskIdIn(taskIds);
            }
        } else {
            //全取
            taskList = houseQmCheckTaskService.selectByProjectIdAndCategoryCls(req.getProject_id(), req.getCategory_cls());
        }
        int taskLen = taskList.size();
        for (int i = 0; i < taskList.size(); i++) {
            taskName += taskList.get(i).getName();
            if (taskName.length() > 50) {
                taskName += "等" + taskLen + "个任务";
                break;
            }
            if (i != taskLen - 1) {
                taskName += "、";
            }
        }
        // 导出项配置
        UrlUtils.getUrl(statExportServerAddr + "/stat_export/houseqm_check_task_issue/index_json/");

        //todo  设置url参数 即要导出的数据


        // 获取userid //c.MustGet("user").(*zj3user_models.User).Id
        int userId = 1;
        Map<String, String> args = Maps.newHashMap();
        args.put("url", "");

        String nowTime = DateUtil.getNowTimeStr("yyyyMMddhhmm");
        String exportName = "【" + proj.getName() + "】整改报告." + nowTime + ".pdf";
        // todo 把导出的信息插入到数据库中
        iHouseqmIssueService.create(userId, proj.getTeamId(), req.getProject_id(), 0, args, exportName, new Date());


        LjBaseResponse<Object> response = new LjBaseResponse<>();
        return response;
    }

    /**
     * @Author hy
     * @Description 批量指派issue
     * http://192.168.37.159:3000/project/8/interface/api/3320
     * @Date 14:03 2019/1/11
     * @Param [req]
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.IssueBatchAppointRspVo>
     **/
    @PostMapping(value = "batch_appoint/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchAppointRspVo> batchAppoint(@Valid IssueBatchAppointReq req) throws Exception {
        //todo 鉴权 _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.问题管理.编辑", "项目.工程检查.问题管理.编辑"})

        // 过滤掉不同task下的问题，感觉有点多余，不过还是处理下
        List<String> issueUuids = StringSplitToListUtil.splitToStringComma(req.getIssue_uuids(), ",");
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByTaskIdUuidIn(req.getTask_id(), issueUuids);
        List<String> uuids= Lists.newArrayList();
        for (HouseQmCheckTaskIssue issue : issues) {
            if (issue.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId())){
                throw new Exception("有问题已销项，不能被指派");
            }
            if (req.getProject_id().equals(issue.getProjectId())){
                uuids.add(issue.getUuid());
            }
        }
        //todo 获取userid
        int uid=7556;
        iHouseqmIssueService.updateBatchIssueRepairInfoByUuids(uuids,req.getProject_id(),uid,req.getRepairer_id(),req.getRepair_follower_ids(),req.getPlan_end_on());


        return null;
    }

    public LjBaseResponse batchApprove() {

        return null;
    }

    public LjBaseResponse batchDelete() {

        return null;
    }


}
