package com.longfor.longjian.houseqm.app.vo.houseqmrepossessession;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApiRepossessionInfo implements Serializable {
    private Integer id=0;

    private Integer project_id=0;
    private Integer task_id=0;

    private Integer area_id=0;

    private String area_path_and_id="";

    private String house_owner_name = "";

    private String house_owner_phone = "";

    private Integer status=0;

    private Integer status_client_update_at=0;

    private Boolean has_take_key=false;

    private Integer trust_key_count=0;

    private Integer key_client_update_at=0;

    private String accompany_sign_md5_list="";

    private Integer accompany_sign_client_update_at=0;

    private Integer sign_status=0;

    private String sign_comment="";

    private String sign_md5_list="";

    private Integer sign_client_update_at=0;

    private String remark="";

    private Integer expect_date=0;

    private Integer repair_status=0;

    private String repair_sign_md5_list="";

    private Integer repair_client_update_at=0;

    private Integer updated=0;

    private Integer deleted=0;

}
