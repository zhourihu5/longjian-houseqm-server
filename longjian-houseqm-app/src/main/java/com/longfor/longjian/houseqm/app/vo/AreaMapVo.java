package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.consts.AreaTypeEnum;
import com.longfor.longjian.houseqm.po.Area;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
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
        List<String> names = Lists.newArrayList();
        Area area = this.get(id);
        List<Integer> ids = StringSplitToListUtil.splitToIdsComma(area.getPath(), "/");
        ids.add(area.getId());
        ids.forEach(aid -> {
            names.add(getName(aid));
        });
        return names;
    }

    public String getName(Integer id) {
        if (areas.containsKey(id)) {
            return areas.get(id).getName();
        } else
            return "";
    }

    public Map<String, String> getAllNames(int id) {
        Map<String, String> map = Maps.newHashMap();
        String room = "";
        String house = "";
        String floor = "";
        String building = "";
        Area area = get(id);
        if (area == null) return null;
        List<Integer> totalIds = StringSplitToListUtil.strToInts(area.getPath(), "/");
        totalIds.add(area.getId());
        for (int i = totalIds.size() - 1; i >= 0; i--) {
            Area area1 = get(totalIds.get(i));
            if (area1 != null) {
                AreaTypeEnum e = null;
                for (AreaTypeEnum value : AreaTypeEnum.values()) {
                    if (value.getId().equals(area.getType())) e = value;
                }
                if (e != null) {
                    switch (e) {
                        case ROOM:
                            room = area.getName();
                            break;
                        case HOUSE:
                        case FLOOR_PUBLIC:
                            house = area.getName();
                            break;
                        case FLOOR:
                            floor = area.getName();
                            break;
                        case BUILDING:
                        case VILLA:
                            building = area.getName();
                            break;
                    }
                }
            }
        }
        map.put("room", room);
        map.put("house", house);
        map.put("floor", floor);
        map.put("building", building);
        return map;
    }

    public Area get(Integer id) {
        if (this.areas == null) return null;
        return areas.get(id);
    }

    public Map<Integer, Area> GetAreas() {
        if (areas == null)
            return new HashMap<Integer, Area>();
        else return areas;
    }

}
