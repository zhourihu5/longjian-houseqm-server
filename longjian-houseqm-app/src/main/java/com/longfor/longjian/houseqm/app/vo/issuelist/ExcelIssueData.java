package com.longfor.longjian.houseqm.app.vo.issuelist;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issuelist
 * @ClassName: ExcelIssueData
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/16 13:42
 */
@Data
@NoArgsConstructor
public class ExcelIssueData implements Serializable {
    // self.issue_id = 0
    //        self.task_name = ''
    //        self.status_name = ''
    //        self.area_path = []
    //        self.category_name = ''
    //        self.condition_name = ''
    //        self.is_overdue = False
    //        self.checker = ''
    //        self.check_at = ''
    //        self.assigner = ''
    //        self.assign_at = ''
    //        self.repairer = ''
    //        self.repair_plan_end_on = ''
    //        self.repair_end_on = ''
    //        self.destroy_user = ''
    //        self.destroy_at = ''
    //        self.content = ''
    //        self.check_item = []
    private Integer issue_id=0;
    private String task_name="";
    private String status_name="";
    private List<String> area_path= Lists.newArrayList();
    private String category_name="";
    private String condition_name="";
    private Boolean is_overdue=false;//是否超期
    private String checker="";//检查人
    private String check_at="";
    private String assigner="";
    private String assign_at="";
    private String repairer="";
    private String repair_plan_end_on="";
    private String repair_end_on="";
    private String destroy_user="";
    private String destroy_at="";
    private String content="";
    private List<String> check_item= Lists.newArrayList();

}
