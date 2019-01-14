package com.longfor.longjian.houseqm.app.vo.export;

import com.longfor.longjian.houseqm.po.Area;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.export
 * @ClassName: AreaNode
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 11:40
 */
@Data
@NoArgsConstructor
public class AreaNode implements Serializable {

    private Area area;
    private List<AreaNode> children;



}
