package com.longfor.longjian.houseqm.app.req.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ProjectListReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 17:52
 */
@Data
@NoArgsConstructor
public class ProjectListReq implements Serializable {

    @NotNull
    private String source;//app名称，如：ydyf、gcgl

    private Integer timestamp=0;//上次更新时间
}
