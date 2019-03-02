package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.po.zj2db.CategoryV3;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Houyan
 * @date 2018/12/23 0023 14:16
 */
@Data
@NoArgsConstructor
public class CategoryMapVo implements Serializable {

    private Map<String, CategoryV3> categoryV3Map;

    public static CategoryMapVo NewCategoryMap(List<CategoryV3> c) {
        CategoryMapVo categoryV3MapVo = new CategoryMapVo();
        HashMap<String, CategoryV3> map = Maps.newHashMap();
        for (int i = 0; i < c.size(); i++) {
            map.put(c.get(i).getKey(), c.get(i));
        }
        categoryV3MapVo.setCategoryV3Map(map);
        return categoryV3MapVo;
    }

    public List<String> getFullNamesByKey(String key) {
        if (categoryV3Map.containsKey(key)) {
            return getFullNames(categoryV3Map.get(key));
        }
        return Lists.newArrayList();
    }

    public List<String> getFullNames(CategoryV3 categoryV3) {
        List<String> r = Lists.newArrayList();
        List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(categoryV3.getPath(), "/", "/");
        list.add(categoryV3.getKey());
        list.forEach(key -> {
            if (categoryV3Map.containsKey(key)) {
                r.add(categoryV3Map.get(key).getName());
            }
        });
        return r;
    }

    public String getNameByKey(String categoryKey) {
        if (categoryV3Map.containsKey(categoryKey)) {
            return categoryV3Map.get(categoryKey).getName();
        }
        return "";
    }
}
