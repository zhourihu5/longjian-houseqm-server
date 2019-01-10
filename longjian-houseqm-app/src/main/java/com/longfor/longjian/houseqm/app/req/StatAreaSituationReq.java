package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req
 * @ClassName: StatAreaSituationReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 15:34
 */
@Data
@NoArgsConstructor
public class StatAreaSituationReq implements Serializable {

    //ProjectId   int `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"`     // 项目ID
    //	AreaId      int `json:"area_id" zpf_reqd:"true" zpf_name:"area_id"`           // 区域ID
    //	CategoryCls int `json:"category_cls" zpf_reqd:"true" zpf_name:"category_cls"` // 模块类型
    @NotNull
    private Integer project_id;
    @NotNull
    private Integer area_id;
    @NotNull
    private Integer category_cls;

}
