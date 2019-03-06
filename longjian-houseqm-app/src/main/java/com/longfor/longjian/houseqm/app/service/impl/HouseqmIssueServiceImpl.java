package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.service.IHouseqmIssueService;
import com.longfor.longjian.houseqm.app.utils.HouseQmCheckTaskIssueHelperVo;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.ApiHouseQmCheckTaskReportRsp;
import com.longfor.longjian.houseqm.app.vo.url.ExportVo;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueCheckStatusEnum;
import com.longfor.longjian.houseqm.domain.internalservice.ExportFileRecordService;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalservice.ProjectService;
import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

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
    @Resource
    private HouseQmCheckTaskIssueHelperVo helper;
    private Random rand;
    private static final String STATUS="status";
    public HouseqmIssueServiceImpl() throws NoSuchAlgorithmException {
        rand = SecureRandom.getInstanceStrong();
    }

    // 删除问题
    @Override
    public void deleteHouseQmCheckTaskIssueByProjUuid(Integer projectId, String issueUuid) {
        int affect = houseQmCheckTaskIssueService.deleteHouseQmCheckTaskIssueByProjUuid(projectId, issueUuid);
        if (affect <= 0) {
            throw new LjBaseRuntimeException(-1, "删除问题失败");
        }
    }

    // 批量销项问题
    @Override
    public List<String> updateBatchIssueApproveStatusByUuids(List<String> uuids, int projectId, int senderId, int status, String desc, String attachmentMd5List){
        int eInt = -1;
        String eStr = "";
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByProjIdAndUuidIn(projectId, uuids);
        HouseQmCheckTaskIssueHelperVo helper = new HouseQmCheckTaskIssueHelperVo();
        helper.init(projectId);
        for (HouseQmCheckTaskIssue issue : issues) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            int nowTimestamp = DateUtil.datetimeToTimeStamp(new Date());

            Map<String,Object>map=new HashMap<>();
            map.put("taskId",issue.getTaskId());
            map.put("uuid",uuid);
            map.put("issueUUId",issue.getUuid());
            map.put("senderId",senderId);
            map.put("desc",desc);
            map.put("eStr1",eStr);
            map.put("nowTimestamp",nowTimestamp);
            map.put("eStr2",eStr);


            Map<String,Object>detailMap=new HashMap<>();
            detailMap.put("areaId",eInt);
            detailMap.put("posX",eInt);
            detailMap.put("posY",eInt);
            detailMap.put("planEndOn",eInt);
            detailMap.put("endOn",eInt);
            detailMap.put("repairerId",eInt);
            detailMap.put("repairerFollowerIds",eStr);
            detailMap.put("condition",eInt);
            detailMap.put("categoryCls",eInt);
            detailMap.put("checkItemKey",eStr);
            detailMap.put("categoryKey",eStr);
            detailMap.put("drawingMd5",eStr);
            detailMap.put("issueReason",eInt);
            detailMap.put("issueReasonDetail",eStr);
            detailMap.put("issueSuggest",eStr);
            detailMap.put("potentialRisk",eStr);
            detailMap.put("preventiveActionDetail",eStr);
            detailMap.put("removeMemoAudioMd5List",eStr);
            detailMap.put("typ",eInt);

            if (HouseQmCheckTaskIssueCheckStatusEnum.CheckYes.getId().equals(status)) {

                map.put(STATUS,HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
                map.put("str",eStr);
                // 审核通过
                helper.start().setNormalField(map).
                        setDetailField(detailMap).done();
            } else if (HouseQmCheckTaskIssueCheckStatusEnum.CheckNo.getId().equals(status)) {
                map.put(STATUS,HouseQmCheckTaskIssueCheckStatusEnum.CheckNo.getId());
                map.put("str",attachmentMd5List);
                helper.start().setNormalField(map).
                        setDetailField(detailMap).done();
            }
        }
        try {
            helper.execute();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        List<String> dropUuids = Lists.newArrayList();
        List<ApiHouseQmCheckTaskReportRsp> droppedIssue = helper.getDroppedIssue();
        for (ApiHouseQmCheckTaskReportRsp drop : droppedIssue) {
            dropUuids.add(drop.getUuid());
        }
        return dropUuids;
    }

    @Override
    public List<String> updateBatchIssueRepairInfoByUuids(List<String> uuids, Integer projectId, int senderId, Integer repairerId, String repairFollowerIds, Integer planEndOn) {
        int eInt = -1;
        String eStr = "";
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByProjIdAndUuidIn(projectId, uuids);

        int status = eInt;
        helper.init(projectId);
        for (HouseQmCheckTaskIssue issue : issues) {
            if (HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId().equals(issue.getStatus()) && repairerId > 0) {
                status = HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId();
            }

            // 整改负责人变化了，则需要将他添加进跟进人
            String issueRepairerFollowerIds = "";
            if (repairerId.equals(issue.getRepairerId()) && issue.getRepairerId() > 0) {
                List<Integer> followers = StringSplitToListUtil.strToInts(repairFollowerIds, ",");
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

            Map<String,Object>map=new HashMap<>();
            map.put("taskId",issue.getTaskId());
            map.put("uuid",uuid);
            map.put("issueUUId",issue.getUuid());
            map.put("senderId",senderId);
            map.put("desc",eStr);
            map.put("eStr1",eStr);
            map.put("nowTimestamp",nowTimestamp);
            map.put("eStr2",eStr);
            map.put(STATUS,status);
            map.put("str",eStr);


            Map<String,Object>detailMap=new HashMap<>();
            detailMap.put("areaId",eInt);
            detailMap.put("posX",eInt);
            detailMap.put("posY",eInt);
            detailMap.put("planEndOn",planEndOn);
            detailMap.put("endOn",eInt);
            detailMap.put("repairerId",repairerId);
            detailMap.put("repairerFollowerIds",issueRepairerFollowerIds);
            detailMap.put("condition",eInt);
            detailMap.put("categoryCls",eInt);
            detailMap.put("checkItemKey",eStr);
            detailMap.put("categoryKey",eStr);
            detailMap.put("drawingMd5",eStr);
            detailMap.put("issueReason",eInt);
            detailMap.put("issueReasonDetail",eStr);
            detailMap.put("issueSuggest",eStr);
            detailMap.put("potentialRisk",eStr);
            detailMap.put("preventiveActionDetail",eStr);
            detailMap.put("removeMemoAudioMd5List",eStr);
            detailMap.put("typ",eInt);

            helper.start().setNormalField(map)
                    .setDetailField(detailMap).done();
        }
        try {
            helper.execute();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        List<String> dropUuids = Lists.newArrayList();
        List<ApiHouseQmCheckTaskReportRsp> droppedIssue = helper.getDroppedIssue();
        for (ApiHouseQmCheckTaskReportRsp drop : droppedIssue) {
            dropUuids.add(drop.getUuid());
        }
        return dropUuids;
    }

    @Override
    public ExportFileRecord create(int userId, Integer teamId, Integer projectId, int exportType, Map<String, String> args, String exportName, Date executeAt) throws IOException {
        //生成随机数
        long randCount = (long) (rand.nextDouble() * Long.MAX_VALUE);
        String baseDir = exportVo.getBase_dir();
        Integer ts = DateUtil.datetimeToTimeStamp(new Date());
        String inputFilename = String.format("%d%d.%s", randCount, ts, "input");
        String outputFilename = String.format("/export/%d%d.%s", randCount, ts, "output");
        String filepath = baseDir + inputFilename;
        String data = JSON.toJSONString(args);
        this.writeInput(data, exportName, filepath);
        //记录导出的内容到数据库
        String resultFilePath=  String.format("%s%s%s",baseDir,"/",outputFilename);
        return exportFileRecordService.insertFull(userId, teamId, projectId, exportType, inputFilename + " " + outputFilename,
                resultFilePath, exportName, 0, "", executeAt);
    }

    private void writeInput(String data, String exportName, String filepath) throws IOException {
        FileOutputStream out = null;
        OutputStreamWriter op = null;
        try {
            log.info("erxportName :{}", exportName);
            out = new FileOutputStream(String.format("%s", filepath));
            op = new OutputStreamWriter(out, "utf-8");
            File file = new File(String.format("%s", filepath));

            if (!file.getParentFile().exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                log.info("createNewFile flag{}", newFile);
            }
            op.append(data);
            op.flush();
        } catch (IOException e) {
            log.error("error:", e);
        } finally {
            if (op != null) {
                op.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }

    @Override
    public Project getProjectByProjId(Integer projectId) {
        return projectService.getOneByProjId(projectId);
    }

    @Override
    public List<HouseQmCheckTaskIssue> searchHouseQmIssueListByProjUuidIn(Integer projectId, List<String> uuids) {
        return houseQmCheckTaskIssueService.searchByProjIdAndUuidIn(projectId, uuids);
    }


}
