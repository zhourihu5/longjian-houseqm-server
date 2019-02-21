package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.app.service.IHouseqmIssueService;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskIssueHelperVo;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.ApiHouseQmCheckTaskReportRsp;
import com.longfor.longjian.houseqm.app.vo.url.ExportVo;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueCheckStatusEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.ExportFileRecordService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.ProjectService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.service.impl
 * @ClassName: HouseqmIssueServiceImpl
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:27
 */
@Service
@Slf4j
public class HouseqmIssueServiceImpl implements IHouseqmIssueService {

    @Resource
    private ProjectService projectService;

    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private ExportVo exportVo;
    @Resource
    private ExportFileRecordService exportFileRecordService;


    // 删除问题
    @Override
    public void deleteHouseQmCheckTaskIssueByProjUuid(Integer project_id, String issueUuid) throws Exception {
        int affect = houseQmCheckTaskIssueService.deleteHouseQmCheckTaskIssueByProjUuid(project_id, issueUuid);
        if (affect <= 0) {
            throw new Exception("删除问题失败");
        }
    }

    // 批量销项问题
    @Override
    public List<String> updateBatchIssueApproveStatusByUuids(List<String> uuids, int projectId, int senderId, int status, String desc, String attachmentMd5List) throws Exception {
        int eInt = -1;
        String eStr = "";
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByProjIdAndUuidIn(projectId, uuids);
        HouseQmCheckTaskIssueHelperVo helper = new HouseQmCheckTaskIssueHelperVo();
        helper.init(projectId);
        for (HouseQmCheckTaskIssue issue : issues) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            int nowTimestamp = DateUtil.datetimeToTimeStamp(new Date());
            if (HouseQmCheckTaskIssueCheckStatusEnum.CheckYes.getId().equals(status)) {
                // 审核通过
                helper.start().setNormalField(issue.getTaskId(), uuid, issue.getUuid(), senderId, desc, HouseQmCheckTaskIssueStatusEnum.CheckYes.getId(), eStr, eStr, nowTimestamp, eStr).
                        setDetailField(eInt, eInt, eInt, eInt, eInt, eInt, eStr, eInt, eInt, eStr, eStr, eStr, eInt, eStr, eStr, eStr, eStr, eStr, eInt).done();
            } else if (HouseQmCheckTaskIssueCheckStatusEnum.CheckNo.getId().equals(status)) {
                helper.start().setNormalField(issue.getTaskId(), uuid, issue.getUuid(), senderId, desc, HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId(), attachmentMd5List, eStr, nowTimestamp, eStr).
                        setDetailField(eInt, eInt, eInt, eInt, eInt, eInt, eStr, eInt, eInt, eStr, eStr, eStr, eInt, eStr, eStr, eStr, eStr, eStr, eInt).done();
            }
        }
        try {
            helper.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> dropUuids = Lists.newArrayList();
        List<ApiHouseQmCheckTaskReportRsp> droppedIssue = helper.getDroppedIssue();
        for (ApiHouseQmCheckTaskReportRsp drop : droppedIssue) {
            dropUuids.add(drop.getUuid());
        }
        return dropUuids;
    }

