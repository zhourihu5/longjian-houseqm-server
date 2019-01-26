package com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ProjectCheckerStatRspVo
 * @Description: 统计信息
 * @Author: hy
 * @CreateDate: 2019/1/22 10:24
 */
@Data
@NoArgsConstructor
public class ProjectCheckerStatRspVo implements Serializable {

    private List<ApiHouseQmCheckerStatVo> items;
}
