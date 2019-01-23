package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/22.
 */
@Data
@NoArgsConstructor
public class ApiBuildingQmCheckTaskIssueDetailVo {
  private Integer  id =0;
    private Integer   team_id  =0;
    private String team_name ="";
    private Integer   project_id =0;
    private String project_name= "";
    private Integer   task_id =0;
    private String task_name="";
    private String  uuid ="";
    private Integer  plan_end_on =0;
    private Integer  end_on  =0;
    private Integer   area_id  =0;
    private String area_path_and_id="";
    private String  area_path_name  ="";
    private Integer   category_cls =0;
    private String category_key  ="";
    private String  category_path_and_key ="";
    private String   category_path_name ="";
    private String  check_item_key  ="";
    private String  check_item_path_and_key="";
    private String check_item_path_name ="";
    private String  drawing_url ="";
    private String   drawing_md5  ="";
    private Integer   pos_x   =0;
    private Integer   pos_y =0;
    private String title   ="";
    private String  content  ="";
    private Integer  condition =0;
    private Integer  status  =0;
    private List<ApiUserDetail> repairer ;
    private List<ApiUserDetail> repairer_followers;
   private List<ApiMemoAudioDetail> memo_audio_info_list ;
    private String  memo_audio_md5_list ="";
    private Integer  client_create_at  =0;
    private Integer   update_at =0;
    @NoArgsConstructor
    @Data
    public class ApiUserDetail{
      private Integer  user_id =0;
      private String  user_name="";

    }
    @NoArgsConstructor
    @Data
    public class ApiMemoAudioDetail{
      private String  url=""  ;
        private Integer  create_at =0;

    }

}
