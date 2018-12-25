package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.po.CategoryV3;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Houyan
 * @date 2018/12/23 0023 14:16
 */
@Data
@NoArgsConstructor
public class CategoryV3MapVo implements Serializable {

    private Map<String, CategoryV3> categoryV3Map;

    public List<String> getFullNamesByKey(String key){
        if (categoryV3Map.containsKey(key)){
            return getFullNames(categoryV3Map.get(key));
        }
        return Lists.newArrayList();
    }

    public List<String> getFullNames(CategoryV3 categoryV3){
        List<String> r = Lists.newArrayList();
        List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(categoryV3.getPath(), "/", "/");
        list.add(categoryV3.getKey());
        list.forEach(key -> {
           if (categoryV3Map.containsKey(key)){
               r.add(categoryV3Map.get(key).getName());
           }
        });
        return r;
    }
}
