package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:37
 */
@Service
@Slf4j
public class HouseQmCheckTaskService {


    @Resource
    HouseQmCheckTaskMapper houseQmCheckTaskMapper;


    /**
     *  根据TaskID 只拿出 未删除的
     *
     * @param taskIds
     * @return
     */
    public List<HouseQmCheckTask> selectByTaskIds(Set<Integer> taskIds){

        return houseQmCheckTaskMapper.selectByTaskIds(taskIds,"false");
    }


    /**
     *  不限制，即使删除的，也要取出来
     *
     * @param taskIds
     * @return
     */
    public List<HouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIds){

        return houseQmCheckTaskMapper.selectByTaskIds(taskIds,"true");
    }

}