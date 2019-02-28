package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AppListFixingPresetVo implements Serializable {

    private Integer last_id;
    private List<AppFixingPresetProtoVo> items;

}
