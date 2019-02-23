package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:37
 */
@Transactional
@Service
@Slf4j
public class HouseQmCheckTaskServiceImpl implements HouseQmCheckTaskService {


    @Resource
    HouseQmCheckTaskMapper houseQmCheckTaskMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public Integer searchTotalByProjIdAndCategoryClsAndStatus(Integer projId, Integer category_cls, Integer status) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projId).andEqualTo("categoryCls",category_cls).andEqualTo("status",status);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectCountByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> searchByProjIdAndCategoryClsAndStatusByPage(Integer projId, Integer category_cls, Integer status, int limit, int start) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projId).andEqualTo("categoryCls",category_cls).andEqualTo("status",status);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectByExampleAndRowBounds(example,new RowBounds(start,limit));
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> searchByProjectIdAndCategoryClsInAndTaskIdIn(Integer project_id, List<Integer> category_cls_list, List<Integer> task_ids) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", project_id);
        if (category_cls_list.size() > 0) criteria.andIn("categoryCls", category_cls_list);
        if (task_ids.size() > 0) criteria.andIn("taskId", task_ids);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> searchByProjectIdInAndCategoryClsIn(List<Integer> project_ids, List<Integer> categoryClsList) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (project_ids.size() > 0) criteria.andIn("projectId", project_ids);
        criteria.andIn("categoryCls", categoryClsList);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> searchHouseQmCheckTaskByTaskIdIn(List<Integer> taskIds) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("taskId", taskIds);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectByExample(example);
    }

    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId).andEqualTo("taskId", taskId);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectOneByExample(example);
    }

    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask getHouseQmCheckTaskByProjTaskIdUnscoped(Integer projectId, Integer taskId) {//Unscoped true
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId).andEqualTo("taskId", taskId);
        return houseQmCheckTaskMapper.selectOneByExample(example);
    }

    /**
     * 根据TaskID 只拿出 未删除的
     *
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByTaskIds(Set<Integer> taskIds) {
        return houseQmCheckTaskMapper.selectByTaskIds(taskIds, "false");
    }


    /**
     * 不限制，即使删除的，也要取出来
     *
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIds) {
        if (CollectionUtils.isEmpty(taskIds))return Lists.newArrayList();
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("taskId", taskIds);
        return houseQmCheckTaskMapper.selectByExample(example);
    }

    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByProjectIdAndCategoryCls(Integer projectId, Integer categoryCls) {
        return houseQmCheckTaskMapper.selectByProjectIdAndCategoryCls(projectId, categoryCls, "false");
    }

    /**
     * 取未删除的数据
     *
     * @param houseQmCheckTask
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByProjectIdAndCategoryClsAndStatus(HouseQmCheckTask houseQmCheckTask) {
        return houseQmCheckTaskMapper.selectByProjectIdAndCategoryClsAndStatus(houseQmCheckTask, "false");
    }

    /**
     * 根据项目id 任务id查
     *
     * @param projectId
     * @param taskId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectByProjectIdAndTaskId(Integer projectId, Integer taskId) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId).andEqualTo("taskId", taskId);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectOneByExample(example);
    }

    /**
     * @param projectId
     * @param categoryCls
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> searchByProjectIdAndCategoryClsIn(Integer projectId, List<Integer> categoryCls) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId).andIn("categoryCls", categoryCls);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(Integer projectId, Integer taskId) {
        return houseQmCheckTaskMapper.selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(projectId, taskId, "false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int removeHouseQmCheckTaskByProjectIdAndTaskId(Integer project_id, Integer task_id) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", project_id).andEqualTo("taskId", task_id);
        ExampleUtil.addDeleteAtJudge(example);
        List<HouseQmCheckTask> houseQmCheckTasks = houseQmCheckTaskMapper.selectByExample(example);
        for (HouseQmCheckTask task : houseQmCheckTasks) {
            task.setDeleteAt(new Date());
            houseQmCheckTaskMapper.updateByPrimaryKey(task);
        }
        return houseQmCheckTasks.size();
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectByTaskId(Integer taskId) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskMapper.selectOneByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectByProjectIdAndTaskIdAndDel(Integer projectId, Integer taskId) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId).andEqualTo("taskId", taskId).andIsNull("deleteAt");
        return houseQmCheckTaskMapper.selectOneByExample(example);
    }
    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public int update(HouseQmCheckTask taskInfo) {
        taskInfo.setUpdateAt(new Date());
        return houseQmCheckTaskMapper.updateByPrimaryKeySelective(taskInfo);
    }

    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public int delete(HouseQmCheckTask houseQmCheckTask) {
        List<HouseQmCheckTask> tasks = houseQmCheckTaskMapper.select(houseQmCheckTask);
        for (HouseQmCheckTask task : tasks) {
            task.setUpdateAt(new Date());
            task.setDeleteAt(new Date());
            houseQmCheckTaskMapper.updateByPrimaryKeySelective(task);
        }
        return tasks.size();
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByProjectIdsAndCategoryClsNotDel(ArrayList<Integer> parentIds, List<Integer> categorylist) {
        Example example = new Example(HouseQmCheckTask.class);
        example.createCriteria().andIn("projectId",parentIds).andIn("categoryCls",categorylist).andIsNull("deleteAt");
        return houseQmCheckTaskMapper.selectByExample(example);
    }

    /**
     * @param taskId
     * @return com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectUpdateAtByTaskIdAndNoDeleted(Integer taskId) {
        return houseQmCheckTaskMapper.selectUpdateAtByTaskIdAndNoDeleted(taskId, "false");
    }
    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public int add(HouseQmCheckTask houseQmCheckTask) {
        houseQmCheckTask.setUpdateAt(new Date());
        houseQmCheckTask.setCreateAt(new Date());
        houseQmCheckTaskMapper.insert(houseQmCheckTask);
        return houseQmCheckTask.getId();
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectById(int one) {
        return houseQmCheckTaskMapper.selectByPrimaryKey(one);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask getByTaskId(Integer taskId) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId",taskId);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskMapper.selectOneByExample(example);
    }
}
