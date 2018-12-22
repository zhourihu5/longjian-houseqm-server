package com.longfor.longjian.houseqm.app.service.impl;

import com.longfor.longjian.houseqm.app.vo.IssueListVo.DetailVo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.gaia.gfs.core.bean.PageInfo;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.vo.IssueListVo;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.*;
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
    CategoryV3Service categoryV3Service;

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

