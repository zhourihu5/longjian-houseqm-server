package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.CategoryV3;

import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 16:51
 */
public interface CategoryV3Service {

    List<CategoryV3>  searchCategoryV3ByKeyInAndNoDeleted(List<String> categoryKeys);

}
