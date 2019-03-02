package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.AreaMapper;
import com.longfor.longjian.houseqm.domain.internalservice.AreaService;
import com.longfor.longjian.houseqm.po.zj2db.Area;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import com.longfor.longjian.houseqm.util.StringUtil;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Houyan
 * @date 2018/12/17 0017 15:14
 */

/**
 * Created by Dongshun on 2018/12/18.
 */
@Service
@Slf4j
public class AreaServiceImpl implements AreaService {

    @Resource
    AreaMapper areaMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public String getRootRegexpConditionByAreaIds(List<Integer> areaIds) {
        List<Area> areas = selectAreasByIdInAreaIds(areaIds);
        List<String> idsStr = Lists.newArrayList();
        List<String> areaPaths = Lists.newArrayList();
        for (Area area : areas) {
            areaPaths.add(String.format("%s%d/", area.getPath(), area.getId()));
            idsStr.add(String.format("/%d/", area.getId()));
        }
        Collections.sort(areaPaths);
        for (int i = 0; i < areaPaths.size(); i++) {//areaPaths[i] = fmt.Sprintf("(%s[^,]*)*", path)
            String path = areaPaths.get(i);
            areaPaths.remove(i);
            areaPaths.add(i, String.format("(%s[^,]*)*", path));
        }
        String pathsRegexp = "^" + StringSplitToListUtil.dataToString(areaPaths, ",{0,1}") + "$";
        String idsRegexp = StringSplitToListUtil.dataToString(idsStr, "|");
        return idsRegexp;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<Area> searchRelatedAreaByAreaIdIn(Integer project_id, List<Integer> areaIds) {
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", project_id).andIn("id", areaIds);
        ExampleUtil.addDeleteAtJudge(example);
        List<Area> fAreas = areaMapper.selectByExample(example);

        List<Integer> area_ids = Lists.newArrayList();
        for (Area item : fAreas) {
            if (item.getPath().startsWith("/")) {
                List<String> paths = StringSplitToListUtil.splitToStringComma(item.getPath(), "/");
                for (String path : paths) {
                    if (path.length() <= 0) {
                        continue;
                    }
                    Integer id = Integer.valueOf(path);
                    area_ids.add(id);
                }
            }
        }
        area_ids.addAll(areaIds);
        HashSet<Integer> set = new HashSet<>(area_ids);

        Example example1 = new Example(Area.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("projectId", project_id).andIn("id", set);
        for (Area item : fAreas) {
            criteria1.orLike("path", item.getPath() + item.getId() + "/%%");
        }
        ExampleUtil.addDeleteAtJudge(example1);
        return areaMapper.selectByExample(example1);
    }

    /**
     * 查询区域列表
     *
     * @param areaIds
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<Area> selectAreasByIdInAreaIds(List<Integer> areaIds) {
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", areaIds);
        ExampleUtil.addDeleteAtJudge(example);
        return areaMapper.selectByExample(example);
    }

    /**
     * 根据id查 delete_at == null
     *
     * @param areaId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public Area selectById(Integer areaId) {
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", areaId);
        ExampleUtil.addDeleteAtJudge(example);
        return areaMapper.selectOneByExample(example);
    }


    @LFAssignDataSource("zhijian2")
    public List<Integer> getIntersectAreas(List<Integer> areaIds, List<Integer> areaList) {
        // 取出两个ids中的所有path
        List<Integer> result = Lists.newArrayList();
        List<String> pathsA = Lists.newArrayList();
        List<String> pathsB = Lists.newArrayList();
        // 两边去重，取最大范围
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        if (areaIds.size() > 0) criteria.andIn("id", areaIds);
        else return result;
        ExampleUtil.addDeleteAtJudge(example);
        List<Area> areaA = areaMapper.selectByExample(example);
        for (Area area : areaA) {
            pathsA.add(area.getPath() + area.getId() + "/");
        }
        List<String> pathA = getUnionPaths(pathsA);

        Example example1 = new Example(Area.class);
        Example.Criteria criteria1 = example1.createCriteria();
        if (areaList.size() > 0) criteria1.andIn("id", areaList);
        else return result;
        ExampleUtil.addDeleteAtJudge(example1);
        List<Area> areaB = areaMapper.selectByExample(example1);
        for (Area area : areaB) {
            pathsB.add(area.getPath() + area.getId() + "/");
        }
        List<String> pathB = getUnionPaths(pathsB);

        // 遍历比较，发现一个包含另一个直接跳过
        for (String pA : pathA) {
            for (String pB : pathB) {
                if (pA.startsWith(pB)) {
                    List<Integer> ids = StringUtil.strToInts(pA, "/");
                    if (ids.size() > 0) result.add(ids.get(ids.size() - 1));
                    continue;
                }
                if (pB.startsWith(pA)) {
                    List<Integer> ids = StringUtil.strToInts(pB, "/");
                    if (ids.size() > 0) result.add(ids.get(ids.size() - 1));
                    continue;
                }
            }
        }
        return result.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
    }

    @LFAssignDataSource("zhijian2")
    public List<Area> searchAreaListByRootIdAndTypes(Integer projectId, List<Integer> rootIds, List<Integer> types) {
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        if (rootIds.size() > 0) criteria.andIn("id", rootIds);
        ExampleUtil.addDeleteAtJudge(example);
        List<Area> areas = areaMapper.selectByExample(example);

        List<Area> areaList = remainTopAreas(areas);

        List<Area> items = Lists.newArrayList();
        for (Area area : areaList) {
            String likePath = String.format("%s%d/%%", area.getPath(), area.getId());
            Example example1 = new Example(Area.class);
            Example.Criteria criteria1 = example1.createCriteria();
            Example.Criteria criteria2 = example1.createCriteria();
            criteria1.andEqualTo("projectId", projectId);
            criteria2.andLike("path", likePath).orEqualTo("id", area.getId());

            example1.and(criteria2);
            if (types.size() > 0) {
                criteria1.andIn("type", types);
            }
            ExampleUtil.addDeleteAtJudge(example1);
            List<Area> subAreas = areaMapper.selectByExample(example1);
            items.addAll(subAreas);
        }
        return items;
    }


    @LFAssignDataSource("zhijian2")
    public List<Area> selectByAreaIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) return Lists.newArrayList();
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids).andIsNull("deleteAt");
        return areaMapper.selectByExample(example);
    }


    @LFAssignDataSource("zhijian2")
    public List<Area> selectByFatherId(Integer prodectId, Integer i) {
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", prodectId).andEqualTo("fatherId", i).andIsNull("deleteAt");
        return areaMapper.selectByExample(example);
    }

    /**
     * @param areaPaths
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.Area>
     * @author hy
     * @date 2018/12/21 0021
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<Area> searchAreaByIdInAndNoDeleted(List<Integer> areaPaths) {
        return areaMapper.selectAreaByIdInAndNoDeleted(areaPaths, "false");
    }


    private List<String> getUnionPaths(List<String> paths) {
        Collections.sort(paths);
        String lastPath = "Nothing";
        List<String> result = Lists.newArrayList();
        for (String p : paths) {
            if (!p.startsWith(lastPath)) {//不包含
                lastPath = p;
                result.add(p);
            }
        }
        return result;
    }

    private List<Area> remainTopAreas(List<Area> areas) {
        List<String> paths = Lists.newArrayList();
        Map<String, Area> areaMap = Maps.newHashMap();
        for (Area area : areas) {
            String p = area.getPath() + area.getId() + "/";
            paths.add(p);
            areaMap.put(p, area);
        }
        Collections.sort(paths);

        List<String> remainPath = Lists.newArrayList();
        String lastPath = paths.get(0);

        for (int i = 1; i < paths.size(); i++) {
            if (paths.get(i).startsWith(lastPath)) {
            } else {
                remainPath.add(lastPath);
                lastPath = paths.get(i);
            }
        }
        remainPath.add(lastPath);

        List<Area> items = Lists.newArrayList();
        for (int i = 0; i < remainPath.size(); i++) {
            items.add(areaMap.get(remainPath.get(i)));
        }
        return items;
    }


}
