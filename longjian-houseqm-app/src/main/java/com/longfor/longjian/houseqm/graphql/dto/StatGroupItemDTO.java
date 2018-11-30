package com.longfor.longjian.houseqm.graphql.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/11/29 15:07
 */

@Data
@NoArgsConstructor
public class StatGroupItemDTO {

    private List<StatGroupDTO> statGroup;

}
