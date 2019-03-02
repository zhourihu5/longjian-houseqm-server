package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.HouseOwnerInfo;

import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalservice
 * @ClassName: HouseOwnerInfoService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 17:46
 */
public interface HouseOwnerInfoService {

    List<HouseOwnerInfo> searchHouseOwnerInfoByProjInAreaIdIn(List<Integer> projIds, List<Integer> houseIds);
}
