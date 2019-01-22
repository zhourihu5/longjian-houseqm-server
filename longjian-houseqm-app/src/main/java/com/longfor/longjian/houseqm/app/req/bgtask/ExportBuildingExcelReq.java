package com.longfor.longjian.houseqm.app.req.bgtask;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.bgtask
 * @ClassName: ExportPicExcelReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/15 11:19
 */
@Data
@NoArgsConstructor
public class ExportBuildingExcelReq implements Serializable {

    @NotNull
    private Integer project_id;//项目ID
    @NotNull
    private Integer category_cls;//模块类型

    private Integer task_id=0;//任务ID
    private String category_key="";//任务类型
    private String check_item_key="";//检查项类型
    private String area_ids="";//区域ID列表,多个用半角逗号“,”分隔
    private String status_in="";//问题状态列表,多个用半角逗号“,”分隔

    private Integer checker_id=0;//检查人ID
    private Integer repairer_id=0;//整改人ID
    private Integer type=0;//问题类型
    private Integer condition=0;//严重程度
    private String key_word="";//关键词
    private String create_on_begin="";//开始时间范围
    private String create_on_end="";//结束时间范围
    private Boolean is_overdue=false;//是否超期



}
