package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.Area;

import java.util.List;

public interface AreaService {

    List<Area> selectAreasByIdInAreaIds(List<Integer> areaIds);

    Area selectById(Integer areaId);

    List<Integer> getIntersectAreas(List<Integer> areaIds,List<Integer> areaList);

    List<Area> searchAreaListByRootIdAndTypes(Integer projectId,List<Integer> rootIds,List<Integer> types);

    List<Area> selectByAreaIds(List<Integer> integers);

    List<Area> selectByFatherId(Integer prodectId,Integer i);

}
