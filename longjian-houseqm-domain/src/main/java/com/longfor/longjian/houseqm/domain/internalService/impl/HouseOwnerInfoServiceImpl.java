package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseOwnerInfoMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseOwnerInfoService;
import com.longfor.longjian.houseqm.po.zj2db.HouseOwnerInfo;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService.impl
 * @ClassName: HouseOwnerInfoServiceImpl
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 17:46
 */
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
        criteria.andIn("projectId",projIds).andIn("areaId",houseIds);
        ExampleUtil.addDeleteAtJudge(example);
        return houseOwnerInfoMapper.selectByExample(example);
    }
}
