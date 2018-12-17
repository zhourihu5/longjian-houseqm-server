package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.AreaMapper;
import com.longfor.longjian.houseqm.po.Area;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/17 0017 15:14
 */
@Service
@Slf4j
public class AreaService {

    @Resource
    AreaMapper areaMapper;

    /**
     * 查询区域列表
     * @param areaIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<Area> selectAreasByIdInAreaIds(List<Integer> areaIds){
        return areaMapper.selectByIdInAreaIds(areaIds);
    }
}
