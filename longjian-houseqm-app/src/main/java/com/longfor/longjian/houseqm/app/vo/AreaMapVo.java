package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.po.Area;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Houyan
 * @date 2018/12/23 0023 13:51
 */
@Data
@NoArgsConstructor
public class AreaMapVo implements Serializable {
    private Map<Integer, Area> areas;
    private List<Area> list;


    public List<String> getPathNames(Integer id) {
        List<String> nams = Lists.newArrayList();

        Area area = get(id);
        List<Integer> ids = StringSplitToListUtil.splitToIdsComma(area.getPath(), "/");
        ids.add(area.getId());
        ids.forEach(aid -> {
            nams.add(getName(aid));
        });
        return nams;
    }
    public String getName(Integer id){
        if (areas.containsKey(id)){
            return areas.get(id).getName();
        }
        return "";
    }

    public Area get(Integer id) {
        return areas.get(id);
    }

public Map<Integer, Area>GetAreas(){
        return areas;
}
}
