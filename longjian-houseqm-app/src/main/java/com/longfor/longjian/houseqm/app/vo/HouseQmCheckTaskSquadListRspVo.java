package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/9.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskSquadListRspVo implements Serializable {
    private Integer id;
    private Integer squad_type;
    private String name;

    @Data
    @NoArgsConstructor
    public class HouseQmCheckTaskSquadListRspVoList implements Serializable {
        List<HouseQmCheckTaskSquadListRspVo> squad_list;
    }
}
