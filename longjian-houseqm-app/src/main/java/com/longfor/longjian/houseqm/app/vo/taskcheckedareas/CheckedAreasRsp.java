package com.longfor.longjian.houseqm.app.vo.taskcheckedareas;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.taskcheckedareas
 * @ClassName: CheckedAreasRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/19 11:51
 */
@Data
@NoArgsConstructor
public class CheckedAreasRsp implements Serializable {

    private String check_area_ids;//楼栋IDs

}
