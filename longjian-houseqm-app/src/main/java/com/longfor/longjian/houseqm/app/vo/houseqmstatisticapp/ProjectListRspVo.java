package com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ProjectListRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 18:01
 */
@Data
@NoArgsConstructor
public class ProjectListRspVo implements Serializable {

    private List<ApiHouseqmStatisticProjectList> items;
}
