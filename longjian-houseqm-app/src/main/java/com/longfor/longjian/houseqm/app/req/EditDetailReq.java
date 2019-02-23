package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req
 * @ClassName: EditDetailReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/23 15:28
 */
@Data
@NoArgsConstructor
public class EditDetailReq implements Serializable {

    //project_id=required.IntegerField(desc='项目ID'),
    //            issue_uuid=required.StringField(desc='issue的UUID'),
    //            typ=required.IntegerField(desc='更新类型'),
    //            data=required.StringField(desc='更新的内容')
    @NotNull
    private Integer project_id;
    @NotNull
    private String issue_uuid;
    @NotNull
    private Integer typ;
    @NotNull
    private String data;
}
