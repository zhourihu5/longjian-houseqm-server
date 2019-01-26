package com.longfor.longjian.houseqm.app.req.issue;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmissue
 * @ClassName: IssueBatchDeleteReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 18:45
 */
@Data
@NoArgsConstructor
public class IssueBatchDeleteReq implements Serializable {
    @NotNull
    private Integer project_id;// 项目ID
    @NotNull
    private String issue_uuids;// issue的UUID,多个用半角逗号“,”分隔
}
