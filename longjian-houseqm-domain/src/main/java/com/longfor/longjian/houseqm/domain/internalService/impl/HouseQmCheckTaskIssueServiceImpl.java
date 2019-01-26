package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.google.common.collect.Lists;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueEnum;
import com.longfor.longjian.houseqm.consts.HouseQmIssuePlanStatusEnum;
import com.longfor.longjian.houseqm.dao.*;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.dto.CheckerIssueStatusStatDto;
import com.longfor.longjian.houseqm.dto.HouseQmCheckTaskIssueDto;
import com.longfor.longjian.houseqm.dto.HouseQmCheckTaskIssueListDto;
import com.longfor.longjian.houseqm.dto.RepaireIssueStatusStatDto;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    private HouseQmCheckTaskIssueMapper houseQmCheckTaskIssueMapper;
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
    public List<HouseQmCheckTaskIssue> searchByTaskIdInGroupByTaskIdAndStatus(List<Integer> taskIds) {
        return houseQmCheckTaskIssueMapper.searchByTaskIdInGroupByTaskIdAndStatus(taskIds);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByProjIdAndCategoryClsInAndRepairerIdAndClientCreateAtAndTypInAndStatusInAndTaskIdOrderByClientCreateAt(Integer project_id, List<Integer> category_cls_list, String statBegin, String statEnd, List<Integer> typs, List<Integer> status, Integer task_id, List<Integer> my_task_ids) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", project_id);
        if (category_cls_list.size() > 0) criteria.andIn("categoryCls", category_cls_list);
        criteria.andNotEqualTo("repairerId", 0);
        criteria.andGreaterThanOrEqualTo("clientCreateAt", statBegin);
        criteria.andLessThanOrEqualTo("clientCreateAt", statEnd);
        if (typs.size() > 0) criteria.andIn("typ", typs);
        if (status.size() > 0) criteria.andIn("status", status);
        if (task_id > 0) {
            criteria.andEqualTo("taskId", task_id);
        } else {
            if (my_task_ids.size() > 0) criteria.andIn("taskId", my_task_ids);
        }
        example.orderBy("clientCreateAt").desc();
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskIssueMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByProjIdAndCategoryClsInAndSenderIdAndClientCreateAtAndTypAndTaskIdOrderByClientCreateAt(Integer project_id, List<Integer> category_cls_list, String statBegin, String statEnd, List<Integer> typs, Integer task_id, List<Integer> my_task_ids) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", project_id);
        if (category_cls_list.size() > 0) criteria.andIn("categoryCls", category_cls_list);
        criteria.andNotEqualTo("senderId", 0);
        criteria.andGreaterThanOrEqualTo("clientCreateAt", statBegin);
        criteria.andLessThanOrEqualTo("clientCreateAt", statEnd);
        if (typs.size() > 0) criteria.andIn("typ", typs);
        if (task_id > 0) {
            criteria.andEqualTo("taskId", task_id);
        } else {
            if (my_task_ids.size() > 0) criteria.andIn("taskId", my_task_ids);
        }
        example.orderBy("clientCreateAt").desc();
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskIssueMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByProjectIdAndCategoryClsInAndTaskIdInAndAreaPathAndIdLike(Integer project_id, List<Integer> category_cls_list, List<Integer> task_ids, String area_path) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", project_id);
        if (category_cls_list.size() > 0) criteria.andIn("categoryCls", category_cls_list);
        if (area_path.length() > 0) criteria.andLike("areaPathAndId", "%" + area_path + "%");
        ExampleUtil.addDeleteAtJudge(example);
        example.orderBy("clientCreateAt").desc();
        return houseQmCheckTaskIssueMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByProjIdAndTaskIdAndAreaIdInAndRepairedIdAndClientCreateAt(Integer project_id, Integer task_id, List<Integer> subAreaIds, Integer repairer_id, Date begin_on, Date end_on) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", project_id).andEqualTo("taskId", task_id).andIn("areaId", subAreaIds);
        if (repairer_id > 0) {
            criteria.andEqualTo("repairerId", repairer_id);
        }
        if (begin_on.getTime() > 0) {
            criteria.andLessThanOrEqualTo("clientCreateAt", begin_on);
        }
        if (end_on.getTime() > 0) {
            criteria.andLessThanOrEqualTo("clientCreateAt", end_on);
        }
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskIssueMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int deleteHouseQmCheckTaskIssueByProjUuid(Integer project_id, String issueUuid) {
        try {
            Example example = new Example(HouseQmCheckTaskIssue.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("projectId", project_id).andEqualTo("uuid", issueUuid);
            ExampleUtil.addDeleteAtJudge(example);
            return houseQmCheckTaskIssueMapper.deleteByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            return 0;
        }
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int insertOneHouseQmCheckTaskIssue(HouseQmCheckTaskIssue issue) {
        return houseQmCheckTaskIssueMapper.insertSelective(issue);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByProjIdAndUuidIn(Integer project_id, List<String> uuids) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", project_id);
        if (uuids.size() > 0) criteria.andIn("uuid", uuids);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskIssueMapper.selectByExample(example);
    }

    /**
     * 根据问题uuid 客户端创建时间 查 取 未删除的数据
     *
     * @param issueUuids
     * @param timestamp
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByIssueUuidsAndclientCreateAt(Set<String> issueUuids, int timestamp) {
        List<HouseQmCheckTaskIssue> taskIssues = houseQmCheckTaskIssueMapper.selectByIssueUuidsAndclientCreateAt(issueUuids, timestamp, "false");
        return taskIssues;
    }

    /**
     * 查询未删除 根据项目id 任务id 数据
     *
     * @param projectId
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds) {
        List<CheckerIssueStat> checkerIssueStats = houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdIn(projectId, taskIds, "false");
        return checkerIssueStats;
    }

    /**
     * 获取创建时间 以yyyy-MM-dd 格式
     *
     * @param projectId
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> searchHouseQmCheckTaskIssueActiveDateByProjTaskIdIn(Integer projectId, List<Integer> taskIds) {
        return houseQmCheckTaskIssueMapper.selectCreateAtByProjectIdAndTaskIdsIn(projectId, taskIds, "false");
    }

    /**
     * 根据项目id 任务id 客户端创建时间>=date 小于=date+1 查询项目任务信息
     *
     * @param projectId
     * @param taskIds
     * @param date
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> getIssueSituationDailyByProjTaskIdInDate(Integer projectId, List<Integer> taskIds, String date) {
        List<CheckerIssueStat> checkerIssueStat = houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdAndClientCreateAt(projectId, taskIds, date, "false");
        return checkerIssueStat;
    }

    /**
     * 根据客户端创建时间 lte endOn=(date+1)
     *
     * @param projectId
     * @param taskIds
     * @param date
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> searchByProjectIdAndTaskIdsAndClientCreateAt(Integer projectId, List<Integer> taskIds, String date) {
        return houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdsAndClientCreateAt(projectId, taskIds, date, "false");
    }

    /**
     * @param projectId
     * @param taskId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<CheckerIssueStat> searchByProjectIdAndTaskId(Integer projectId, Integer taskId) {
        List<CheckerIssueStat> checkerIssueStats = houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskId(projectId, taskId, "false");
        return checkerIssueStats;
    }

    /**
     * @param taskId
     * @param areaPath
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchByTaskIdAndAreaPathAndIdLike(Integer taskId, String areaPath) {
        return houseQmCheckTaskIssueMapper.selectByTaskIdAndAreaPathAndIdLike(taskId, areaPath, "false");
    }

    /**
     * @param taskId
     * @param types
     * @param areaPathLike
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeInAndAreaPathAndIdLike(Integer taskId, List<Integer> types, String areaPathLike) {
        return houseQmCheckTaskIssueMapper.selectByTaskIdAndTyeInAndAreaPathAndIdLike(taskId, types, areaPathLike, "false");
    }

    /**
     * @param taskId
     * @param types
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeIn(Integer taskId, List<Integer> types) {
        return houseQmCheckTaskIssueMapper.selectByTaskIdAndTyeIn(taskId, types, "false");
    }

    /**
     * @param taskId
     * @param areaPathLike
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(Integer taskId, String areaPathLike) {
        return houseQmCheckTaskIssueMapper.selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(taskId, areaPathLike, "false");
    }

    /**
     * @param taskId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskId(Integer taskId) {
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
        List<HouseQmCheckTaskIssue> items = new ArrayList<>();
        try {
            List<UserInHouseQmCheckTask> rs = userInHouseQmCheckTaskMapper.searchByTaskIdUserIdRoleType(userId, task_id, checker);
            List<Integer> squadIds = new ArrayList<>();
            rs.forEach(r -> squadIds.add(r.getSquadId()));
            List<Integer> list = CollectionUtil.removeDuplicate(squadIds);
            List<UserInHouseQmCheckTask> res;
            if (list.size() > 0) res = userInHouseQmCheckTaskMapper.searchBySquadIdIn(list);
            else {
                res = Lists.newArrayList();
            }
            List<Integer> userIds = new ArrayList<>();
            res.forEach(r -> userIds.add(r.getUserId()));
            List<Integer> userIdList = CollectionUtil.removeDuplicate(userIds);
            if (userIdList.size() == 0) {
                userIdList.add(userId);
            }
            userIdList.add(userId);
            items = houseQmCheckTaskIssueMapper.searchByConditionOrderByPageUnscoped(task_id, last_id, timestamp, userIdList, userId, start, limit);
        } catch (Exception e) {
            log.error("error" + e);
        }

        return items;
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
    public List<HouseQmCheckTaskIssueAttachment> searchHouseQmCheckTaskIssueAttachmentByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit, Integer privateInt, Integer publicInt) {
        try {
            //A找出与自己同组的人
            ////找到任务中，用户所在的所有组
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
                ////找出所有组的，确保其有效
                houseQmCheckTaskSquads = houseQmCheckTaskSquadMapper.searchByInId(squadIds);
                houseQmCheckTaskSquads.forEach(houseQmCheckTaskSquad -> {
                    existsSquadIds.add(houseQmCheckTaskSquad.getId());
                });
            }
            ////再根据组ID获取相关的组用户信息
            List<UserInHouseQmCheckTask> rs = userInHouseQmCheckTaskMapper.searchByTaskIdSquadIdIn(task_id, existsSquadIds);
            //B找出相关的附件
            List<Integer> userIds = new ArrayList<>();
            rs.forEach(userInHouseQmCheckTask -> {
                userIds.add(userInHouseQmCheckTask.getUserId());
            });
            //
            List<HouseQmCheckTaskIssueAttachment> houseQmCheckTaskIssueAttachments = houseQmCheckTaskIssueAttachmentMapper.searchByTaskIdAndSelfJoinOrderByIdASCPageUnscoped(task_id, userId, timestamp, userIds, privateInt, publicInt, start, limit);
            return houseQmCheckTaskIssueAttachments;
        } catch (Exception e) {
            log.error("error:" + e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return java.lang.Integer
     * @author hy
     * @date 2018/12/21 0021
     * * @param map
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public Integer searchTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(map);
    }

    /**
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     * @author hy
     * @date 2018/12/21 0021
     * * @param map
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
    public HouseQmCheckTaskIssueListDto selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai2(Integer projectId, Integer taskId, List<Integer> categoryClsList, Integer areaId, Integer planStatus, Date beginOn, Date endOn, Integer page, Integer pageSize) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId).andIn("categoryCls", categoryClsList);
        if (taskId != null && taskId > 0) criteria.andEqualTo("taskId", taskId);
        if (areaId != null && areaId > 0) criteria.andLike("areaPathAndId", "%%/" + areaId + "/%%");
        if (beginOn != null && beginOn.getTime() / 1000 > 0)
            criteria.andGreaterThanOrEqualTo("clientCreateAt", beginOn);
        if (endOn != null && endOn.getTime() / 1000 > 0) criteria.andGreaterThanOrEqualTo("clientCreateAt", endOn);
        ArrayList<Integer> typs = Lists.newArrayList();
        typs.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        typs.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        criteria.andIn("typ", typs);
        String nowTimeStr = DateUtil.getNowTimeStr("yyyy-MM-dd hh:mm:ss");
        HouseQmIssuePlanStatusEnum e = null;
        for (HouseQmIssuePlanStatusEnum value : HouseQmIssuePlanStatusEnum.values()) {
            if (planStatus.equals(value.getId())) e = value;
        }
        if (e != null) {
            switch (e) {
                case OnTimeFinish:
                    criteria.andNotEqualTo("status", HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId())
                            .andNotEqualTo("status", HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId())
                            .andCondition("plan_end_on > '1970-01-02'")
                            .andCondition("end_on <= plan_end_on");
                    break;
                case UnOnTimeNotFinish:
                    criteria.andEqualTo("status", HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId())
                            .andCondition("plan_end_on >" + nowTimeStr);
                    break;
                case NoSettingTime:

                    criteria.andIsNotNull("planEndOn")
                            .orCondition("plan_end_on < '1970-01-02'")
                            .andNotEqualTo("status", HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId());
                    break;
                case OverTimeFinish:
                    criteria.andNotEqualTo("status", HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId())
                            .andNotEqualTo("status", HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId())
                            .andCondition("plan_end_on > '1970-01-02' AND end_on > plan_end_on");
                    break;
                case OverTimeNotFinish:
                    criteria.andEqualTo("status", HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId())
                            .andCondition("plan_end_on > '1970-01-02'")
                            .andCondition("plan_end_on <" + nowTimeStr);
                    break;
                default:
                    break;
            }
        }
        int start = 0;
        if (page != null && page > 0) {
            start = (page - 1) * pageSize;
        }
        ExampleUtil.addDeleteAtJudge(example);
        int total = houseQmCheckTaskIssueMapper.selectCountByExample(example);
        example.orderBy("updateAt").desc();
        List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = houseQmCheckTaskIssueMapper.selectByExampleAndRowBounds(example, new RowBounds(start, pageSize));
        HouseQmCheckTaskIssueListDto houseQmCheckTaskIssueListVo = new HouseQmCheckTaskIssueListDto();
        houseQmCheckTaskIssueListVo.setTotal(total);
        houseQmCheckTaskIssueListVo.setHouseQmCheckTaskIssues(houseQmCheckTaskIssues);
        return houseQmCheckTaskIssueListVo;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(Map<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(map);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(Integer projectId, Integer taskId, List<Integer> areaIds) {
        return houseQmCheckTaskIssueMapper.selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(projectId, taskId, areaIds, "false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<IssueRepairCount> selectByProjectIdAndTaskIdAndTyeInAndDongTai(Map<String, Object> map) {
        return houseQmCheckTaskIssueMapper.selectByProjectIdAndTaskIdAndTyeInAndDongTai(map);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public ArrayList<HouseQmCheckTaskIssue> houseQmCheckTaskIssueByProTaskIdAreaidBegin(Integer projectId, Integer taskId, Integer areaId, Date begin, Date endOns, List<Integer> types) {
        return houseQmCheckTaskIssueMapper.searchhouseQmCheckTaskIssueByProTaskIdAreaidBegin(projectId, taskId, areaId, begin, endOns, types, "false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssueDto searchHouseQmCheckTaskIssueByProjCategoryKeyAreaId(HashMap<String, Object> condiMap) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", condiMap.get("projectId")).andIn("typ", (List<Integer>) condiMap.get("types")).andLike("categoryPathAndKey", "%" + (String) condiMap.get("categoryKey") + "%");
        if (condiMap.get("areaId") != null) {
            criteria.andLike("areaPathAndId", "%" + String.valueOf((Integer) condiMap.get("areaId")) + "%");
        }
        HouseQmCheckTaskIssueDto dto = new HouseQmCheckTaskIssueDto();
        int i = houseQmCheckTaskIssueMapper.selectCountByExample(example);
        example.orderBy("clientCreateAt");
        PageHelper.startPage((Integer) condiMap.get("page"), (Integer) condiMap.get("pageSize"));
        List<HouseQmCheckTaskIssue> qmCheckTaskIssueList = houseQmCheckTaskIssueMapper.selectByExample(example);
        dto.setTotal(i);
        dto.setItems(qmCheckTaskIssueList);
        return dto;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<IssueRepairCount> selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(HashMap<String, Object> condiMap) {
        return houseQmCheckTaskIssueMapper.selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(condiMap);
    }

    /**
     * @param task_id
     * @param issueUpdateTime
     * @param userIds
     * @param issueUuids
     * @return com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssue selectIdByTaskIdAndIdGtAndUpdateAtGtAndSenderIdInOrUuidInAndNoDeletedOrderById(Integer task_id, Date issueUpdateTime, List<Integer> userIds, List<String> issueUuids) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", task_id).andGreaterThan("id", 0).andGreaterThan("updateAt", issueUpdateTime);
        Example.Criteria criteria1 = example.createCriteria();
        if (userIds.size() > 0) {
            criteria1.andIn("senderId", userIds);
        }
        if (issueUuids.size() > 0) {
            criteria1.orIn("uuid", issueUuids);
        }
        example.and(criteria);
        example.and(criteria1);
        ExampleUtil.addDeleteAtJudge(example);
        example.orderBy("id").asc();
        return houseQmCheckTaskIssueMapper.selectOneByExample(example);
    }

    /**
     * @param userIds
     * @param task_id
     * @param issueUuids
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectUuidBySenderIdInOrTaskIdAndUuidIn(List<Integer> userIds, Integer task_id, List<String> issueUuids) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        if (userIds.size() > 0) criteria.andIn("senderId", userIds);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("taskId", task_id);
        if (issueUuids.size() > 0) criteria1.andIn("uuid", issueUuids);
        example.and(criteria);
        example.or(criteria1);
        return houseQmCheckTaskIssueMapper.selectByExample(example);
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
        criteria.andEqualTo("projectId", project_id).andEqualTo("taskId", task_id);
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
        return houseQmCheckTaskIssueMapper.selectByProjIdAndCategoryClsAndAreaPathAndIdLikeGroupByStatus(project_id, category_cls, areaPath);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssue selectByUuidAndNotDelete(String issueUuid) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uuid", issueUuid);
        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskIssueMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssue getByUuidUnscoped(String issueUuid) {//unscoped true
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uuid", issueUuid);
        return houseQmCheckTaskIssueMapper.selectOneByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssue getIssueByProjectIdAndUuid(Integer projectId, String issueUuid) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("uuid", issueUuid);
        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskIssueMapper.selectOneByExample(example);
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
        criteria.andEqualTo("projectId", projectId);
        criteria.andIn("id", issueIds);
        criteria.andIn("status", statusList);
        return houseQmCheckTaskIssueMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssue selectByTaskIdAndUuidAndNotDel(Integer taskId, String issueUuid) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        example.createCriteria().andEqualTo("taskId", taskId).andEqualTo("uuid", issueUuid).andIsNull("deleteAt");
        return houseQmCheckTaskIssueMapper.selectOneByExample(example);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int add(HouseQmCheckTaskIssue issue) {
        return    houseQmCheckTaskIssueMapper.insert(issue);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> selectByUuids(List<String> issueUuids) {
        Example example = new Example(HouseQmCheckTaskIssue.class);
        example.createCriteria().andIn("uuid",issueUuids);
        example.orderBy("clientCreateAt");
        return houseQmCheckTaskIssueMapper.selectByExample(example);
    }

}
