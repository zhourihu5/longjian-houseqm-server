package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.CategoryV3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryV3Mapper extends LFMySQLMapper<CategoryV3> {

    List<CategoryV3> selectCategoryV3ByKeyInAndNoDeleted(@Param("categoryKeys") List<String> categoryKeys, @Param("deleted") String deleted);
}