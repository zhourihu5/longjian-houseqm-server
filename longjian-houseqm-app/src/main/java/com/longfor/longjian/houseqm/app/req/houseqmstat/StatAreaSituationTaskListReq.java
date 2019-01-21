package com.longfor.longjian.houseqm.app.req.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstat
 * @ClassName: StatAreaSituationTaskListReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/17 20:53
 */
@Data
@NoArgsConstructor
public class StatAreaSituationTaskListReq implements Serializable {

    @NotNull
    private Integer project_id;// 项目ID
    @NotNull
    private Integer area_id;// 区域ID
    @NotNull
    private Integer category_cls;// 模块类型

}
