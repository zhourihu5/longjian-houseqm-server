package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lipeishuai on 2018/11/11.
 */
@Data
@NoArgsConstructor
public class CompanyListVo implements Serializable{

    private List<CompanyVo> items;
    private List<LevelVo> levels;

}
