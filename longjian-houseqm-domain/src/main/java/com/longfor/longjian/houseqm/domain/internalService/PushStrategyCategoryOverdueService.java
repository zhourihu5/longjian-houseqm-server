package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.PushStrategyCategoryOverdueMapper;
import com.longfor.longjian.houseqm.po.PushStrategyCategoryOverdue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/13 0013 19:46
 */
@Service
@Slf4j
@LFAssignDataSource("zhijian2_notify")
public class PushStrategyCategoryOverdueService {

    @Resource
    PushStrategyCategoryOverdueMapper pushStrategyCategoryOverdueMapper;
    /**
     *
     * @param taskIds
     * @return
     */
    public List<PushStrategyCategoryOverdue> searchByTaskIds(Set<Integer> taskIds){
        return pushStrategyCategoryOverdueMapper.selectByTaskIds(taskIds,"false");
    }
}
