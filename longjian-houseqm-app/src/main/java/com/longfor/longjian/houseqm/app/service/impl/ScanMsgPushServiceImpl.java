package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.*;
import com.longfor.longjian.common.push.UmPushUtil;
import com.longfor.longjian.houseqm.app.service.ScanMsgPushService;
import com.longfor.longjian.houseqm.app.vo.GcglIssueStatRecordVo;
import com.longfor.longjian.houseqm.app.vo.NoticeStatKey;
import com.longfor.longjian.houseqm.app.vo.NoticeStatValue;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zhijian2_notify.GcglIssueStatRecord;
import com.longfor.longjian.houseqm.po.zhijian2_notify.NoticeStatRecord;
import com.longfor.longjian.houseqm.po.zhijian2_notify.NoticeUserRecord;
import com.longfor.longjian.houseqm.po.zhijian2_notify.StatScanRecord;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskNotifyRecord;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Wang on 2019/2/28.
 */
@Slf4j
@Service
public class ScanMsgPushServiceImpl implements ScanMsgPushService {

    private static  final int PUSH_APP_GCGL = 1;
    private static  final int USER_ID_SIZE_MAX = 50;

    @Resource
    private StatScanRecordService statScanRecordService;

    @Resource
    private HouseQmCheckTaskNotifyRecordService qmCheckTaskNotifyRecordService;

    @Resource
    private UserService userService;

    @Resource
    private ProjectService projectService;

    @Resource
    private TeamService teamService;

    @Resource
    private GcglIssueStatRecordService gcglIssueStatRecordService;

    @Resource
    private NoticeStatRecordService noticeStatRecordService;

    @Resource
    private NoticeUserRecordService noticeUserRecordService;

    @Value("${push_config.enterprise_id}")
    private String enterpriseId;
    @Value("${push_config.gcgl.app_key_android}")
    private  String gcglappKeyAndroid;

    @Value("${push_config.gcgl.app_master_secret_android}")
    private  String gcglappMasterSecretAndroid;

    @Value("${push_config.gcgl.app_key_ios}")
    private String gcglappKeyIOS;

    @Value("${push_config.gcgl.app_master_secret_ios}")
    private  String gcglappMasterSecretIOS;

    @Value("${push_config.gcgl.app_secret_xiao_mi}")
    private  String gcglappSecretXiaoMi;

    @Value("${push_config.gcgl.package_name_xiao_mi}")
    private  String gcglpackageNameXiaomi;

    @Override
    public LjBaseResponse scanNoticeCenter(String categoryCls) {

        Integer[] moduleIds;

        if(StringUtils.isNotBlank(categoryCls)){
            String [] module=categoryCls.split(",");
            moduleIds=new Integer[module.length];
            for(int i=0;i<module.length;i++){
                moduleIds[i]=Integer.parseInt(module[i]);
            }
        }else{
            moduleIds=new Integer[1];
            moduleIds[0]=ModuleInfoEnum.GCGL.getValue();
        }
        log.info("notice center begin to scan, task list----------------:{}", StringUtils.join(moduleIds, ','));

        for(Integer moduleId:moduleIds){
            scanNoticeCenterAndPush(moduleId);
        }


        return null;
    }
    @SuppressWarnings("squid:S3776")
    private LjBaseResponse scanNoticeCenterAndPush(Integer moduleId){


        long statTimestamp=(long) 1000 * 60 * 60 * 24;

        long now=new Date().getTime();

        Date statBeg= DateUtil.timeStampTwoToDate((now -statTimestamp),"yy-MM-dd HH:mm:ss");
        Date statEnd=DateUtil.timeStampTwoToDate(now,"yy-MM-dd HH:mm:ss");


        StatScanRecord statScanRecord=statScanRecordService.findByExample(moduleId);

        if(statScanRecord!=null){
            statBeg=statScanRecord.getLastScanAt();
            statScanRecord.setPrevScanAt(statScanRecord.getLastScanAt());
            statScanRecord.setLastScanAt(statEnd);
            statScanRecord.setUpdateAt(new Date());
            statScanRecordService.update(statScanRecord);
        }else{
            statScanRecord=new StatScanRecord();
            statScanRecord.setModuleId(moduleId);
            statScanRecord.setModuleName(ModuleInfoEnum.getLabel(moduleId));
            statScanRecord.setPrevScanAt(statBeg);
            statScanRecord.setLastScanAt(statEnd);
            statScanRecord.setCreateAt(new Date());
            statScanRecord.setUpdateAt(new Date());
            statScanRecordService.add(statScanRecord);
        }

        List<Integer>statusList=new ArrayList<>();
        statusList.add(HouseQmCheckTaskIssueStatus.AssignNoReform.getValue());
        statusList.add(HouseQmCheckTaskIssueStatus.ReformNoCheck.getValue());

        Map<String,Object> map=new HashMap<>();
        map.put("moduleId",moduleId);
        map.put("statBeg",statBeg);
        map.put("statEnd",statEnd);

        List<HouseQmCheckTaskNotifyRecord>recordList=qmCheckTaskNotifyRecordService.findExample(map,statusList);

        log.debug("recordList:---------------- {}",recordList==null?"":JSON.toJSONString(recordList));

        Map<NoticeStatKey,NoticeStatValue>statMap=new HashMap<>();
        List<Integer>projectIds=new ArrayList<>();
        List<Integer>noticeUserIds=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(recordList)){
            for(HouseQmCheckTaskNotifyRecord record:recordList){
                String desUserIds=record.getDesUserIds();
                if(StringUtils.isNotBlank(desUserIds)){
                    String [] userIds=desUserIds.split(",");
                    for(String id:userIds){
                        NoticeStatKey statKey=new NoticeStatKey(record.getProjectId(),Integer.parseInt(id));
                        if(statMap.get(statKey)==null){
                            statMap.put(statKey,setNoticeStatValue(record.getIssueStatus(),record.getIssueId()));
                        }else {
                            statMap.put(statKey,addNoticeStatValue(statMap.get(statKey),record.getIssueStatus(),record.getIssueId()));
                        }

                        if(!projectIds.contains(record.getProjectId())){
                            projectIds.add(record.getProjectId());
                        }
                        if(!noticeUserIds.contains(Integer.parseInt(id))){
                            noticeUserIds.add(Integer.parseInt(id));
                        }
                    }
                }
            }
        }

