package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:37
 */
@Service
@Slf4j
public class HouseQmCheckTaskServiceImpl implements HouseQmCheckTaskService {


    @Resource
    HouseQmCheckTaskMapper houseQmCheckTaskMapper;


    /**
     *  根据TaskID 只拿出 未删除的
     *
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByTaskIds(Set<Integer> taskIds){
        return houseQmCheckTaskMapper.selectByTaskIds(taskIds,"false");
    }


    /**
     *  不限制，即使删除的，也要取出来
     *
     * @param taskIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIds){
        return houseQmCheckTaskMapper.selectByTaskIds(taskIds,"true");
    }

    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByProjectIdAndCategoryCls(Integer projectId, Integer categoryCls) {
        return houseQmCheckTaskMapper.selectByProjectIdAndCategoryCls(projectId, categoryCls, "false");
    }
    /**
     * 取未删除的数据
     * @param houseQmCheckTask
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> selectByProjectIdAndCategoryClsAndStatus(HouseQmCheckTask houseQmCheckTask){
        return houseQmCheckTaskMapper.selectByProjectIdAndCategoryClsAndStatus(houseQmCheckTask,"false");
    }

    /**
     *  根据项目id 任务id查
     * @param projectId
     * @param taskId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectByProjectIdAndTaskId(Integer projectId,Integer taskId){
        return houseQmCheckTaskMapper.selectByProjectIdAndTaskId(projectId,taskId);
    }

    /**
     *
     * @param projectId
     * @param categoryCls
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTask> searchByProjectIdAndCategoryClsIn(Integer projectId,List<Integer> categoryCls){
        return houseQmCheckTaskMapper.selectByProjectIdAndCategoryClsIn(projectId,categoryCls);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(Integer projectId, Integer taskId) {
        return houseQmCheckTaskMapper.selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(projectId,taskId,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int removeHouseQmCheckTaskByProjectIdAndTaskId(Integer project_id, Integer task_id) {
        Example example = new Example(HouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",project_id).andEqualTo("taskId",task_id);
        return houseQmCheckTaskMapper.deleteByExample(example);
    }

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param taskId
     * @return com.longfor.longjian.houseqm.po.HouseQmCheckTask
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTask selectUpdateAtByTaskIdAndNoDeleted(Integer taskId) {
        return houseQmCheckTaskMapper.selectUpdateAtByTaskIdAndNoDeleted(taskId,"false");
    }

}
