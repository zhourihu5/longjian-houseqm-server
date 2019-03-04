package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.consts.AreaTypeEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.util.DateUtil;
import com.longfor.longjian.houseqm.app.service.IHouseqmExportService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.AreaMapVo;
import com.longfor.longjian.houseqm.app.vo.CategoryMapVo;
import com.longfor.longjian.houseqm.app.vo.export.AreaNode;
import com.longfor.longjian.houseqm.app.vo.export.ProjectIssueInfo;
import com.longfor.longjian.houseqm.app.vo.export.ProjectOrdersVo;
import com.longfor.longjian.houseqm.app.vo.houseqm.HouseqmExportVo;
import com.longfor.longjian.houseqm.consts.CategoryClsTypeEnum;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class HouseqmExportServiceImpl implements IHouseqmExportService {

    @Resource
    private AreaService areaService;
    @Resource
    private IHouseqmStatisticService houseqmStatisticService;
    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private ProjectSettingService projectSettingService;
    @Resource
    private UserService userService;
    @Resource
    private CategoryV3Service categoryV3Service;
    @Resource
    private ProjectService projectService;
    @Resource
    private HouseOwnerInfoService houseOwnerInfoService;

    @Override
    public List<ProjectOrdersVo> searchProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(Integer projectId, Integer taskId, List<Integer> areaIds, Integer repairerId, Date beginOn, Date endOn, Integer categoryCls) {
        //取出和这些areaids相关的区域 后面用来创建树使用
        List<Area> areas = areaService.searchRelatedAreaByAreaIdIn(projectId, areaIds);

        //创建这颗树
        List<AreaNode> nodes = AreaNode.createAreaTree(areas);
        //获取森林下面的在areaIds下面的 所有区域 包含自己
        List<Area> subAreas = AreaNode.getSubTreeAllAreasByAreaId(nodes, areaIds);
        //把和这些areaids相关的区域 创建一个 areamMao类
        AreaMapVo areaMap = houseqmStatisticService.createAreasMapByAreaList(areas);

        //获取户下面的区域 户的区域  户与房间关系map
        List<Integer> subAreaIds = Lists.newArrayList();
        List<Integer> houseIds = Lists.newArrayList();
        Map<Integer, List<Integer>> roomSubAreaIdsMap = Maps.newHashMap();
        for (Area item : subAreas) {
            if (AreaTypeEnum.HOUSE.getId().equals(item.getType())) {
                houseIds.add(item.getId());
            }
        }
        CollectionUtil.removeDuplicate(houseIds);
        //带条件检索 检查的问题
        List<HouseQmCheckTaskIssue> resIssue = houseQmCheckTaskIssueService.searchByProjIdAndTaskIdAndAreaIdInAndRepairedIdAndClientCreateAt(projectId, taskId, subAreaIds, repairerId, beginOn, endOn);

        //获取工程处理单配置名称
        ProjectSetting projSetting = projectSettingService.getSettingByProjectIdSKey(projectId, "PROJ_YDYF_PROJ_ORDER_NAME");
        String projSettingName = "";
        boolean isShowHouseOwnerInfo = false;
        if (projSetting != null && projSetting.getValue().length() > 0) {
            projSettingName = projSetting.getValue();
        } else {
            projSettingName = "工程维修处理单";
        }

        //获取是否显示业主信息
        Map<Integer, String> projectOrderHouseOwnerInfoMap = Maps.newHashMap();
        projectOrderHouseOwnerInfoMap.put(CategoryClsTypeEnum.RHYF.getId(), "PROJ_YDYF_PROJ_ORDER_RHYF_HOUSE_OWNER");
        projectOrderHouseOwnerInfoMap.put(CategoryClsTypeEnum.FHYS.getId(), "PROJ_YDYF_PROJ_ORDER_FHYS_HOUSE_OWNER");

        if (projectOrderHouseOwnerInfoMap.containsKey(categoryCls)) {
            ProjectSetting projectSetting = projectSettingService.getSettingByProjectIdSKey(projectId, projectOrderHouseOwnerInfoMap.get(categoryCls));
            if (projectSetting != null) {
                if ("是".equals(projectSetting.getValue())) {
                    isShowHouseOwnerInfo = true;
                }
            }
        }

        List<String> checkItemKeys = Lists.newArrayList();
        List<String> categoryKeys = Lists.newArrayList();
        List<Integer> senderIds = Lists.newArrayList();
        List<Integer> repairedIds = Lists.newArrayList();
        List<Integer> userIds = Lists.newArrayList();

        Map<Integer, List<HouseQmCheckTaskIssue>> issueMap = Maps.newHashMap();
        for (HouseQmCheckTaskIssue item : resIssue) {
            checkItemKeys.add(item.getCheckItemKey());
            senderIds.add(item.getSenderId());
            repairedIds.add(item.getRepairerId());
            if (issueMap.containsKey(item.getAreaId())) {
                issueMap.get(item.getAreaId()).add(item);
            } else {
                issueMap.put(item.getAreaId(), Arrays.asList(item));
            }
            categoryKeys.addAll(StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getCategoryPathAndKey(), "/", "/"));
        }
        userIds.addAll(senderIds);
        userIds.addAll(repairedIds);
        List<Integer> userList = CollectionUtil.removeDuplicate(userIds);
        List<String> categoryKeysList = CollectionUtil.removeDuplicate(categoryKeys);

        //获取用户map
        Map<Integer, User> userMap = userService.selectByIds(userList);
        //获取检查项 方便后面获取构件检查项map ，方便获取检查项名字
        List<CategoryV3> categorys = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeysList);
        for (CategoryV3 item : categorys) {
            categoryKeysList.addAll(item.getPathSlice());
        }
        Set<String> set = new HashSet<>(categoryKeysList);
        List<String> categorykeys = set.stream().collect(Collectors.toList());
        List<CategoryV3> categoryV3s = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categorykeys);
        CategoryMapVo categoryMap = CategoryMapVo.NewCategoryMap(categoryV3s);
        //获取项目名称
        Project project = projectService.getOneByProjId(projectId);
        //获取业主信息
        List<Integer> projIds = Lists.newArrayList();
        projIds.add(projectId);
        List<HouseOwnerInfo> houseOwners = houseOwnerInfoService.searchHouseOwnerInfoByProjInAreaIdIn(projIds, houseIds);

        Map<Integer, HouseOwnerInfo> houseOwnerMap = houseOwners.stream().collect(Collectors.toMap(HouseOwnerInfo::getAreaId, h -> h));
        //构造工程处理单 数据
        List<ProjectOrdersVo> r = Lists.newArrayList();
        for (Integer areaId : houseIds) {
            ProjectOrdersVo item = new ProjectOrdersVo();
            item.setTitle(projSettingName);
            item.setProjectName(project.getName());
            item.setViewHouseOwner(isShowHouseOwnerInfo);
            item.setHouseOwnerName("");
            item.setHouseOwnerPhone("");
            item.setIssueItems(Lists.newArrayList());

            Map<String, String> allNames = areaMap.getAllNames(areaId);
            item.setAreaNames(allNames.get("building") + "-" + allNames.get("floor") + "-" + allNames.get("house"));
            if (houseOwnerMap.containsKey(areaId)) {
                item.setHouseOwnerName(houseOwnerMap.get(areaId).getOwnerName());
                item.setHouseOwnerPhone(houseOwnerMap.get(areaId).getPhone());
            }
            if (roomSubAreaIdsMap.containsKey(areaId)) {
                List<Integer> subIds = roomSubAreaIdsMap.get(areaId);
                for (Integer subId : subIds) {
                    if (issueMap.containsKey(subId)) {
                        continue;
                    }
                    List<HouseQmCheckTaskIssue> issues = issueMap.get(subId);
                    for (HouseQmCheckTaskIssue issue : issues) {
                        ProjectIssueInfo issueItem = new ProjectIssueInfo();
                        issueItem.setName(areaMap.getName(subId));
                        issueItem.setIssueDesc(issue.getContent());
                        issueItem.setCheckItemName(StringUtils.join(categoryMap.getFullNamesByKey(issue.getCategoryKey()), "/"));
                        issueItem.setRecords("");
                        issueItem.setRepaired("");
                        issueItem.setStatus("");
                        if (userMap.containsKey(issue.getSenderId())) {
                            issueItem.setRecords(userMap.get(issue.getSenderId()).getRealName() + " " + DateUtil.dateToString(issue.getClientCreateAt()));
                        }
                        if (userMap.containsKey(issue.getRepairerId())) {
                            issueItem.setRepaired(userMap.get(issue.getRepairerId()).getRealName());
                        }
                        for (HouseQmCheckTaskIssueStatusEnum value : HouseQmCheckTaskIssueStatusEnum.values()) {
                            if (value.getId().equals(issue.getStatus())) {
                                issueItem.setStatus(value.getValue());
                            }
                        }
                        item.getIssueItems().add(issueItem);
                    }
                }
            }
            r.add(item);
        }

        return r;
    }

    @Override
    public String exportProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(HouseqmExportVo hevo) {
        Integer projectId=hevo.getProjectId();
        Integer taskId=hevo.getTaskId();
        List<Integer> areaIds=hevo.getAreaIds();
        Integer repairerId=hevo.getRepairerId();
        Date beginOn=hevo.getBeginOn();
        Date endOn=hevo.getEndOn();
        Integer categoryCls=hevo.getCategoryCls();
        List<ProjectOrdersVo> res = searchProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(projectId, taskId, areaIds, repairerId, beginOn, endOn, categoryCls);
        if (CollectionUtils.isEmpty(res)) {
            return "";//errors.New("没有任何导出数据。")
        }

        return null;
    }
}
