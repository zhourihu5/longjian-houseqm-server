package com.longfor.longjian.houseqm.app.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.app.vo.ApiBuildingInfo;
import com.longfor.longjian.houseqm.app.vo.HouseqmStatisticTaskBuildingListRspMsgVo;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskRspService;
import com.longfor.longjian.houseqm.po.Area;
import com.longfor.longjian.houseqm.app.vo.CheckTaskHouseStatInfoVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskSimpleRspVo;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAreaGroupModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    HouseQmCheckTaskRspService houseQmCheckTaskRspService;
    @Resource
    AreaService areaService;
    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;

    public List<HouseQmCheckTaskSimpleRspVo> SearchHouseQmCheckTaskByProjCategoryCls(Integer project_id, Integer category_cls) {
        List<HouseQmCheckTask> houseQmCheckTasks = houseQmCheckTaskService.selectByProjectIdAndCategoryCls(project_id, category_cls);
        ArrayList<HouseQmCheckTaskSimpleRspVo> hQCTSRlist = new ArrayList<>();
        for (int i = 0; i < houseQmCheckTasks.size(); i++) {
            String[] split = houseQmCheckTasks.get(i).getAreaTypes().split(",");
            HouseQmCheckTaskSimpleRspVo rspVo = new HouseQmCheckTaskSimpleRspVo();
            rspVo.setProjectId(houseQmCheckTasks.get(i).getProjectId());
            rspVo.setTaskId(houseQmCheckTasks.get(i).getTaskId());
            rspVo.setAreaTypes(Integer.parseInt(split[0]));
            rspVo.setName(houseQmCheckTasks.get(i).getName());
            rspVo.setPlanEndOn(houseQmCheckTasks.get(i).getPlanEndOn());
            rspVo.setCreateAt(houseQmCheckTasks.get(i).getCreateAt());
            rspVo.setPlanBeginOn(houseQmCheckTasks.get(i).getPlanBeginOn());
            hQCTSRlist.add(rspVo);
        }
        return hQCTSRlist;
    }

    public CheckTaskHouseStatInfoVo GetHouseQmCheckTaskHouseStatByTaskId(Integer prodectId, Integer taskId, Integer areaId) {
        try {
            CheckTaskHouseStatInfoVo vo = new CheckTaskHouseStatInfoVo();
            vo.setApprovedCount(0);
            vo.setRepairedCount(0);
            vo.setHasIssueCount(0);
            vo.setCheckedCount(0);
            vo.setHouseCount(0);
            //读取任务
            HouseQmCheckTask taskByProjTaskId = houseQmCheckTaskRspService.getHouseQmCheckTaskByProjTaskId(prodectId, taskId);
            // 获取出任务下的区域与检验类型的交集
            List<Integer> areaIds = splitToIdsComma(taskByProjTaskId.getAreaIds(), ",");
            List<Integer> areaTypes = splitToIdsComma(taskByProjTaskId.getAreaTypes(), ",");
            if (areaIds.size() == 0 || areaTypes.size() == 0) {
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
                if (status.getValue().getMinStatus() == HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId()) {
                    vo.setRepairedCount(vo.getRepairedCount() + 1);
                }
                if (status.getValue().getMinStatus() == HouseQmCheckTaskIssueStatusEnum.CheckYes.getId()) {
                    vo.setRepairedCount(vo.getRepairedCount() + 1);
                    vo.setApprovedCount(vo.getApprovedCount() + 1);
                }
            }
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public HouseqmStatisticTaskBuildingListRspMsgVo taskBuildingList(Integer prodectId, Integer taskId) {

        HouseQmCheckTask taskByProjTask = houseQmCheckTaskRspService.getHouseQmCheckTaskByProjTaskId(prodectId, taskId);

        if (taskByProjTask == null) {
            return null;
        }
        String[] split = taskByProjTask.getAreaIds().split(",");
        //转成int数组
        int[] order_int = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            order_int[i] = Integer.parseInt(split[i]);
        }
        //转成list
        ArrayList<Integer> objects = new ArrayList<>();
        for (int i = 0; i < order_int.length; i++) {
            objects.add(order_int[i]);
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

    public List<ApiBuildingInfo> PSelectByFatherId(Integer prodectId) {
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

    private Map<Integer, HouseqmStaticService.IssueMinStatus> getIssueMinStatusMapByTaskIdAndAreaId(Integer taskId, Integer areaId, Boolean onlyIssue) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        List<HouseQmCheckTaskIssueAreaGroupModel> result = Lists.newArrayList();

        if (onlyIssue && areaId > 0) {
            result = houseQmCheckTaskIssueService.selectByTaskIdAndTyeInAndAreaPathAndIdLike(taskId, types, "%%/" + areaId + "/%%");
        } else if (onlyIssue && areaId <= 0) {
            result = houseQmCheckTaskIssueService.selectByTaskIdAndTyeIn(taskId, types);
        } else if (!onlyIssue && areaId > 0) {
            result = houseQmCheckTaskIssueService.selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(taskId, "%%/" + areaId + "/%%");
        } else {
            result = houseQmCheckTaskIssueService.selectByTaskId(taskId);
        }

        HashMap<Integer, IssueMinStatus> maps = Maps.newHashMap();
        for (HouseQmCheckTaskIssueAreaGroupModel area : result) {
            List<Integer> aIds = splitToIdsComma(area.getAreaPath(), "/");
            if (aIds.size() > 0) {
                IssueMinStatus minStatus = new IssueMinStatus();
                minStatus.setCount(area.getExtendCol());
                minStatus.setMinStatus(area.getStatus());
                maps.put(aIds.get(aIds.size() - 1), minStatus);
            }
        }
        return maps;
    }

    /**
     * 字符串分割 转换为int类型的
     *
     * @param ids
     * @return
     */
    private List<Integer> splitToIdsComma(String ids, String sep) {
        List<Integer> list = Lists.newArrayList();
        ids.trim();
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

    /**
     * 用于getIssueMinStatusMapByTaskIdAndAreaId()方法
     */
    @NoArgsConstructor
    @Data
    public class IssueMinStatus {
        private Integer count;
        private Integer minStatus;
    }


}
