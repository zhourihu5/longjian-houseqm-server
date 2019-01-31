package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.CheckItemV3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckItemV3Mapper extends LFMySQLMapper<CheckItemV3> {

    List<CheckItemV3> selectCheckItemyV3ByKeyInAndNoDeleted(@Param("checkItems") List<String> checkItems, @Param("deleted") String deleted);

}