package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.IssueFieldSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/11.
 */

public interface BuildingqmSettingMapper extends LFMySQLMapper<IssueFieldSetting> {


    List<IssueFieldSetting> findProjectIdsAndModuleId(@Param(value = "projectIdList") ArrayList<Integer> projectIdList);
}
