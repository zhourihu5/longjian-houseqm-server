package com.longfor.longjian.houseqm.app.service.impl;

import com.longfor.longjian.houseqm.app.service.IHouseqmExportService;
import com.longfor.longjian.houseqm.app.vo.export.ProjectOrdersVo;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.po.Area;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.service.impl
 * @ClassName: HouseqmExportServiceImpl
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 10:08
 */
@Service
@Slf4j
public class HouseqmExportServiceImpl implements IHouseqmExportService {

    @Resource
    private AreaService areaService;

    @Override
    public List<ProjectOrdersVo> searchProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(Integer project_id, Integer task_id, List<Integer> areaIds, Integer repairer_id, Date begin_on, Date end_on, Integer category_cls) {
        //取出和这些areaids相关的区域 后面用来创建树使用
        List<Area> areas=areaService.searchRelatedAreaByAreaIdIn(project_id,areaIds);

        //创建这颗树


        return null;
    }

    @Override
    public String exportProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(Integer project_id, Integer task_id, List<Integer> areaIds, Integer repairer_id, Date begin_on, Date end_on, Integer category_cls, boolean withRule) {
        List<ProjectOrdersVo> res = searchProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(project_id, task_id, areaIds, repairer_id, begin_on, end_on, category_cls);
        if (res.size() <= 0) {
            return "";
        }
        return null;
    }
}
