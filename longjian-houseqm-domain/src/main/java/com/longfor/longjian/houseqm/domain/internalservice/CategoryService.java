package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.CategoryV3;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */

public interface CategoryService {
    List<CategoryV3> searchCategoryByKeyIn(List<String> keys);

    List<CategoryV3> searchCategoryByFatherKey(String categoryRootKey);
}
