package com.longfor.longjian.houseqm.app.vo.export;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.export
 * @ClassName: NodeVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/21 18:21
 */
@Data
@NoArgsConstructor
public class NodeVo implements Serializable {

    private NodeDataVo data;
    private List<NodeVo> child_list;

    public NodeVo(NodeDataVo data) {
        this.data = data;
        this.child_list = Lists.newArrayList();
    }
}
