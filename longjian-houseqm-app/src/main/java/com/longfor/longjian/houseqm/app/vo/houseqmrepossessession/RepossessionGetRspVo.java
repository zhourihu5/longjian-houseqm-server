package com.longfor.longjian.houseqm.app.vo.houseqmrepossessession;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmrepossessession
 * @ClassName: RepossessionGetRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/12 10:38
 */
@Data
@NoArgsConstructor
public class RepossessionGetRspVo implements Serializable {

    private List<ApiRepossessionInfo> repossession_info;
    private List<ApiRepossessionMeterRecord> repossession_meter_record;
}
