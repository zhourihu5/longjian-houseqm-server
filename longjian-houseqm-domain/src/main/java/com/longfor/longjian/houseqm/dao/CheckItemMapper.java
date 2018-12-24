package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */

public interface CheckItemMapper extends LFMySQLMapper<CheckItem> {
    List<CheckItem> searchCheckItemByKeyIn(@Param(value = "idList") List<String> keys);
}
