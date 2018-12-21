package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.CheckItemV3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckItemV3Mapper extends LFMySQLMapper<CheckItemV3> {

    List<CheckItemV3> selectCheckItemyV3ByKeyInAndNoDeleted(@Param("checkItems") List<String> checkItems, @Param("deleted") String deleted);

}