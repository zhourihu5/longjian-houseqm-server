package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/3/6.
 */
@NoArgsConstructor
@Data
public class IssueMinStatus implements Serializable {
    private Integer count;
    private Integer minStatus;
}
