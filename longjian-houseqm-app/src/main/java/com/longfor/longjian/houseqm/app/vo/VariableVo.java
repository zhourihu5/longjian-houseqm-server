package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/12/6 17:22
 */

@Data
@NoArgsConstructor
public class VariableVo implements Serializable {

    private String categoryKey;
    private Integer timeFrameMax;
    private String timeFrameType;
    private List<Integer> teamIds;
    private String timeFrameEnd;
    private String timeFrameBegin;

}