        //扫描后无须推送通知
        if(statMap.size()==0){
            log.info("has not issue stat need to notice, module_id=", moduleId);
            return new LjBaseResponse();
        }

        log.debug("statMap ---------------: {}",JSON.toJSONString(statMap));

        Map<Integer,User>userMap=createUserMap(noticeUserIds);
        Map<Integer,Project>projectMap= createProjectMap(projectIds);
        Map<Integer,Team>teamMap=createTeamMap(projectMap);

        List<GcglIssueStatRecordVo> gcglIssueStatRecordList=new ArrayList<>();

        for (Map.Entry<NoticeStatKey,NoticeStatValue> entry : statMap.entrySet()) {

            NoticeStatKey noticeStatKey=entry.getKey();

            NoticeStatValue noticeStatValue=entry.getValue();

            GcglIssueStatRecordVo gcglIssueStatRecordVo= createNoticeCenterRealTimeMessage(teamMap,projectMap,userMap,moduleId,noticeStatKey,
                                         noticeStatValue,DateUtil.datetimeToTimeStamp(statBeg),DateUtil.datetimeToTimeStamp(statEnd));
            gcglIssueStatRecordList.add(gcglIssueStatRecordVo);
        }

        sendAndSaveRecord(gcglIssueStatRecordList);

