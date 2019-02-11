package com.longfor.longjian.houseqm.app.req.houseqmrepossession;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmrepossession
 * @ClassName: RepossessionGetReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 15:16
 */
@Data
public class RepossessionGetReq implements Serializable {

    @NotBlank
    @Length(max = 1024,min = 1)
    private String task_ids;

    private Integer timestamp;
}
