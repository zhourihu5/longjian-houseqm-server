package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class StatTeamReq implements Serializable {

    private String query;
    private VariableVo variables;


    @Data
    @NoArgsConstructor
    public class VariableVo implements Serializable{

        private String categoryKey;
        private int timeFrameMax;
        private String timeFrameType;

    }


}
