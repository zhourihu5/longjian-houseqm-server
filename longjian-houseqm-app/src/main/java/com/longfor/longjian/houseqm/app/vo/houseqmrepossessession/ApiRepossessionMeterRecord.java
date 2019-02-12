package com.longfor.longjian.houseqm.app.vo.houseqmrepossessession;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApiRepossessionMeterRecord implements Serializable {
    @JsonProperty("id")
    private Integer id = 0;
    @JsonProperty("uuid")
    private String uuid = "";
    @JsonProperty("task_id")
    private Integer task_id = 0;
    @JsonProperty("area_id")
    private Integer area_id = 0;
    @JsonProperty("repossession_id")
    private Integer repossession_id = 0;

    @JsonProperty("item_id")
    private Integer meter_type = 0;//'验收项目id，目前暂时保持与meter_type值一致'
    @JsonProperty("content")
    private String content = "";
    @JsonProperty("drawing_md5_list")
    private String drawing_md5_list = "";
    @JsonProperty("client_write_at")
    private Integer client_write_at = 0;
    @JsonProperty("updated")
    private Integer updated = 0;
    @JsonProperty("deleted")
    private Integer deleted = 0;

}
