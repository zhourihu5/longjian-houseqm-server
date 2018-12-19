package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.AreaMapper;
import com.longfor.longjian.houseqm.po.Area;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/18.
 */

@Service
@Slf4j
public class AreaService {
    @Resource
    AreaMapper areaMapper;
    @LFAssignDataSource("zhijian2")
    public List<Area> selectByAreaIds(List<Integer> integers) {
       return  areaMapper.selectByAreaIds(integers);


    }


    @LFAssignDataSource("zhijian2")
    public List<Area> selectByFatherId(Integer prodectId,Integer i) {
        return areaMapper.selectByFatherId(prodectId,0);
    }
}
