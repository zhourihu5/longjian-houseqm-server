package com.longfor.longjian.houseqm.app.vo.export;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.po.zj2db.Area;
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


    /**
     * 不要构造整棵树
     * 速度很慢 @ToDo 待优化
     * 只需构建相关的树即可
     * by ss
     * from (go) ->zhijian_server_v3
     **/
    public static List<AreaNode> createAreaTree(List<Area> areas) {
        List<AreaNode> rootNode = Lists.newArrayList();
        for (Area item : areas) {
            if (item.getFatherId().equals(0)) {
                AreaNode node = new AreaNode();
                node.setArea(item);
                rootNode.add(node);
            }
        }
        rootNode.forEach(item -> {
            item.addChildren(areas);
        });
        return rootNode;
    }

    //获取森林下 areaid 下的所有区域 ，包含它自己
    public static List<Area> getSubTreeAllAreasByAreaId(List<AreaNode> nodes, List<Integer> areaIds) {
        List<AreaNode> subNodes = Lists.newArrayList();
        for (Integer areaId : areaIds) {
            for (AreaNode node : nodes) {
                AreaNode v = node.getChildrenNodeByAreaId(areaId);
                if (v != null) {
                    subNodes.add(v);
                    continue;
                }
            }
        }
        List<Area> r = Lists.newArrayList();
        if (subNodes.size() > 0) {
            for (AreaNode node : subNodes) {
                r.addAll(node.getSubAreas());
            }
        }
        return r;
    }

    //获取这个节点下的所有区域，包含自己
    public List<Area> getSubAreas() {
        List<Area> r= Lists.newArrayList();
        if (this.area != null) {
            r.add(this.area);
        }
        for (AreaNode item : this.children) {
            List<Area> areas = item.getSubAreas();
            r.addAll(areas);
        }
        return r;
    }

    //获取这个id下的节点
    public AreaNode getChildrenNodeByAreaId(int areaId) {
        if (this.area == null) return null;
        if (this.area.getId().equals(areaId)) return this;
        for (AreaNode item : this.children) {
            AreaNode node = item.getChildrenNodeByAreaId(areaId);
            if (node != null) return node;
        }
        return null;
    }

    //添加孩子 @ToDo 待优化
    public void addChildren(List<Area> areas) {
        for (Area item : areas) {
            if (item.getFatherId().equals(0)) {
                continue;
            }
            if (item.getFatherId().equals(this.area.getId())) {
                AreaNode node = new AreaNode();
                this.children.add(node);
                node.addChildren(areas);
            }
        }
    }


}
