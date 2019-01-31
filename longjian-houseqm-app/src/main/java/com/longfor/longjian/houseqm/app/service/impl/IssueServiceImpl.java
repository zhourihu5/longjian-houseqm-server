package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.HouseQmCheckTaskActionLogType;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueLogStatus;
import com.longfor.longjian.common.consts.ModuleInfoEnum;
import com.longfor.longjian.common.consts.UserInIssueRoleType;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.push.UmPushUtil;
import com.longfor.longjian.common.push.xiaomi.XmPushUtil;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.IssueListVo.DetailVo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.vo.issuelist.IssueListRsp;
import com.longfor.longjian.houseqm.consts.AppPlatformTypeEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Houyan
 * @date 2018/12/21 0021 11:42
 */
@Repository
@Service
@Slf4j
public class IssueServiceImpl implements IIssueService {
    @Value("${push_config.enterprise_id}")
    private String ENTERPRISEID;

    @Value("${push_config.gcgl.app_key_android}")
    private String APP_KEY_ANDROID;
    @Value("${push_config.gcgl.app_master_secret_android}")
    private String APP_MASTER_SECRET_ANDROID;
    @Value("${push_config.gcgl.app_key_ios}")
    private String APP_KEY_IOS;
    @Value("${push_config.gcgl.app_master_secret_ios}")
    private String APP_MASTER_SECRET_IOS;
    @Value("${push_config.gcgl.app_secret_xiao_mi}")
    private String APP_SECRET_XIAO_MI;
    @Value("${push_config.gcgl.package_name_xiao_mi}")
    private String PACKAGE_NAME_XIAO_MI;


    @Resource
    private HouseQmCheckTaskIssueAttachmentService houseQmCheckTaskIssueAttachmentService;
    @Resource
    private HouseQmCheckTaskIssueUserService houseQmCheckTaskIssueUserService;
    @Resource
    private HouseQmCheckTaskNotifyRecordService houseQmCheckTaskNotifyRecordService;

    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;

    @Resource
    private CategoryV3Service categoryV3Service;
    @Resource
    private HouseQmCheckTaskIssueLogService houseQmCheckTaskIssueLogService;

    @Resource
    private CheckItemV3Service checkItemV3Service;

    @Resource
    private UserService userService;

    @Resource
    private AreaService areaService;

    @Resource
    private FileResourceService fileResourceService;
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private ProjectSettingV2Service projectSettingV2Service;

    @Value("${env_info.host_list}")
    private String envInfo;

