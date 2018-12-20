package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.po.CheckerIssueStat;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAreaGroupModel;
import com.longfor.longjian.houseqm.po.IssueRepairCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/11 0011 15:39
 */
@Service
@Slf4j
@LFAssignDataSource("zhijian2")
public class HouseQmCheckTaskIssueServiceImpl implements HouseQmCheckTaskIssueService {
    @Resource
    HouseQmCheckTaskIssueMapper houseQmCheckTaskIssueMapper;

    /**
     * 根据问题uuid 客户端创建时间 查 取 未删除的数据
     * @param issueUuids
     * @param timestamp
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByIssueUuidsAndclientCreateAt(Set<String> issueUuids,int timestamp){
        List<HouseQmCheckTaskIssue> taskIssues = houseQmCheckTaskIssueMapper.selectByIssueUuidsAndclientCreateAt(issueUuids, timestamp, "false");
        return taskIssues;
    }

    /**
     * 查询未删除 根据项目id 任务id 数据
     * @param projectId
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds){
        List<CheckerIssueStat> checkerIssueStats = houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdIn(projectId, taskIds, "false");
        return checkerIssueStats;
    }

    /**
     *  获取创建时间 以yyyy-MM-dd 格式
     * @param projectId
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> searchHouseQmCheckTaskIssueActiveDateByProjTaskIdIn(Integer projectId, List<Integer> taskIds){
        List<CheckerIssueStat> taskIssues = houseQmCheckTaskIssueMapper.selectCreateAtByProjectIdAndTaskIdsIn(projectId, taskIds, "false");
        return taskIssues;
    }

    /**
     *  根据项目id 任务id 客户端创建时间>=date 小于=date+1 查询项目任务信息
     * @param projectId
     * @param taskIds
     * @param date
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> getIssueSituationDailyByProjTaskIdInDate(Integer projectId, List<Integer> taskIds,String date){
        List<CheckerIssueStat> checkerIssueStat = houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdAndClientCreateAt(projectId, taskIds, date, "false");
        return checkerIssueStat;
    }

    /**
     * 根据客户端创建时间 lte endOn=(date+1)
     * @param projectId
     * @param taskIds
     * @param date
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> searchByProjectIdAndTaskIdsAndClientCreateAt(Integer projectId, List<Integer> taskIds,String date){
        return houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdsAndClientCreateAt(projectId, taskIds,date,"false");
    }

    /**
     *
     * @param projectId
     * @param taskId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> searchByProjectIdAndTaskId(Integer projectId,Integer taskId){
        List<CheckerIssueStat> checkerIssueStats = houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskId(projectId, taskId, "false");
        return checkerIssueStats;
    }

    /**
     *
     * @param taskId
     * @param areaPath
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByTaskIdAndAreaPathAndIdLike(Integer taskId,String areaPath){
        return houseQmCheckTaskIssueMapper.selectByTaskIdAndAreaPathAndIdLike(taskId,areaPath,"false");
    }

    /**
     *
     * @param taskId
     * @param types
     * @param areaPathLike
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeInAndAreaPathAndIdLike(Integer taskId,List<Integer> types,String areaPathLike){
        return houseQmCheckTaskIssueMapper.selectByTaskIdAndTyeInAndAreaPathAndIdLike(taskId,types,areaPathLike,"false");
    }

    /**
     *
     * @param taskId
     * @param types
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeIn(Integer taskId,List<Integer> types){
        return houseQmCheckTaskIssueMapper.selectByTaskIdAndTyeIn(taskId, types, "false");
    }

    /**
     *
     * @param taskId
     * @param areaPathLike
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(Integer taskId,String areaPathLike){
        return houseQmCheckTaskIssueMapper.selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(taskId, areaPathLike, "false");
    }

    /**
     *
     * @param taskId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskId(Integer taskId){
        return houseQmCheckTaskIssueMapper.selectByTaskId(taskId, "false");
    }

    /**
     *
     * @param projectId
     * @param taskId
     * @param types
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<IssueRepairCount> selectByProjectIdAndTaskIdAndTyeIn(Integer projectId, Integer taskId, List<Integer> types){
        return houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdAndTypeIn(projectId,taskId ,types,"false");
    }

}
