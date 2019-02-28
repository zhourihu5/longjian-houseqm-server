package com.longfor.longjian.houseqm.app.vo.houseqmissue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmissue
 * @ClassName: HouseQmIssue
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 16:58
 */
@Data
@NoArgsConstructor
public class HouseQmIssue implements Serializable {

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
    private Long timestamp;

    public HouseQmIssue(String uuid, int proj_id, int task_id, int checker_id, int repairer_id, int area_id, String area_path_and_id, String category_key, String category_path_and_key, int sender_id, long timestamp) {
        this.uuid = uuid;
        this.proj_id = proj_id;
        this.task_id = task_id;
        this.checker_id = checker_id;
        this.repairer_id = repairer_id;
        this.area_id = area_id;
        this.area_path_and_id = area_path_and_id;
        this.category_key = category_key;
        this.category_path_and_key = category_path_and_key;
        this.sender_id = sender_id;
        this.timestamp = timestamp;
    }
}
