package com.longfor.longjian.houseqm.app.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lipeishuai on 2018/11/11.
 */

@Setter
@Getter
@NoArgsConstructor
public class LevelVo implements Serializable {
    private Integer value;
    private String name;
}
