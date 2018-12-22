package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */

public interface CategoryMapper extends LFMySQLMapper<Category> {
    List<Category> searchCategoryByKeyIn(@Param("idList")List<String> keys);

    List<Category> searchCategoryByFatherKey(@Param("fatherKey") String categoryRootKey);
}
