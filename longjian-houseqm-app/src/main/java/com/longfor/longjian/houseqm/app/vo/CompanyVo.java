package com.longfor.longjian.houseqm.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lipeishuai on 2018/11/11.
 */

@ApiModel("公司信息")
@Setter
@Getter
@NoArgsConstructor
public class CompanyVo implements Serializable {

    @ApiModelProperty(value = "自增id")
    private int id;
    private String name;
    private int parent_id;
    private String path;
    private int level;
    private int display_index;
    private int is_group;

}
