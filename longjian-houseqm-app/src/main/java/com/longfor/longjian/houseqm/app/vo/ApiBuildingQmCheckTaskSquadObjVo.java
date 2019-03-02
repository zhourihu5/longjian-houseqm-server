package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/1/8.
 */
@Data
@NoArgsConstructor
public class ApiBuildingQmCheckTaskSquadObjVo implements Serializable {
    private Integer id = 0;
    private String name = "";
    private String user_ids = "";
    private String approve_ids = "";
    private String direct_approve_ids = "";
    private String reassign_ids = "";
}
