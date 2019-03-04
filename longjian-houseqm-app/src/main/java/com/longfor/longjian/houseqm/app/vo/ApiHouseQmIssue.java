package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/3/2.
 */
@Data
@NoArgsConstructor
public class ApiHouseQmIssue implements Serializable {
    private String uuid;
    private Integer proj_id;
    private Integer task_id;
    private Integer checker_id;
    private Integer repairer_id;
    private Integer area_id;
    private String area_path_and_id;
    private String category_key;
    private String category_path_and_key;
    private Integer sender_id;
    private Integer timestamp;
}