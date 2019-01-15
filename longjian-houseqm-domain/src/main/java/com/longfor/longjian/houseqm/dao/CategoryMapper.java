package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.Category;
import com.longfor.longjian.houseqm.po.CategoryV3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */

public interface CategoryMapper extends LFMySQLMapper<Category> {
    List<CategoryV3> searchCategoryByKeyIn(@Param("idList")List<String> keys);

    List<CategoryV3> searchCategoryByFatherKey(@Param("fatherKey") String categoryRootKey);
}
