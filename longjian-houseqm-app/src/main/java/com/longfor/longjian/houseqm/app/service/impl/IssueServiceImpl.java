package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.HouseQmCheckTaskActionLogType;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueLogStatus;
import com.longfor.longjian.common.consts.checktask.CheckTaskIssueStatus;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskIssueHistoryLogVo;
import com.longfor.longjian.houseqm.app.vo.IssueListVo.DetailVo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.gaia.gfs.core.bean.PageInfo;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.vo.IssueListVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.JsonUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
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

    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;

    @Resource
    CategoryV3Service categoryV3Service;
    @Resource
    HouseQmCheckTaskIssueLogService houseQmCheckTaskIssueLogService;

    @Resource
    CheckItemV3Service checkItemV3Service;

    @Resource
    UserService userService;

    @Resource
    AreaService areaService;

    @Resource
    FileResourceService fileResourceService;

    @Value("${env_info.host_list}")
    String envInfo;

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
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
     */
    @Override
    public PageInfo<IssueListVo> list(Integer projectId, String categoryCls, Integer taskId, String categoryKey, String checkItemKey, String areaIds, String statusIn,
                                      Integer checkerId, Integer repairerId, Integer type, Integer condition, String keyWord, String createOnBegin,
                                      String createOnEnd, Boolean isOverDue, Integer pageNum, Integer pageSize) {

        PageInfo<IssueListVo> pageInfo = new PageInfo<>();
        Integer start = (pageNum - 1) * pageSize;

        //读取配置信息 env_info = dict(**config.ENV_INFO)  host_list: longjianapp.longhu.net lh.zj.com
        String[] hosts = envInfo.split(" ");
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
            /*ocondi = ((HouseQmCheckTaskIssueModel.status.in_(
                    [CheckTaskIssueStatus.NoteNoAssign, CheckTaskIssueStatus.AssignNoReform])) & (
                    HouseQmCheckTaskIssueModel.plan_end_on >= '1980-01-01 08:00:00') & (
                    HouseQmCheckTaskIssueModel.plan_end_on <= CommonUtil.get_datetime())) //获取当前时间
            ocondi = ocondi | ((HouseQmCheckTaskIssueModel.status.in_(
                    [CheckTaskIssueStatus.ReformNoCheck, CheckTaskIssueStatus.CheckYes])) & (
                    HouseQmCheckTaskIssueModel.plan_end_on >= '1980-01-01 08:00:00') & (
                    HouseQmCheckTaskIssueModel.plan_end_on <= HouseQmCheckTaskIssueModel.end_on))
            condi = condi & (ocondi)*/
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
                if (!attachmentMd5List.contains(item)) attachments.add(item);
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
            item.setCategory_path_and_key(issue.getCheckItemPathAndKey());
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
            //todo 确认 python源码 item.delete_time = CommonUtil.datetime_to_timestamp(issue.delete_at)
            item.setDelete_time(DateUtil.datetimeToTimeStamp(issue.getDeleteTime()));

            List<String> attachmentsList = StringSplitToListUtil.splitToStringComma(issue.getAttachmentMd5List(), ",");
            List<String> pictures = Lists.newArrayList();
            String finalHost = host;
            attachmentsList.forEach(md5 -> {
                if (attachmentMap.containsKey(md5)) {
                    String url = "https://" + finalHost + attachmentMap.get(md5).getStoreKey();
                    pictures.add(url);
                }
            });
            item.setPictures(pictures.toArray(new String[pictures.size()]));
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
        pageInfo.setTotal(total);
        pageInfo.setList(issueList);
        return pageInfo;
    }

    @Override
    public ArrayList<HouseQmCheckTaskIssueHistoryLogVo>  getHouseQmCheckTaskIssueActionLogByIssueUuid(String issueUuid) {
        HouseQmCheckTaskIssue issue_info= houseQmCheckTaskIssueService.selectByUuidAndNotDelete(issueUuid);
            if(issue_info==null){
                 throw  new LjBaseRuntimeException(272,"找不到此问题");
            }
        List<HouseQmCheckTaskIssueLog> issue_log_info=houseQmCheckTaskIssueLogService.selectByUuidAndNotDelete(issueUuid);
        ArrayList<Integer> uids = Lists.newArrayList();
        HashMap<Integer, String> user_id_real_name_map = Maps.newHashMap();
        for (int i = 0; i < issue_log_info.size(); i++) {
            uids.add(issue_log_info.get(i).getSenderId());
            Map<String, Object> issue_log_detail =  JSON.parseObject(issue_log_info.get(i).getDetail(),Map.class);
            if((Integer) issue_log_detail.get("RepairerId")>0){
                        uids.add((Integer) issue_log_detail.get("RepairerId"));
                }
            if(((String)issue_log_detail.get("RepairerFollowerIds")).length()>0){
                String replace = ((String) issue_log_detail.get("RepairerFollowerIds")).replace(",,", ",");
                String[] split = replace.substring(1, replace.length() - 1).split(",");
                for (int j = 0; j < split.length; j++) {
                    uids.add(Integer.valueOf(split[j]));
                }
                List uidlist = CollectionUtil.removeDuplicate(uids);
                List<User> user_info = userService.searchByUserIdInAndNoDeleted(uidlist);
                for (int j = 0; j < user_info.size(); j++) {
                    user_id_real_name_map.put(user_info.get(i).getUserId(),user_info.get(i).getRealName());
                }

            }
        }
        ArrayList<HouseQmCheckTaskIssueHistoryLogVo> result = Lists.newArrayList();
        boolean  hasCreateLog =false;
        for (int i = 0; i < issue_log_info.size(); i++) {

            Map<String, Object> issue_log_detail =   JSON.parseObject(issue_log_info.get(i).getDetail(),Map.class);
            HouseQmCheckTaskIssueHistoryLogVo single_item = new HouseQmCheckTaskIssueHistoryLogVo();
            single_item.setUser_id(issue_log_info.get(i).getSenderId());
            single_item.setUser_name(user_id_real_name_map.get(issue_log_info.get(i).getSenderId()));
            single_item.setCreate_at(DateUtil.datetimeToTimeStamp(issue_log_info.get(i).getCreateAt()));
            List<HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem> items= Lists.newArrayList();
            if(issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.NoteNoAssign.getValue())){
                hasCreateLog = true;
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Create.getValue());
                items.add(log_item);
                result.add(single_item);
                continue;

            }


            if(issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.Repairing.getValue())){
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Assign.getValue());
                log_item.setTarget_user_id((Integer) issue_log_detail.get("RepairerId"));
                log_item.setTarget_user_name(user_id_real_name_map.get(issue_log_detail.get("RepairerId")));
                ArrayList<String> followers = Lists.newArrayList();
                if((Integer)issue_log_detail.get("RepairerFollowerIds")>0){

                    List<Integer> followers_id = StringSplitToListUtil.splitToIdsComma((String) issue_log_detail.get("RepairerFollowerIds"), ",");
                    for (int j = 0; j <followers_id.size() ; j++) {
                            followers.add(user_id_real_name_map.get(followers_id.get(j)));
                    }
                }
                HashMap<String, Object> log_data = Maps.newHashMap();
                log_data.put("plan_end_on",issue_log_detail.get("PlanEndOn"));
                log_data.put("followers",followers);
                log_item.setData( JSON.toJSONString(log_data));
                items.add(log_item);
                result.add(single_item);
                continue;
            }

            if(issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.ReformNoCheck.getValue())){
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Repair.getValue());
               items.add(log_item);
                result.add(single_item);
                continue;
            }

            if(issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.CheckYes.getValue())){
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Approve.getValue());
                items.add(log_item);
                result.add(single_item);
                continue;
            }
            if((Integer)issue_log_detail.get("PlanEndOn")!=-1 ||(Integer)issue_log_detail.get("RepairerId") > 0 || (
                    !issue_log_detail.get("RepairerFollowerIds").equals("-1"))||
                    ((String) issue_log_detail.get("RepairerFollowerIds")).length()>0){
                HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_item = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                log_item.setLog_type(HouseQmCheckTaskActionLogType.Assign.getValue());
                log_item.setTarget_user_id((Integer) issue_log_detail.get("RepairerId"));
                if((Integer)issue_log_detail.get("RepairerId")>0){
                    log_item.setTarget_user_name(user_id_real_name_map.get(issue_log_detail.get("RepairerId")));
                }else {
                    log_item.setTarget_user_name("");
                }
                ArrayList<String> followers = Lists.newArrayList();

                if(issue_log_info.get(i).getStatus().equals(HouseQmCheckTaskIssueLogStatus.Repairing.getValue())){

                    if((Integer)issue_log_detail.get("RepairerFollowerIds")>0){
                        List<Integer> followers_id = StringSplitToListUtil.splitToIdsComma((String) issue_log_detail.get("RepairerFollowerIds"), ",");
                        for (int j = 0; j <followers_id.size() ; j++) {
                            if(user_id_real_name_map.containsKey(followers_id.get(j))){
                                followers.add(user_id_real_name_map.get(followers_id.get(j)));
                            }

                        }
                    }
                    HashMap<String, Object> log_data = Maps.newHashMap();
                    log_data.put("plan_end_on",issue_log_detail.get("PlanEndOn"));
                    log_data.put("followers",followers);
                    log_item.setData( JSON.toJSONString(log_data));
                    items.add(log_item);

                }
                if(issue_log_info.get(i).getDesc().length()>0){
                    HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_items = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                    log_items.setLog_type(HouseQmCheckTaskActionLogType.AddDesc.getValue());
                                    items.add(log_items);

                }
                if(issue_log_info.get(i).getAttachmentMd5List().length()>0){
                    HouseQmCheckTaskIssueHistoryLogVo.HouseQmCheckTaskIssueHistoryLogItem log_items = new HouseQmCheckTaskIssueHistoryLogVo().new HouseQmCheckTaskIssueHistoryLogItem();
                    log_items.setLog_type(HouseQmCheckTaskActionLogType.AddAttachment.getValue());
                    items.add(log_items);
                }
                if(items.size()>0){
                        result.add(single_item);

                }

            }


        }
        if(!hasCreateLog){
            HouseQmCheckTaskIssueHistoryLogVo history_log = new HouseQmCheckTaskIssueHistoryLogVo();
            HouseQmCheckTaskIssue issue_infos = houseQmCheckTaskIssueService.selectByUuidAndNotDelete(issueUuid);
            Integer senderId = issue_info.getSenderId();
          User user_info=  userService.selectByUserIdAndNotDelete(senderId);
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
        HouseQmCheckTaskIssue issue_info= getIssueByProjectIdAndUuid(projectId,issueUuid);
            if(issue_info==null){
      throw new LjBaseRuntimeException(432,"找不到此问题");
            }
        issue_info.setUpdateAt(new Date());
        issue_info.setDeleteAt(new Date());
        houseQmCheckTaskIssueService.update(issue_info);
    }

    @Override
    public LjBaseResponse updeteIssueDescByUuid(Integer projectId, String issueUuid, Integer uid,String content) {
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
            if(issueInfo==null){
                LjBaseResponse<Object> response = new LjBaseResponse<>();
                response.setResult(1);
                response.setMessage("找不到此问题");
                return  response;
            }

        Integer taskId = issueInfo.getTaskId();
      UserInHouseQmCheckTask  usersInfo=   userInHouseQmCheckTaskService.selectByTaskIdAndUserIdAndNotDel(taskId,uid);

        if(usersInfo==null){
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("只有任务参与人员才可以操作");
            return  response;
        }
        if (issueInfo.getContent().equals(content)){
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage("没有做改动");
            return  response;
        }
        String[] split = issueInfo.getContent().replace(";;", ";").split(";");
        List<String> strings = Arrays.asList(split);
        strings.add(content);
        String contentNew=";";
        for (int i = 0; i <strings.size() ; i++) {
            contentNew+=strings.get(i);
        }
        issueInfo.setUpdateAt(new Date());
        issueInfo.setContent(contentNew);

        houseQmCheckTaskIssueService.update(issueInfo);
        String uuid = UUID.randomUUID().toString().replace("-", "");


        HashMap<String, Object> logDetail = Maps.newHashMap();
        logDetail.put( "PlanEndOn", -1);
        logDetail.put("EndOn", -1);
        logDetail.put( "RepairerId", -1);
        logDetail.put( "RepairerFollowerIds","");
        logDetail.put( "Condition", -1);
        logDetail.put(  "AreaId", -1);
        logDetail.put(  "PosX", -1);
        logDetail.put(  "PosY",-1);
        logDetail.put(   "Typ", -1);
        logDetail.put(  "Title", "");
        logDetail.put(  "CheckItemKey", "");
        logDetail.put(   "CategoryCls", -1);
        logDetail.put(   "CategoryKey", "");
        logDetail.put(   "DrawingMD5", "");
        logDetail.put(  "RemoveMemoAudioMd5List", "");
        logDetail.put(    "IssueReason", -1);
        logDetail.put(   "IssueReasonDetail", "");
        logDetail.put(   "IssueSuggest","");
        logDetail.put(   "PotentialRisk", "");
        logDetail.put(  "PreventiveActionDetail","");

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
        newIssueLog.setUpdateAt( new Date());
        newIssueLog.setDetail(JSON.toJSONString(logDetail));
        houseQmCheckTaskIssueLogService.add(newIssueLog);
        return  new LjBaseResponse();
    }

    @Override
    public LjBaseResponse updateIssuePlanEndOnByProjectAndUuid(Integer projectId, String issueUuid, Integer uid, Integer plan_end_on) {
        HouseQmCheckTaskIssue issueInfo = getIssueByProjectIdAndUuid(projectId, issueUuid);
        if(issueInfo==null){
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(1);
            response.setMessage("找不到此问题");
            return  response;
        }
        if(DateUtil.datetimeToTimeStamp(issueInfo.getPlanEndOn())
                ==plan_end_on){
            LjBaseResponse<Object> response = new LjBaseResponse<>();
            response.setResult(0);
            response.setMessage("没有改变");
            return  response;
        }
        issueInfo.setPlanEndOn(DateUtil.transForDate(plan_end_on));
        issueInfo.setUpdateAt(new Date());
         houseQmCheckTaskIssueService.update(issueInfo);
  int status=-1;
  if(issueInfo.getStatus()== HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId()){
      status=HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId();
  }
        HashMap<String, Object> detail = Maps.newHashMap();
        detail.put( "PlanEndOn", -1);
        detail.put("EndOn", -1);
        detail.put( "RepairerId", -1);
        detail.put( "RepairerFollowerIds","");
        detail.put( "Condition", -1);
        detail.put(  "AreaId", -1);
        detail.put(  "PosX", -1);
        detail.put(  "PosY",-1);
        detail.put(   "Typ", -1);
        detail.put(  "Title", "");
        detail.put(  "CheckItemKey", "");
        detail.put(   "CategoryCls", -1);
        detail.put(   "CategoryKey", "");
        detail.put(   "DrawingMD5", "");
        detail.put(  "RemoveMemoAudioMd5List", "");
        detail.put(    "IssueReason", -1);
        detail.put(   "IssueReasonDetail", "");
        detail.put(   "IssueSuggest","");
        detail.put(   "PotentialRisk", "");
        detail.put(  "PreventiveActionDetail","");
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
        issueLog.setUpdateAt( new Date());
        issueLog.setDetail(JSON.toJSONString(detail));
        houseQmCheckTaskIssueLogService.add(issueLog);
        return  new LjBaseResponse();
    }

    private HouseQmCheckTaskIssue getIssueByProjectIdAndUuid(Integer projectId, String issueUuid) {
     return  houseQmCheckTaskIssueService.getIssueByProjectIdAndUuid(projectId,issueUuid);
    }


    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param categoryKeys
     * @return java.util.Map<java.lang.String,com.longfor.longjian.houseqm.po.CategoryV3>
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
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param checkItems
     * @return java.util.Map<java.lang.String,com.longfor.longjian.houseqm.po.CheckItemV3>
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
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param repairers
     * @return java.util.Map<java.lang.Integer,com.longfor.longjian.houseqm.po.User>
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
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param areaPaths
     * @return java.util.Map<java.lang.Integer,com.longfor.longjian.houseqm.po.Area>
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
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param attachments
     * @return java.util.Map<java.lang.String,com.longfor.longjian.houseqm.po.FileResource>
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
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param map
     * @param areaPathAndId
     * @return java.util.List<java.lang.String>
     */
    private List<String> getAreaPathName(Map<Integer, Area> map, String areaPathAndId) {
        List<String> areaNames = Lists.newArrayList();
        List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(areaPathAndId, "/");
        areaIds.forEach(id -> {
            String areaName = id+"";
            if (map.containsKey(id)) {
                areaName = map.get(id).getName();

            }
            areaNames.add(areaName);
        });
        return areaNames;
    }

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param checkItemMap
     * @param pathAndKey
     * @return java.util.List<java.lang.String>
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


}

