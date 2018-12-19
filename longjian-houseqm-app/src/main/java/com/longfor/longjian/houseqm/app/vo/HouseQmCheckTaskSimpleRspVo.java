package com.longfor.longjian.houseqm.app.vo;

import com.sun.javafx.collections.MappingChange;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Dongshun on 2018/12/14.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskSimpleRspVo implements Serializable {
    private Integer projectId;
    private Integer taskId;
    private String name;
    private Integer areaTypes;
    private Date planBeginOn;
    private Date planEndOn;
    private Date createAt;
}
