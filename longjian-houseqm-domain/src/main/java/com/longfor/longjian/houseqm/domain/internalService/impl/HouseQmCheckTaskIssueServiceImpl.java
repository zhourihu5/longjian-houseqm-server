package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.*;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.dto.CheckerIssueStatusStatDto;
import com.longfor.longjian.houseqm.dto.RepaireIssueStatusStatDto;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.ArrayList;

/**
 * @author Houyan
 * @date 2018/12/11 0011 15:39
 */
@Transactional
@Service
@Slf4j
@LFAssignDataSource("zhijian2")
public class HouseQmCheckTaskIssueServiceImpl implements HouseQmCheckTaskIssueService {
    @Resource
    HouseQmCheckTaskIssueMapper houseQmCheckTaskIssueMapper;
    @Resource
    private UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;
    @Resource
    private HouseQmCheckTaskIssueUserMapper houseQmCheckTaskIssueUserMapper;

    @Resource
    private HouseQmCheckTaskSquadMapper houseQmCheckTaskSquadMapper;
    @Resource
    private HouseQmCheckTaskIssueAttachmentMapper houseQmCheckTaskIssueAttachmentMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByProjIdAndUuidIn(Integer project_id, List<String> uuids) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",project_id).andIn("uuid",uuids);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskIssueMapper.selectByExample(example);
    }

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

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByTaskIdUuidIn(Integer task_id, List<String> uuids) {
        return houseQmCheckTaskIssueMapper.searchHouseQmCheckTaskIssueByTaskIdUuidIn(task_id, uuids);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit, Integer checker) {
        List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = new ArrayList<>();
        try {
            List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskMapper.searchByTaskIdUserIdRoleType(userId, task_id, checker);
            List<Integer> squadIds = new ArrayList<>();
            List<Integer> userIds = new ArrayList<>();
            userInHouseQmCheckTasks.forEach(userInHouseQmCheckTask -> {
                squadIds.add(userInHouseQmCheckTask.getSquadId());
            });
            List<UserInHouseQmCheckTask> userInHouseQmCheckTaskSearchSquadIdsList = userInHouseQmCheckTaskMapper.searchBySquadIdIn(squadIds);
            userInHouseQmCheckTaskSearchSquadIdsList.forEach(userInHouseQmCheckTask -> {
                userIds.add(userInHouseQmCheckTask.getUserId());
            });
            if (userIds.size() == 0) {
                userIds.add(userId);
            }
            userIds.add(userId);
            houseQmCheckTaskIssues = houseQmCheckTaskIssueMapper.searchByConditionOrderByPageUnscoped(task_id, last_id, timestamp, userIds, userId, start, limit);
        } catch (Exception e) {
            log.error("error" + e);
        }

        return houseQmCheckTaskIssues;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueUser> searchHouseQmCheckTaskIssueUserByTaskIdLastIdUpdateAtGt(Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit) {
        try {
            List<HouseQmCheckTaskIssueUser> houseQmCheckTaskIssueUsers = houseQmCheckTaskIssueUserMapper.searchByConditionOrderByPageUnscoped(task_id, last_id, timestamp, start, limit);
            return houseQmCheckTaskIssueUsers;
        } catch (Exception e) {
            log.error("error:" + e);
        }
        return null;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAttachment> searchHouseQmCheckTaskIssueAttachmentByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit,Integer privateInt,Integer publicInt ) {
        try {
            List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskMapper.searchByCondition(task_id, userId);
            List<Integer> squadIds = new ArrayList<>();
            userInHouseQmCheckTasks.forEach(userInHouseQmCheckTask -> {
                if (userInHouseQmCheckTask.getSquadId() != null) {
                    squadIds.add(userInHouseQmCheckTask.getSquadId());
                }
            });
            List<HouseQmCheckTaskSquad> houseQmCheckTaskSquads = null;
            List<Integer> existsSquadIds = new ArrayList<>();
            if (squadIds.size() > 0) {
                //找出所有组的，确保其有效
                houseQmCheckTaskSquads = houseQmCheckTaskSquadMapper.searchByInId(squadIds);
                houseQmCheckTaskSquads.forEach(houseQmCheckTaskSquad -> {
                    existsSquadIds.add(houseQmCheckTaskSquad.getId());
                });
            }
            //再根据组ID获取相关的组用户信息
            List<UserInHouseQmCheckTask> userInHouseQmCheckTaskSquadIdInList = userInHouseQmCheckTaskMapper.searchByTaskIdSquadIdIn(task_id, existsSquadIds);
            List<Integer> userIds = new ArrayList<>();
            userInHouseQmCheckTaskSquadIdInList.forEach(userInHouseQmCheckTask -> {
                userIds.add(userInHouseQmCheckTask.getUserId());
            });
            List<HouseQmCheckTaskIssueAttachment> houseQmCheckTaskIssueAttachments=houseQmCheckTaskIssueAttachmentMapper.searchByConditionOrderByPageUnscoped(task_id,userId,timestamp,userIds,privateInt,publicInt,start,limit);
            return  houseQmCheckTaskIssueAttachments;
        } catch (Exception e) {
            log.error("error:" + e);
        }
        return null;
    }

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     *  * @param map
     * @return java.lang.Integer
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public Integer searchTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(map);
    }

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     *  * @param map
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByPageAndProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectHouseQmCheckTaskIssueByPageAndProjectIdAndCategoryClsAndNoDeletedAndDongTai(map);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public Integer selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai(Map<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai(map);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(Map<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(map);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(Integer projectId, Integer taskId, List<Integer> areaIds) {
        return houseQmCheckTaskIssueMapper.selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(projectId,taskId,areaIds,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<IssueRepairCount> selectByProjectIdAndTaskIdAndTyeInAndDongTai(Map<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdAndTyeInAndDongTai(map);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public ArrayList<HouseQmCheckTaskIssue> houseQmCheckTaskIssueByProTaskIdAreaidBegin(Integer projectId, Integer taskId, Integer areaId, Date begin, Date endOns, List<Integer> types) {
        return houseQmCheckTaskIssueMapper.searchhouseQmCheckTaskIssueByProTaskIdAreaidBegin(projectId,  taskId,  areaId,  begin,  endOns,  types,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByProjCategoryKeyAreaId(HashMap<String, Object> condiMap) {
        return houseQmCheckTaskIssueMapper.searchHouseQmCheckTaskIssueByProjCategoryKeyAreaId(condiMap);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<IssueRepairCount> selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(HashMap<String, Object> condiMap) {
        return houseQmCheckTaskIssueMapper.selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(condiMap);
    }

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param task_id
     * @param issueUpdateTime
     * @param userIds
     * @param issueUuids
     * @return com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectIdByTaskIdAndIdGtAndUpdateAtGtAndSenderIdInOrUuidInAndNoDeletedOrderById(Integer task_id, Date issueUpdateTime, List<Integer> userIds, List<String> issueUuids) {
        return houseQmCheckTaskIssueMapper.selectIdByTaskIdAndIdGtAndUpdateAtGtAndSenderIdInOrUuidInAndNoDeletedOrderById(task_id,issueUpdateTime,userIds,issueUuids,"false");
    }

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param userIds
     * @param task_id
     * @param issueUuids
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectUuidBySenderIdInOrTaskIdAndUuidIn(List<Integer> userIds, Integer task_id, List<String> issueUuids) {
        return houseQmCheckTaskIssueMapper.selectUuidBySenderIdInOrTaskIdAndUuidIn(userIds,task_id,issueUuids,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAreaPathAndIdAndStatusIn(HashMap<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectByTaskIdAreaPathAndIdAndStatusIn(map);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int removeHouseQmCheckTaskIssueByProjectIdAndTaskId(Integer project_id, Integer task_id) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",project_id).andEqualTo("taskId",task_id);
        return houseQmCheckTaskIssueMapper.deleteByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStatusStatDto> searchCheckerIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(Map<String, Object> condi) {
        return houseQmCheckTaskIssueMapper.selectCheckerIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(condi);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<RepaireIssueStatusStatDto> searchRepaireIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(Map<String, Object> condi) {
        return houseQmCheckTaskIssueMapper.selectRepaireIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(condi);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByProjIdAndCategoryClsAndAreaPathAndIdLikeGroupByStatus(Integer project_id, Integer category_cls, String areaPath) {
        return houseQmCheckTaskIssueMapper.selectByProjIdAndCategoryClsAndAreaPathAndIdLikeGroupByStatus(project_id,category_cls,"%"+areaPath+"%");
    }
    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssue selectByUuidAndNotDelete(String issueUuid) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria .andEqualTo("uuid",issueUuid);
        criteria.andIsNull("deleteAt");
        return   houseQmCheckTaskIssueMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssue getByUuidUnscoped(String issueUuid) {//unscoped true
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria .andEqualTo("uuid",issueUuid);
        return   houseQmCheckTaskIssueMapper.selectOneByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssue getIssueByProjectIdAndUuid(Integer projectId, String issueUuid) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria .andEqualTo("projectId",projectId);
        criteria .andEqualTo("uuid",issueUuid);
        criteria.andIsNull("deleteAt");
        return   houseQmCheckTaskIssueMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public void update(HouseQmCheckTaskIssue issue_info) {
        houseQmCheckTaskIssueMapper.updateByPrimaryKeySelective(issue_info);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByProIdAndIdAndStatus(Integer projectId, List<Integer> issueIds, ArrayList<Integer> statusList) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",projectId);
        criteria.andIn("id",issueIds);
        criteria.andIn("status",statusList);
        return   houseQmCheckTaskIssueMapper.selectByExample(example);
    }

}
