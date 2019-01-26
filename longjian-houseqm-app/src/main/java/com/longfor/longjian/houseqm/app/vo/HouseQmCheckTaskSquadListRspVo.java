package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/1/9.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskSquadListRspVo implements Serializable {
   private Integer id;
    private Integer  squad_type;
    private String   name;
}
