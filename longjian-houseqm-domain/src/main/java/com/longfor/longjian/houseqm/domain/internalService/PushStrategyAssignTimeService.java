package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.PushStrategyAssignTimeMapper;
import com.longfor.longjian.houseqm.po.PushStrategyAssignTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/13 0013 19:30
 */
@Service
@Slf4j
@LFAssignDataSource("zhijian2_notify")
public class PushStrategyAssignTimeService {
    @Resource
    PushStrategyAssignTimeMapper pushStrategyAssignTimeMapper;

    /**
     *
     * @param taskIds
     * @return
     */
    public List<PushStrategyAssignTime> searchByTaskIds(Set<Integer> taskIds){
        return pushStrategyAssignTimeMapper.selectByTaskIds(taskIds,"false");
    }

}
