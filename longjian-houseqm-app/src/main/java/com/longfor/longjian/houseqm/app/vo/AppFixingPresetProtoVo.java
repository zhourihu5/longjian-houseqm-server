package com.longfor.longjian.houseqm.app.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class AppFixingPresetProtoVo implements Serializable {
    private String id;
    private Integer project_id;
    private Integer area_id;
    private String root_category_key;
    private String category_key;
    private String check_item_key;
    private String user_ids;
    private Integer days;
    private Integer minutes;
}
