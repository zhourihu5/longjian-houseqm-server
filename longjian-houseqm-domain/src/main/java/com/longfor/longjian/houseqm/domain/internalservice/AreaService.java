package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.Area;

import java.util.List;

public interface AreaService {

    String getRootRegexpConditionByAreaIds(List<Integer> areaIds);

    List<Area> searchRelatedAreaByAreaIdIn(Integer projectId, List<Integer> areaIds);

    List<Area> selectAreasByIdInAreaIds(List<Integer> areaIds);

    Area selectById(Integer areaId);

    List<Integer> getIntersectAreas(List<Integer> areaIds, List<Integer> areaList);

    List<Area> searchAreaListByRootIdAndTypes(Integer projectId, List<Integer> rootIds, List<Integer> types);

    List<Area> selectByAreaIds(List<Integer> integers);

    List<Area> selectByFatherId(Integer prodectId, Integer i);

    /**
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.Area>
     * @author hy
     * @date 2018/12/21 0021
     * @param areaPaths
     */
    List<Area> searchAreaByIdInAndNoDeleted(List<Integer> areaPaths);
}
