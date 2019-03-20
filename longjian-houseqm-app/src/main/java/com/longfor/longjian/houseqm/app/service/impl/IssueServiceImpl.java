package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.*;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.push.UmPushUtil;
import com.longfor.longjian.common.push.xiaomi.XmPushUtil;
import com.longfor.longjian.houseqm.app.req.IssueListDoActionReq;
import com.longfor.longjian.houseqm.app.req.bgtask.ExportBuildingExcelReq;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.utils.DocumentHandler;
import com.longfor.longjian.houseqm.app.utils.ExportUtils;
import com.longfor.longjian.houseqm.app.utils.FileUtil;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.issuelist.ExcelIssueData;
import com.longfor.longjian.houseqm.app.vo.issuelist.IssueListRsp;
import com.longfor.longjian.houseqm.consts.AppPlatformTypeEnum;
import com.longfor.longjian.houseqm.consts.CommonGlobalEnum;
import com.longfor.longjian.houseqm.consts.HouseQmUserInIssueRoleTypeEnum;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import com.longfor.longjian.houseqm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Houyan
 * @date 2018/12/21 0021 11:42
 */
@Repository
@Service
@Slf4j
public class IssueServiceImpl implements IIssueService {
    private static final String ILLEGAL_CHARACTERS_RE = "[\\000-\\010]|[\\013-\\014]|[\\016-\\037]|\\xef|\\xbf";
    private static final String CAN_NOT_FIND_THIS_ISSUE = "找不到此问题";
    private static final String NO_CHANGES = "没有改变";
    private static final String ISSUE_REASON = "IssueReason";
    private static final String ISSUE_REASON_DETAIL = "IssueReasonDetail";
    private static final String ISSUE_SUGGEST = "IssueSuggest";
    private static final String POTENTIAL_RISK = "PotentialRisk";
    private static final String PREVENTIVE_ACTION_DETAIL = "PreventiveActionDetail";
    private static final String PLAN_END_ON = "planEndOn";
    private static final String END_ON = "EndOn";
    private static final String REPAIRER_ID = "RepairerId";
    private static final String REPAIRER_FOLLOWER_IDS = "RepairerFollowerIds";
    private static final String CONDITION = "Condition";
    private static final String AREA_ID = "AreaId";
    private static final String TITLE = "Title";
    private static final String CHECK_ITEM_KEY = "CheckItemKey";
    private static final String CATEGORY_CLS = "CategoryCls";
    private static final String CATEGORY_KEY = "CategoryKey";
    private static final String DRAWING_MD5 = "DrawingMD5";
    private static final String REMOVE_MEMO_AUDIO_MD5_LIST = "RemoveMemoAudioMd5List";
    private static final String CONTENT = "content";
    @Value("${push_config.enterprise_id}")
    private String enterpriseId;
    @Value("${push_config.gcgl.app_key_android}")
    private String appKeyAndroid;
    @Value("${push_config.gcgl.app_master_secret_android}")
    private String appMasterSecretAndroid;
    @Value("${push_config.gcgl.app_key_ios}")
    private String appKeyIOS;
    @Value("${push_config.gcgl.app_master_secret_ios}")
    private String appMasterSecretIOS;
    @Value("${push_config.gcgl.app_secret_xiao_mi}")
    private String appSecretXiaoMi;
    @Value("${push_config.gcgl.package_name_xiao_mi}")
    private String packageNameXiaomi;
    @Resource
    private HouseQmCheckTaskIssueAttachmentService houseQmCheckTaskIssueAttachmentService;
    @Resource
    private HouseQmCheckTaskIssueUserService houseQmCheckTaskIssueUserService;
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
    @Resource
    private ProjectSettingService projectSettingService;
    @Resource
    private ProjectService projectService;
    @Value("${env_info.host_list}")
    private String envInfo;

    @Override
    public LjBaseResponse<Object> updateIssueDetailByProjectAndUuid(Integer userId, Integer projectId, String issueUuid, Integer typ, String data) {
        LjBaseResponse<Object> result = new LjBaseResponse<>();
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            result.setResult(1);
            result.setMessage(CAN_NOT_FIND_THIS_ISSUE);
            return result;
        }
        int reason = -1;
        String issueReasonDetail = "-1";
        String issueSuggest = "-1";
        String potentialRisk = "-1";
        String preventiveActionDetail = "-1";

        int detailTypeReason = 1;
        int detailTypeReasonDetail = 2;
        int detailTypeSuggest = 3;
        int detailTypePotentialRisk = 4;
        int detailTypePreventiveActionDetail = 5;

        Map<String, Object> issueDetail = JSON.parseObject(issueInfo.getDetail(), Map.class);

        if (typ == detailTypeReason) {
            reason = Integer.parseInt(data);
            issueDetail.put(ISSUE_REASON, reason);
        }
        if (typ == detailTypeReasonDetail) {
            issueReasonDetail = data;
            issueDetail.put(ISSUE_REASON_DETAIL, data);
        }
        if (typ == detailTypeSuggest) {
            issueSuggest = data;
            issueDetail.put(ISSUE_SUGGEST, data);
        }
        if (typ == detailTypePotentialRisk) {
            potentialRisk = data;
            issueDetail.put(POTENTIAL_RISK, data);
        }
        if (typ == detailTypePreventiveActionDetail) {
            preventiveActionDetail = data;
            issueDetail.put(PREVENTIVE_ACTION_DETAIL, data);
        }
        issueInfo.setDetail(JSON.toJSONString(issueDetail));
        issueInfo.setUpdateAt(new Date());
        houseQmCheckTaskIssueService.update(issueInfo);

        Map<String, Object> logDetail = Maps.newHashMap();
        initLogDetail(logDetail);

        logDetail.put(ISSUE_REASON, reason);
        logDetail.put(ISSUE_REASON_DETAIL, issueReasonDetail);
        logDetail.put(ISSUE_SUGGEST, issueSuggest);
        logDetail.put(POTENTIAL_RISK, potentialRisk);
        logDetail.put(PREVENTIVE_ACTION_DETAIL, preventiveActionDetail);

        int status = HouseQmCheckTaskIssueLogStatus.EditBaseInfo.getValue();
        HouseQmCheckTaskIssueLog newIssueLog = new HouseQmCheckTaskIssueLog();
        newIssueLog.setTaskId(issueInfo.getTaskId());
        newIssueLog.setProjectId(issueInfo.getProjectId());
        newIssueLog.setUuid(UUID.randomUUID().toString().replace("-", ""));
        newIssueLog.setIssueUuid(issueInfo.getUuid());
        newIssueLog.setSenderId(userId);
        newIssueLog.setStatus(status);
        newIssueLog.setAttachmentMd5List("");
        newIssueLog.setAudioMd5List("");
        newIssueLog.setMemoAudioMd5List("");
        newIssueLog.setClientCreateAt(new Date());
        newIssueLog.setCreateAt(new Date());
        newIssueLog.setUpdateAt(new Date());
        newIssueLog.setDesc("");

