package com.longfor.longjian.houseqm.graphql.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/12/6 17:22
 */

@Data
@NoArgsConstructor
public class PassedVariableVo implements Serializable {

    private String categoryKey;
    private Integer timeFrameMax;
    private String timeFrameType;
    private List<Integer> teamIds;
    private Date dateField;

}
