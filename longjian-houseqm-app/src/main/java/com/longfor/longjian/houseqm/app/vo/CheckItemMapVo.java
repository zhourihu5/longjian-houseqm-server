package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.po.zj2db.CheckItem;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Houyan
 * @date 2018/12/23 0023 14:24
 */
@Data
@NoArgsConstructor
public class CheckItemMapVo implements Serializable {
    private Map<String, CheckItem> checkItemV3Map;

    public List<String> getFullNamesByKey(String key) {
        if (checkItemV3Map.containsKey(key)) {
            return getFullNames(checkItemV3Map.get(key));
        }
        return Lists.newArrayList();
    }

    public List<String> getFullNames(CheckItem checkItemV3) {
        List<String> r = Lists.newArrayList();
        if (checkItemV3 == null) return r;
        List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(checkItemV3.getPath(), "/", "/");
        list.add(checkItemV3.getKey());
        list.forEach(key -> {
            if (checkItemV3Map.containsKey(key)) {
                r.add(checkItemV3Map.get(key).getName());
            }
        });
        return r;
    }

    public CheckItemMapVo NewCategoryMap(List<CheckItem> c) {
        CheckItemMapVo categoryV3MapVo = new CheckItemMapVo();
        HashMap<String, CheckItem> map = Maps.newHashMap();
        for (int i = 0; i < c.size(); i++) {
            map.put(c.get(i).getKey(), c.get(i));
        }
        categoryV3MapVo.setCheckItemV3Map(map);
        return categoryV3MapVo;
    }

    public String getNameByKey(String checkitemKey) {
        if (checkItemV3Map.containsKey(checkitemKey)) {
            return checkItemV3Map.get(checkitemKey).getName();
        }
        return "";
    }
}
