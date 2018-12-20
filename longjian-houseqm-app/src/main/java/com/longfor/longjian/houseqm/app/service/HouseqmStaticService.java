package com.longfor.longjian.houseqm.app.service;


import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.longfor.longjian.houseqm.app.vo.ApiBuildingInfo;
import com.longfor.longjian.houseqm.app.vo.HouseqmStatisticTaskBuildingListRspMsgVo;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskRspService;
import com.longfor.longjian.houseqm.po.Area;
import com.longfor.longjian.houseqm.app.vo.CheckTaskHouseStatInfoVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskSimpleRspVo;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     /*   CheckTaskHouseStatInfoVo vo = new CheckTaskHouseStatInfoVo();
        HouseQmCheckTask taskByProjTaskId = houseQmCheckTaskRspService.getHouseQmCheckTaskByProjTaskId(prodectId, taskId);
        String[] areaIds = taskByProjTaskId.getAreaIds().split(",");
        int[] areaIdsA = new int[]{};
        for (int i = 0; i < areaIds.length; i++) {
            areaIdsA[i]=Integer.parseInt(areaIds[i]);
        }
        String[] areaTypes = taskByProjTaskId.getAreaTypes().split(",");
        if(areaIds.length==0||areaTypes.length==0){
            return null;

        }
        int[] ints = new int[]{};
          ints[0]=prodectId;
        if(areaId>0){
            int[] areasIds = GetIntersectAreas(areaIdsA,ints );
        }



      List<Area>areaList=  areaService.SearchAreaListByRootIdAndTypes(prodectId, areaIds, areaTypes);
                    if(areaList!=null&&areaList.size()>0){
vo.setHouseCount(areaList.size());
                    }
        //计算总户数
        // 找出拥有问题的最小状态，用来区分是否所有问题都处理完了






*/
     return  null;
    }
    private  int [] GetIntersectAreas(int []a,int[]b) {
        int[] result = new int[]{};
        String[] pathsA = new String[]{};
        String[] pathsB = new String[]{};
        //数组转集合 通过id查找
        List<Area> areaVoLista = areaService.selectByAreaIds(Ints.asList(a));
        for (int i = 0; i < areaVoLista.size(); i++) {
            pathsA[areaVoLista.get(i).getId()] = areaVoLista.get(i).getPath();
        }
        String[] pathsa = getUnionPaths(pathsA);
        List<Area> areaVoListb = areaService.selectByAreaIds(Ints.asList(b));

        for (int j = 0; j < areaVoListb.size(); j++) {
            pathsB[areaVoListb.get(j).getId()] = areaVoListb.get(j).getPath();
        }
        //遍历比较，发现一个包含另一个直接跳过
        for (int i = 0; i < pathsA.length; i++) {
            for (int j = 0; j < pathsB.length; j++) {
                if (pathsA[i].startsWith(pathsB[j])) {
                    String[] ids = pathsA[i].split("/");
                    if (ids != null && ids.length > 0) {
                        for (int k = 0; k < ids.length; k++) {
                            result[k] = Integer.parseInt(ids[k]);
                        }
                    }
                    continue;
                }
                if (pathsB[i].startsWith(pathsA[j])) {
                    String[] ids = pathsB[i].split("/");
                    if (ids != null && ids.length > 0) {
                        for (int k = 0; k < ids.length; k++) {
                            result[k] = Integer.parseInt(ids[k]);
                        }
                    }
                    continue;
                }
            }

        }
        return result;
    }

    private String [] getUnionPaths(String [] paths){
        Arrays.sort(paths);
        String[] result  = new String[]{};
        String 	lastPath = "Nothing";
        for (int i = 0; i <paths.length ; i++) {
         if(!paths[i].startsWith(lastPath)){
lastPath=paths[i];
             result[i]=lastPath;

         }
        }
        return  result;
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
       List<Area>areaList= areaService.selectByFatherId(prodectId,0);
        ArrayList<ApiBuildingInfo> buildlist = Lists.newArrayList();

        for (int i = 0; i <areaList.size() ; i++) {
            ApiBuildingInfo buildingInfo = new ApiBuildingInfo();
            buildingInfo.setId(areaList.get(i).getId());
            buildingInfo.setName(areaList.get(i).getName());
            buildlist.add(buildingInfo);
        }
               return buildlist;
    }
}
