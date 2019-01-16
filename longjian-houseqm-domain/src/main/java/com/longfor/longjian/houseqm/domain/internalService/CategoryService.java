package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.Category;
import com.longfor.longjian.houseqm.po.CategoryV3;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */

public interface CategoryService {
    List<CategoryV3> SearchCategoryByKeyIn(List<String> keys);

    List<CategoryV3> SearchCategoryByFatherKey(String categoryRootKey);
}
