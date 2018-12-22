package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * Created by Dongshun on 2018/12/18.
 */
public interface AreaMapper extends LFMySQLMapper<Area> {

    /**
     *
     * @param areaIds
     * @return
     */
    List<Area> selectByIdInAreaIds(@Param("areaIds") List<Integer> areaIds);

    /**
     *
     * @param areaId
     * @return
     */
    Area selectById(@Param("areaId") Integer areaId);

    /**
     *
     * @param projectId
     * @param areaIds
     * @return
     */
    List<Area> selectByProjectIdAndIdIn(@Param("projectId") Integer projectId,@Param("areaIds") List<Integer> areaIds);

    /**
     *
     * @param projectId
     * @param likePath
     * @param id
     * @param types
     * @return
     */
    List<Area> selectByProjectIdAndPathLikeOrIdAndTypeIn(@Param("projectId") Integer projectId,@Param("likePath") String likePath,@Param("id") Integer id,@Param("types") List<Integer> types);


    List<Area> selectByAreaIds(@Param(value = "idList") List<Integer> integers);

    List<Area> selectByFatherId(@Param(value="projectId")Integer prodectId, @Param(value="fatherId")int i);

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param areaPaths
     * @param deleted
     * @return java.util.List<com.longfor.longjian.houseqm.po.Area>
     */
    List<Area> selectAreaByIdInAndNoDeleted(@Param("areaPaths") List<Integer> areaPaths,@Param("deleted") String deleted);

}
