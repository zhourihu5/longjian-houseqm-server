package com.longfor.longjian.houseqm.domain.internalService.impl.test;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseOwnerInfoMapper;
import com.longfor.longjian.houseqm.domain.internalservice.HouseOwnerInfoService;
import com.longfor.longjian.houseqm.po.zj2db.HouseOwnerInfo;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class HouseOwnerInfoServiceImpl implements HouseOwnerInfoService {

    @Resource
    private HouseOwnerInfoMapper houseOwnerInfoMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseOwnerInfo> searchHouseOwnerInfoByProjInAreaIdIn(List<Integer> projIds, List<Integer> houseIds) {
        Example example = new Example(HouseOwnerInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("projectId", projIds).andIn("areaId", houseIds);
        ExampleUtil.addDeleteAtJudge(example);
        return houseOwnerInfoMapper.selectByExample(example);
    }
}
