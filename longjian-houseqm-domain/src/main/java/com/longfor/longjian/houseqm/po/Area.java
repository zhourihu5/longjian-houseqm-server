package com.longfor.longjian.houseqm.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/17.
 */
@Data
@Table(name = "area")
public class Area implements Serializable {
    @Id
    private Integer id;        // 区域Id
    @Column(name = "project_id")
    private Integer projectId; // 所属项目Id Project.Id
    @Column(name = "name")
    private String name;    // 区域名称
    @Column(name="custom_code")
    private String customCode;
    @Column(name = "path")
    private String path;     // 区域路径
    @Column(name = "type")
    private Integer type;
    @Column(name = "area_class_id")// 区域类型：1，公区；2，楼栋；3，层；4，户；5，房间；99，其它
    private Integer areaClassId;   // 户型id
    @Column(name = "is_lock")
    private Integer isLock;// 是否被锁住
    @Column(name = "order_by")
    private Integer orderBy;   // 区域排序
    @Column(name = "drawing_md5")
    private String drawingMd5;   // 图纸文件Md5
    @Column(name = "location")
    private String location;    // 该区域多边形坐标集，必须闭合，格式：px1,py1|px2,py2|...
    @Column(name = "father_id")
    private Integer fatherId;   // 所属区域Id
    @Column(name = "create_at")
    private Date createAt;      // 创建时间
    @Column(name = "update_at")
    private Date updateAt;  // 更新时间
    @Column(name = "delete_at")
    private Date deleteAt;     // 删除时间
}
