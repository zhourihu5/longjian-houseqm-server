package com.longfor.longjian.houseqm.app.vo.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstat
 * @ClassName: StatCategoryStatRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 11:06
 */
@Data
@NoArgsConstructor
public class StatCategoryStatRspVo implements Serializable {

    private Integer issue_count;
    private List<HouseQmStatCategorySituationRspVo> items;
}
