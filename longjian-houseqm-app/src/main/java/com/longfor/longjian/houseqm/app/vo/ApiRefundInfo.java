package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/3/2.
 */
@Data
@NoArgsConstructor
public class ApiRefundInfo implements Serializable {
    private Integer repairer;
    private Integer checker;
}
