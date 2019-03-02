package com.longfor.longjian.houseqm.app.req.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ProjectIssueStatReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 17:58
 */
@Data
@NoArgsConstructor
public class ProjectIssueStatReq implements Serializable {

    @NotNull
    private Integer project_id;//项目ID
    @NotNull
    private String source;//app名称，如：ydyf、gcgl

    private Integer area_id = 0;//区域ID，留空或为0则表示所有
    private Integer timestamp = 0;//上次更新时间
}
