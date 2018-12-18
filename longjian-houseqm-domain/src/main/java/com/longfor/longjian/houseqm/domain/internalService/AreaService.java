package com.longfor.longjian.houseqm.domain.internalService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.AreaMapper;
import com.longfor.longjian.houseqm.po.Area;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Houyan
 * @date 2018/12/17 0017 15:14
 */
@Service
@Slf4j
public class AreaService {

    @Resource
    AreaMapper areaMapper;

    /**
     * 查询区域列表
     * @param areaIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<Area> selectAreasByIdInAreaIds(List<Integer> areaIds){
        return areaMapper.selectByIdInAreaIds(areaIds);
    }

    /**
     * 根据id查
     * @param areaId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public Area selectById(Integer areaId){
        return areaMapper.selectById(areaId);
    }

    /**
     *
     * @param areaIds
     * @param areaList
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<Integer> getIntersectAreas(List<Integer> areaIds,List<Integer> areaList){
        List<Integer> result = Lists.newArrayList();
        List<String> pathsA = Lists.newArrayList();
        List<String> pathsB = Lists.newArrayList();
        List<Area> areaA = areaMapper.selectByIdInAreaIds(areaIds);
        for (Area area : areaA) {
            pathsA.add(area.getPath()+area.getId()+"/");
        }
        List<String> pathA = getUnionPaths(pathsA);
        List<Area> areaB = areaMapper.selectByIdInAreaIds(areaList);
        for (Area area : areaB) {
            pathsB.add(area.getPath()+area.getId()+"/");
        }
        List<String> pathB = getUnionPaths(pathsB);
        // 遍历比较，发现一个包含另一个直接跳过
        for (String pA : pathA) {
            for (String pB : pathB) {
                if (pA.startsWith(pB)){
                    List<Integer> ids = splitToIds(pA, "/");
                    result.add(ids.get(ids.size()-1));
                    continue;
                }
                if (pB.startsWith(pA)){
                    List<Integer> ids = splitToIds(pB, "/");
                    result.add(ids.get(ids.size()-1));
                    continue;
                }
            }
        }
        return result;
    }

    /**
     *
     * @param projectId
     * @param rootIds
     * @param types
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<Area> searchAreaListByRootIdAndTypes(Integer projectId,List<Integer> rootIds,List<Integer> types){
        List<Area> areas=areaMapper.selectByProjectIdAndIdIn(projectId,rootIds);
        List<Area> items = Lists.newArrayList();
        List<Area> areaList = remainTopAreas(areas);
        for (Area area : areaList) {
            String likePath=area.getPath()+area.getId()+"/%%";
            List<Area> areas1=areaMapper.selectByProjectIdAndPathLikeOrIdAndTypeIn(projectId,likePath,area.getId(),types);
            items.addAll(areas1);
        }
        return items;
    }

    /**
     * 字符串筛选
     * @param paths
     * @return
     */
    private List<String> getUnionPaths(List<String> paths){
        Collections.sort(paths);
        String lastPath="Nothing";
        List<String> result= Lists.newArrayList();
        for (String p : paths) {
            if (p.startsWith(lastPath)){//不包含
                lastPath=p;
                result.add(p);
            }
        }
        return result;
    }

    /**
     * 字符串切割，并转换成Integer集合
     * @param idstr
     * @param sep
     * @return
     */
    private List<Integer> splitToIds(String idstr,String sep){
        List<Integer> result=Lists.newArrayList();
        String[] ids = idstr.split(sep);
        for (String id : ids) {
            id.trim();
            if (id.equals(""))continue;
            int i = Integer.parseInt(id);
            result.add(i);
        }
        return result;
    }

    /**
     *
     * @param areas
     * @return
     */
    private List<Area> remainTopAreas(List<Area> areas){
        List<String> paths = Lists.newArrayList();
        Map<String, Area> areaMap = Maps.newHashMap();
        for (Area area : areas) {
            String p=area.getPath()+area.getId()+"/";
            paths.add(p);
            areaMap.put(p, area);
        }
        Collections.sort(paths);
        List<String> remainPath = Lists.newArrayList();
        String lastPath=paths.get(0);
        for (int i=1;i<paths.size();i++){
            if (paths.get(i).startsWith(lastPath))continue;
            else {
                remainPath.add(lastPath);
                lastPath=paths.get(i);
            }
        }
        remainPath.add(lastPath);
        ArrayList<Area> items = Lists.newArrayList();
        for (int i=0;i<remainPath.size();i++){
            items.add(areaMap.get(remainPath.get(i)));
        }
        return items;
    }

}
