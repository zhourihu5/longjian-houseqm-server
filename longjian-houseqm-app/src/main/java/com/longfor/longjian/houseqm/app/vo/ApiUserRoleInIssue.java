package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Dongshun on 2019/1/24.
 */
@Data
@NoArgsConstructor
public class ApiUserRoleInIssue implements Serializable {
    private Map<RoleUser, Boolean> user_role = Maps.newHashMap();
    private Integer task_id = 0;

    @Data
    @NoArgsConstructor
    public class RoleUser implements Serializable {
        private Integer user_id;
        private Integer role_type;
    }

}
