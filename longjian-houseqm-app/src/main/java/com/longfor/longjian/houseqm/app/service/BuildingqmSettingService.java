package com.longfor.longjian.houseqm.app.service;

import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.domain.internalService.BuildingqmCheckQuestionService;
import com.longfor.longjian.houseqm.po.IssueFieldSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Dongshun on 2018/12/11.
 */
@Repository
@Service
@Slf4j
public class BuildingqmSettingService {
    @Resource
    private BuildingqmCheckQuestionService buildingqmCheckQuestionService;


    public LjBaseResponse<IssueFieldSetting> get_issuefiled_setting(List<String> projectIds, Integer timestamp) throws ParseException {
        ArrayList<Integer> projectIdList = new ArrayList<>();
        ArrayList<IssueFieldSetting> issueFileds = new ArrayList<>();
        for (int i = 0; i < projectIds.size(); i++) {
            projectIdList.add(Integer.parseInt(projectIds.get(i)));
        }
        //通过projectId查询IssueFieldSetting集合
        List<IssueFieldSetting> settingList = buildingqmCheckQuestionService.findProjectIdsAndModuleId(projectIdList);
      //key为projectId，value为IssueFieldSetting对象的集合
        HashMap<Integer, List<IssueFieldSetting>> projectMap = Maps.newHashMap();
      //用于存放IssueFieldSetting对象
        ArrayList<IssueFieldSetting> list = new ArrayList<>();
        for (int j = 0; j < projectIdList.size(); j++) {
            for (int i = 0; i < settingList.size(); i++) {
                IssueFieldSetting issueFieldSetting = settingList.get(i);
                list.add(issueFieldSetting);

            }

            projectMap.put(projectIdList.get(j), list);
        }
        for (int i = 0; i < projectIdList.size(); i++) {
            //判断map中是否含有projectId
            if (projectMap.containsKey(projectIdList.get(i))) {
                //获取map里的list集合
                for (Map.Entry<Integer, List<IssueFieldSetting>> entry : projectMap.entrySet()) {
                    for (int j = 0; j < entry.getValue().size(); j++) {
                        Date date = transForDate(timestamp);
                        //对比时间戳
                        if (entry.getValue().get(j).getUpdateAt().before(date)) {
                            IssueFieldSetting model = new IssueFieldSetting();
                            model.setId(entry.getValue().get(j).getId());
                            model.setProjectId(entry.getValue().get(j).getProjectId());
                            model.setFieldId(entry.getValue().get(j).getFieldId());
                            model.setName(entry.getValue().get(j).getName());
                            model.setAlias(entry.getValue().get(j).getAlias());
                            model.setDisplayStatus(entry.getValue().get(j).getDisplayStatus());
                            model.setRequiredStatus(entry.getValue().get(j).getRequiredStatus());
                            model.setAliasStatus(entry.getValue().get(j).getAliasStatus());
                            model.setModuleId(entry.getValue().get(j).getModuleId());
                            model.setModifyUserId(entry.getValue().get(j).getModifyUserId());
                            model.setCreateAt(entry.getValue().get(j).getCreateAt());
                            model.setUpdateAt(entry.getValue().get(j).getUpdateAt());
                            model.setDeleteAt(entry.getValue().get(j).getDeleteAt());
                            issueFileds.add(model);
                        }
                            IssueFieldSetting model = new IssueFieldSetting();
                            model.setId(entry.getValue().get(j).getId());
                            model.setProjectId(entry.getValue().get(j).getProjectId());
                            model.setFieldId(entry.getValue().get(j).getFieldId());
                            model.setName(entry.getValue().get(j).getName());
                            model.setAlias(entry.getValue().get(j).getAlias());
                            model.setDisplayStatus(entry.getValue().get(j).getDisplayStatus());
                            model.setRequiredStatus(entry.getValue().get(j).getRequiredStatus());
                            model.setAliasStatus(entry.getValue().get(j).getAliasStatus());
                            model.setModuleId(entry.getValue().get(j).getModuleId());
                            model.setModifyUserId(entry.getValue().get(j).getModifyUserId());
                            model.setCreateAt(entry.getValue().get(j).getCreateAt());
                            model.setUpdateAt(entry.getValue().get(j).getUpdateAt());
                            model.setDeleteAt(entry.getValue().get(j).getDeleteAt());
                            issueFileds.add(model);
                    }

                }

            }else {
                List<IssueFieldSetting> setting = initDefaultSetting(projectIdList.get(i));
                for (int k = 0; k < setting.size(); k++) {
                    IssueFieldSetting model = new IssueFieldSetting();
                    model.setId(setting.get(k).getId());
                    model.setProjectId(setting.get(k).getProjectId());
                    model.setFieldId(setting.get(k).getFieldId());
                    model.setName(setting.get(k).getName());
                    model.setAlias(setting.get(k).getAlias());
                    model.setDisplayStatus(setting.get(k).getDisplayStatus());
                    model.setRequiredStatus(setting.get(k).getRequiredStatus());
                    model.setAliasStatus(setting.get(k).getAliasStatus());
                    model.setModuleId(setting.get(k).getModuleId());
                    model.setModifyUserId(setting.get(k).getModifyUserId());
                    model.setCreateAt(setting.get(k).getCreateAt());
                    model.setUpdateAt(setting.get(k).getUpdateAt());
                    model.setDeleteAt(setting.get(k).getDeleteAt());
                    issueFileds.add(model);
                }
            }


        }
        LjBaseResponse response = new LjBaseResponse();
        response.setData(issueFileds);
        return response;

    }

    private List<IssueFieldSetting> initDefaultSetting(Integer projectId) {
        String uid = null;
        ArrayList<IssueFieldSetting> settingList = new ArrayList<>();
        List<IssueFieldSetting> IssueFieldList = buildingqmCheckQuestionService.selectByProjectId(projectId);
        return IssueFieldList;
    }

    // 时间戳转日期
    public static Date transForDate(Integer ms) {
        if (ms == null) {
            ms = 0;
        }
        long msl = (long) ms * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = null;
        if (ms != null) {
            try {
                String str = sdf.format(msl);
                temp = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

}
