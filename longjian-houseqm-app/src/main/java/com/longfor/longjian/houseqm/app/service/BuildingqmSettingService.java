package com.longfor.longjian.houseqm.app.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.ModuleInfoEnum;
import com.longfor.longjian.houseqm.app.vo.ApiIssueFiledSettingMsg;
import com.longfor.longjian.houseqm.consts.IssueFieldAliasStatusEnum;
import com.longfor.longjian.houseqm.consts.IssueFieldDefaultListEnum;
import com.longfor.longjian.houseqm.consts.IssueFieldDisplayStatusEnum;
import com.longfor.longjian.houseqm.consts.IssueFieldRequiredStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.IssueFieldSettingService;
import com.longfor.longjian.houseqm.po.zhijian2_setting.IssueFieldSetting;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
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
    private IssueFieldSettingService issueFieldSettingService;


    public LjBaseResponse<ApiIssueFiledSettingMsg.IssueFileds> getIssuefiledSetting(String projectIds, Integer timestamp) {
        List<Integer> projectIdList = StringSplitToListUtil.splitToIdsComma(projectIds, ",");
        ArrayList<ApiIssueFiledSettingMsg> issueFileds = Lists.newArrayList();
        List<IssueFieldSetting> settingList = issueFieldSettingService.findProjectIdsAndModuleId(projectIdList, ModuleInfoEnum.GCGL.getValue());
        HashMap<Integer, List<IssueFieldSetting>> projectMap = Maps.newHashMap();
        for (int i = 0; i < settingList.size(); i++) {
            if (!projectMap.containsKey(settingList.get(i).getProjectId())) {
                projectMap.put(settingList.get(i).getProjectId(), new ArrayList<>());
            }
            projectMap.get(settingList.get(i).getProjectId()).add(settingList.get(i));
        }
        for (int i = 0; i < projectIdList.size(); i++) {
            if (projectMap.containsKey(projectIdList.get(i))) {
                List<IssueFieldSetting> objects = projectMap.get(projectIdList.get(i));
                for (int j = 0; j < objects.size(); j++) {
                    if (DateUtil.datetimeToTimeStamp(objects.get(j).getCreateAt()) >= timestamp) {
                        ApiIssueFiledSettingMsg msg = new ApiIssueFiledSettingMsg();
                        msg.setProject_id(objects.get(j).getProjectId());
                        msg.setField_id(objects.get(j).getFieldId());
                        msg.setName(objects.get(j).getName());
                        msg.setAlias(objects.get(j).getAlias());
                        msg.setDisplay_status(objects.get(j).getDisplayStatus());
                        msg.setRequired_status(objects.get(j).getRequiredStatus());
                        msg.setAlias_status(objects.get(j).getAliasStatus());
                        issueFileds.add(msg);
                    }
                }
            } else {
                List<IssueFieldSetting> issueFieldSettings = initDefaultSetting(projectIdList.get(i));
                for (int j = 0; j < issueFieldSettings.size(); j++) {
                    ApiIssueFiledSettingMsg msg = new ApiIssueFiledSettingMsg();
                    msg.setProject_id(issueFieldSettings.get(i).getProjectId());
                    msg.setField_id(issueFieldSettings.get(i).getFieldId());
                    msg.setName(issueFieldSettings.get(i).getName());
                    msg.setAlias(issueFieldSettings.get(i).getAlias());
                    msg.setDisplay_status(issueFieldSettings.get(i).getDisplayStatus());
                    msg.setRequired_status(issueFieldSettings.get(i).getRequiredStatus());
                    msg.setAlias_status(issueFieldSettings.get(i).getAliasStatus());
                    issueFileds.add(msg);
                }

            }
        }
        LjBaseResponse<ApiIssueFiledSettingMsg.IssueFileds> response = new LjBaseResponse<>();
        ApiIssueFiledSettingMsg.IssueFileds fileds = new ApiIssueFiledSettingMsg().new IssueFileds();
        fileds.setIssue_fileds(issueFileds);
        response.setData(fileds);
        return response;
    }


    private List<IssueFieldSetting> initDefaultSetting(Integer projectId) {
        Integer uid = 0;
        ArrayList<IssueFieldSetting> settingList = new ArrayList<>();
        IssueFieldSetting setting = new IssueFieldSetting();
        setting.setProjectId(projectId);
        setting.setFieldId(IssueFieldDefaultListEnum.JCX.getId());
        setting.setName(IssueFieldDefaultListEnum.JCX.getValue());
        setting.setAlias("");
        setting.setDisplayStatus(IssueFieldDisplayStatusEnum.Yes.getId());
        setting.setRequiredStatus(IssueFieldRequiredStatusEnum.Yes.getId());
        setting.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        setting.setModuleId(ModuleInfoEnum.GCGL.getValue());
        setting.setModifyUserId(uid);
        setting.setCreateAt(new Date());
        setting.setUpdateAt(new Date());
        int one = issueFieldSettingService.add(setting);
        if (one <= 0) {
            return settingList;
        }
        settingList.add(setting);

        IssueFieldSetting settings = new IssueFieldSetting();
        settings.setProjectId(projectId);
        settings.setFieldId(IssueFieldDefaultListEnum.BCMS.getId());
        settings.setName(IssueFieldDefaultListEnum.BCMS.getValue());
        settings.setAlias("");
        settings.setDisplayStatus(IssueFieldDisplayStatusEnum.Yes.getId());
        settings.setRequiredStatus(IssueFieldRequiredStatusEnum.Yes.getId());
        settings.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        settings.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings.setModifyUserId(uid);
        settings.setCreateAt(new Date());
        settings.setUpdateAt(new Date());
        int two = issueFieldSettingService.add(settings);
        if (two <= 0) {
            return settingList;
        }
        settingList.add(settings);

        IssueFieldSetting settings3 = new IssueFieldSetting();
        settings3.setProjectId(projectId);
        settings3.setFieldId(IssueFieldDefaultListEnum.MSYY.getId());
        settings3.setName(IssueFieldDefaultListEnum.MSYY.getValue());
        settings3.setAlias("");
        settings3.setDisplayStatus(IssueFieldDisplayStatusEnum.No.getId());
        settings3.setRequiredStatus(IssueFieldRequiredStatusEnum.No.getId());
        settings3.setAliasStatus(IssueFieldAliasStatusEnum.No.getId());
        settings3.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings3.setModifyUserId(uid);
        settings3.setCreateAt(new Date());
        settings3.setUpdateAt(new Date());
        int three = issueFieldSettingService.add(settings3);
        if (three <= 0) {
            return settingList;
        }
        settingList.add(settings3);

        IssueFieldSetting settings4 = new IssueFieldSetting();
        settings4.setProjectId(projectId);
        settings4.setFieldId(IssueFieldDefaultListEnum.JCBW.getId());
        settings4.setName(IssueFieldDefaultListEnum.JCBW.getValue());
        settings4.setAlias("");
        settings4.setDisplayStatus(IssueFieldDisplayStatusEnum.Yes.getId());
        settings4.setRequiredStatus(IssueFieldRequiredStatusEnum.Yes.getId());
        settings4.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        settings4.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings4.setModifyUserId(uid);
        settings4.setCreateAt(new Date());
        settings4.setUpdateAt(new Date());
        int four = issueFieldSettingService.add(settings4);
        if (four <= 0) {
            return settingList;
        }
        settingList.add(settings4);

        IssueFieldSetting settings5 = new IssueFieldSetting();
        settings5.setProjectId(projectId);
        settings5.setFieldId(IssueFieldDefaultListEnum.TZWZ.getId());
        settings5.setName(IssueFieldDefaultListEnum.TZWZ.getValue());
        settings5.setAlias("");
        settings5.setDisplayStatus(IssueFieldDisplayStatusEnum.Yes.getId());
        settings5.setRequiredStatus(IssueFieldRequiredStatusEnum.No.getId());
        settings5.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        settings5.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings5.setModifyUserId(uid);
        settings5.setCreateAt(new Date());
        settings5.setUpdateAt(new Date());
        int five = issueFieldSettingService.add(settings5);
        if (five <= 0) {
            return settingList;
        }
        settingList.add(settings5);

        IssueFieldSetting settings6 = new IssueFieldSetting();
        settings6.setProjectId(projectId);
        settings6.setFieldId(IssueFieldDefaultListEnum.BWL.getId());
        settings6.setName(IssueFieldDefaultListEnum.BWL.getValue());
        settings6.setAlias("");
        settings6.setDisplayStatus(IssueFieldDisplayStatusEnum.No.getId());
        settings6.setRequiredStatus(IssueFieldRequiredStatusEnum.No.getId());
        settings6.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        settings6.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings6.setModifyUserId(uid);
        settings6.setCreateAt(new Date());
        settings6.setUpdateAt(new Date());
        int six = issueFieldSettingService.add(settings6);
        if (six <= 0) {
            return settingList;
        }
        settingList.add(settings6);

        IssueFieldSetting settings7 = new IssueFieldSetting();
        settings7.setProjectId(projectId);
        settings7.setFieldId(IssueFieldDefaultListEnum.ZGFZR.getId());
        settings7.setName(IssueFieldDefaultListEnum.ZGFZR.getValue());
        settings7.setAlias("");
        settings7.setDisplayStatus(IssueFieldDisplayStatusEnum.Yes.getId());
        settings7.setRequiredStatus(IssueFieldRequiredStatusEnum.No.getId());
        settings7.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        settings7.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings7.setModifyUserId(uid);
        settings7.setCreateAt(new Date());
        settings7.setUpdateAt(new Date());
        int seven = issueFieldSettingService.add(settings7);
        if (seven <= 0) {
            return settingList;
        }
        settingList.add(settings7);

        IssueFieldSetting settings8 = new IssueFieldSetting();
        settings8.setProjectId(projectId);
        settings8.setFieldId(IssueFieldDefaultListEnum.ZGCYR.getId());
        settings8.setName(IssueFieldDefaultListEnum.ZGCYR.getValue());
        settings8.setAlias("");
        settings8.setDisplayStatus(IssueFieldDisplayStatusEnum.Yes.getId());
        settings8.setRequiredStatus(IssueFieldRequiredStatusEnum.No.getId());
        settings8.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        settings8.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings8.setModifyUserId(uid);
        settings8.setCreateAt(new Date());
        settings8.setUpdateAt(new Date());
        int eight = issueFieldSettingService.add(settings8);
        if (eight <= 0) {
            return settingList;
        }
        settingList.add(settings8);


        IssueFieldSetting settings9 = new IssueFieldSetting();
        settings9.setProjectId(projectId);
        settings9.setFieldId(IssueFieldDefaultListEnum.ZGQX.getId());
        settings9.setName(IssueFieldDefaultListEnum.ZGQX.getValue());
        settings9.setAlias("");
        settings9.setDisplayStatus(IssueFieldDisplayStatusEnum.Yes.getId());
        settings9.setRequiredStatus(IssueFieldRequiredStatusEnum.No.getId());
        settings9.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        settings9.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings9.setModifyUserId(uid);
        settings9.setCreateAt(new Date());
        settings9.setUpdateAt(new Date());
        int nine = issueFieldSettingService.add(settings9);
        if (nine <= 0) {
            return settingList;
        }
        settingList.add(settings9);


        IssueFieldSetting settings10 = new IssueFieldSetting();
        settings10.setProjectId(projectId);
        settings10.setFieldId(IssueFieldDefaultListEnum.YZCD.getId());
        settings10.setName(IssueFieldDefaultListEnum.YZCD.getValue());
        settings10.setAlias("");
        settings10.setDisplayStatus(IssueFieldDisplayStatusEnum.No.getId());
        settings10.setRequiredStatus(IssueFieldRequiredStatusEnum.No.getId());
        settings10.setAliasStatus(IssueFieldAliasStatusEnum.Yes.getId());
        settings10.setModuleId(ModuleInfoEnum.GCGL.getValue());
        settings10.setModifyUserId(uid);
        settings10.setCreateAt(new Date());
        settings10.setUpdateAt(new Date());
        int ten = issueFieldSettingService.add(settings10);
        if (ten <= 0) {
            return settingList;
        }
        settingList.add(settings10);
        return settingList;
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
