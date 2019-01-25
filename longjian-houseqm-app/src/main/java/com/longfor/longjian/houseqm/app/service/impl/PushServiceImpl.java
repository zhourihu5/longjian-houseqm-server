package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.push.UmPushUtil;
import com.longfor.longjian.common.push.xiaomi.XmPushUtil;
import com.longfor.longjian.houseqm.app.service.PushService;
import com.longfor.longjian.houseqm.app.vo.PushConfigVo;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.HouseQmCheckTaskIssueVo;
import com.longfor.longjian.houseqm.consts.AppPlatformTypeEnum;
import com.longfor.longjian.houseqm.consts.CategoryClsTypeEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.service.impl
 * @ClassName: PushServiceImpl
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 17:52
 */
@Service
@Slf4j
public class PushServiceImpl implements PushService {


    @Resource
    private PushConfigVo pushConfigVo;

    private final int PUSH_APP_GCGL = 1;
    private final int PUSH_APP_YDYF = 2;
    private final int PUSH_APP_GXGL = 3;

    @Override
    public void sendUPushByIssues(List<HouseQmCheckTaskIssueVo> issues) throws Exception {
        if (issues == null || issues.isEmpty()) {
            log.warn("send_upush_empty！");
            return;
        }
        // 按TaskId把Issue分组
        Map<Integer, List<HouseQmCheckTaskIssueVo>> issuesMap = Maps.newHashMap();
        for (HouseQmCheckTaskIssueVo issue : issues) {
            if (issue == null) {
                log.warn("houseqmissue is nil");
                continue;
            }
            List<HouseQmCheckTaskIssueVo> issueList = issuesMap.get(issue.getTaskId());
            if (issueList == null) {
                issueList = Lists.newArrayList();
            }
            issueList.add(issue);
            issuesMap.put(issue.getTaskId(), issueList);
        }

        for (Map.Entry<Integer, List<HouseQmCheckTaskIssueVo>> entry : issuesMap.entrySet()) {
            Integer taskId = entry.getKey();
            List<HouseQmCheckTaskIssueVo> issueList = entry.getValue();
            Integer categoryCls = issueList.get(0).getCategoryCls();

            //定义  参数 要推送
            int appFlag;
            String appName;
            List<Integer> userIds = Lists.newArrayList();

            if (CategoryClsTypeEnum.FHYS.getId().equals(categoryCls) || CategoryClsTypeEnum.RHYF.getId().equals(categoryCls)) {
                appFlag = PUSH_APP_YDYF;
                appName = "移动验房";
            } else {
                appFlag = PUSH_APP_GCGL;
                appName = "工程检查";
            }
            for (HouseQmCheckTaskIssueVo issue : issueList) {
                log.debug("send_push_issueList: ", issueList);
                HouseQmCheckTaskIssueStatusEnum e = null;
                for (HouseQmCheckTaskIssueStatusEnum value : HouseQmCheckTaskIssueStatusEnum.values()) {
                    if (value.getId().equals(issue.getStatus())) {
                        e = value;
                    }
                }
                switch (e) {
                    case AssignNoReform: {
                        userIds.add(issue.getRepairerId());
                        List<Integer> fids = StringSplitToListUtil.strToInts(issue.getRepairerFollowerIds(), ",");
                        userIds.addAll(fids);
                        break;
                    }
                    case ReformNoCheck: {
                        userIds.add(issue.getSenderId());
                        break;
                    }
                    default:
                        break;
                }
            }
            String msg = "您在［" + appName + "］有新的待处理问题，请进入App同步更新。";
            sendUPush("", msg, taskId, userIds, appFlag);
        }
    }

    // appFlag: 1-推送到工程管理APP，2-推送到移动验房APP，3-全部推送
    public void sendUPush(String title, String msg, int taskId, List<Integer> userIds, int appFlag) throws Exception {
        if (userIds.size() == 0) {
            log.warn("Len of UserIds is zero");
            return;
        } else if (userIds.size() > 50) {
            List<Integer> userIdList = Lists.newArrayList();
            for (int i = 50; i < userIds.size(); i++) {
                userIdList.add(userIds.get(i));
            }
            sendUPush(title, msg, taskId, userIdList, appFlag);
            List<Integer> user_id_list = Lists.newArrayList();
            for (int i = 0; i < 50; i++) {
                user_id_list.add(userIds.get(i));
            }
            userIds.clear();
            userIds.addAll(user_id_list);
        }


        //
        PushConfigVo.AppInfo cfgPush=null;
        switch (appFlag) {
            case PUSH_APP_GCGL:
                cfgPush = pushConfigVo.getGcgl();
                break;
            case PUSH_APP_YDYF:
                cfgPush = pushConfigVo.getYdyf();
                break;
            case PUSH_APP_GXGL:
                cfgPush = pushConfigVo.getGxgl();
                break;
            default:
                log.error("appFlag 错误");
                break;
        }
        if (cfgPush==null){
            throw new Exception("config of Push unfound, notice will not sended");
        }
        log.info("Sending_upush [taskId:%s] [userIds:%s] [appFlag:%s]", taskId, userIds, appFlag);
        ////接收者 信息
        ArrayList<String> alias = Lists.newArrayList();
        for (int i = 0; i <userIds.size() ; i++) {
            alias.add("user_id_"+userIds.get(i));
            alias.add("user_id_"+pushConfigVo.getEnterprise_id()+"_"+userIds.get(i)+"");
        }
        String alia = StringUtils.join(alias, ",");

        // 推送
        // 友盟推送Android
        UmPushUtil.sendAndroidCustomizedcast(cfgPush.getApp_key_android(),cfgPush.getApp_master_secret_android(),alia, AppPlatformTypeEnum.PUSH_PLATFORM_UMENG_ANDROID.getValue(),
                "Android",title,msg,msg,String.valueOf(taskId));

        //// 友盟推送Ios
        UmPushUtil.sendIOSCustomizedcast(cfgPush.getApp_key_ios(),cfgPush.getApp_master_secret_ios(),alia,AppPlatformTypeEnum.PUSH_PLATFORM_UMENG_IOS.getValue(),
                msg,String.valueOf(taskId));

        // xiaomi
        XmPushUtil.sendMessageToUserAccounts(cfgPush.getApp_secret_xiao_mi(),cfgPush.getPackage_name_xiao_mi(),title,msg,alias);

    }

}
