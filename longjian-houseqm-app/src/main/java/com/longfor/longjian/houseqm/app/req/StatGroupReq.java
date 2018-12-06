package com.longfor.longjian.houseqm.app.req;

import com.longfor.longjian.houseqm.app.vo.VariableVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class StatGroupReq implements Serializable {

    private String query;
    private VariableVo variables;

}
