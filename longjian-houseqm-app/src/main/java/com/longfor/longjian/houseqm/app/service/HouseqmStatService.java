package com.longfor.longjian.houseqm.app.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.app.vo.CheckerStatListVo;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.UserService;
import com.longfor.longjian.houseqm.po.CheckerIssueStat;
import com.longfor.longjian.houseqm.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Houyan
 * @date 2018/12/14 0014 19:45
 */
@Repository
@Service
@Slf4j
public class HouseqmStatService {

    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;

    @Resource
    UserService userService;
    /**
     *
     * @param projectId
     * @param taskIds
     * @return
     */
    public CheckerStatListVo searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds){
        CheckerStatListVo statListVo = new CheckerStatListVo();
        List<CheckerStatListVo.CheckerStatVo> checkerStatList = Lists.newArrayList();
        List<CheckerIssueStat> checkerIssueStats = houseQmCheckTaskIssueService.searchCheckerIssueStatisticByProjIdAndTaskId(projectId, taskIds);

        List<Integer> list = Lists.newArrayList();
        for (CheckerIssueStat stat : checkerIssueStats) {
            list.add(stat.getUserId());
        }
        Map<Integer, User> userMap = userService.selectByIds(list);

        Map<Integer, CheckerStatListVo.CheckerStatVo> checkerMap = Maps.newHashMap();
        Map<Integer, Map<String,Boolean>> areaMap = Maps.newHashMap();
        Map<String, Boolean> fatherPathMap = Maps.newHashMap();
        for (CheckerIssueStat l : checkerIssueStats) {
            if (!checkerMap.containsKey(l.getUserId())){
                CheckerStatListVo checkerStatListVo = new CheckerStatListVo();
                CheckerStatListVo.CheckerStatVo checkerStatVo = checkerStatListVo.new CheckerStatVo();
                checkerStatVo.setRecords_count(0);
                checkerStatVo.setIssue_count(0);
                checkerStatVo.setChecked_count(0);
                checkerStatVo.setUser_id(l.getUserId());
                checkerMap.put(l.getUserId(),checkerStatVo);

                if (userMap.containsKey(l.getUserId())){
                    checkerMap.get(l.getUserId()).setReal_name(userMap.get(l.getUserId()).getRealName());
                }
            }
            CheckerStatListVo.CheckerStatVo stat = checkerMap.get(l.getUserId());
            // 以下应使用枚举类，由于未改动包结构 先写死
            if (l.getTyp()==99){
                stat.setRecords_count(l.getCount()+stat.getRecords_count());
            }else if (l.getTyp()==1||l.getTyp()==3){
                stat.setIssue_count(l.getCount()+stat.getIssue_count());
            }

            if (!areaMap.containsKey(l.getUserId())){
                HashMap<String, Boolean> map = Maps.newHashMap();
                areaMap.put(l.getUserId(), map);
            }
            String areapath=l.getAreaId()+"/";
            String fatherPath=l.getAreaPathAndId().replace(areapath, "");

            fatherPathMap.put(fatherPath, true);
            areaMap.put(l.getUserId(), fatherPathMap);
        }

        //计算检查数
        for (Map.Entry<Integer, CheckerStatListVo.CheckerStatVo> entry : checkerMap.entrySet()) {
            entry.getValue().setChecked_count(areaMap.get(entry.getValue().getUser_id()).size());
            checkerStatList.add(entry.getValue());
        }
        statListVo.setItems(checkerStatList);
        return statListVo;
    }

}
