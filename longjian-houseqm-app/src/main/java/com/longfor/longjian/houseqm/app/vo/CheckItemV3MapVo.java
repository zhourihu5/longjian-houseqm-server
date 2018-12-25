package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.po.CategoryV3;
import com.longfor.longjian.houseqm.po.CheckItemV3;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Houyan
 * @date 2018/12/23 0023 14:24
 */
@Data
@NoArgsConstructor
public class CheckItemV3MapVo implements Serializable {
    private Map<String, CheckItemV3> checkItemV3Map;

    public List<String> getFullNamesByKey(String key){
        if (checkItemV3Map.containsKey(key)){
            return getFullNames(checkItemV3Map.get(key));
        }
        return Lists.newArrayList();
    }

    public List<String> getFullNames(CheckItemV3 checkItemV3){
        List<String> r = Lists.newArrayList();
        if (checkItemV3==null)return r;
        List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(checkItemV3.getPath(), "/", "/");
        list.add(checkItemV3.getKey());
        list.forEach(key -> {
            if (checkItemV3Map.containsKey(key)){
                r.add(checkItemV3Map.get(key).getName());
            }
        });
        return r;
    }

}