        newIssueLog.setDetail(JSON.toJSONString(logDetail));
        houseQmCheckTaskIssueLogService.add(newIssueLog);
        result.setMessage("success");
        result.setResult(0);
        return result;
    }

    private void initLogDetail(Map<String, Object> logDetail) {
        logDetail.put(PLAN_END_ON, -1);
        logDetail.put(END_ON, -1);
        logDetail.put(REPAIRER_ID, -1);
        logDetail.put(REPAIRER_FOLLOWER_IDS, "");
        logDetail.put(CONDITION, -1);
        logDetail.put(AREA_ID, -1);
        logDetail.put("PosX", -1);
        logDetail.put("PosY", -1);
        logDetail.put("Typ", -1);
        logDetail.put(TITLE, "");
        logDetail.put(CHECK_ITEM_KEY, "");
        logDetail.put(CATEGORY_CLS, -1);
        logDetail.put(CATEGORY_KEY, "");
        logDetail.put(DRAWING_MD5, "");
        logDetail.put(REMOVE_MEMO_AUDIO_MD5_LIST, "");
    }

    @Override
    @SuppressWarnings("squid:S3776")
    public Map<String, Object> exportExcel(Integer uid, ExportBuildingExcelReq req) {

        //准备数据
        List<Integer> areaIdList = com.longfor.longjian.common.util.StringUtil.strToInts(req.getArea_ids(), ",");
        List<Integer> statusInList = com.longfor.longjian.common.util.StringUtil.strToInts(req.getStatus_in(), ",");
        Map<String, Object> condiMap = Maps.newHashMap();
        condiMap.put("projectId", req.getProject_id());
        condiMap.put("categoryCls", req.getCategory_cls());
        if (req.getTask_id() == null) req.setTask_id(0);
        if (req.getTask_id() != null && req.getTask_id() > 0) condiMap.put("taskId", req.getTask_id());
        if (req.getStatus_in().length() > 0) condiMap.put("status", statusInList);
        if (req.getCategory_key().length() > 0) condiMap.put("categoryPathAndKey", "%/" + req.getCategory_key() + "/%");
        if (req.getCheck_item_key().length() > 0) condiMap.put("checkItemKey", req.getCheck_item_key());
        if (!areaIdList.isEmpty()) {
            List<String> areaPathAndIdLikeList = Lists.newArrayList();
            for (Integer i : areaIdList) {
                areaPathAndIdLikeList.add("%/" + i + "/%");
            }
            condiMap.put("areaPathAndId", areaPathAndIdLikeList);
        }
        if (req.getType() == null) req.setType(0);
        if (req.getType() != null && req.getType() > 0) condiMap.put("type", req.getType());
        if (req.getCondition() == null) req.setCondition(0);
        if (req.getCondition() != null && req.getCondition() > 0) condiMap.put("condition", req.getCondition());
        if (req.getChecker_id() == null) req.setChecker_id(0);
        if (req.getChecker_id() != null && req.getChecker_id() > 0) condiMap.put("senderId", req.getChecker_id());
        if (req.getRepairer_id() == null) req.setRepairer_id(0);
        if (req.getRepairer_id() != null && req.getRepairer_id() > 0) condiMap.put("repairerId", req.getRepairer_id());
        if (req.getCreate_on_begin().length() > 0)
            condiMap.put("clientCreateAtGte", req.getCreate_on_begin() + " 00:00:00");
        if (req.getCreate_on_end().length() > 0)
            condiMap.put("clientCreateAtLte", req.getCreate_on_end() + " 23:59:59");
        if (req.getIs_overdue() == null) req.setIs_overdue(false);
        if (req.getIs_overdue()) {
            List<Integer> status1 = Lists.newArrayList();
            List<Integer> status2 = Lists.newArrayList();
            status1.add(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId());
            status1.add(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
            condiMap.put("status1", status1);
            status2.add(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());
            status2.add(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
            condiMap.put("status2", status2);
        }
        if (req.getKey_word().length() > 0) {//content like xxx
            condiMap.put(CONTENT, "%/" + req.getKey_word() + "/%");
            if (StringSplitToListUtil.isInteger(req.getKey_word())) {// or id=xxx
                condiMap.put("id", req.getKey_word());
            }
        }
        condiMap.put("deleted", "false");
        condiMap.put("reverse", true);
        List<HouseQmCheckTaskIssue> validIssues = houseQmCheckTaskIssueService.searchByProjectIdAndCategoryClsAndNoDeletedAndDongTai(condiMap);
        List<Integer> taskIds = Lists.newArrayList();
        List<String> categoryKeys = Lists.newArrayList();
        List<Integer> userIds = Lists.newArrayList();
        List<String> checkItems = Lists.newArrayList();
        List<Integer> areaPaths = Lists.newArrayList();

        for (HouseQmCheckTaskIssue issue : validIssues) {
            if (!taskIds.contains(issue.getTaskId())) {
                taskIds.add(issue.getTaskId());
            }
            Integer senderId = issue.getSenderId();
            if (senderId != null && senderId != 0 && !userIds.contains(senderId)) userIds.add(senderId);
            Integer repairerId = issue.getRepairerId();
            if (repairerId != null && repairerId != 0 && !userIds.contains(repairerId)) userIds.add(repairerId);
            String followerIds = issue.getRepairerFollowerIds();
            if (followerIds != null) {
                List<Integer> followerIdsList = StringUtil.strToInts(followerIds, ",");
                for (Integer item : followerIdsList) {
                    if (!userIds.contains(item)) userIds.add(item);
                }
            }
            Integer assigner = issue.getLastAssigner();
            if (assigner != null && assigner != 0 && !userIds.contains(assigner)) userIds.add(assigner);
            Integer destroyUser = issue.getDestroyUser();
            if (destroyUser != null && destroyUser != 0 && !userIds.contains(destroyUser)) userIds.add(destroyUser);
            List<String> categoryPathAndKeys = StringUtil.strToStrs(issue.getCategoryPathAndKey(), "/");
            for (String item : categoryPathAndKeys) {
                if (!categoryKeys.contains(item)) categoryKeys.add(item);
            }
            List<String> checkItemPathAndKeys = StringUtil.strToStrs(issue.getCheckItemPathAndKey(), "/");
            for (String item : checkItemPathAndKeys) {
                if (!checkItems.contains(item)) checkItems.add(item);
            }
            List<Integer> areaPathAndIds = StringUtil.strToInts(issue.getAreaPathAndId(), "/");
            for (Integer item : areaPathAndIds) {
                if (!areaPaths.contains(item)) areaPaths.add(item);
            }
        }
        Map<Integer, HouseQmCheckTask> taskMap = createTaskMap(taskIds);
        Map<String, CategoryV3> categoryMap = createCategoryKeyMap(categoryKeys);
        Map<String, CheckItemV3> checkItemMap = createCheckItemMap(checkItems);
        Map<Integer, User> userMap = createUserMap(userIds);
        Map<Integer, Area> areaMap = createAreaMap(areaPaths);
        boolean conditionOpen = false;
        ProjectSetting conditionSetting = projectSettingService.getSettingByProjectIdSKey(req.getProject_id(), "PROJ_ISSUE_CONDITION");
        if (conditionSetting != null && conditionSetting.getValue().equals("是")) {
            conditionOpen = true;
        }
        List<ExcelIssueData> data = Lists.newArrayList();
        for (HouseQmCheckTaskIssue issue : validIssues) {
            ExcelIssueData item = new ExcelIssueData();
            item.setIssue_id(issue.getId());
            item.setTask_name(taskMap.containsKey(issue.getTaskId()) ? taskMap.get(issue.getTaskId()).getName() : "");
            item.setStatus_name(HouseQmCheckTaskIssueStatus.getLabel(issue.getStatus()));
            item.setArea_path(getAreaPath(areaMap, issue.getAreaId()));
            item.setCategory_name(categoryMap.containsKey(issue.getCategoryKey()) ? categoryMap.get(issue.getCategoryKey()).getName() : "");
            if (conditionOpen)
                item.setCondition_name(HouseQmCheckTaskIssueCondition.getLabel(issue.getCondition()));
            item.setChecker(userMap.get(issue.getSenderId()) != null ? userMap.get(issue.getSenderId()).getRealName() : "");
            item.setCheck_at(com.longfor.longjian.common.util.DateUtil.dateToString(issue.getClientCreateAt()));
            item.setAssigner(userMap.get(issue.getLastAssigner()) != null ? userMap.get(issue.getLastAssigner()).getRealName() : "");
            item.setAssign_at(DateUtil.datetimeZero(issue.getLastAssignAt()) ? "" : DateUtil.formatBySec(issue.getLastAssignAt()));
            item.setRepairer(userMap.get(issue.getRepairerId()) != null ? userMap.get(issue.getRepairerId()).getRealName() : "");
            item.setRepair_plan_end_on(DateUtil.datetimeZero(issue.getPlanEndOn()) ? "" : DateUtil.formatBySec(issue.getPlanEndOn()));
            item.setRepair_end_on(DateUtil.datetimeZero(issue.getEndOn()) ? "" : DateUtil.formatBySec(issue.getEndOn()));
            item.setDestroy_user(userMap.get(issue.getDestroyUser()) != null ? userMap.get(issue.getDestroyUser()).getRealName() : "");
            item.setDestroy_at(DateUtil.datetimeZero(issue.getDestroyAt()) ? "" : DateUtil.formatBySec(issue.getDestroyAt()));
            item.setContent(issue.getContent().replaceAll(ILLEGAL_CHARACTERS_RE, ""));
            item.getCheck_item().addAll(getCategoryPathName(categoryMap, issue.getCategoryPathAndKey()));
            item.getCheck_item().addAll(getCheckItemPathName(checkItemMap, issue.getCheckItemPathAndKey()));

            if (issue.getStatus().intValue() == HouseQmCheckTaskIssueStatus.NoteNoAssign.getValue().intValue() ||
                    issue.getStatus().intValue() == HouseQmCheckTaskIssueStatus.AssignNoReform.getValue().intValue()) {
                if (!DateUtil.datetimeZero(issue.getPlanEndOn()) && DateUtil.datetimeBefore(issue.getPlanEndOn(), new Date())) {
                    item.setIs_overdue(true);
                }
            } else if ((issue.getStatus().intValue() == HouseQmCheckTaskIssueStatus.ReformNoCheck.getValue().intValue() ||
                    issue.getStatus().intValue() == HouseQmCheckTaskIssueStatus.CheckYes.getValue().intValue()) && (!DateUtil.datetimeZero(issue.getPlanEndOn()) && DateUtil.datetimeBefore(issue.getPlanEndOn(), issue.getEndOn()))) {
                item.setIs_overdue(true);
            }
            data.add(item);
        }

        String projectName = "";
        Project project = projectService.getOneByProjId(req.getProject_id());
        if (project != null) projectName = project.getName();
        // 数据 格式化到表格 输出
        SXSSFWorkbook wb = ExportUtils.exportExcel(data, conditionOpen);
        //        ret = export_issue_excel(data, conditionOpen)
        //        path = ret.get('path', '')

        String dt = DateUtil.getNowTimeStr("yyyyMMddHHmmss");
        String fileName = String.format("%s-%s-问题列表_%s.xls", CategoryClsTypeEnum.getName(req.getCategory_cls()), projectName, dt);
        Map<String, Object> map = Maps.newHashMap();
        map.put("fileName", fileName);
        map.put("workbook", wb);

        return map;
    }

    private List<String> getAreaPath(Map<Integer, Area> areaMap, Integer areaId) {
        List<String> names = new ArrayList<>();
        Area area = areaMap.get(areaId);
        if (area == null || StringUtils.isEmpty(area.getPath())) {
            return names;
        }
        List<Integer> areaIdList = StringUtil.strToInts(area.getPath(), "/").stream().map(Integer::valueOf).collect(Collectors.toList());
        areaIdList.add(areaId);
        for (Integer id : areaIdList) {
            if (areaMap.get(id) != null) {
                names.add(areaMap.get(id).getName());
            } else {
                log.info(String.format("areaid not in map, areaid=%d", id));
            }
        }
        return names;
    }

    public Map<Integer, HouseQmCheckTask> createTaskMap(List<Integer> taskIds) {
        if (CollectionUtils.isEmpty(taskIds)) return Maps.newHashMap();
        List<HouseQmCheckTask> tasks = houseQmCheckTaskService.searchHouseQmCheckTaskByTaskIdIn(taskIds);
        return tasks.stream().collect(Collectors.toMap(HouseQmCheckTask::getTaskId, t -> t));
    }

    @Override
    @SuppressWarnings("squid:S3776")
    public IssueListRsp list(IssueListDoActionReq req) {

        IssueListRsp issueListRsp = new IssueListRsp();
        Integer start = (req.getPage() - 1) * req.getPage_size();

        //读取配置信息 env_info = dict(**config.ENV_INFO)  host_list: longjianapp.longhu.net lh.zj.com
        String[] hosts = envInfo.trim().split(" ");
        String host = null;
        if (hosts.length > 0) {
            host = hosts[0];
        } else {
            host = "";
        }
        List<Integer> areaIdList = StringUtil.strToInts(req.getArea_ids(), ",");
        List<Integer> statusInList = StringUtil.strToInts(req.getStatus_in(), ",");
        Map<String, Object> condiMap = Maps.newHashMap();
        condiMap.put("projectId", req.getProject_id());
        condiMap.put("categoryCls", req.getCategory_cls());
        if (req.getTask_id() != null && req.getTask_id() > 0) condiMap.put("taskId", req.getTask_id());
        if (req.getStatus_in().length() > 0) condiMap.put("status", statusInList);
        if (req.getCategory_key().length() > 0) condiMap.put("categoryPathAndKey", "%/" + req.getCategory_key() + "/%");
        if (req.getCheck_item_key().length() > 0) condiMap.put("checkItemKey", req.getCheck_item_key());
        if (!areaIdList.isEmpty()) {
            List<String> areaPathAndIdLikeList = Lists.newArrayList();
            for (Integer i : areaIdList) {
                areaPathAndIdLikeList.add("%/" + i + "/%");
            }
            condiMap.put("areaPathAndId", areaPathAndIdLikeList);
        }
        if (req.getType() != null && req.getType() > 0) condiMap.put("type", req.getType());
        if (req.getCondition() != null && req.getCondition() > 0) condiMap.put("condition", req.getCondition());
        if (req.getChecker_id() != null && req.getChecker_id() > 0) condiMap.put("senderId", req.getChecker_id());
        if (req.getRepairer_id() != null && req.getRepairer_id() > 0) condiMap.put("repairerId", req.getRepairer_id());
        if (req.getCreate_on_begin().length() > 0)
            condiMap.put("clientCreateAtGte", req.getCreate_on_begin() + " 00:00:00");
        if (req.getCreate_on_end().length() > 0)
            condiMap.put("clientCreateAtLte", req.getCreate_on_end() + " 23:59:59");
        if (req.is_overdue()) {
            List<Integer> status1 = Lists.newArrayList();
            List<Integer> status2 = Lists.newArrayList();
            status1.add(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId());
            status1.add(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
            condiMap.put("status1", status1);
            status2.add(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());
            status2.add(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
            condiMap.put("status2", status2);
        }
        if (req.getKey_word().length() > 0) {
            condiMap.put(CONTENT, "%" + req.getKey_word() + "%");
            if (StringSplitToListUtil.isInteger(req.getKey_word())) {
                condiMap.put("id", req.getKey_word());
            }
        }
        condiMap.put("deleted", "false");
        // 调用HouseQmCheckTaskIssueService
        Integer total = houseQmCheckTaskIssueService.searchTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(condiMap);
        condiMap.put("start", start);
        condiMap.put("pageSize", req.getPage_size());
        condiMap.put("reverse", true);
        List<HouseQmCheckTaskIssue> validIssues = houseQmCheckTaskIssueService.searchByPageAndProjectIdAndCategoryClsAndNoDeletedAndDongTai(condiMap);
        List<String> categoryKeys = Lists.newArrayList();
        List<Integer> repairers = Lists.newArrayList();
        List<String> checkItems = Lists.newArrayList();
        List<Integer> areaPaths = Lists.newArrayList();
        List<String> attachments = Lists.newArrayList();

        for (HouseQmCheckTaskIssue issue : validIssues) {
            if (!categoryKeys.contains(issue.getCategoryKey())) categoryKeys.add(issue.getCategoryKey());
            if (issue.getRepairerId() != 0) repairers.add(issue.getRepairerId());
            List<String> checkItemPathAndKeyList = StringUtil.strToStrs(issue.getCheckItemPathAndKey(), "/");
            checkItemPathAndKeyList.forEach(s -> {
                if (!checkItems.contains(s)) checkItems.add(s);
            });
            List<Integer> areaPathAndIdList = StringUtil.strToInts(issue.getAreaPathAndId(), "/");
            areaPathAndIdList.forEach(item -> {
                if (!areaPaths.contains(item)) areaPaths.add(item);
            });
            List<String> attachmentMd5List =StringUtil.strToStrs(issue.getAttachmentMd5List(), ",");
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
            item.setLast_assigner(issue.getLastAssigner() != null ? issue.getLastAssigner() : 0);
            item.setLast_assigner_at(DateUtil.datetimeToTimeStamp(issue.getLastAssignAt()));
            item.setLast_repairer(issue.getLastRepairer() != null ? issue.getLastRepairer() : 0);
            item.setLast_repairer_at(DateUtil.datetimeToTimeStamp(issue.getLastRepairerAt()));
            item.setDestroy_user(issue.getDestroyUser());
            item.setDestroy_at(DateUtil.datetimeToTimeStamp(issue.getDestroyAt()));
            item.setDelete_user(issue.getDeleteUser());
            item.setDelete_time(DateUtil.datetimeToTimeStamp(issue.getDeleteTime()));

            List<String> attachmentsList = StringUtil.strToStrs(issue.getAttachmentMd5List(), ",");
            List<String> pictures = item.getPictures();
            for (String md5 : attachmentsList) {
                if (attachmentMap.containsKey(md5)) {
                    String url = "https://" + host + "/" + attachmentMap.get(md5).getStoreKey();
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

            DetailVo detail = JSON.parseObject(issue.getDetail(), DetailVo.class);
            item.setDetail(detail);
            issueList.add(item);
        }

        issueListRsp.setTotal(total);
        issueListRsp.setIssue_list(issueList);
        return issueListRsp;
    }

    @Override
    @SuppressWarnings("squid:S3776")
    public List<HouseQmCheckTaskIssueHistoryLogVo> getHouseQmCheckTaskIssueActionLogByIssueUuid(String issueUuid) {
        HouseQmCheckTaskIssue issueInfo = houseQmCheckTaskIssueService.selectByUuidAndNotDelete(issueUuid);
        if (issueInfo == null) {
            throw new LjBaseRuntimeException(272, CAN_NOT_FIND_THIS_ISSUE);
        }
        List<HouseQmCheckTaskIssueLog> issueLogInfo = houseQmCheckTaskIssueLogService.selectByUuidAndNotDelete(issueUuid);
        ArrayList<Integer> uids = Lists.newArrayList();
        HashMap<Integer, String> userIdRealNameMap = Maps.newHashMap();
        for (int i = 0; i < issueLogInfo.size(); i++) {
            uids.add(issueLogInfo.get(i).getSenderId());
            Map<String, Object> issueLogDetail = JSON.parseObject(issueLogInfo.get(i).getDetail(), Map.class);
            if ((Integer) issueLogDetail.get(REPAIRER_ID) > 0) {
                uids.add((Integer) issueLogDetail.get(REPAIRER_ID));
            }
            if (StringUtils.isNotBlank((String) issueLogDetail.get(REPAIRER_FOLLOWER_IDS))) {
                String replace = ((String) issueLogDetail.get(REPAIRER_FOLLOWER_IDS)).replace(",,", ",");

                List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(replace, ",", ",");
                for (String s : list) {
                    if (StringUtils.isNotBlank(s)) {
                        try {
                            int b = Integer.parseInt(s);
                            uids.add(b);
                        } catch (NumberFormatException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
            }
            List uidlist = CollectionUtil.removeDuplicate(uids);
            List<User> userInfo = userService.searchByUserIdInAndNoDeleted(uidlist);
            for (int j = 0; j < userInfo.size(); j++) {
                userIdRealNameMap.put(userInfo.get(j).getUserId(), userInfo.get(j).getRealName());
            }
        }
        ArrayList<HouseQmCheckTaskIssueHistoryLogVo> result = Lists.newArrayList();
        boolean hasCreateLog = false;
        for (int i = 0; i < issueLogInfo.size(); i++) {
            Map<String, Object> issueLogDetail = JSON.parseObject(issueLogInfo.get(i).getDetail(), Map.class);
            HouseQmCheckTaskIssueHistoryLogVo singleItem = new HouseQmCheckTaskIssueHistoryLogVo();
            singleItem.setUser_id(issueLogInfo.get(i).getSenderId());
            singleItem.setUser_name(userIdRealNameMap.get(issueLogInfo.get(i).getSenderId()));
            singleItem.setCreate_at(DateUtil.datetimeToTimeStamp(issueLogInfo.get(i).getCreateAt()));
            List<HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem> items = Lists.newArrayList();
            if (issueLogInfo.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.NoteNoAssign.getValue())) {
                hasCreateLog = true;
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItem = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                logItem.setLog_type(HouseQmCheckTaskActionLogType.Create.getValue());
                items.add(logItem);
                singleItem.setItems(items);
                result.add(singleItem);
            } else if (issueLogInfo.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.Repairing.getValue())) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItem = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                logItem.setLog_type(HouseQmCheckTaskActionLogType.Assign.getValue());
                logItem.setTarget_user_id((Integer) issueLogDetail.get(REPAIRER_ID));
                logItem.setTarget_user_name(userIdRealNameMap.get(issueLogDetail.get(REPAIRER_ID)));
                ArrayList<String> followers = Lists.newArrayList();
                if ((Integer) issueLogDetail.get(REPAIRER_FOLLOWER_IDS) > 0) {

                    List<Integer> followersId = StringUtil.strToInts((String) issueLogDetail.get(REPAIRER_FOLLOWER_IDS), ",");
                    for (int j = 0; j < followersId.size(); j++) {
                        followers.add(userIdRealNameMap.get(followersId.get(j)));
                    }
                }
                HashMap<String, Object> logData = Maps.newHashMap();
                logData.put(PLAN_END_ON, issueLogDetail.get(PLAN_END_ON));
                logData.put("followers", followers);
                logItem.setData(JSON.toJSONString(logData));
                items.add(logItem);
                singleItem.setItems(items);
                result.add(singleItem);
            } else if (issueLogInfo.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.ReformNoCheck.getValue())) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItem = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                logItem.setLog_type(HouseQmCheckTaskActionLogType.Repair.getValue());
                items.add(logItem);
                singleItem.setItems(items);
                result.add(singleItem);
            } else if (issueLogInfo.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.CheckYes.getValue())) {
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItem = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                logItem.setLog_type(HouseQmCheckTaskActionLogType.Approve.getValue());
                items.add(logItem);
                singleItem.setItems(items);
                result.add(singleItem);
            } else {
                if ((Integer) issueLogDetail.get(PLAN_END_ON) != -1 || (Integer) issueLogDetail.get(REPAIRER_ID) > 0 || (Objects.nonNull(issueLogDetail.get(REPAIRER_FOLLOWER_IDS))&&
                        !issueLogDetail.get(REPAIRER_FOLLOWER_IDS).equals("-1") &&
                                ((String) issueLogDetail.get(REPAIRER_FOLLOWER_IDS)).length() > 0)) {
                    HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItem = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                    logItem.setLog_type(HouseQmCheckTaskActionLogType.Assign.getValue());
                    logItem.setTarget_user_id((Integer) issueLogDetail.get(REPAIRER_ID));
                    if ((Integer) issueLogDetail.get(REPAIRER_ID) > 0) {
                        logItem.setTarget_user_name(userIdRealNameMap.get(issueLogDetail.get(REPAIRER_ID)));
                    } else {
                        logItem.setTarget_user_name("");
                    }
                    ArrayList<String> followers = Lists.newArrayList();

                    if (StringUtils.isNotBlank((String) issueLogDetail.get(REPAIRER_FOLLOWER_IDS))) {
                        List<Integer> followersId = StringUtil.strToInts((String) issueLogDetail.get(REPAIRER_FOLLOWER_IDS), ",");
                        for (int j = 0; j < followersId.size(); j++) {
                            if (userIdRealNameMap.containsKey(followersId.get(j))) {
                                followers.add(userIdRealNameMap.get(followersId.get(j)));
                            }

                        }
                    }
                    HashMap<String, Object> logData = Maps.newHashMap();
                    logData.put(PLAN_END_ON, issueLogDetail.get(PLAN_END_ON));
                    logData.put("followers", followers);
                    logItem.setData(JSON.toJSONString(logData));
                    items.add(logItem);
                    singleItem.setItems(items);

                }

                if (issueLogInfo.get(i).getDesc().length() > 0) {
                    HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItems = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                    logItems.setLog_type(HouseQmCheckTaskActionLogType.AddDesc.getValue());
                    items.add(logItems);
                    singleItem.setItems(items);

                }
                if (issueLogInfo.get(i).getAttachmentMd5List().length() > 0) {
                    HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItems = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                    logItems.setLog_type(HouseQmCheckTaskActionLogType.AddAttachment.getValue());
                    items.add(logItems);
                    singleItem.setItems(items);
                }
                if (!items.isEmpty()) {
                    result.add(singleItem);
                }
            }
        }
        if (!hasCreateLog) {
            HouseQmCheckTaskIssueHistoryLogVo historyLog = new HouseQmCheckTaskIssueHistoryLogVo();
            HouseQmCheckTaskIssue issueInfos = houseQmCheckTaskIssueService.selectByUuidAndNotDelete(issueUuid);
            Integer senderId = issueInfo.getSenderId();
            User userInfo = userService.selectByUserIdAndNotDelete(senderId);
            historyLog.setUser_id(senderId);
            historyLog.setUser_name(userInfo.getRealName());
            historyLog.setCreate_at(DateUtil.datetimeToTimeStamp(issueInfos.getCreateAt()));
            HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem logItem = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
            logItem.setLog_type(HouseQmCheckTaskActionLogType.Create.getValue());
            List<HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem> items = Lists.newArrayList();
            items.add(logItem);
            historyLog.setItems(items);
            result.add(historyLog);

        }

        return result;
    }

    @Override
    public void deleteHouseqmCheckTaskIssueByProjectAndUuid(Integer projectId, String issueUuid) {
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            throw new LjBaseRuntimeException(-1, CAN_NOT_FIND_THIS_ISSUE);
        }
        issueInfo.setUpdateAt(new Date());
        issueInfo.setDeleteAt(new Date());
        houseQmCheckTaskIssueService.update(issueInfo);
    }

    @Override
    public LjBaseResponse updeteIssueDescByUuid(Integer projectId, String issueUuid, Integer uid, String content) {
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage(CAN_NOT_FIND_THIS_ISSUE);
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
        String replace = issueInfo.getContent().replace(";;", ";");
        List<String> strings = StringSplitToListUtil.removeStartAndEndStrAndSplit(replace, ";", ";");
        strings.add(content);

        String contentNew = StringUtils.join(strings, ";");
        issueInfo.setUpdateAt(new Date());
        issueInfo.setContent(contentNew);

        houseQmCheckTaskIssueService.update(issueInfo);
        String uuid = UUID.randomUUID().toString().replace("-", "");


        HashMap<String, Object> logDetail = Maps.newHashMap();
        initLogDetail(logDetail);
        logDetail.put(ISSUE_REASON, -1);
        logDetail.put(ISSUE_REASON_DETAIL, "");
        logDetail.put(ISSUE_SUGGEST, "");
        logDetail.put(POTENTIAL_RISK, "");
        logDetail.put(PREVENTIVE_ACTION_DETAIL, "");

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
    public LjBaseResponse updateIssuePlanEndOnByProjectAndUuid(Integer projectId, String issueUuid, Integer uid, Integer planEndOn) {
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage(CAN_NOT_FIND_THIS_ISSUE);
            return response;
        }
        if (DateUtil.datetimeToTimeStamp(issueInfo.getPlanEndOn())
                == planEndOn) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage(NO_CHANGES);
            return response;
        }
        issueInfo.setPlanEndOn(DateUtil.transForDate(planEndOn));
        issueInfo.setUpdateAt(new Date());
        houseQmCheckTaskIssueService.update(issueInfo);
        int status = -1;
        if (issueInfo.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId())) {
            status = HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId();
        }
        HashMap<String, Object> detail = Maps.newHashMap();

        initLogDetail(detail);
        detail.put(ISSUE_REASON, -1);
        detail.put(ISSUE_REASON_DETAIL, "");
        detail.put(ISSUE_SUGGEST, "");
        detail.put(POTENTIAL_RISK, "");
        detail.put(PREVENTIVE_ACTION_DETAIL, "");
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
            response.setMessage(CAN_NOT_FIND_THIS_ISSUE);
            return response;
        }
        if (issueInfo.getStatus().equals(status)) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage("没有更改");
            return response;
        }
        if (issueInfo.getTyp().equals(CheckTaskIssueType.Record.getValue())) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("状态为记录，不能更改");
            return response;
        }
        if (status.equals(CheckTaskIssueCheckStatus.CheckYes.getValue())) {
            if (issueInfo.getStatus().equals(CheckTaskIssueStatus.NoteNoAssign.getValue()) ||
                    issueInfo.getStatus().equals(CheckTaskIssueStatus.AssignNoReform.getValue())
            ) {
                issueInfo.setEndOn(new Date());
            }
            issueInfo.setStatus(CheckTaskIssueStatus.CheckYes.getValue());
        }
        if (status.equals(CheckTaskIssueCheckStatus.CheckNo.getValue())) {
            issueInfo.setStatus(CheckTaskIssueStatus.AssignNoReform.getValue());
        }
        issueInfo.setUpdateAt(new Date());
        issueInfo.setDestroyAt(new Date());
        issueInfo.setDestroyUser(uid);
        houseQmCheckTaskIssueService.update(issueInfo);
        HashMap<String, Object> logDetail = Maps.newHashMap();
        initLogDetail(logDetail);
        logDetail.put(ISSUE_REASON, -1);
        logDetail.put(ISSUE_REASON_DETAIL, "");
        logDetail.put(ISSUE_SUGGEST, "");
        logDetail.put(POTENTIAL_RISK, "");
        logDetail.put(PREVENTIVE_ACTION_DETAIL, "");
        HouseQmCheckTaskIssueLog issueLog = new HouseQmCheckTaskIssueLog();
        issueLog.setProjectId(issueInfo.getProjectId());
        issueLog.setTaskId(issueInfo.getTaskId());
        issueLog.setUuid(UUID.randomUUID().toString().replace("-", ""));
        issueLog.setIssueUuid(issueUuid);
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
            issueLog.setStatus(CheckTaskIssueStatus.CheckYes.getValue());
        }
        if (status.equals(CheckTaskIssueCheckStatus.CheckNo.getValue())) {
            issueLog.setStatus(CheckTaskIssueStatus.AssignNoReform.getValue());
        }
        houseQmCheckTaskIssueLogService.add(issueLog);
        return new LjBaseResponse();
    }

    @Override
    public Boolean repairNotifyExport2(Integer uid, Integer projectId, String issueUuid, HttpServletResponse resp) {
        List<ExportNotifyDetail2Vo> input = Lists.newArrayList();
        List<Integer> issueIds = StringUtil.strToInts(issueUuid, ",");
        if (CollectionUtils.isEmpty(issueIds)) {
            throw new LjBaseRuntimeException(-99, "");
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
            if ((String) JSON.parseObject(issueList.get(i).getDetail(), Map.class).get(ISSUE_SUGGEST) != null) {
                vo.setIssue_suggest((String) JSON.parseObject(issueList.get(i).getDetail(), Map.class).get(ISSUE_SUGGEST));
            } else {
                vo.setIssue_suggest("");
            }
            input.add(vo);
        }
        //翻转
        Collections.reverse(input);
        User user = userService.selectByUserIdAndNotDelete(uid);
        String userName = "";
        if (user.getRealName() != null) {
            userName = user.getRealName();
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", userName);
        ArrayList<String> list = Lists.newArrayList();
        ArrayList<String> list2 = Lists.newArrayList();
        input.forEach(item -> {
            //判空

            list.add(item.getContent().replace("\n", " "));
            list2.add(item.getIssue_suggest().replace("\n", " "));
        });
        dataMap.put("qingkuang", list);
        dataMap.put("request", list2);
        String str = DateUtil.getNowTimeStr("yyyy_MM_dd_hh_mm_ss");
        return new DocumentHandler().exportDoc("notify_template2", "导出问题通知单_" + str.replace("_", ""), dataMap, resp);
    }

    @Override
    @SuppressWarnings("squid:S3776")
    public Map<String, Object> repairReplyExport(Integer projectId, String issueIds) {

        //准备数据
        ExportReplyDetail input = new ExportReplyDetail();
        List<Integer> newIssueIds = StringUtil.strToInts(issueIds, ",");
        if (CollectionUtils.isEmpty(newIssueIds)) {
            throw new LjBaseRuntimeException(-99, CommonGlobalEnum.RES_ERROR.getValue());
        }
        ArrayList<Integer> statusList = Lists.newArrayList();
        statusList.add(CheckTaskIssueStatus.NoteNoAssign.getValue());
        statusList.add(CheckTaskIssueStatus.AssignNoReform.getValue());
        statusList.add(CheckTaskIssueStatus.ReformNoCheck.getValue());
        statusList.add(CheckTaskIssueStatus.CheckYes.getValue());
        List<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.selectHouseQmCheckTaskIssueByProIdAndIdAndStatus(projectId, newIssueIds, statusList);
        List<Integer> taskIds = Lists.newArrayList();
        List<String> issueUUids = Lists.newArrayList();
        for (HouseQmCheckTaskIssue issue : issueList) {
            if (!taskIds.contains(issue.getTaskId())) taskIds.add(issue.getTaskId());
            if (!issueUUids.contains(issue.getUuid())) issueUUids.add(issue.getUuid());
        }
        List<String> baseCategories = Lists.newArrayList();
        String baseItem = "";
        if (taskIds.size() == 1) {//所选问题为同一个任务
            HouseQmCheckTask taskInfo = houseQmCheckTaskService.selectByTaskId(taskIds.get(0));
            if (taskInfo != null) {
                input.setTask_name(taskInfo.getName());
            }
            baseCategories.addAll(StringUtil.strToStrs(issueList.get(0).getCategoryPathAndKey(), "/"));
            baseItem = issueList.get(0).getCheckItemPathAndKey();
            for (int i = 1; i < issueList.size(); i++) {
                if (baseItem.length() == 0 || !baseItem.equals(issueList.get(i).getCheckItemPathAndKey()))
                    baseItem = "";
                List<String> tempCategories = StringUtil.strToStrs(issueList.get(i).getCategoryPathAndKey(), "/");
                int length = (baseCategories.size() <= tempCategories.size()) ? baseCategories.size() : tempCategories.size();
                List<String> valid = Lists.newArrayList();
                for (int j = 0; j < length; j++) {
                    if (!baseCategories.get(j).equals(tempCategories.get(j))) break;
                    valid.add(baseCategories.get(j));
                }
                if (CollectionUtils.isEmpty(valid)) break;
                baseCategories.clear();
                baseCategories.addAll(valid);
            }
        }

        if (!baseCategories.isEmpty()) {
            Map<String, CategoryV3> categoryMap = createCategoryKeyMap(baseCategories);
            List<String> categoryNames = getCategoryPathName(categoryMap, StringUtils.join(baseCategories, "/"));
            if (baseItem.length() > 0) {
                Map<String, CheckItemV3> checkItemMap = createCheckItemMap(StringUtil.strToStrs(baseItem, "/"));
                categoryNames.addAll(getCheckItemPathName(checkItemMap, baseItem));
            }
            input.setCheck_item_name(StringUtils.join(categoryNames, "/"));
        }
        List<String> oldAttachmentMd5s = Lists.newArrayList();
        Map<String, HouseQmCheckTaskIssueLog> logMap = createIssueLogMap(issueUUids);
        for (Map.Entry<String, HouseQmCheckTaskIssueLog> entry : logMap.entrySet()) {
            oldAttachmentMd5s.addAll(StringUtil.strToStrs(entry.getValue().getAttachmentMd5List(), ","));
        }
        List<String> attachmentMd5s = CollectionUtil.removeDuplicate(oldAttachmentMd5s);
        Map<String, FileResource> attachmentMap = createAttachmentMap(attachmentMd5s);

        for (HouseQmCheckTaskIssue issue : issueList) {
            ExportReplyDetail replyDetail = new ExportReplyDetail();
            ExportReplyDetail.ExportIssueDetail detail = replyDetail.new ExportIssueDetail();
            detail.setIssue_id(issue.getId());
            detail.setQues_content(issue.getContent());
            detail.setAnsw_content(logMap.containsKey(issue.getUuid()) ? logMap.get(issue.getUuid()).getDesc() : "");
            detail.setAnsw_attachment_path(Lists.newArrayList());
            if (logMap.containsKey(issue.getUuid())) {
                for (String attachment : StringUtil.strToStrs(logMap.get(issue.getUuid()).getAttachmentMd5List(), ",")) {
                    if (attachmentMap.containsKey(attachment) && attachmentMap.get(attachment).getStoreKey().length() > 0) {
                        if (CollectionUtils.isNotEmpty(detail.getAnsw_attachment_path())&&detail.getAnsw_attachment_path().size() >= 2) break;
                        List<String> answAttachMentPath = detail.getAnsw_attachment_path();
                        answAttachMentPath.add(String.format("/data/zhijian/%s", attachmentMap.get(attachment).getStoreKey()));
                        detail.setAnsw_attachment_path(answAttachMentPath);
                    }
                }
            }
            List<ExportReplyDetail.ExportIssueDetail> issueDetail = input.getIssue_detail();
            issueDetail.add(detail);
            input.setIssue_detail(issueDetail);
        }
        // 调用方法导出word
        Map<String, Object> map = Maps.newHashMap();
        try {
            XWPFDocument doc = ExportUtils.exportRepairReply(input);
            String dt = DateUtil.getNowTimeStr("yyyyMMddHHmmss");
            String filename = String.format("整改回复单_%s.docx", dt);
            map.put("filename", filename);
            map.put("doc", doc);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 导出失败
            throw new LjBaseRuntimeException(500, "word 导出失败");
        }
        return map;
    }

    @Override
    public List<ProjectSettingV2> getProjectSettingId(Integer projectId) {
        return projectSettingV2Service.getProjectSettingId(projectId);

    }

    @Override
    @SuppressWarnings("squid:S3776")
    public LjBaseResponse updateIssueRepairInfoByProjectAndUuid(Integer uid, Integer repairerId, String repairFollowerIds, Integer projectId, String issueUuid) {
        Integer tempRepairerId = 0;
        ArrayList<String> notifyUserIds = Lists.newArrayList();
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage(CAN_NOT_FIND_THIS_ISSUE);
            return response;
        }
        Integer status = -1;
        if (issueInfo.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId()) && repairerId > 0) {
            status = HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId();
            issueInfo.setStatus(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
        }

        List<String> followers = StringSplitToListUtil.removeStartAndEndStrAndSplit(StringSplitToListUtil.removeStartAndEndStr(repairFollowerIds, "[", "]"), ",", ",");

        if (!followers.contains(String.valueOf(issueInfo.getRepairerId())) && repairerId > 0 && !repairerId.equals(issueInfo.getRepairerId())) {
            followers.add(String.valueOf(issueInfo.getRepairerId()));
            tempRepairerId = issueInfo.getRepairerId();
        }
        if (CollectionUtils.isNotEmpty(followers)) {
            String join = StringUtils.join(followers, ",");
            List<String> strings = StringSplitToListUtil.removeStartAndEndStrAndSplit(join, ",", ",");
            if (strings.contains(",,")) {
                Collections.replaceAll(strings, ",,", ",");
            }
            String s1 = strings.toString().replaceAll(" ", "");
            String s = StringSplitToListUtil.removeStartAndEndStr(s1, "[", "]");
            repairFollowerIds = "," + s + ",";
        }
        HashMap<String, Object> logDetail = Maps.newHashMap();
        initLogDetail(logDetail);
        logDetail.put(ISSUE_REASON, -1);
        logDetail.put(ISSUE_REASON_DETAIL, "-1");
        logDetail.put(ISSUE_SUGGEST, "-1");
        logDetail.put(POTENTIAL_RISK, "-1");
        logDetail.put(PREVENTIVE_ACTION_DETAIL, "-1");
        if (repairerId.equals(issueInfo.getRepairerId()) && repairFollowerIds.equals(issueInfo.getRepairerFollowerIds())) {
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setMessage(NO_CHANGES);
            return response;
        }
        if (!issueInfo.getRepairerId().equals(repairerId)) {
            if (issueInfo.getRepairerId().equals(0)) {
                status = HouseQmCheckTaskIssueLogStatus.AssignNoReform.getValue();
            }
            if (!issueInfo.getRepairerId().equals(0)) {
                status = HouseQmCheckTaskIssueLogStatus.EditBaseInfo.getValue();
            }
            issueInfo.setRepairerId(repairerId);
            notifyUserIds.add(String.valueOf(repairerId));//# 增加待办问题埋点
            logDetail.put(REPAIRER_ID, issueInfo.getRepairerId());
        }
        if (issueInfo.getRepairerId().equals(0)) {
            issueInfo.setStatus(HouseQmCheckTaskIssueStatus.NoteNoAssign.getValue());
        }
        if (!issueInfo.getRepairerFollowerIds().equals(repairFollowerIds)) {
            String s = StringSplitToListUtil.removeStartAndEndStr(issueInfo.getRepairerFollowerIds(), "[", "]");
            // # 增加待办问题埋点
            List<String> oldRepairerFollowerIdList = StringSplitToListUtil.removeStartAndEndStrAndSplit(s, ",", ",");
            List<String> userIds = StringSplitToListUtil.removeStartAndEndStrAndSplit(repairFollowerIds, ",", ",");
            for (int i = 0; i < userIds.size(); i++) {
                if (!oldRepairerFollowerIdList.contains(userIds.get(i)) && !userIds.get(i).equals(String.valueOf(tempRepairerId))) {
                    notifyUserIds.add(userIds.get(i));
                }
            }
            logDetail.put(REPAIRER_FOLLOWER_IDS, repairFollowerIds);
            issueInfo.setRepairerFollowerIds(repairFollowerIds);

        }
        issueInfo.setUpdateAt(new Date());
        houseQmCheckTaskIssueService.update(issueInfo);
        //# 处理代办事项消息推送
        if (CollectionUtils.isNotEmpty(notifyUserIds)) {
            String title = "新的待处理问题";
            String msg = "您在［工程检查］有新的待处理问题，请进入App同步更新。";
            pushBaseMessage(issueInfo.getTaskId(), notifyUserIds, title, msg);
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
            HouseQmCheckTaskIssueUser repairerUserInfo = houseQmCheckTaskIssueUserService.selectByIssueUUidAndUserIdAndRoleTypeAndNotDel(issueInfo.getUuid(), repairerId, HouseQmUserInIssueRoleTypeEnum.Repairer.getId());
            if (repairerUserInfo == null) {
                HouseQmCheckTaskIssueUser repairerUserInfos = new HouseQmCheckTaskIssueUser();
                repairerUserInfos.setTaskId(issueInfo.getTaskId());
                repairerUserInfos.setIssueUuid(issueInfo.getUuid());
                repairerUserInfos.setUserId(repairerId);
                repairerUserInfos.setRoleType(HouseQmUserInIssueRoleTypeEnum.Repairer.getId());
                repairerUserInfos.setCreateAt(new Date());
                repairerUserInfos.setUpdateAt(new Date());
                houseQmCheckTaskIssueUserService.add(repairerUserInfos);
            } else {
                repairerUserInfo.setUpdateAt(new Date());
                houseQmCheckTaskIssueUserService.update(repairerUserInfo);
            }
        }
        List<HouseQmCheckTaskIssueUser> userFollowersInfo = Lists.newArrayList();
        ArrayList<Integer> intFollowers = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(followers)) {

            for (int i = 0; i < followers.size(); i++) {
                intFollowers.add(Integer.valueOf(followers.get(i)));
            }
            userFollowersInfo = houseQmCheckTaskIssueUserService.selectByRoleTypeAndUserIdAndIssueUuid(HouseQmUserInIssueRoleTypeEnum.RepairerFollower.getId(), intFollowers, issueInfo.getUuid());
        }
        ArrayList<HouseQmCheckTaskIssueUser> insertData = Lists.newArrayList();
        HashMap<Object, Object> baseData = Maps.newHashMap();
        baseData.put("task_id", issueInfo.getTaskId());
        baseData.put("issueUuid", issueInfo.getUuid());
        baseData.put("role_type", HouseQmUserInIssueRoleTypeEnum.RepairerFollower.getId());
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
                HouseQmCheckTaskIssueUser user = new HouseQmCheckTaskIssueUser();
                user.setTaskId(issueInfo.getTaskId());
                user.setIssueUuid(issueInfo.getUuid());
                user.setRoleType(HouseQmUserInIssueRoleTypeEnum.RepairerFollower.getId());
                user.setCreateAt(new Date());
                user.setUpdateAt(new Date());
                user.setUserId(intFollowers.get(i));
                insertData.add(user);
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
            throw new LjBaseRuntimeException(272, CAN_NOT_FIND_THIS_ISSUE);
        }
        ArrayList<Integer> issueLogStatus = Lists.newArrayList();
        issueLogStatus.add(HouseQmCheckTaskIssueLogStatus.ReformNoCheck.getValue());
        issueLogStatus.add(HouseQmCheckTaskIssueLogStatus.Repairing.getValue());
        List<HouseQmCheckTaskIssueDetailRepairLogVo> result = Lists.newArrayList();
        int one = houseQmCheckTaskIssueLogService.selectByIssueUuIdAndStatusNotDelAndCount(issueUuid, issueLogStatus);
        if (one < 1) {
            LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage(CAN_NOT_FIND_THIS_ISSUE);
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
                single.setAttachment_md5_list(StringSplitToListUtil.removeStartAndEndStr(attachmentMdeList.toString(), "[", "]").replaceAll(" ", ""));
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
    @SuppressWarnings("squid:S3776")
    public LjBaseResponse<IssueInfoVo> getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(Integer uid, Integer projectId, String issueUuid) {
        Date limitTime = new Date(0);
        HouseQmCheckTaskIssue issueInfo = houseQmCheckTaskIssueService.getIssueByProjectIdAndUuid(projectId, issueUuid);
        if (issueInfo == null) {
            LjBaseResponse<IssueInfoVo> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage(CAN_NOT_FIND_THIS_ISSUE);
            return response;
        }
        Map issueDetail = JSON.parseObject(issueInfo.getDetail(), Map.class);
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
        if (StringUtils.isNotBlank(issueInfo.getRepairerFollowerIds())) {
            repairerFollowerIds = StringUtil.strToInts(issueInfo.getRepairerFollowerIds(), ",");
            if (repairerFollowerIds.contains(0)) {
                repairerFollowerIds.remove(0);
            }
            if (!repairerFollowerIds.isEmpty()) {
                for (int i = 0; i < repairerFollowerIds.size(); i++) {
                    allUserId.add(repairerFollowerIds.get(i));
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
        detailMap.put("issue_reason", issueDetail.get(ISSUE_REASON));
        detailMap.put("issue_reason_detail", issueDetail.get(ISSUE_REASON_DETAIL));
        detailMap.put("issue_suggest", issueDetail.get(ISSUE_SUGGEST));
        detailMap.put("potential_risk", issueDetail.get(POTENTIAL_RISK));
        detailMap.put("preventive_action_detail", issueDetail.get(PREVENTIVE_ACTION_DETAIL));
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

    @Override
    @SuppressWarnings("squid:S3776")
    public Boolean repairNotifyExport(Integer userId, int projectId, String issueUuid, HttpServletResponse response, HttpServletRequest request) {
        List<ExportNotifyDetailVo> input = Lists.newArrayList();
        List<Integer> issueIds = StringUtil.strToInts(issueUuid, ",");
        if (CollectionUtils.isEmpty(issueIds)) {
            throw new LjBaseRuntimeException(-99, "");
        }
        ArrayList<Integer> statusList = Lists.newArrayList();
        statusList.add(CheckTaskIssueStatus.NoteNoAssign.getValue());
        statusList.add(CheckTaskIssueStatus.AssignNoReform.getValue());
        statusList.add(CheckTaskIssueStatus.ReformNoCheck.getValue());
        statusList.add(CheckTaskIssueStatus.CheckYes.getValue());
        List<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.selectHouseQmCheckTaskIssueByProIdAndIdAndStatus(projectId, issueIds, statusList);
        List<Integer> taskIds = Lists.newArrayList();
        List<Integer> areaIds = Lists.newArrayList();
        List<String> categoryKeys = Lists.newArrayList();
        List<String> checkItems = Lists.newArrayList();
        List<String> attachmentMd5s = Lists.newArrayList();
        issueList.forEach(item -> {
            if (!taskIds.contains(item.getTaskId())) {
                taskIds.add(item.getTaskId());
            }
            areaIds.addAll(com.longfor.longjian.common.util.StringUtil.strToInts(item.getAreaPathAndId(), "/"));
            categoryKeys.addAll(com.longfor.longjian.common.util.StringUtil.strToStrs(item.getCategoryPathAndKey(), "/"));
            checkItems.addAll(com.longfor.longjian.common.util.StringUtil.strToStrs(item.getCheckItemPathAndKey(), "/"));
            attachmentMd5s.addAll(com.longfor.longjian.common.util.StringUtil.strToStrs(item.getAttachmentMd5List(), ","));
        });
        Map<Integer, HouseQmCheckTask> taskMap = createTaskMap(CollectionUtil.removeDuplicate(taskIds));
        Map<Integer, Area> areaMap = createAreaMap(CollectionUtil.removeDuplicate(areaIds));
        Map<String, CategoryV3> categoryMap = createCategoryKeyMap(CollectionUtil.removeDuplicate(categoryKeys));
        Map<String, CheckItemV3> checkItemMap = createCheckItemMap(CollectionUtil.removeDuplicate(checkItems));
        Map<String, FileResource> attachmentMap = createAttachmentMap(CollectionUtil.removeDuplicate(attachmentMd5s));
        for (HouseQmCheckTaskIssue issue : issueList) {
            ExportNotifyDetailVo detailVo = new ExportNotifyDetailVo();
            detailVo.setIssue_id(issue.getId());
            if (taskMap.containsKey(issue.getTaskId())) {
                detailVo.setTask_name(taskMap.get(issue.getTaskId()).getName());
            } else {
                detailVo.setTask_name("");
            }
            detailVo.setArea_name(StringUtils.join(getAreaPathName(areaMap, issue.getAreaPathAndId()), "/"));
            List<String> checkItemNames = getCategoryPathName(categoryMap, issue.getCategoryPathAndKey());
            checkItemNames.addAll(getCheckItemPathName(checkItemMap, issue.getCheckItemPathAndKey()));
            detailVo.setCheck_item_name(StringUtils.join(checkItemNames, "/"));
            detailVo.setContent(issue.getContent());
            ArrayList<String> storeKeyList = Lists.newArrayList();
            for (String attachment : com.longfor.longjian.common.util.StringUtil.strToStrs(issue.getAttachmentMd5List(), ",")) {
                if (attachmentMap.containsKey(attachment) && StringUtils.isNotBlank((attachmentMap.get(attachment).getStoreKey()))) {
                    if (detailVo.getAttachment_path().size() >= 2) {
                        break;
                    }
                    String attachmentPath = FileUtil.execDir() + File.separator + attachmentMap.get(attachment).getStoreKey();
                    storeKeyList.add(attachmentPath);
                    detailVo.setAttachment_path(storeKeyList);
                }
            }
            input.add(detailVo);
        }
        log.info("input={}", JSON.toJSONString(input));
        if (issueIds.size() == 1) {
            HashMap<String, Object> map = Maps.newHashMap();
            //单个文件直接导出
            ArrayList<Object> picList = Lists.newArrayList();
            for (ExportNotifyDetailVo vo : input) {
                map.put("name", vo.getTask_name());
                map.put("buwei", vo.getArea_name());
                map.put("neirong", vo.getCheck_item_name());
                map.put(CONTENT, vo.getContent().replace("\n", ""));
                log.info("getAttachment_path={}", vo.getAttachment_path());
                if (vo.getAttachment_path().size() > 0) {
                    for (String s : vo.getAttachment_path()) {
                        try {
                            picList.add(DocumentHandler.getImageBase(s));
                        } catch (Exception e) {
                            log.error("Exception={}", e.getMessage());

                        }
                    }
                }
                map.put("image", picList);
            }
            log.info("picList={}", JSON.toJSONString(picList));
            new DocumentHandler().exportDoc("notify_template", "整改通知单_" + issueIds.get(0), map, response);
        } else {
            //多个word文件打包成zip
            List<String> issueIdsList = Lists.newArrayList();
            List<Map<String, Object>> docList = Lists.newArrayList();
            for (ExportNotifyDetailVo vo : input) {
                List<Object> picList = Lists.newArrayList();
                HashMap<String, Object> map = Maps.newHashMap();
                issueIdsList.add(String.valueOf(vo.getIssue_id()));
                //基本数据
                map.put("name",vo.getTask_name());
                map.put("buwei",vo.getArea_name());
                map.put("neirong",vo.getCheck_item_name());
                map.put(CONTENT,vo.getContent().replace("\n",""));
                log.info("getAttachment_path={}",vo.getAttachment_path());
                if(CollectionUtils.isNotEmpty(vo.getAttachment_path())){
                    List<String> attachmentPath = vo.getAttachment_path();
                    for (String s : attachmentPath) {
                        try {
                            //导出图片
                            picList.add(DocumentHandler.getImageBase(s));
                        } catch (Exception e) {
                            log.error("error:",e.getMessage());

                        }
                    }

                }
                map.put("image",picList);
                docList.add(map);
                log.info("picList={}",JSON.toJSONString(picList));
            }

            //导出
            return new DocumentHandler().exportWordBatch(request, response, docList, issueIdsList, "notify_template.ftl");
        }

        return false;
    }


    public void pushBaseMessage(Integer taskId, ArrayList<String> notifyUserIds, String title, String msg) {
        ArrayList<String> alias = Lists.newArrayList();
        for (int i = 0; i < notifyUserIds.size(); i++) {
            alias.add("user_id_" + enterpriseId + "_" + notifyUserIds.get(i) + "");
        }

        String alia = StringUtils.join(alias, ",");
        UmPushUtil.sendAndroidCustomizedcast(appKeyAndroid, appMasterSecretAndroid,
                alia, AppPlatformTypeEnum.PUSH_PLATFORM_UMENG_ANDROID.getValue(),
                "Android", title, msg, msg, String.valueOf(taskId));
        UmPushUtil.sendIOSCustomizedcast(appKeyIOS, appMasterSecretIOS, alia,
                AppPlatformTypeEnum.PUSH_PLATFORM_UMENG_IOS.getValue(), msg, String.valueOf(taskId));
        XmPushUtil.sendMessageToUserAccounts(appSecretXiaoMi, packageNameXiaomi, title, msg, alias);
    }


    private HouseQmCheckTaskIssue getIssueByProjectIdAndUuid(Integer projectId, String issueUuid) {
        return houseQmCheckTaskIssueService.getIssueByProjectIdAndUuid(projectId, issueUuid);
    }


    private Map<String, CategoryV3> createCategoryKeyMap(List<String> categoryKeys) {
        Map<String, CategoryV3> categoryMap = Maps.newHashMap();
        if (categoryKeys.isEmpty()) return categoryMap;
        List<CategoryV3> categoryV3s = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeys);
        categoryV3s.forEach(item ->
                categoryMap.put(item.getKey(), item)
        );
        return categoryMap;
    }


    private Map<String, CheckItemV3> createCheckItemMap(List<String> checkItems) {
        Map<String, CheckItemV3> checkItemMap = Maps.newHashMap();
        if (checkItems.isEmpty()) return checkItemMap;
        List<CheckItemV3> checkItemV3s = checkItemV3Service.searchCheckItemyV3ByKeyInAndNoDeleted(checkItems);
        checkItemV3s.forEach(item ->
                checkItemMap.put(item.getKey(), item)
        );
        return checkItemMap;
    }


    private Map<Integer, User> createUserMap(List<Integer> repairers) {
        Map<Integer, User> userMap = Maps.newHashMap();
        if (repairers.isEmpty()) return userMap;
        List<User> users = userService.searchByUserIdInAndNoDeleted(repairers);
        users.forEach(item ->
                userMap.put(item.getUserId(), item)
        );
        return userMap;
    }


    private Map<Integer, Area> createAreaMap(List<Integer> areaPaths) {
        Map<Integer, Area> areaMap = Maps.newHashMap();
        if (areaPaths.isEmpty()) return areaMap;
        List<Area> areas = areaService.searchAreaByIdInAndNoDeleted(areaPaths);
        areas.forEach(item ->
                areaMap.put(item.getId(), item)
        );
        return areaMap;
    }


    private Map<String, FileResource> createAttachmentMap(List<String> attachments) {
        Map<String, FileResource> fileResourceMap = Maps.newHashMap();
        if (attachments.isEmpty()) return fileResourceMap;
        List<FileResource> fileResources = fileResourceService.searchFileResourceByFileMd5InAndNoDeleted(attachments);
        fileResources.forEach(item ->
                fileResourceMap.put(item.getFileMd5(), item)
        );
        return fileResourceMap;
    }

    private Map<String, HouseQmCheckTaskIssueLog> createIssueLogMap(List<String> issueUUids) {
        if (CollectionUtils.isEmpty(issueUUids)) return Maps.newHashMap();
        List<HouseQmCheckTaskIssueLog> lst = houseQmCheckTaskIssueLogService.selectByIssueUuIdInAndStatus(issueUUids, CheckTaskIssueStatus.ReformNoCheck.getValue());
        Map<String, HouseQmCheckTaskIssueLog> map = Maps.newHashMap();
        for (HouseQmCheckTaskIssueLog item : lst) {
            map.put(item.getIssueUuid(), item);
        }
        return map;
    }
    
    private List<String> getAreaPathName(Map<Integer, Area> map, String areaPathAndId) {
        List<String> areaNames = Lists.newArrayList();
        List<Integer> areaIds = StringUtil.strToInts(areaPathAndId, "/");
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
        List<String> paths = StringUtil.strToStrs(pathAndKey, "/");
        paths.forEach(path -> {
            String pathName = path;
            if (checkItemMap.containsKey(path)) {
                pathName = checkItemMap.get(path).getName();
            }
            pathNames.add(pathName);
        });
        return pathNames;
    }

    private List<String> getCategoryPathName(Map<String, CategoryV3> categoryMap, String pathAndKey) {
        List<String> pathNames = Lists.newArrayList();
        List<String> paths = com.longfor.longjian.common.util.StringUtil.strToStrs(pathAndKey, "/");
        paths.forEach(path -> {
            String pathName = path;
            if (categoryMap.containsKey(path)) {
                pathName = categoryMap.get(path).getName();
            }
            pathNames.add(pathName);
        });
        return pathNames;
    }


}

