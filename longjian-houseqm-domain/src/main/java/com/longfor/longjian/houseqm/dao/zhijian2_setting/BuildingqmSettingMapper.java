package com.longfor.longjian.houseqm.dao.zhijian2_setting;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zhijian2_setting.IssueFieldSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/11.
 */

public interface BuildingqmSettingMapper extends LFMySQLMapper<IssueFieldSetting> {


    List<IssueFieldSetting> findProjectIdsAndModuleId(@Param(value = "projectIdList") List<Integer> projectIdList);
}
