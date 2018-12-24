package com.longfor.longjian.houseqm.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/20.
 */
@Data
@Table(name="category_v3")
public class Category implements Serializable {
    @Id
    private Integer id;
    @Column(name = "father_key")
    private String fatherKey;
    @Column(name = "key")
    private String key;               // 唯一编码
    @Column(name = "path")
    private String path;                      // 所在路径
    @Column(name = "cls")
    private Integer cls;             // 任务类型分类
    @Column(name = "root_category_id")
    private Integer rootCategoryId;          // 顶级任务类型Id
    @Column(name = "team_id")
    private Integer teamId;          // 所属公司Id
    @Column(name = "order")
    private String order;              // 排序编号
    @Column(name = "zj_std_key")
    private String zjStdKey;                            // 智检标准码
    @Column(name = "custom_key")
    private String customKey;    // 用户自定义识别码
    @Column(name = "name")
    private String name;               // 类型名称
    @Column(name = "desc")
    private String desc;            // 说明
    @Column(name = "file_md5")
    private String fileMd5;                     // 最近更新文件的Md5
    @Column(name = "node_status")
    private Integer nodeStatus;                          // 节点类型
    @Column(name = "create_at")
    private Date createAt;                            // 创建时间
    @Column(name = "update_at")
    private Date updateAt;                           // 最后更新时间
    @Column(name = "delete_at")
    private Date deleteAt;           // 删除时间
}
