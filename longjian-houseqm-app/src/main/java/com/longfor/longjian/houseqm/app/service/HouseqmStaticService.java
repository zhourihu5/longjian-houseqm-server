package com.longfor.longjian.houseqm.app.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.util.StringUtil;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueTypeEnum;
import com.longfor.longjian.houseqm.consts.RepossessionRepairStatusEnum;
import com.longfor.longjian.houseqm.consts.RepossessionStatusEnum;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAreaGroupModel;
import com.longfor.longjian.houseqm.po.zj2db.Area;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.zj2db.RepossessionStatus;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Dongshun on 2018/12/17.
 */
@Repository
@Service
@Slf4j
public class HouseqmStaticService {
    @Resource
    HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    AreaService areaService;
    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    RepossessionStatusService repossessionStatusService;
    @Resource
    UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;

    public List<HouseQmCheckTaskSimpleRspVo> searchHouseQmCheckTaskByProjCategoryCls(Integer projectId, Integer categoryCls) {
        List<HouseQmCheckTask> houseQmCheckTasks = houseQmCheckTaskService.selectByProjectIdAndCategoryCls(projectId, categoryCls);
        ArrayList<HouseQmCheckTaskSimpleRspVo> hQCTSRlist = new ArrayList<>();
        for (int i = 0; i < houseQmCheckTasks.size(); i++) {
            HouseQmCheckTaskSimpleRspVo rspVo = new HouseQmCheckTaskSimpleRspVo();
            rspVo.setProject_id(houseQmCheckTasks.get(i).getProjectId());
            rspVo.setTask_id(houseQmCheckTasks.get(i).getTaskId());
            List<Integer> split = StringUtil.strToInts(houseQmCheckTasks.get(i).getAreaTypes(), ",");
            rspVo.setArea_types(split);
            rspVo.setName(houseQmCheckTasks.get(i).getName());
            rspVo.setPlan_end_on(DateUtil.datetimeToTimeStamp(houseQmCheckTasks.get(i).getPlanEndOn()));
            rspVo.setCreate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTasks.get(i).getCreateAt()));
            rspVo.setPlan_begin_on(DateUtil.datetimeToTimeStamp(houseQmCheckTasks.get(i).getPlanBeginOn()));
            hQCTSRlist.add(rspVo);
        }
        return hQCTSRlist;
    }

    public CheckTaskHouseStatInfoVo getHouseQmCheckTaskHouseStatByTaskId(Integer prodectId, Integer taskId, Integer areaId) {
        try {
            CheckTaskHouseStatInfoVo vo = new CheckTaskHouseStatInfoVo();
            vo.setApprovedCount(0);
            vo.setRepairedCount(0);
            vo.setHasIssueCount(0);
            vo.setCheckedCount(0);
            vo.setHouseCount(0);
            //读取任务
            HouseQmCheckTask taskByProjTaskId = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(prodectId, taskId);
            // 获取出任务下的区域与检验类型的交集
            List<Integer> areaIds = splitToIdsComma(taskByProjTaskId.getAreaIds(), ",");
            List<Integer> areaTypes = splitToIdsComma(taskByProjTaskId.getAreaTypes(), ",");
            if (CollectionUtils.isEmpty(areaIds) || CollectionUtils.isEmpty(areaTypes)) {
                return null;
            }
            List<Integer> areaIdsList = Lists.newArrayList();
            List<Integer> areaIdList = Lists.newArrayList();
            areaIdList.add(areaId);
            if (areaId > 0) {
                areaIdsList = areaService.getIntersectAreas(areaIds, areaIdList);
            }
            List<Area> areas = null;
            if (areaIdsList.isEmpty()) {
                areas = areaService.searchAreaListByRootIdAndTypes(prodectId, areaIds, areaTypes);
            } else {
                areas = areaService.searchAreaListByRootIdAndTypes(prodectId, areaIdsList, areaTypes);
            }
            vo.setHouseCount(areas.size());

            //计算总户数
            // 找出拥有问题的最小状态，用来区分是否所有问题都处理完了
            Map<Integer, IssueMinStatus> areaIssueMap = getIssueMinStatusMapByTaskIdAndAreaId(taskId, areaId, true);
            Map<Integer, IssueMinStatus> checkedAreaIssueMap = getIssueMinStatusMapByTaskIdAndAreaId(taskId, areaId, false);
            vo.setCheckedCount(checkedAreaIssueMap.size());
            for (Map.Entry<Integer, IssueMinStatus> status : areaIssueMap.entrySet()) {
                vo.setHasIssueCount(vo.getHasIssueCount() + 1);
                if (status.getValue().getMinStatus().equals(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId())) {
                    vo.setRepairedCount(vo.getRepairedCount() + 1);
                }
                if (status.getValue().getMinStatus().equals(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId())) {
                    vo.setRepairedCount(vo.getRepairedCount() + 1);
                    vo.setApprovedCount(vo.getApprovedCount() + 1);
                }
            }
            return vo;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    public HouseqmStatisticTaskBuildingListRspMsgVo taskBuildingList(Integer prodectId, Integer taskId) {

        HouseQmCheckTask taskByProjTask = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(prodectId, taskId);

        if (taskByProjTask == null) {
            return null;
        }
        String[] split = taskByProjTask.getAreaIds().split(",");
        //转成int数组
        int[] orderInt = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            orderInt[i] = Integer.parseInt(split[i]);
        }
        //转成list
        ArrayList<Integer> objects = new ArrayList<>();
        for (int i = 0; i < orderInt.length; i++) {
            objects.add(orderInt[i]);
        }
        List<Area> areas = areaService.selectByAreaIds(objects);
        ArrayList<ApiBuildingInfo> buildingInfoArrayList = Lists.newArrayList();

        for (int i = 0; i < areas.size(); i++) {
            ApiBuildingInfo apiBuildingInfo = new ApiBuildingInfo();
            apiBuildingInfo.setId(areas.get(i).getId());
            apiBuildingInfo.setName(areas.get(i).getName());
            buildingInfoArrayList.add(apiBuildingInfo);
        }
        HouseqmStatisticTaskBuildingListRspMsgVo msgVo = new HouseqmStatisticTaskBuildingListRspMsgVo();
        msgVo.setItems(buildingInfoArrayList);
        return msgVo;
    }

    public List<ApiBuildingInfo> pSelectByFatherId(Integer prodectId) {
        List<Area> areaList = areaService.selectByFatherId(prodectId, 0);
        ArrayList<ApiBuildingInfo> buildlist = Lists.newArrayList();

        for (int i = 0; i < areaList.size(); i++) {
            ApiBuildingInfo buildingInfo = new ApiBuildingInfo();
            buildingInfo.setId(areaList.get(i).getId());
            buildingInfo.setName(areaList.get(i).getName());
            buildlist.add(buildingInfo);
        }
        return buildlist;
    }

    public Map<Integer,IssueMinStatus> getIssueMinStatusMapByTaskIdAndAreaId(Integer taskId, Integer areaId, Boolean onlyIssue) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());

        List<HouseQmCheckTaskIssueAreaGroupModel> result = houseQmCheckTaskIssueService.selectByTaskIdAndTyeInAndAreaPathAndIdLike(onlyIssue, taskId, types, areaId);

        HashMap<Integer, IssueMinStatus> maps = Maps.newHashMap();
        for (HouseQmCheckTaskIssueAreaGroupModel area : result) {
            List<Integer> aIds = splitToIdsComma(area.getAreaPath(), "/");
            if (CollectionUtils.isNotEmpty(aIds)) {
                IssueMinStatus minStatus = new IssueMinStatus();
                minStatus.setCount(area.getExtendCol());
                minStatus.setMinStatus(area.getStatus());
                maps.put(aIds.get(aIds.size() - 1), minStatus);
            }
        }
        return maps;
    }


    public List<Integer> splitToIdsComma(String ids, String sep) {
        List<Integer> list = Lists.newArrayList();
        ids = ids.trim();
        String[] str = ids.split(sep);
        List<String> areaList = Arrays.asList(str);
        for (String s : areaList) {
            if (s.equals("")) {
                continue;
            }
            list.add(Integer.valueOf(s));
        }
        return list;
    }
    @SuppressWarnings("squid:S3776")
    public RepossessionTasksStatusInfoVo getRepossessionTasksStatusInfo(Integer prodectId, List<Integer> taskIds, Integer areaId) {
        RepossessionTasksStatusInfoVo info = new RepossessionTasksStatusInfoVo();

        for (Integer taskId : taskIds) {
            List<Area> areas = searchTargetAreaByTaskId(prodectId, taskId);
            if (areas == null) {
                continue;
            }
            int total = areas.size();
            List<RepossessionStatus> items = repossessionStatusService.searchByTaskIdAreaIdLike(taskId, areaId);
            List<String> hasIssuePaths = getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, null, areaId);
            HashMap<Integer, Boolean> hasIssueAreaId = Maps.newHashMap();
            for (int j = 0; j < hasIssuePaths.size(); j++) {
                List<Integer> ids = StringUtil.strToInts(hasIssuePaths.get(j), "/");
                if (CollectionUtils.isNotEmpty(ids)) {
                    for (int k = 0; k < ids.size(); k++) {
                        hasIssueAreaId.put(ids.get(ids.size() - 1), true);
                    }

                }
            }
            ArrayList<Integer> statuses = Lists.newArrayList();
            statuses.add(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId());
            statuses.add(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
            statuses.add(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());
            // 有问题但是未销项完成的
            List<String> hasIssueNoApprovedPaths = getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, statuses, areaId);
            HashMap<Integer, Boolean> hasIssueNoApprovedAreaId = Maps.newHashMap();
            for (int j = 0; j < hasIssueNoApprovedPaths.size(); j++) {
                List<Integer> ids = StringUtil.strToInts(hasIssueNoApprovedPaths.get(j), "/");
                if (CollectionUtils.isNotEmpty(ids)) {
                    for (int k = 0; k < ids.size(); k++) {
                        hasIssueNoApprovedAreaId.put(ids.get(ids.size() - 1), true);
                    }

                }
            }
            int checkedCount = 0;
            HashMap<Integer, Boolean> rAreaId = Maps.newHashMap();
            for (int j = 0; j < items.size(); j++) {
                //去重
                if (rAreaId.containsKey(items.get(j).getAreaId())) {
                    continue;
                } else {
                    rAreaId.put(items.get(j).getAreaId(), true);
                }
                info.setAcceptCount(0);
                info.setAcceptHasIssueCount(0);
                info.setAcceptHasIssueSignCount(0);
                info.setAcceptApprovedCount(0);
                info.setRepairConfirmCount(0);
                info.setAcceptNoIssueCount(0);
                info.setAcceptNoIssueSignCount(0);
                info.setRejectCount(0);
                info.setUnacceptCount(0);
                info.setOnlyWatch(0);
                info.setUnacceptCount(0);
                info.setTotal(0);
                info.setCheckedCount(0);
                info.setUncheckedCount(0);

                if (items.get(j).getStatus().equals(RepossessionStatusEnum.Accept.getId())) {
                    checkedCount += 1;
                    info.setAcceptCount(info.getAcceptCount() + 1);
                    if (hasIssueAreaId.containsKey(items.get(j).getAreaId())) {
                        info.setAcceptHasIssueCount(info.getAcceptHasIssueCount() + 1);
                        if (items.get(j).getStatus() == 1) {
                            info.setAcceptHasIssueSignCount(info.getAcceptHasIssueSignCount() + 1);
                        }
                        if (!hasIssueNoApprovedAreaId.containsKey(items.get(j).getAreaId())) {
                            info.setAcceptApprovedCount(info.getAcceptApprovedCount() + 1);
                        }
                        if (items.get(j).getRepairStatus().equals(RepossessionRepairStatusEnum.Confirmed.getId())) {
                            info.setRepairConfirmCount(info.getRepairConfirmCount() + 1);
                        }
                    } else {
                        info.setAcceptNoIssueCount(info.getAcceptNoIssueCount() + 1);
                        if (items.get(j).getSignStatus() == 1) {
                            info.setAcceptNoIssueSignCount(info.getAcceptNoIssueSignCount() + 1);
                        }
                    }
                }
                if (items.get(j).getStatus().equals(RepossessionStatusEnum.RejectAccept.getId())) {
                    checkedCount += 1;
                    info.setRejectCount(info.getRejectCount() + 1);
                    info.setUnacceptCount(info.getRejectCount() + 1);
                }
                if (items.get(j).getStatus().equals(RepossessionStatusEnum.OnlyCheck.getId())) {
                    checkedCount += 1;
                    info.setOnlyWatch(info.getOnlyWatch() + 1);
                    info.setUnacceptCount(info.getUnacceptCount() + 1);
                }
            }
            info.setTotal(info.getTotal()==null?0:info.getTotal() + total);
            info.setCheckedCount(info.getCheckedCount()==null?0:info.getCheckedCount() + checkedCount);
            info.setUncheckedCount(info.getUncheckedCount()==null?0:info.getUncheckedCount() + total - checkedCount);

        }
        info.setCheckedRate((float) info.getCheckedCount() / (float) info.getTotal() / 100 + "f");
        return info;
    }

    public List<String> getHasIssueTaskCheckedAreaPathListByTaskId(Integer taskId, boolean onlyIssue, List<Integer> statuses, Integer areaId) {
        //通过问题状态，只取出里面相关部分(如果是无问题的，是取出所有，然后扣除掉有问题的)
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueTypeEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        types.add(HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("taskId", taskId);
        if (onlyIssue) {
            map.put("types", types);
        }
        if (areaId > 0) {
            map.put("areaId", "%/" + areaId + "/%");
        }
        if (statuses != null) {
            map.put("statuses", statuses);
        }
        List<HouseQmCheckTaskIssueAreaGroupModel> groupList = houseQmCheckTaskIssueService.selectByTaskIdAreaPathAndIdAndStatusIn(map);
        ArrayList<String> paths = Lists.newArrayList();
        for (int i = 0; i < groupList.size(); i++) {
            paths.add(groupList.get(i).getAreaPath());
        }
        return paths;
    }

    public List<Area> searchTargetAreaByTaskId(Integer prodectId, Integer taskId) {

        //读取任务
        HouseQmCheckTask taskByProjTaskId = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(prodectId, taskId);
        if (taskByProjTaskId==null)return Lists.newArrayList();
        // 获取出任务下的区域与检验类型的交集
        List<Integer> areaIds = splitToIdsComma(taskByProjTaskId.getAreaIds(), ",");
        List<Integer> areaTypes = splitToIdsComma(taskByProjTaskId.getAreaTypes(), ",");
        if (CollectionUtils.isEmpty(areaIds) || CollectionUtils.isEmpty(areaTypes)) {
            return Lists.newArrayList();
        }
        return areaService.searchAreaListByRootIdAndTypes(prodectId, areaIds, areaTypes);

    }

    public List<UserInHouseQmCheckTask> searchUserInHouseQmCheckTaskByUserIdRoleType(Integer uid, Integer id) {
        return userInHouseQmCheckTaskService.searchUserInHouseQmCheckTaskByUserIdRoleType(uid, id);

    }

    public List<HouseQmCheckTask> searchHouseQmCheckTaskByProjCategoryClsIn(Integer projectId, List<Integer> categoryClsList) {
        return houseQmCheckTaskService.searchByProjectIdAndCategoryClsIn(projectId, categoryClsList);

    }

    public List<HouseQmCheckTask> searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(Integer projectId, Integer areaId, List<Integer> categoryClsList) {
        List<HouseQmCheckTask> tasks = houseQmCheckTaskService.searchByProjectIdAndCategoryClsIn(projectId, categoryClsList);
        ArrayList<Integer> areaIds = Lists.newArrayList();
        for (int i = 0; i < tasks.size(); i++) {
            List<Integer> ids = StringUtil.strToInts(tasks.get(i).getAreaIds(), ",");
            areaIds.addAll(ids);
        }
        List list = CollectionUtil.removeDuplicate(areaIds);
        List<Area> areas = areaService.selectByAreaIds(list);
        HashMap<Integer, String> areaMap = Maps.newHashMap();
        for (int i = 0; i < areas.size(); i++) {
            areaMap.put(areas.get(i).getId(), areas.get(i).getPath() + areas.get(i).getId());
        }
        for (int i = 0; i < tasks.size(); i++) {
            List<Integer> comma = StringUtil.strToInts(tasks.get(i).getAreaIds(), ",");
            Boolean b = checkRootAreaIntersectAreas(areaMap, areaId, comma);
            if (b) {
                return tasks;
            }
        }

        return Lists.newArrayList();
    }

    public Boolean checkRootAreaIntersectAreas(Map<Integer, String> areaMap, Integer areaId, List<Integer> comma) {
        for (int i = 0; i < comma.size(); i++) {
            if (comma.get(i).equals(areaId)) {
                return true;
            }
            if (areaMap.containsKey(comma.get(i))&&areaMap.get(comma.get(i)).contains(String.valueOf(areaId))) {
                    return true;
            }
        }

        return false;
    }
}
