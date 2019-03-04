package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/1/10.
 */
@Data
@NoArgsConstructor
public class RepairNotifyExportVo implements Serializable {
    private Integer result;
    private String message;
    private String path;
    private String filename;
}
