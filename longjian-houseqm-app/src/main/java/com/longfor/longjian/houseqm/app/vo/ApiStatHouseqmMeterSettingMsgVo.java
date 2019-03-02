package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/21.
 */
@Data
@NoArgsConstructor
public class ApiStatHouseqmMeterSettingMsgVo implements Serializable {
    private Integer project_id = 0;
    private Integer item_id = 0;
    private String item_name = "";
    private Integer input_type = 0;
    private String sub_setting = "";

    @Data
    @NoArgsConstructor
    public class HouseqmMeterSetting implements Serializable {
        List<ApiStatHouseqmMeterSettingMsgVo> acceptance_items;
    }
}
