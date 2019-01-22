package com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ProjectRepairerStatRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 12:13
 */
@Data
@NoArgsConstructor
public class ProjectRepairerStatRspVo implements Serializable {

    private List<ApiHouseQmRepairerStatVo> items;

}