    /**
     * @param projectId
     * @param categoryCls
     * @param taskId
     * @param categoryKey
     * @param checkItemKey
     * @param areaIds
     * @param statusIn
     * @param checkerId
     * @param repairerId
     * @param type
     * @param condition
     * @param keyWord
     * @param createOnBegin
     * @param createOnEnd
     * @param isOverDue
     * @param pageNum
     * @param pageSize
     * @return com.longfor.gaia.gfs.core.bean.PageInfo<com.longfor.longjian.houseqm.app.vo.IssueListVo>
     * @author hy
     * @date 2018/12/21 0021
     */
    @Override
    public IssueListRsp list(Integer projectId, Integer categoryCls, Integer taskId, String categoryKey, String checkItemKey, String areaIds, String statusIn,
                             Integer checkerId, Integer repairerId, Integer type, Integer condition, String keyWord, String createOnBegin,
                             String createOnEnd, Boolean isOverDue, Integer pageNum, Integer pageSize) {

        //PageInfo<IssueListVo> pageInfo = new PageInfo<>();
        IssueListRsp issueListRsp = new IssueListRsp();
        Integer start = (pageNum - 1) * pageSize;

        //读取配置信息 env_info = dict(**config.ENV_INFO)  host_list: longjianapp.longhu.net lh.zj.com
        String[] hosts = envInfo.trim().split(" ");
        String host = null;
        if (hosts.length > 0) {
            host = hosts[0];
        } else {
            host = "";
        }
        List<Integer> areaIdList = StringSplitToListUtil.splitToIdsComma(areaIds, ",");
        List<Integer> statusInList = StringSplitToListUtil.splitToIdsComma(statusIn, ",");
        Map<String, Object> condiMap = Maps.newHashMap();
        condiMap.put("projectId", projectId);
        condiMap.put("categoryCls", categoryCls);
        if (taskId > 0) condiMap.put("taskId", taskId);
        if (statusIn.length() > 0) condiMap.put("status", statusInList);
        if (categoryKey.length() > 0) condiMap.put("categoryPathAndKey", "%%/" + categoryKey + "/%%");
        if (checkItemKey.length() > 0) condiMap.put("checkItemKey", checkItemKey);
        if (areaIdList.size() > 0) {
            List<String> areaPathAndIdLikeList = Lists.newArrayList();
            for (Integer i : areaIdList) {
                areaPathAndIdLikeList.add("%%/" + i + "/%%");
            }
            condiMap.put("areaPathAndId", areaPathAndIdLikeList);
        }
        if (type > 0) condiMap.put("type", type);
        if (condition > 0) condiMap.put("condition", condition);
        if (checkerId > 0) condiMap.put("senderId", checkerId);
        if (repairerId > 0) condiMap.put("repairerId", repairerId);
        if (createOnBegin.length() > 0) condiMap.put("clientCreateAtGte", createOnBegin + " 00:00:00");
        if (createOnEnd.length() > 0) condiMap.put("clientCreateAtLte", createOnEnd + " 23:59:59");
        if (isOverDue) {
            List<Integer> status1 = Lists.newArrayList();
            List<Integer> status2 = Lists.newArrayList();
            status1.add(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId());
            status1.add(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
            condiMap.put("status1", status1);
            status2.add(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());
            status2.add(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
            condiMap.put("status2", status2);
        }
        if (keyWord.length() > 0) {//content like xxx
            condiMap.put("content", "%%/" + keyWord + "/%%");
            if (StringSplitToListUtil.isInteger(keyWord)) {// or id=xxx
                condiMap.put("id", keyWord);
            }
        }
        condiMap.put("deleted", "false");
        // 调用HouseQmCheckTaskIssueService
        Integer total = houseQmCheckTaskIssueService.searchTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(condiMap);
        condiMap.put("start", start);
        condiMap.put("pageSize", pageSize);
        condiMap.put("reverse", true);
        List<HouseQmCheckTaskIssue> validIssues = houseQmCheckTaskIssueService.searchByPageAndProjectIdAndCategoryClsAndNoDeletedAndDongTai(condiMap);
        // category_keys, repairers, check_items, area_paths, attachments = [], [], [], [], []
        List<String> categoryKeys = Lists.newArrayList();
        List<Integer> repairers = Lists.newArrayList();
        List<String> checkItems = Lists.newArrayList();
        List<Integer> areaPaths = Lists.newArrayList();
        List<String> attachments = Lists.newArrayList();

        for (HouseQmCheckTaskIssue issue : validIssues) {
            if (!categoryKeys.contains(issue.getCategoryKey())) categoryKeys.add(issue.getCategoryKey());
            if (issue.getRepairerId() != 0) repairers.add(issue.getRepairerId());
            List<String> checkItemPathAndKeyList = StringSplitToListUtil.splitToStringComma(issue.getCheckItemPathAndKey(), "/");
            checkItemPathAndKeyList.forEach(s -> {
                if (!checkItems.contains(s)) checkItems.add(s);
            });
            List<Integer> areaPathAndIdList = StringSplitToListUtil.splitToIdsComma(issue.getAreaPathAndId(), "/");
            areaPathAndIdList.forEach(item -> {
                if (!areaPaths.contains(item)) areaPaths.add(item);
            });
            List<String> attachmentMd5List = StringSplitToListUtil.splitToStringComma(issue.getAttachmentMd5List(), ",");
            attachmentMd5List.forEach(item -> {
                if (!attachments.contains(item)) attachments.add(item);
            });
        }

        //
        Map<String, CategoryV3> categoryKeyMap = createCategoryKeyMap(categoryKeys);
        Map<String, CheckItemV3> checkItemMap = createCheckItemMap(checkItems);
        Map<Integer, User> userMap = createUserMap(repairers);
        Map<Integer, Area> areaMap = createAreaMap(areaPaths);
        Map<String, FileResource> attachmentMap = createAttachmentMap(attachments);

        List<IssueListVo> issueList = Lists.newArrayList();
        for (HouseQmCheckTaskIssue issue : validIssues) {
            IssueListVo item = new IssueListVo();
            item.setId(issue.getId());
            item.setProject_id(issue.getProjectId());
            item.setTask_id(issue.getTaskId());
            item.setUuid(issue.getUuid());
            item.setSender_id(issue.getSenderId());
            item.setPlan_end_on(DateUtil.datetimeToTimeStamp(issue.getPlanEndOn()));
            item.setEnd_on(DateUtil.datetimeToTimeStamp(issue.getEndOn()));
            item.setArea_id(issue.getAreaId());
            item.setArea_path_and_id(issue.getAreaPathAndId());
            item.setCategory_cls(issue.getCategoryCls());
            item.setCategory_key(issue.getCategoryKey());
            item.setCategory_path_and_key(issue.getCategoryPathAndKey());
            if (categoryKeyMap.containsKey(issue.getCategoryKey())) {
                item.setCategory_name(categoryKeyMap.get(issue.getCategoryKey()).getName());
            } else {
                item.setCategory_name("");
            }
            item.setCheck_item_key(issue.getCheckItemKey());
            item.setCheck_item_path_and_key(issue.getCheckItemPathAndKey());
            if (checkItemMap.containsKey(issue.getCheckItemKey())) {
                item.setCheck_item_name(checkItemMap.get(issue.getCheckItemKey()).getName());
            } else {
                item.setCheck_item_name("");
            }
            item.setDrawing_md5(issue.getDrawingMD5());
            item.setPos_x(issue.getPosX());
            item.setPos_y(issue.getPosY());
            item.setTitle(issue.getTitle());
            item.setTyp(issue.getTyp());
            item.setContent(issue.getContent());
            item.setCondition(issue.getCondition());
            item.setStatus(issue.getStatus());
            item.setAttachment_md5_list(issue.getAttachmentMd5List());
            item.setClient_create_at(DateUtil.datetimeToTimeStamp(issue.getClientCreateAt()));
            item.setLast_assigner(issue.getLastAssigner());
            item.setLast_assigner_at(DateUtil.datetimeToTimeStamp(issue.getLastAssignAt()));
            item.setLast_repairer(issue.getLastRepairer());
            item.setLast_repairer_at(DateUtil.datetimeToTimeStamp(issue.getLastRepairerAt()));
            item.setDestroy_user(issue.getDestroyUser());
            item.setDestroy_at(DateUtil.datetimeToTimeStamp(issue.getDestroyAt()));
            item.setDelete_user(issue.getDeleteUser());
            //todo 确认 python源码 item.delete_time = CommonUtil.datetime_to_timestamp(houseqmissue.delete_at)
            item.setDelete_time(DateUtil.datetimeToTimeStamp(issue.getDeleteTime()));

            List<String> attachmentsList = StringSplitToListUtil.splitToStringComma(issue.getAttachmentMd5List(), ",");
            List<String> pictures = item.getPictures();
            for (String md5 : attachmentsList) {
                if (attachmentMap.containsKey(md5)) {
                    String url = "https://" + host +"/"+ attachmentMap.get(md5).getStoreKey();
                    pictures.add(url);
                }
            }
            item.setPictures(pictures);
            List<String> areaPathNames = getAreaPathName(areaMap, issue.getAreaPathAndId());
            String areaPathName = StringSplitToListUtil.dataToString(areaPathNames, "-");
            item.setArea_path_name(areaPathName);
            List<String> checkItemPathNames = getCheckItemPathName(checkItemMap, issue.getCheckItemPathAndKey());
            String checkItemNamePath = StringSplitToListUtil.dataToString(checkItemPathNames, "/");
            item.setCheck_item_name_path(checkItemNamePath);
            if (userMap.containsKey(issue.getRepairerId())) {
                item.setLast_repairer_name(userMap.get(issue.getRepairerId()).getRealName());
            } else {
                item.setLast_repairer_name("");
            }
            DetailVo detailVo = item.new DetailVo();
            Map<String, Object> map = JsonUtil.GsonToMaps(issue.getDetail());

            detailVo.setIssue_reason(new Double((Double) map.get("IssueReason")).intValue());
            detailVo.setIssue_reason_detail((String) map.get("IssueReasonDetail"));
            detailVo.setIssue_suggest((String) map.get("IssueSuggest"));
            detailVo.setPotential_risk((String) map.get("PotentialRisk"));
            detailVo.setPreventive_action_detail((String) map.get("PreventiveActionDetail"));
            item.setDetail(detailVo);
            issueList.add(item);
        }
        /*pageInfo.setTotal(total);
        pageInfo.setList(issueList);*/
        issueListRsp.setTotal(total);
        issueListRsp.setIssue_list(issueList);
        return issueListRsp;
    }

    @Override
    public List<HouseQmCheckTaskIssueHistoryLogVo> getHouseQmCheckTaskIssueActionLogByIssueUuid(String issueUuid) {
        HouseQmCheckTaskIssue issue_info = houseQmCheckTaskIssueService.selectByUuidAndNotDelete(issueUuid);
        if (issue_info == null) {
            throw new LjBaseRuntimeException(272, "找不到此问题");
        }
        List<HouseQmCheckTaskIssueLog> issue_log_info = houseQmCheckTaskIssueLogService.selectByUuidAndNotDelete(issueUuid);
        ArrayList<Integer> uids = Lists.newArrayList();
        HashMap<Integer, String> user_id_real_name_map = Maps.newHashMap();
        for (int i = 0; i < issue_log_info.size(); i++) {
            uids.add(issue_log_info.get(i).getSenderId());
            Map<String, Object> issue_log_detail = JSON.parseObject(issue_log_info.get(i).getDetail(), Map.class);
            if ((Integer) issue_log_detail.get("RepairerId") > 0) {
                uids.add((Integer) issue_log_detail.get("RepairerId"));
            }
            if (((String) issue_log_detail.get("RepairerFollowerIds")).length() > 0) {
                String replace = ((String) issue_log_detail.get("RepairerFollowerIds")).replace(",,", ",");
                List<String> split = StringSplitToListUtil.removeStartAndEndStrAndSplit(replace, ",", ",");
                for (int j = 0; j < split.size(); j++) {
                    uids.add(Integer.valueOf(split.get(j)));
                }
                List uidlist = CollectionUtil.removeDuplicate(uids);
                List<User> user_info = userService.searchByUserIdInAndNoDeleted(uidlist);
                for (int j = 0; j < user_info.size(); j++) {
                    user_id_real_name_map.put(user_info.get(j).getUserId(), user_info.get(j).getRealName());
                }

            }
        }
        ArrayList<HouseQmCheckTaskIssueHistoryLogVo> result = Lists.newArrayList();
        boolean hasCreateLog = false;
        for (int i = 0; i < issue_log_info.size(); i++) {

            Map<String, Object> issue_log_detail = JSON.parseObject(issue_log_info.get(i).getDetail(), Map.class);
            HouseQmCheckTaskIssueHistoryLogVo single_item = new HouseQmCheckTaskIssueHistoryLogVo();
            single_item.setUser_id(issue_log_info.get(i).getSenderId());
            single_item.setUser_name(user_id_real_name_map.get(issue_log_info.get(i).getSenderId()));
            single_item.setCreate_at(DateUtil.datetimeToTimeStamp(issue_log_info.get(i).getCreateAt()));
            List<HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem> items = Lists.newArrayList();
            if (issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.NoteNoAssign.getValue())) {
                hasCreateLog = true;
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Create.getValue());
                items.add(log_item);
                single_item.setItems(items);
                result.add(single_item);
                continue;

            }


            if (issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.Repairing.getValue())) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Assign.getValue());
                log_item.setTarget_user_id((Integer) issue_log_detail.get("RepairerId"));
                log_item.setTarget_user_name(user_id_real_name_map.get(issue_log_detail.get("RepairerId")));
                ArrayList<String> followers = Lists.newArrayList();
                if ((Integer) issue_log_detail.get("RepairerFollowerIds") > 0) {

                    List<Integer> followers_id = StringSplitToListUtil.splitToIdsComma((String) issue_log_detail.get("RepairerFollowerIds"), ",");
                    for (int j = 0; j < followers_id.size(); j++) {
                        followers.add(user_id_real_name_map.get(followers_id.get(j)));
                    }
                }
                HashMap<String, Object> log_data = Maps.newHashMap();
                log_data.put("plan_end_on", issue_log_detail.get("PlanEndOn"));
                log_data.put("followers", followers);
                log_item.setData(JSON.toJSONString(log_data));
                items.add(log_item);
                single_item.setItems(items);
                result.add(single_item);
                continue;
            }

            if (issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.ReformNoCheck.getValue())) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Repair.getValue());
                items.add(log_item);
                single_item.setItems(items);
                result.add(single_item);

                continue;
            }

            if (issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.CheckYes.getValue())) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Approve.getValue());
                items.add(log_item);
                single_item.setItems(items);
                result.add(single_item);
                continue;
            }
            if ((Integer) issue_log_detail.get("PlanEndOn") != -1 || (Integer) issue_log_detail.get("RepairerId") > 0 || (
                    !issue_log_detail.get("RepairerFollowerIds").equals("-1") &&
                            ((String) issue_log_detail.get("RepairerFollowerIds")).length() > 0)) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Assign.getValue());
                log_item.setTarget_user_id((Integer) issue_log_detail.get("RepairerId"));
                if ((Integer) issue_log_detail.get("RepairerId") > 0) {
                    log_item.setTarget_user_name(user_id_real_name_map.get(issue_log_detail.get("RepairerId")));
                } else {
                    log_item.setTarget_user_name("");
                }
                ArrayList<String> followers = Lists.newArrayList();