        return new LjBaseResponse();
    }

    private NoticeStatValue setNoticeStatValue(Integer issueStatus,Integer issueId){

        List<Integer> reformNoCheckIssueIds=new ArrayList<>();

        List<Integer> assignNoReformIssueIds=new ArrayList<>();

        if(issueStatus>0&&issueId>0){

            if(issueStatus.equals(HouseQmCheckTaskIssueStatus.AssignNoReform.getValue())){
                assignNoReformIssueIds.add(issueId);
            }else{
                reformNoCheckIssueIds.add(issueId);
            }
        }
        return new NoticeStatValue(assignNoReformIssueIds,reformNoCheckIssueIds);
    }

    private NoticeStatValue addNoticeStatValue(NoticeStatValue noticeStatValue,Integer issueStatus,Integer issueId){

        if(issueStatus.equals(HouseQmCheckTaskIssueStatus.AssignNoReform.getValue())){

            if(!noticeStatValue.getAssignNoReformIssueIds().contains(issueId)){
                noticeStatValue.getAssignNoReformIssueIds().add(issueId);
            }
        }else{
            if(!noticeStatValue.getReformNoCheckIssueIds().contains(issueId)){
                noticeStatValue.getReformNoCheckIssueIds().add(issueId);
            }
        }
        return noticeStatValue;
    }

    public Map<Integer,User>createUserMap(List<Integer>noticeUserIds){

        Map<Integer,User>userMap=new HashMap<>();

        List<User>userList=userService.searchByUserIdInAndNoDeleted(noticeUserIds);

        for(User user:userList){
            userMap.put(user.getUserId(),user);
        }
        return userMap;
    }

    private Map<Integer,Project> createProjectMap(List<Integer>projectIds){
        Map<Integer,Project>projectMap=new HashMap<>();

        List<Project>projectList=projectService.searchByProjectIdIn(projectIds);

        for(Project project:projectList){
            projectMap.put(project.getId(),project);
        }
        return projectMap;
    }

    private Map<Integer,Team> createTeamMap(Map<Integer,Project>projectMap){
        Map<Integer,Team> teamMap=new HashMap<>();
        List<Integer> teamIds=new ArrayList<>();
        projectMap.forEach((key, value) ->
            teamIds.add(value.getTeamId())
        );

        List<Team>teamList=teamService.searchByTeamIdIn(teamIds);

        for(Team team:teamList){
            teamMap.put(team.getTeamId(),team);
        }
        return teamMap;
    }

    @SuppressWarnings("squid:S3776")
    private GcglIssueStatRecordVo createNoticeCenterRealTimeMessage(Map<Integer,Team>teamMap, Map<Integer,Project>projectMap,
                                                   Map<Integer,User>userMap, Integer moduleId,
                                                   NoticeStatKey noticeStatKey, NoticeStatValue noticeStatValue, int  statBeg, int statEnd){


     Integer teamId=projectMap.get(noticeStatKey.getProjectId()).getTeamId()==null?0:projectMap.get(noticeStatKey.getProjectId()).getTeamId();
    String teamName=teamMap.get(teamId).getTeamName()==null?"":teamMap.get(teamId).getTeamName();
    String title= ModuleNameEnum.getValue(moduleId);
    Integer projectId=noticeStatKey.getProjectId();
    String projectName=projectMap.get(projectId).getName()==null?"":projectMap.get(projectId).getName();
    Integer srcUserId=0;
    String srcUserName="";
    Integer desUserId=noticeStatKey.getUserId();
    String desUserName=userMap.get(desUserId).getRealName()==null?"处理人":userMap.get(desUserId).getRealName();
    Integer noticeType= NoticeType.WORK_REMINDER.getValue();
    String moduleName=ModuleInfoEnum.getLabel(moduleId);
    Integer behaviorType= NoticeBehaviorType.CONSULT.getValue();
    String behaviorName="查看问题列表";


    GcglIssueStatRecord gcglIssueStatRecord=new GcglIssueStatRecord();

    GcglIssueStatRecordVo gcglIssueStatRecordVo=new GcglIssueStatRecordVo();

    gcglIssueStatRecord.setTeamId(teamId);
    gcglIssueStatRecord.setTitle(title);
    gcglIssueStatRecord.setNoticeType(noticeType);
    gcglIssueStatRecord.setSourceType(SourceType.TODO_NOTICE.getKey());
    gcglIssueStatRecord.setTeamId(teamId);
    gcglIssueStatRecord.setProjectId(projectId);
    gcglIssueStatRecord.setSrcUserId(srcUserId);
    gcglIssueStatRecord.setDesUserId(desUserId);
    gcglIssueStatRecord.setModuleId(moduleId);
    gcglIssueStatRecord.setBehaviorType(behaviorType);
    gcglIssueStatRecord.setBehaviorName(behaviorName);

    gcglIssueStatRecordVo.setTeamName(teamName);
    gcglIssueStatRecordVo.setSrcUserName(srcUserName);
    gcglIssueStatRecordVo.setModuleName(moduleName);
    gcglIssueStatRecordVo.setProjectName(projectName);
    gcglIssueStatRecordVo.setDesUserName(desUserName);

    String recordUuid="";
    String description="";
    String content="";

    //待整改推送
    if(noticeStatValue.getAssignNoReformIssueIds().size()>0){
      recordUuid = UUID.randomUUID().toString();
      description="「"+projectName+"」有新的问题需要整改";
      content="你好,"+desUserName+"项目「"+projectName+"」新增待整改问题"+noticeStatValue.getAssignNoReformIssueIds().size()+"条，请及时跟进处理。";

      Map<String,Object>gcglMap=new HashMap<>();
      gcglMap.put("stat_id",recordUuid);
      gcglMap.put("stat_begin_time",statBeg);
      gcglMap.put("stat_end_time",statEnd);

      gcglIssueStatRecord.setUuid(recordUuid);
      gcglIssueStatRecord.setDescription(description);
      gcglIssueStatRecord.setContent(content);
      gcglIssueStatRecord.setIssueIds(StringUtils.join(noticeStatValue.getAssignNoReformIssueIds(),","));
      gcglIssueStatRecord.setExtraInfo(JSON.toJSONString(gcglMap));
      gcglIssueStatRecord.setCreateAt(new Date());
      gcglIssueStatRecord.setUpdateAt(new Date());

      try {
          gcglIssueStatRecordService.add(gcglIssueStatRecord);
      }catch (Exception e){
          log.error("待整改推送记录保存失败:  {}",e.getMessage());
      }
    }

    //待销项推送
    if(noticeStatValue.getReformNoCheckIssueIds().size()>0) {
        recordUuid = UUID.randomUUID().toString();

        description = "「" + projectName + "」有新的问题需要销项";

        content = "你好," + desUserName + "项目「" + projectName + "」新增待销项问题" + noticeStatValue.getReformNoCheckIssueIds().size() + "条，请及时跟进处理。";

        Map<String, Object> gcglMap = new HashMap<>();
        gcglMap.put("stat_id", recordUuid);
        gcglMap.put("stat_begin_time", statBeg);
        gcglMap.put("stat_end_time", statEnd);

        gcglIssueStatRecord.setUuid(recordUuid);
        gcglIssueStatRecord.setDescription(description);
        gcglIssueStatRecord.setContent(content);
        gcglIssueStatRecord.setIssueIds(StringUtils.join(noticeStatValue.getReformNoCheckIssueIds(), ","));
        gcglIssueStatRecord.setExtraInfo(JSON.toJSONString(gcglMap));
        gcglIssueStatRecord.setCreateAt(new Date());
        gcglIssueStatRecord.setUpdateAt(new Date());
        try {
            gcglIssueStatRecordService.add(gcglIssueStatRecord);
        }catch (Exception e){
            log.error("待销项推送记录保存失败:  {}",e.getMessage());
        }
    }

       gcglIssueStatRecordVo.setGcglIssueStatRecord(gcglIssueStatRecord);

       log.debug("gcglIssueStatRecordVo  :{}",JSON.toJSONString(gcglIssueStatRecordVo));

       return gcglIssueStatRecordVo;

    }


    private void sendAndSaveRecord(List<GcglIssueStatRecordVo>gcglIssueStatRecordList){

        List<Integer>noticeTypeList=new ArrayList<>();
        noticeTypeList.add(NoticeType.WORK_REMINDER.getValue());
        noticeTypeList.add(NoticeType.SYSTEM_NOTIFICATION.getValue());
        noticeTypeList.add(NoticeType.SYSTEM_NOTIFICATION_EX.getValue());
        for(GcglIssueStatRecordVo recordVo:gcglIssueStatRecordList){
            GcglIssueStatRecord record=recordVo.getGcglIssueStatRecord();
            if(record.getDesUserId()==0||!noticeTypeList.contains(record.getNoticeType())){
                log.info("param error, des_user_id="+record.getDesUserId()+
                        "notice_type="+record.getNoticeType());
            }

            List<Integer>desUserIds=new ArrayList<>();
            if(record.getDesUserId()>0){
                NoticeStatRecord noticeStatRecord=new NoticeStatRecord();
                noticeStatRecord.setTeamId(record.getTeamId());
                noticeStatRecord.setTeamName(recordVo.getTeamName());
                noticeStatRecord.setProjectId(record.getProjectId());
                noticeStatRecord.setProjectName(recordVo.getProjectName());
                noticeStatRecord.setSrcUserId(record.getSrcUserId());
                noticeStatRecord.setSrcUserName(recordVo.getSrcUserName());
                noticeStatRecord.setDesUserId(record.getDesUserId());
                noticeStatRecord.setDesUserName(recordVo.getDesUserName());
                noticeStatRecord.setDesUserIds("");
                noticeStatRecord.setDesUserNames("");
                noticeStatRecord.setTitle(record.getTitle());
                noticeStatRecord.setDescription(record.getDescription());
                noticeStatRecord.setNoticeType(record.getNoticeType());
                noticeStatRecord.setModuleId(record.getModuleId());
                noticeStatRecord.setModuleName(recordVo.getModuleName());
                noticeStatRecord.setContent(record.getContent());
                noticeStatRecord.setBehaviorName(record.getBehaviorName());
                noticeStatRecord.setBehaviorType(record.getBehaviorType());
                noticeStatRecord.setReadStatus(NoticeReadStatusType.UN_READ.getValue());
                noticeStatRecord.setExtraInfo(record.getExtraInfo());
                desUserIds.add(record.getDesUserId());

                try{
                    noticeStatRecordService.add(noticeStatRecord);
                }catch (Exception e){
                    log.info("add notice record failed：{}"+e.getMessage());
                }

                NoticeUserRecord noticeUserRecord=new NoticeUserRecord();
                noticeUserRecord.setUpdateAt(new Date());
                noticeUserRecord.setCreateAt(new Date());
                noticeUserRecord.setUserId(record.getDesUserId());
                noticeUserRecord.setModuleId(record.getModuleId());
                noticeUserRecord.setReadStatus(NoticeReadStatusType.UN_READ.getValue());
                noticeUserRecord.setNoticeType(record.getNoticeType());
                noticeUserRecord.setNoticeId(noticeStatRecord.getId());
                try {
                    noticeUserRecordService.add(noticeUserRecord);
                }catch (Exception e){
                    log.error("notice user add failed :{}",e.getMessage());
                }

                sendUPush(record.getTitle(),record.getDescription(),noticeStatRecord.getId(),desUserIds,PUSH_APP_GCGL);
            }
        }
    }

    /**
     * appFlag: 1-推送到工程管理APP，2-推送到移动验房APP，3-全部推送
     *
     * @param title
     * @param msg
     * @param taskId
     * @param userIds
     * @param appFlag
     */
    public void sendUPush(String title, String msg, Integer taskId, List<Integer> userIds, Integer appFlag) {
        try {
            if (CollectionUtils.isEmpty(userIds)) {
                log.warn("Len of UserIds is zero");
                return;
            } else if (userIds.size() > USER_ID_SIZE_MAX) {
                sendUPush(title, msg, taskId, userIds.subList(50, userIds.size()), appFlag);
                userIds = userIds.subList(0, 50);
            }

            String appkeyAndroid;
            String appMasterSecretAndroid;
            String appkeyIos;
            String appMasterSecretIos;

            if (appFlag == PUSH_APP_GCGL) {
                appkeyAndroid = gcglappKeyAndroid;
                appMasterSecretAndroid = gcglappMasterSecretAndroid;
                appkeyIos = gcglappKeyIOS;
                appMasterSecretIos = gcglappMasterSecretIOS;
            } else {
                log.error("appFlag 错误");
                return;
            }

            log.info("Sending_upush [taskId:{}] [userIds:{}] [appFlag:{}]", taskId, userIds, appFlag);

            List<String> aliaList = Lists.newArrayList();
            for (Integer userId : userIds) {
                String aliasEnterprise = String.format("user_id_%s_%d", enterpriseId, userId);
                aliaList.add(aliasEnterprise);
            }
            String alias = StringUtils.join(aliaList, ",");
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("appkeyAndroid",appkeyAndroid);
            paramMap.put("appMasterSecretAndroid",appMasterSecretAndroid);
            paramMap.put("alias",alias);
            // 友盟推送Android
            androidPush(paramMap,
                    "user_id", msg, title, msg, "go_app", taskId.toString());

            // 友盟推送Ios
            iosPush(appkeyIos, appMasterSecretIos, alias, "user_id", msg, taskId.toString());

        } catch (Exception e) {
            log.error("推送错误!taskId:{}, userIds:{}", taskId, userIds, e);
        }
    }

    private void androidPush(Map<String, Object> paramMap, String aliasType,
                             String ticker, String title, String text,
                             String custom, String taskId) {
            try {
                String appkey = (String) paramMap.get("appkeyAndroid");
                String appMasterSecret = (String) paramMap.get("appMasterSecretAndroid");
                String alias = (String) paramMap.get("alias");
                boolean result = UmPushUtil.sendAndroidCustomizedcast(
                        appkey, appMasterSecret, alias,
                        aliasType, ticker, title, text, custom, taskId);
                log.debug("安卓推送结果：-----------------{}",result);
                if (!result) {
                    log.warn("友盟推送Android推送失败alias:{},ticker:{},title:{},text:{},taskId:{}",
                            alias, ticker, title, text, taskId);
                }
            } catch (Exception e) {
                log.error("错误", e);
            }
    }

    private void iosPush(String appKey, String appMasterSecret, String alias, String aliasType, String alert, String taskId) {
            try {
                boolean result = UmPushUtil.sendIOSCustomizedcast(appKey, appMasterSecret, alias, aliasType, alert, taskId);
                log.debug("IOS推送结果：-----------------{}",result);
                if (!result) {
                    log.warn("友盟推送Ios推送失败alias:{},alert:{},taskId:{}", alias, alert, taskId);
                }
            } catch (Exception e) {
                log.error("错误", e);
            }
    }

}
