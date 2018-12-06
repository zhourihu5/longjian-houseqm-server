package com.longfor.longjian.houseqm.app.vo;

import com.longfor.longjian.houseqm.graphql.data.PassedVariableVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class StatItemsVo implements Serializable {

    private List<StatDataVo> items;
    private PassedVariableVo variableVo;
}


