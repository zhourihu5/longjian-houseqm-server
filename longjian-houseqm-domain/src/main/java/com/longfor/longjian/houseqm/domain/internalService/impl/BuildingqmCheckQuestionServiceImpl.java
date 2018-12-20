package com.longfor.longjian.houseqm.domain.internalService.impl;


import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.BuildingqmSettingMapper;
import com.longfor.longjian.houseqm.domain.internalService.BuildingqmCheckQuestionService;
import com.longfor.longjian.houseqm.po.IssueFieldSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/11.
 */
@Service
@Slf4j
public class BuildingqmCheckQuestionServiceImpl implements BuildingqmCheckQuestionService {
    @Resource
    private  BuildingqmSettingMapper buildingqmSettingMapper;
    @LFAssignDataSource("zhijian2_setting")
    public List<IssueFieldSetting> selectByProjectId(Integer projectId) {

        List<IssueFieldSetting> list = buildingqmSettingMapper.selectByIds(String.valueOf(projectId));

        return list;
    }


    @LFAssignDataSource("zhijian2_setting")
    public List<IssueFieldSetting> findProjectIdsAndModuleId(ArrayList<Integer> projectIdList) {
                return buildingqmSettingMapper.findProjectIdsAndModuleId(projectIdList);
    }
}