    @Override
    public List<String> updateBatchIssueRepairInfoByUuids(List<String> uuids, Integer project_id, int senderId, Integer repairer_id, String repair_follower_ids, Integer plan_end_on) throws Exception {
        int eInt = -1;
        String eStr = "";
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByProjIdAndUuidIn(project_id, uuids);

        int status = eInt;
        HouseQmCheckTaskIssueHelperVo helper = new HouseQmCheckTaskIssueHelperVo();
        helper.init(project_id);
        for (HouseQmCheckTaskIssue issue : issues) {
            if (HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId().equals(issue.getStatus()) && repairer_id > 0) {
                status = HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId();
            }

            // 整改负责人变化了，则需要将他添加进跟进人
            String issueRepairerFollowerIds = "";
            if (repairer_id.equals(issue.getRepairerId()) && issue.getRepairerId() > 0) {
                List<Integer> followers = StringSplitToListUtil.strToInts(repair_follower_ids, ",");
                boolean flag = true;
                for (Integer f : followers) {
                    if (f.equals(issue.getRepairerId())) flag = false;
                }
                if (flag) followers.add(issue.getRepairerId());
                issueRepairerFollowerIds = StringSplitToListUtil.dataToString(followers, ",");
            }

            // 变更类型
            String uuid = UUID.randomUUID().toString().replace("-", "");
            int nowTimestamp = DateUtil.datetimeToTimeStamp(new Date());
            helper.start().setNormalField(issue.getTaskId(), uuid, issue.getUuid(), senderId, eStr, status, eStr, eStr, nowTimestamp, eStr)
                    .setDetailField(eInt, eInt, eInt, plan_end_on, eInt, repairer_id, issueRepairerFollowerIds, eInt, eInt, eStr, eStr, eStr, eInt, eStr, eStr, eStr, eStr, eStr, eInt).done();
        }
        try {
            helper.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> dropUuids = Lists.newArrayList();
        List<ApiHouseQmCheckTaskReportRsp> droppedIssue = helper.getDroppedIssue();
        for (ApiHouseQmCheckTaskReportRsp drop : droppedIssue) {
            dropUuids.add(drop.getUuid());
        }
        return dropUuids;
    }

    @Override
    public ExportFileRecord create(int userId, Integer teamId, Integer project_id, int exportType, Map<String, String> args, String exportName, Date executeAt) throws Exception {
        //生成随机数
        Random random = new Random(Long.MAX_VALUE);
        /*long randCount = Math.abs(random.nextLong());
        long ts = new Date().getTime();
        //读取配置 export - basedir baseuri
        String cfg_base_dir = exportVo.getBase_dir();
        String cfg_base_uri = exportVo.getBase_uri();
        //输入输出文件名
        String inputFileName = randCount + ts + ".input";
        String outputFileName = "export/" + randCount + ts + ".output";
        String fileName = cfg_base_dir + "/" + inputFileName;
        byte[] data = JSON.toJSONBytes(args);
        writeInput(data, cfg_base_dir,inputFileName);*/
        long randCount = Math.abs(random.nextLong());
        String base_dir = exportVo.getBase_dir();
        Integer ts = DateUtil.datetimeToTimeStamp(new Date());
        String base_uri = exportVo.getBase_uri();
        String inputFilename = String.format("%d%d.%s", randCount, ts, "input");
        String outputFilename = String.format("/export/%d%d.%s", randCount, ts, "output");
        String filepath = base_dir + inputFilename;
        String data = JSON.toJSONString(args);
        this.writeInput(data,exportName,filepath);
        //记录导出的内容到数据库
        String resultFilePath = base_uri + "/" + outputFilename;
        return exportFileRecordService.insertFull(userId, teamId, project_id, exportType, inputFilename + " " + outputFilename,
                resultFilePath, exportName, 0, "", executeAt);
    }

    private void writeInput(String data, String exportName, String filepath) throws Exception {
        try {
            //File file = new File(String.format("%s", filepath));
            File file = new File(String.format("D:/%s", exportName));

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out;
            //out = new FileOutputStream(String.format("%s", filepath));
            out = new FileOutputStream(String.format("D:/%s",exportName));
            //String data1 = new String(data,"utf-8");
            OutputStreamWriter op = new OutputStreamWriter(out, "utf-8");
            op.append(data);
            //out.write(data);
            op.flush();
            op.close();
        /*out.write(data);
        out.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Project getProjectByProjId(Integer project_id) {
        return projectService.getOneByProjId(project_id);
    }

    @Override
    public List<HouseQmCheckTaskIssue> searchHouseQmIssueListByProjUuidIn(Integer project_id, List<String> uuids) throws Exception {
        return houseQmCheckTaskIssueService.searchByProjIdAndUuidIn(project_id, uuids);
    }


}