/*
                if (issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.Repairing.getValue())) {
*/
                if (!StringUtils.isEmpty((String) issue_log_detail.get("RepairerFollowerIds"))) {
                    List<Integer> followers_id = StringSplitToListUtil.splitToIdsComma((String) issue_log_detail.get("RepairerFollowerIds"), ",");
                    for (int j = 0; j < followers_id.size(); j++) {
                        if (user_id_real_name_map.containsKey(followers_id.get(j))) {
                            followers.add(user_id_real_name_map.get(followers_id.get(j)));
                        }

                    }
                    HashMap<String, Object> log_data = Maps.newHashMap();
                    log_data.put("plan_end_on", issue_log_detail.get("PlanEndOn"));
                    log_data.put("followers", followers);
                    log_item.setData(JSON.toJSONString(log_data));
                    items.add(log_item);
                    single_item.setItems(items);

                }
            }
            if (issue_log_info.get(i).getDesc().length() > 0) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_items = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_items.setLog_type(HouseQmCheckTaskActionLogType.AddDesc.getValue());
                items.add(log_items);
                single_item.setItems(items);

            }
            if (issue_log_info.get(i).getAttachmentMd5List().length() > 0) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_items = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_items.setLog_type(HouseQmCheckTaskActionLogType.AddAttachment.getValue());
                items.add(log_items);
                single_item.setItems(items);
            }
            if (items.size() > 0) {
                result.add(single_item);
            }
        }
        if (!hasCreateLog) {
            HouseQmCheckTaskIssueHistoryLogVo history_log = new HouseQmCheckTaskIssueHistoryLogVo();
            HouseQmCheckTaskIssue issue_infos = houseQmCheckTaskIssueService.selectByUuidAndNotDelete(issueUuid);
            Integer senderId = issue_info.getSenderId();
            User user_info = userService.selectByUserIdAndNotDelete(senderId);
            history_log.setUser_id(senderId);
            history_log.setUser_name(user_info.getRealName());
            history_log.setCreate_at(DateUtil.datetimeToTimeStamp(issue_infos.getCreateAt()));
            HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItem = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
            logItem.setLog_type(HouseQmCheckTaskActionLogType.Create.getValue());
            List<HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem> items = Lists.newArrayList();
            items.add(logItem);
            history_log.setItems(items);
            result.add(history_log);

        }

        return result;
    }

    @Override
    public void deleteHouseqmCheckTaskIssueByProjectAndUuid(Integer projectId, String issueUuid) {
        HouseQmCheckTaskIssue issue_info = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issue_info == null) {
            throw new LjBaseRuntimeException(432, "找不到此问题");
        }
        issue_info.setUpdateAt(new Date());
        issue_info.setDeleteAt(new Date());
        houseQmCheckTaskIssueService.update(issue_info);
    }

    @Override
    public LjBaseResponse updeteIssueDescByUuid(Integer projectId, String issueUuid, Integer uid, String content) {
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("找不到此问题");
            return response;
        }

        Integer taskId = issueInfo.getTaskId();
        UserInHouseQmCheckTask usersInfo = userInHouseQmCheckTaskService.selectByTaskIdAndUserIdAndNotDel(taskId, uid);

        if (usersInfo == null) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("只有任务参与人员才可以操作");
            return response;
        }
        if (issueInfo.getContent().equals(content)) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage("没有做改动");
            return response;
        }
        String[] split = issueInfo.getContent().replace(";;", ";").split(";");
        List<String> strings = Arrays.asList(split);
        strings.add(content);
        String contentNew = ";";
        for (int i = 0; i < strings.size(); i++) {
            contentNew += strings.get(i);
        }
        issueInfo.setUpdateAt(new Date());
        issueInfo.setContent(contentNew);

        houseQmCheckTaskIssueService.update(issueInfo);
        String uuid = UUID.randomUUID().toString().replace("-", "");


        HashMap<String, Object> logDetail = Maps.newHashMap();
        logDetail.put("PlanEndOn", -1);
        logDetail.put("EndOn", -1);
        logDetail.put("RepairerId", -1);
        logDetail.put("RepairerFollowerIds", "");
        logDetail.put("Condition", -1);
        logDetail.put("AreaId", -1);
        logDetail.put("PosX", -1);
        logDetail.put("PosY", -1);
        logDetail.put("Typ", -1);
        logDetail.put("Title", "");
        logDetail.put("CheckItemKey", "");
        logDetail.put("CategoryCls", -1);
        logDetail.put("CategoryKey", "");
        logDetail.put("DrawingMD5", "");
        logDetail.put("RemoveMemoAudioMd5List", "");
        logDetail.put("IssueReason", -1);
        logDetail.put("IssueReasonDetail", "");
        logDetail.put("IssueSuggest", "");
        logDetail.put("PotentialRisk", "");
        logDetail.put("PreventiveActionDetail", "");

        HouseQmCheckTaskIssueLog newIssueLog = new HouseQmCheckTaskIssueLog();
        newIssueLog.setProjectId(projectId);
        newIssueLog.setTaskId(taskId);
        newIssueLog.setUuid(uuid);
        newIssueLog.setIssueUuid(issueInfo.getUuid());
        newIssueLog.setSenderId(uid);
        newIssueLog.setDesc(content);
        newIssueLog.setStatus(HouseQmCheckTaskIssueLogStatus.UpdateIssueInfo.getValue());
        newIssueLog.setAttachmentMd5List("");
        newIssueLog.setAudioMd5List("");
        newIssueLog.setMemoAudioMd5List("");
        newIssueLog.setClientCreateAt(new Date());
        newIssueLog.setCreateAt(new Date());
        newIssueLog.setUpdateAt(new Date());
        newIssueLog.setDetail(JSON.toJSONString(logDetail));
        houseQmCheckTaskIssueLogService.add(newIssueLog);
        return new LjBaseResponse();
    }

    @Override
    public LjBaseResponse updateIssuePlanEndOnByProjectAndUuid(Integer projectId, String issueUuid, Integer uid, Integer plan_end_on) {
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("找不到此问题");
            return response;
        }
        if (DateUtil.datetimeToTimeStamp(issueInfo.getPlanEndOn())
                == plan_end_on) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage("没有改变");
            return response;
        }
        issueInfo.setPlanEndOn(DateUtil.transForDate(plan_end_on));
        issueInfo.setUpdateAt(new Date());
        houseQmCheckTaskIssueService.update(issueInfo);
        int status = -1;
        if (issueInfo.getStatus() == HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId()) {
            status = HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId();
        }
        HashMap<String, Object> detail = Maps.newHashMap();
        detail.put("PlanEndOn", -1);
        detail.put("EndOn", -1);
        detail.put("RepairerId", -1);
        detail.put("RepairerFollowerIds", "");
        detail.put("Condition", -1);
        detail.put("AreaId", -1);
        detail.put("PosX", -1);
        detail.put("PosY", -1);
        detail.put("Typ", -1);
        detail.put("Title", "");
        detail.put("CheckItemKey", "");
        detail.put("CategoryCls", -1);
        detail.put("CategoryKey", "");
        detail.put("DrawingMD5", "");
        detail.put("RemoveMemoAudioMd5List", "");
        detail.put("IssueReason", -1);
        detail.put("IssueReasonDetail", "");
        detail.put("IssueSuggest", "");
        detail.put("PotentialRisk", "");
        detail.put("PreventiveActionDetail", "");
        HouseQmCheckTaskIssueLog issueLog = new HouseQmCheckTaskIssueLog();
        issueLog.setProjectId(issueInfo.getProjectId());
        issueLog.setTaskId(issueInfo.getTaskId());
        issueLog.setUuid(UUID.randomUUID().toString().replace("-", ""));
        issueLog.setIssueUuid(issueInfo.getUuid());
        issueLog.setSenderId(uid);
        issueLog.setStatus(status);
        issueLog.setDesc("");
        issueLog.setAttachmentMd5List("");
        issueLog.setAudioMd5List("");
        issueLog.setMemoAudioMd5List("");
        issueLog.setClientCreateAt(new Date());
        issueLog.setCreateAt(new Date());
        issueLog.setUpdateAt(new Date());
        issueLog.setDetail(JSON.toJSONString(detail));
        houseQmCheckTaskIssueLogService.add(issueLog);
        return new LjBaseResponse();
    }

    @Override
    public LjBaseResponse updateIssueApproveStatusByUuid(Integer projectId, String issueUuid, Integer uid, Integer status, String content) {
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("找不到此问题");
            return response;
        }
        if (issueInfo.getStatus() == status) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage("没有更改");
            return response;
        }
        if (issueInfo.getTyp() == CheckTaskIssueType.Record.getValue()) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("状态为记录，不能更改");
            return response;
        }
        if (status.equals(CheckTaskIssueCheckStatus.CheckYes.getValue())) {
            if (issueInfo.getStatus().equals(CheckTaskIssueStatus.NoteNoAssign.getValue()) ||
                    issueInfo.getStatus().equals(CheckTaskIssueStatus.AssignNoReform.getValue())
            ) {
                issueInfo.setStatus(CheckTaskIssueStatus.CheckYes.getValue());
            }
        }
        if (status.equals(CheckTaskIssueCheckStatus.CheckNo.getValue())) {
            issueInfo.setStatus(CheckTaskIssueStatus.AssignNoReform.getValue());
        }
        issueInfo.setUpdateAt(new Date());
        issueInfo.setDestroyAt(new Date());
        issueInfo.setDestroyUser(uid);
        houseQmCheckTaskIssueService.update(issueInfo);
        HashMap<String, Object> logDetail = Maps.newHashMap();
        logDetail.put("PlanEndOn", -1);
        logDetail.put("EndOn", -1);
        logDetail.put("RepairerId", -1);
        logDetail.put("RepairerFollowerIds", "");
        logDetail.put("Condition", -1);
        logDetail.put("AreaId", -1);
        logDetail.put("PosX", -1);
        logDetail.put("PosY", -1);
        logDetail.put("Typ", -1);
        logDetail.put("Title", "");
        logDetail.put("CheckItemKey", "");
        logDetail.put("CategoryCls", -1);
        logDetail.put("CategoryKey", "");
        logDetail.put("DrawingMD5", "");
        logDetail.put("RemoveMemoAudioMd5List", "");
        logDetail.put("IssueReason", -1);
        logDetail.put("IssueReasonDetail", "");
        logDetail.put("IssueSuggest", "");
        logDetail.put("PotentialRisk", "");
        logDetail.put("PreventiveActionDetail", "");
        HouseQmCheckTaskIssueLog issueLog = new HouseQmCheckTaskIssueLog();
        issueLog.setProjectId(issueInfo.getProjectId());
        issueLog.setTaskId(issueInfo.getTaskId());
        issueLog.setUuid(UUID.randomUUID().toString().replace("-", ""));
        issueLog.setIssueUuid(issueInfo.getUuid());
        issueLog.setSenderId(uid);
        issueLog.setDesc(content);
        issueLog.setAttachmentMd5List("");
        issueLog.setAudioMd5List("");
        issueLog.setMemoAudioMd5List("");
        issueLog.setClientCreateAt(new Date());
        issueLog.setCreateAt(new Date());
        issueLog.setUpdateAt(new Date());
        issueLog.setDetail(JSON.toJSONString(logDetail));
        if (status.equals(CheckTaskIssueCheckStatus.CheckYes.getValue())) {
            issueLog.setStatus(CheckTaskIssueCheckStatus.CheckYes.getValue());
        }
        if (status.equals(CheckTaskIssueCheckStatus.CheckNo.getValue())) {
            issueLog.setStatus(CheckTaskIssueCheckStatus.CheckNo.getValue());
        }
        houseQmCheckTaskIssueLogService.add(issueLog);
        return new LjBaseResponse();
    }

    @Override
    public RepairNotifyExportVo repairNotifyExport2(Integer uid, Integer projectId, String issueUuid) {
        int result = 0;
        String message = "";
        ArrayList<ExportNotifyDetail2Vo> input = Lists.newArrayList();
        List<Integer> issueIds = StringSplitToListUtil.splitToIdsComma(issueUuid, ",");
        if (issueIds.size() == 0) {

        }
        ArrayList<Integer> statusList = Lists.newArrayList();
        statusList.add(CheckTaskIssueStatus.NoteNoAssign.getValue());
        statusList.add(CheckTaskIssueStatus.AssignNoReform.getValue());
        statusList.add(CheckTaskIssueStatus.ReformNoCheck.getValue());
        statusList.add(CheckTaskIssueStatus.CheckYes.getValue());
        List<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.selectHouseQmCheckTaskIssueByProIdAndIdAndStatus(projectId, issueIds, statusList);
        Collections.reverse(issueList);
        for (int i = 0; i < issueList.size(); i++) {
            ExportNotifyDetail2Vo vo = new ExportNotifyDetail2Vo();
            vo.setIssue_id(issueList.get(i).getId());
            vo.setContent(issueList.get(i).getContent());
            vo.setIssue_suggest((String) JSON.parseObject(issueList.get(i).getDetail(), Map.class).get("IssueSuggest"));
            input.add(vo);
        }
        String userName = "";
      /*  User user = UserDao().get(uid);
        if (user!=null){
             userName=user.getRealName();
        }else {
             userName="";
        }

      String  path = exportRepairNotify2(input, userName);
                String filename="整改通知单_ +"+DateUtil.getNowTimeStr("yyyy-MM-dd-HH-mm-ss")+"+.docx";
*/
        return null;
    }

    @Override
    public List<ProjectSettingV2> getProjectSettingId(Integer projectId) {
        return projectSettingV2Service.getProjectSettingId(projectId);

    }

    @Override
    public LjBaseResponse updateIssueRepairInfoByProjectAndUuid(Integer uid, Integer repairerId, String repairFollowerIds, Integer projectId, String issueUuid) {
        Integer tempRepairerId = 0;
        ArrayList<Integer> notifyUserIds = Lists.newArrayList();
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("找不到此问题");
            return response;
        }
        Integer status = -1;
        if (issueInfo.getStatus() == HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId() && repairerId > 0) {
            status = HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId();
            issueInfo.setStatus(CheckTaskIssueStatus.AssignNoReform.getValue());
        }
        List<String> followers = StringSplitToListUtil.removeStartAndEndStrAndSplit(repairFollowerIds, ",", ",");
        if (!followers.contains(issueInfo.getRepairerId()) && repairerId > 0 && !repairerId.equals(issueInfo.getRepairerId())) {
            followers.add(String.valueOf(issueInfo.getRepairerId()));
            tempRepairerId = issueInfo.getRepairerId();
        }
        if (followers.size() > 0) {
            List<String> strings = StringSplitToListUtil.removeStartAndEndStrAndSplit(StringUtils.join(followers, ","), ",", ",");
            Collections.replaceAll(strings, ",,", ",");
            repairFollowerIds = "," + strings.toString() + ",";
        }
        HashMap<String, Object> logDetail = Maps.newHashMap();
        logDetail.put("PlanEndOn", -1);
        logDetail.put("EndOn", -1);
        logDetail.put("RepairerId", -1);
        logDetail.put("RepairerFollowerIds", "");
        logDetail.put("Condition", -1);
        logDetail.put("AreaId", -1);
        logDetail.put("PosX", -1);
        logDetail.put("PosY", -1);
        logDetail.put("Typ", -1);
        logDetail.put("Title", "");
        logDetail.put("CheckItemKey", "-1");
        logDetail.put("CategoryCls", -1);
        logDetail.put("CategoryKey", "-1");
        logDetail.put("DrawingMD5", "-1");
        logDetail.put("RemoveMemoAudioMd5List", "-1");
        logDetail.put("IssueReason", -1);
        logDetail.put("IssueReasonDetail", "-1");
        logDetail.put("IssueSuggest", "-1");
        logDetail.put("PotentialRisk", "-1");
        logDetail.put("PreventiveActionDetail", "-1");
        if (repairerId.equals(issueInfo.getRepairerId()) && repairFollowerIds.equals(issueInfo.getRepairerFollowerIds())) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setMessage("没有改变");
            return response;
        }
        if (!issueInfo.getRepairerId().equals(repairerId)) {
            if (issueInfo.getRepairerId().equals(0)) {
                issueInfo.setStatus(HouseQmCheckTaskIssueLogStatus.AssignNoReform.getValue());
            }
            if (!issueInfo.getRepairerId().equals(0)) {
                issueInfo.setStatus(HouseQmCheckTaskIssueLogStatus.EditBaseInfo.getValue());
            }
            issueInfo.setRepairerId(repairerId);
            notifyUserIds.add(repairerId);
            logDetail.put("RepairerId", issueInfo.getRepairerId());
        }
        if (issueInfo.getRepairerId().equals(0)) {
            issueInfo.setStatus(CheckTaskIssueStatus.NoteNoAssign.getValue());
        }
        if (!issueInfo.getRepairerFollowerIds().equals(repairFollowerIds)) {
            // # 增加待办问题埋点
            List<Integer> oldRepairerFollowerIdList = StringSplitToListUtil.splitToIdsComma(issueInfo.getRepairerFollowerIds(), ",");
            List<Integer> userIds = StringSplitToListUtil.splitToIdsComma(issueInfo.getRepairerFollowerIds(), ",");

            for (int i = 0; i < userIds.size(); i++) {
                if (!oldRepairerFollowerIdList.contains(userIds.get(i)) && !userIds.get(i).equals(tempRepairerId)) {
                    notifyUserIds.add(userIds.get(i));
                }
            }
            logDetail.put("RepairerFollowerIds", repairFollowerIds);
            issueInfo.setRepairerFollowerIds(repairFollowerIds);

        }
        issueInfo.setUpdateAt(new Date());
        houseQmCheckTaskIssueService.update(issueInfo);
        //# 处理代办事项消息推送
        if (CollectionUtils.isNotEmpty(notifyUserIds)) {
            String title = "新的待处理问题";
            String msg = "您在［工程检查］有新的待处理问题，请进入App同步更新。";
            pushBaseMessage(issueInfo.getTaskId(), notifyUserIds, title, msg);

            // # 写入待办记录
        }
        HouseQmCheckTaskNotifyRecord record = new HouseQmCheckTaskNotifyRecord();
        record.setProjectId(issueInfo.getProjectId());
        record.setTaskId(issueInfo.getTaskId());
        record.setSrcUserId(0);
        record.setDesUserIds(StringUtils.join(followers, ","));
        record.setModuleId(ModuleInfoEnum.GCGL.getValue());
        record.setIssueId(issueInfo.getId());
        record.setIssueStatus(CheckTaskIssueStatus.NoteNoAssign.getValue());
        record.setExtraInfo("");

        int one = houseQmCheckTaskNotifyRecordService.add(record);
        if (one < 0) {
            log.info("insert new notify failed, data=" + JsonUtil.GsonString(record) + "");
        }
        HouseQmCheckTaskIssueLog houseQmCheckTaskIssueLog = new HouseQmCheckTaskIssueLog();
        houseQmCheckTaskIssueLog.setProjectId(issueInfo.getProjectId());
        houseQmCheckTaskIssueLog.setTaskId(issueInfo.getTaskId());
        houseQmCheckTaskIssueLog.setUuid(UUID.randomUUID().toString().replace("-", ""));
        houseQmCheckTaskIssueLog.setIssueUuid(issueInfo.getUuid());
        houseQmCheckTaskIssueLog.setSenderId(uid);
        houseQmCheckTaskIssueLog.setStatus(status);
        houseQmCheckTaskIssueLog.setAttachmentMd5List("");
        houseQmCheckTaskIssueLog.setAudioMd5List("");
        houseQmCheckTaskIssueLog.setMemoAudioMd5List("");
        houseQmCheckTaskIssueLog.setClientCreateAt(new Date());
        houseQmCheckTaskIssueLog.setCreateAt(new Date());
        houseQmCheckTaskIssueLog.setUpdateAt(new Date());
        houseQmCheckTaskIssueLog.setDetail(JSON.toJSONString(logDetail));
        houseQmCheckTaskIssueLog.setDesc("");
        houseQmCheckTaskIssueLogService.add(houseQmCheckTaskIssueLog);
        if (repairerId > 0) {
            HouseQmCheckTaskIssueUser repairerUserInfo = houseQmCheckTaskIssueUserService.selectByIssueUUidAndUserIdAndRoleTypeAndNotDel(issueInfo.getUuid(), repairerId, UserInIssueRoleType.Repairer.getValue());
            if (repairerUserInfo == null) {
                HouseQmCheckTaskIssueUser repairerUserInfos = new HouseQmCheckTaskIssueUser();
                repairerUserInfos.setTaskId(issueInfo.getTaskId());
                repairerUserInfos.setIssueUuid(issueInfo.getUuid());
                repairerUserInfos.setUserId(repairerId);
                repairerUserInfos.setRoleType(UserInIssueRoleType.Repairer.getValue());
                repairerUserInfos.setCreateAt(new Date());
                repairerUserInfos.setUpdateAt(new Date());
                houseQmCheckTaskIssueUserService.add(repairerUserInfos);
            } else {
                repairerUserInfo.setUpdateAt(new Date());
            }
            houseQmCheckTaskIssueUserService.update(repairerUserInfo);
        }
        List<HouseQmCheckTaskIssueUser> userFollowersInfo = null;
        ArrayList<Integer> intFollowers = null;
        if (followers.size() > 0) {
            intFollowers = Lists.newArrayList();
            for (int i = 0; i < followers.size(); i++) {
                intFollowers.add(Integer.valueOf(followers.get(i)));
            }
            userFollowersInfo = houseQmCheckTaskIssueUserService.selectByRoleTypeAndUserIdAndIssueUuid(UserInIssueRoleType.RepairerFollower.getValue(), intFollowers, issueInfo.getUuid());
        }
        ArrayList<HouseQmCheckTaskIssueUser> insertData = Lists.newArrayList();
        HashMap<Object, Object> baseData = Maps.newHashMap();
        baseData.put("task_id", issueInfo.getTaskId());
        baseData.put("issue_uuid", issueInfo.getUuid());
        baseData.put("role_type", UserInIssueRoleType.RepairerFollower.getValue());
        baseData.put("create_at", new Date());
        baseData.put("update_at", new Date());

        ArrayList<Integer> dataBaseFollowers = Lists.newArrayList();
        for (int i = 0; i < userFollowersInfo.size(); i++) {
            dataBaseFollowers.add(userFollowersInfo.get(i).getUserId());
            userFollowersInfo.get(i).setUpdateAt(new Date());
            houseQmCheckTaskIssueUserService.update(userFollowersInfo.get(i));
        }
        for (int i = 0; i < intFollowers.size(); i++) {
            if (!dataBaseFollowers.contains(intFollowers.get(i))) {
                baseData.put("user_id", intFollowers.get(i));
                HouseQmCheckTaskIssueUser houseQmCheckTaskIssueUser = JsonUtil.GsonToBean(JSON.toJSONString(baseData), HouseQmCheckTaskIssueUser.class);

                insertData.add(houseQmCheckTaskIssueUser);
            }
        }
        if (CollectionUtils.isNotEmpty(insertData)) {
            houseQmCheckTaskIssueUserService.insertMany(insertData);

        }

        return new LjBaseResponse();
    }

    @Override
    public LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> getDetailRepairLogByIssueUuid(String issueUuid) {
        HouseQmCheckTaskIssue issueInfo = houseQmCheckTaskIssueService.selectByUuidAndNotDelete(issueUuid);
        if (issueInfo == null) {
            throw new LjBaseRuntimeException(272, "找不到此问题");
        }
        ArrayList<Integer> issueLogStatus = Lists.newArrayList();
        issueLogStatus.add(HouseQmCheckTaskIssueLogStatus.ReformNoCheck.getValue());
        issueLogStatus.add(HouseQmCheckTaskIssueLogStatus.Repairing.getValue());
        List<HouseQmCheckTaskIssueDetailRepairLogVo> result = Lists.newArrayList();
        int one = houseQmCheckTaskIssueLogService.selectByIssueUuIdAndStatusNotDelAndCount(issueUuid, issueLogStatus);
        if (one < 1) {
            LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage("找不到此问题");
            return response;

        }
        List<HouseQmCheckTaskIssueLog> issueLogInfo = houseQmCheckTaskIssueLogService.selectByIssueUuIdAndStatusNotDel(issueUuid, issueLogStatus);
        ArrayList<Integer> issueLogUsersId = Lists.newArrayList();
        for (int i = 0; i < issueLogInfo.size(); i++) {
            Integer senderId = issueLogInfo.get(i).getSenderId();
            issueLogUsersId.add(senderId);
        }
        List<User> userInfo = userService.searchByUserIdInAndNoDeleted(issueLogUsersId);
        HashMap<Integer, String> userIdRealNameMap = Maps.newHashMap();
        for (int i = 0; i < userInfo.size(); i++) {
            userIdRealNameMap.put(userInfo.get(i).getUserId(), userInfo.get(i).getRealName());
        }
        for (int i = 0; i < issueLogInfo.size(); i++) {
            HouseQmCheckTaskIssueDetailRepairLogVo single = new HouseQmCheckTaskIssueDetailRepairLogVo();
            single.setContent(issueLogInfo.get(i).getDesc());
            single.setUser_id(issueLogInfo.get(i).getSenderId());
            single.setCreate_at(DateUtil.datetimeToTimeStamp(issueLogInfo.get(i).getCreateAt()));
            if (issueLogInfo.get(i).getAttachmentMd5List().length() > 0) {
                List<String> attachmentMdeList = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueLogInfo.get(i).getAttachmentMd5List(), ",", ",");
                single.setAttachment_md5_list(attachmentMdeList.toString());
            } else {
                single.setAttachment_md5_list("");
            }
            single.setUser_name(userIdRealNameMap.get(issueLogInfo.get(i).getSenderId()));
            result.add(single);
        }
        LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> response = new LjBaseResponse<>();
        response.setData(result);
        return response;
    }

    @Override
    public LjBaseResponse<IssueInfoVo> getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(Integer uid, Integer projectId, String issueUuid) {
        Date limitTime = new Date(0);
        HouseQmCheckTaskIssue issueInfo = houseQmCheckTaskIssueService.getIssueByProjectIdAndUuid(projectId, issueUuid);
        Map issueDetail = JSON.parseObject(issueInfo.getDetail(), Map.class);
        if (issueInfo == null) {
            LjBaseResponse<IssueInfoVo> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("找不到此问题");
            return response;
        }
        Integer taskId = issueInfo.getTaskId();
        List<String> attachmentMd5List = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getAttachmentMd5List(), ",", ",");
        HouseQmCheckTask taskInfo = houseQmCheckTaskService.selectByProjectIdAndTaskIdAndDel(projectId, taskId);
        if (taskInfo == null) {
            LjBaseResponse<IssueInfoVo> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("问题数据错误");
            return response;
        }
        String taskName = taskInfo.getName();
        ArrayList<Object> categoryNames = Lists.newArrayList();
        if (!issueInfo.getCategoryPathAndKey().equals("/")) {
            List<String> categoryKeys = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getCategoryPathAndKey(), "/", "/");
            List<CategoryV3> categoryInfo = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeys);
            HashMap<String, String> categoryKeyNameMap = Maps.newHashMap();
            for (int i = 0; i < categoryInfo.size(); i++) {
                categoryKeyNameMap.put(categoryInfo.get(i).getKey(), categoryInfo.get(i).getName());
            }
            for (int i = 0; i < categoryKeys.size(); i++) {
                categoryNames.add(categoryKeyNameMap.get(categoryKeys.get(i)));
            }
        }
        ArrayList<Object> checkItemNames = Lists.newArrayList();
        if (!issueInfo.getCheckItemPathAndKey().equals("/")) {
            List<String> checkItemKeys = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getCheckItemPathAndKey(), "/", "/");
            List<CheckItemV3> checkItemInfo = checkItemV3Service.searchCheckItemyV3ByKeyInAndNoDeleted(checkItemKeys);
            HashMap<String, String> checkItemKeyNameMap = Maps.newHashMap();
            for (int i = 0; i < checkItemInfo.size(); i++) {
                checkItemKeyNameMap.put(checkItemInfo.get(i).getKey(), checkItemInfo.get(i).getName());
            }
            for (int i = 0; i < checkItemKeys.size(); i++) {
                checkItemNames.add(checkItemKeyNameMap.get(checkItemKeys.get(i)));
            }
        }
        ArrayList<Object> areaNames = Lists.newArrayList();
        if (!issueInfo.getAreaPathAndId().equals("/")) {
            List<String> areaIds = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getAreaPathAndId().replace("//", "/"), "/", "/");
            ArrayList<Integer> areaIdsList = Lists.newArrayList();
            for (int i = 0; i < areaIds.size(); i++) {
                areaIdsList.add(Integer.valueOf(areaIds.get(i)));
            }
            List<Area> areaInfo = areaService.selectByAreaIds(areaIdsList);
            HashMap<Integer, String> areaIdNameMap = Maps.newHashMap();
            for (int i = 0; i < areaInfo.size(); i++) {
                areaIdNameMap.put(areaInfo.get(i).getId(), areaInfo.get(i).getName());
            }
            for (int i = 0; i < areaIdsList.size(); i++) {
                areaNames.add(areaIdNameMap.get(areaIdsList.get(i)));
            }
        }
        ArrayList<Object> allUserId = Lists.newArrayList();
        if (issueInfo.getRepairerId() > 0) {
            allUserId.add(issueInfo.getRepairerId());
        }
        if (issueInfo.getSenderId() > 0) {
            allUserId.add(issueInfo.getSenderId());
        }
        List<Integer> repairerFollowerIds = null;
        if (!StringUtils.isEmpty(issueInfo.getRepairerFollowerIds())) {
            if (issueInfo.getRepairerFollowerIds().length() > 0) {
                repairerFollowerIds = StringSplitToListUtil.splitToIdsComma(issueInfo.getRepairerFollowerIds(), ",");
                if (repairerFollowerIds.contains(0)) {
                    repairerFollowerIds.remove(0);
                }
                if (repairerFollowerIds.size() > 0) {
                    for (int i = 0; i < repairerFollowerIds.size(); i++) {
                        allUserId.add(repairerFollowerIds.get(i));
                    }

                }
            }
        }
        List allUserIdList = CollectionUtil.removeDuplicate(allUserId);
        List<User> allUserInfo = userService.searchByUserIdInAndNoDeleted(allUserIdList);
        HashMap<Object, Object> userIdRealNameMap = Maps.newHashMap();
        for (int i = 0; i < allUserInfo.size(); i++) {
            userIdRealNameMap.put(allUserInfo.get(i).getUserId(), allUserInfo.get(i).getRealName());
        }
        List<HouseQmCheckTaskIssueAttachment> myAudiosInfo = houseQmCheckTaskIssueAttachmentService.selectByissueUuidAnduserIdAndpublicTypeAndattachmentTypeAndNotDel(issueUuid, uid, CheckTaskIssueAttachmentPublicType.Private.getValue(), CheckTaskIssueAttachmentAttachmentType.Audio.getValue());
        ArrayList<IssueInfoVo.HouseQmCheckTaskIssueAttachmentVo> myAudios = Lists.newArrayList();
        for (int i = 0; i < myAudiosInfo.size(); i++) {
            IssueInfoVo.HouseQmCheckTaskIssueAttachmentVo houseQmCheckTaskIssueAttachment = new IssueInfoVo().new HouseQmCheckTaskIssueAttachmentVo();
            houseQmCheckTaskIssueAttachment.setMd5(myAudiosInfo.get(i).getMd5());
            houseQmCheckTaskIssueAttachment.setTyp(CheckTaskIssueAttachmentAttachmentType.Audio.getValue());
            houseQmCheckTaskIssueAttachment.setCreate_at(DateUtil.datetimeToTimeStamp(myAudiosInfo.get(i).getClientCreateAt()));
            myAudios.add(houseQmCheckTaskIssueAttachment);
        }
        ArrayList<IssueInfoVo.HouseQmCheckTaskIssueAttachmentVo> audios = Lists.newArrayList();
        List<HouseQmCheckTaskIssueAttachment> audiosInfo = houseQmCheckTaskIssueAttachmentService.selectByIssueUuidAndpublicTypeAndattachmentTypeAndNotDel(issueUuid, CheckTaskIssueAttachmentPublicType.Public.getValue(), CheckTaskIssueAttachmentAttachmentType.Audio.getValue());
        for (int i = 0; i < audiosInfo.size(); i++) {
            IssueInfoVo.HouseQmCheckTaskIssueAttachmentVo houseQmCheckTaskIssueAttachment = new IssueInfoVo().new HouseQmCheckTaskIssueAttachmentVo();
            houseQmCheckTaskIssueAttachment.setMd5(audiosInfo.get(i).getMd5());
            houseQmCheckTaskIssueAttachment.setTyp(CheckTaskIssueAttachmentAttachmentType.Audio.getValue());
            houseQmCheckTaskIssueAttachment.setCreate_at(DateUtil.datetimeToTimeStamp(audiosInfo.get(i).getClientCreateAt()));
            audios.add(houseQmCheckTaskIssueAttachment);
        }
        HashMap<String, Object> detailMap = Maps.newHashMap();
        detailMap.put("issue_reason",issueDetail.get("IssueReason"));
        detailMap.put("issue_reason_detail",issueDetail.get("IssueReasonDetail"));
        detailMap.put("issue_suggest",issueDetail.get("IssueSuggest"));
        detailMap.put("potential_risk",issueDetail.get("PotentialRisk"));
        detailMap.put("preventive_action_detail",issueDetail.get("PreventiveActionDetail"));
        ArrayList<IssueInfoVo.HouseQmCheckTaskIssueDetailEditLog> editLogs = Lists.newArrayList();

        //  # 为什么是-1？不知道，照着之前能到写的
        ArrayList<Integer> logStatus = Lists.newArrayList();
        for (int i = -1; i <= HouseQmCheckTaskIssueLogStatus.EditBaseInfo.getValue(); i++) {
            logStatus.add(i);
        }
        List<HouseQmCheckTaskIssueLog> issueLogInfo = houseQmCheckTaskIssueLogService.selectByIssueUuIdAndStatusNotDel(issueUuid, logStatus);
        ArrayList<Integer> logUserIds = Lists.newArrayList();
        for (int i = 0; i < issueLogInfo.size(); i++) {
            logUserIds.add(issueLogInfo.get(i).getSenderId());
        }

        List<User> logUserInfo = userService.searchByUserIdInAndNoDeleted(logUserIds);
        HashMap<Object, Object> logUserIdNameMap = Maps.newHashMap();
        for (int i = 0; i < logUserInfo.size(); i++) {
            logUserIdNameMap.put(logUserInfo.get(i).getUserId(), logUserInfo.get(i).getRealName());
        }
        for (int i = 0; i < issueLogInfo.size(); i++) {
            if (issueLogInfo.get(i).getDesc().length() < 1 && issueLogInfo.get(i).getAttachmentMd5List().length() < 1) {
                IssueInfoVo.HouseQmCheckTaskIssueDetailEditLog singleLog = new IssueInfoVo().new HouseQmCheckTaskIssueDetailEditLog();
                singleLog.setContent(issueLogInfo.get(i).getDesc());
                singleLog.setUser_id(issueLogInfo.get(i).getSenderId());
                singleLog.setCreate_at(DateUtil.datetimeToTimeStamp(issueLogInfo.get(i).getCreateAt()));
                if (issueLogInfo.get(i).getAttachmentMd5List().length() > 0) {
                    singleLog.setAttachment_md5_list(StringSplitToListUtil.removeStartAndEndStrAndSplit(issueLogInfo.get(i).getAttachmentMd5List(), ",", ","));
                }
                singleLog.setUser_name((String) logUserIdNameMap.get(issueLogInfo.get(i).getSenderId()));
                editLogs.add(singleLog);
            }
        }
        int planEndOn = -1;
        if (DateUtil.datetimeToTimeStamp(issueInfo.getPlanEndOn()) > DateUtil.datetimeToTimeStamp(limitTime)) {
            planEndOn = DateUtil.datetimeToTimeStamp(issueInfo.getPlanEndOn());
        }
        IssueInfoVo vo = new IssueInfoVo();
        vo.setTask_id(taskId);
        vo.setTask_name(taskName);
        vo.setCreate_at(DateUtil.datetimeToTimeStamp(issueInfo.getCreateAt()));
        vo.setCategory_path_names(categoryNames);
        vo.setCheck_item_path_names(checkItemNames);
        vo.setArea_path_names(areaNames);
        vo.setIssue_type(issueInfo.getTyp());
        vo.setPos_x(issueInfo.getPosX());
        vo.setPos_y(issueInfo.getPosY());
        vo.setDrawing_md5(issueInfo.getDrawingMD5());
        vo.setAttachment_md5_list(attachmentMd5List);
        vo.setClose_status(0);
        vo.setRepair_follower_ids(repairerFollowerIds);
        vo.setRepairer_id(issueInfo.getRepairerId());
        vo.setSender_name((String) userIdRealNameMap.get(issueInfo.getSenderId()));
        vo.setPlan_end_on(planEndOn);
        vo.setContent(issueInfo.getContent());
        vo.setAudios(audios);
        vo.setMy_audio(myAudios);
        vo.setEdit_logs(editLogs);
        vo.setDetail(detailMap);
        vo.setStatus(issueInfo.getStatus());
        LjBaseResponse<IssueInfoVo> response = new LjBaseResponse<>();
        response.setData(vo);
        return response;
    }


    private void pushBaseMessage(Integer taskId, ArrayList<Integer> notifyUserIds, String title, String msg) {
        ArrayList<String> alias = Lists.newArrayList();
        for (int i = 0; i < notifyUserIds.size(); i++) {
            alias.add("user_id_" + ENTERPRISEID + "_" + notifyUserIds.get(i) + "");
        }

        String alia = StringUtils.join(alias, ",");
        UmPushUtil.sendAndroidCustomizedcast(APP_KEY_ANDROID, APP_MASTER_SECRET_ANDROID,
                alia, AppPlatformTypeEnum.PUSH_PLATFORM_UMENG_ANDROID.getValue(),
                "Android", title, msg, msg, String.valueOf(taskId));
        UmPushUtil.sendIOSCustomizedcast(APP_KEY_IOS, APP_MASTER_SECRET_IOS, alia,
                AppPlatformTypeEnum.PUSH_PLATFORM_UMENG_IOS.getValue(), msg, String.valueOf(taskId));
        XmPushUtil.sendMessageToUserAccounts(APP_SECRET_XIAO_MI, PACKAGE_NAME_XIAO_MI, title, msg, alias);
    }


    private HouseQmCheckTaskIssue getIssueByProjectIdAndUuid(Integer projectId, String issueUuid) {
        return houseQmCheckTaskIssueService.getIssueByProjectIdAndUuid(projectId, issueUuid);
    }


    /**
     * @param categoryKeys
     * @return java.util.Map<java.lang.String       ,       com.longfor.longjian.houseqm.po.zj2db.CategoryV3>
     * @author hy
     * @date 2018/12/21 0021
     */
    private Map<String, CategoryV3> createCategoryKeyMap(List<String> categoryKeys) {
        Map<String, CategoryV3> categoryMap = Maps.newHashMap();
        if (categoryKeys.isEmpty()) return categoryMap;
        List<CategoryV3> categoryV3s = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeys);
        categoryV3s.forEach(item -> {
            categoryMap.put(item.getKey(), item);
        });
        return categoryMap;
    }

    /**
     * @param checkItems
     * @return java.util.Map<java.lang.String       ,       com.longfor.longjian.houseqm.po.zj2db.CheckItemV3>
     * @author hy
     * @date 2018/12/21 0021
     */
    private Map<String, CheckItemV3> createCheckItemMap(List<String> checkItems) {
        Map<String, CheckItemV3> checkItemMap = Maps.newHashMap();
        if (checkItems.isEmpty()) return checkItemMap;
        List<CheckItemV3> checkItemV3s = checkItemV3Service.searchCheckItemyV3ByKeyInAndNoDeleted(checkItems);
        checkItemV3s.forEach(item -> {
            checkItemMap.put(item.getKey(), item);
        });
        return checkItemMap;
    }

    /**
     * @param repairers
     * @return java.util.Map<java.lang.Integer       ,       com.longfor.longjian.houseqm.po.zhijian2_apisvr.User>
     * @author hy
     * @date 2018/12/21 0021
     */
    private Map<Integer, User> createUserMap(List<Integer> repairers) {
        Map<Integer, User> userMap = Maps.newHashMap();
        if (repairers.isEmpty()) return userMap;
        List<User> users = userService.searchByUserIdInAndNoDeleted(repairers);
        users.forEach(item -> {
            userMap.put(item.getUserId(), item);
        });
        return userMap;
    }

    /**
     * @param areaPaths
     * @return java.util.Map<java.lang.Integer       ,       com.longfor.longjian.houseqm.po.zj2db.Area>
     * @author hy
     * @date 2018/12/21 0021
     */
    private Map<Integer, Area> createAreaMap(List<Integer> areaPaths) {
        Map<Integer, Area> areaMap = Maps.newHashMap();
        if (areaPaths.isEmpty()) return areaMap;
        List<Area> areas = areaService.searchAreaByIdInAndNoDeleted(areaPaths);
        areas.forEach(item -> {
            areaMap.put(item.getId(), item);
        });
        return areaMap;
    }

    /**
     * @param attachments
     * @return java.util.Map<java.lang.String       ,       com.longfor.longjian.houseqm.po.zj2db.FileResource>
     * @author hy
     * @date 2018/12/21 0021
     */
    private Map<String, FileResource> createAttachmentMap(List<String> attachments) {
        Map<String, FileResource> fileResourceMap = Maps.newHashMap();
        if (attachments.isEmpty()) return fileResourceMap;
        List<FileResource> fileResources = fileResourceService.searchFileResourceByFileMd5InAndNoDeleted(attachments);
        fileResources.forEach(item -> {
            fileResourceMap.put(item.getFileMd5(), item);
        });
        return fileResourceMap;
    }

    /**
     * @param map
     * @param areaPathAndId
     * @return java.util.List<java.lang.String>
     * @author hy
     * @date 2018/12/21 0021
     */
    private List<String> getAreaPathName(Map<Integer, Area> map, String areaPathAndId) {
        List<String> areaNames = Lists.newArrayList();
        List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(areaPathAndId, "/");
        areaIds.forEach(id -> {
            String areaName = id + "";
            if (map.containsKey(id)) {
                areaName = map.get(id).getName();

            }
            areaNames.add(areaName);
        });
        return areaNames;
    }

    /**
     * @param checkItemMap
     * @param pathAndKey
     * @return java.util.List<java.lang.String>
     * @author hy
     * @date 2018/12/21 0021
     */
    private List<String> getCheckItemPathName(Map<String, CheckItemV3> checkItemMap, String pathAndKey) {
        List<String> pathNames = Lists.newArrayList();
        List<String> paths = StringSplitToListUtil.splitToStringComma(pathAndKey, "/");
        paths.forEach(path -> {
            String pathName = path;
            if (checkItemMap.containsKey(path)) {
                pathName = checkItemMap.get(path).getName();
            }
            pathNames.add(pathName);
        });
        return pathNames;
    }

    private List<String> getCategoryPathName(Map<String, CategoryV3> category_map, String pathAndKey) {
        List<String> pathNames = Lists.newArrayList();
        List<String> paths = StringSplitToListUtil.splitToStringComma(pathAndKey, "/");
        paths.forEach(path -> {
            String pathName = path;
            if (category_map.containsKey(path)) {
                pathName = category_map.get(path).getName();
            }
            pathNames.add(pathName);
        });
        return pathNames;
    }

}

