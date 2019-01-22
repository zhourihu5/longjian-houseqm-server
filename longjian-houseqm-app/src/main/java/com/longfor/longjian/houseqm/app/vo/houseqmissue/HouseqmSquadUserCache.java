package com.longfor.longjian.houseqm.app.vo.houseqmissue;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.app.service.IHouseqmService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmissue
 * @ClassName: HouseqmSquadUserCache
 * @Description: 用来处理融合app中的消息推送功能，简单缓存一下
 * @Author: hy
 * @CreateDate: 2019/1/12 16:19
 */
@Data
@NoArgsConstructor
@Slf4j
public class HouseqmSquadUserCache implements Serializable {
    private Map<String, List<Integer>> cache;

    @Resource
    private IHouseqmService iHouseqmService;

    public List<Integer> getResolveUserList(int taskId, int userId) {
        String key = this.identifyKey(taskId, userId);
        if (this.cache.get(key) == null) {
            List<Integer> approveIds = null;
            try {
                approveIds = iHouseqmService.searchHouseQmApproveUserIdInMyCheckSquad(userId, taskId);
            } catch (Exception e) {
                log.error(e.getMessage());
                this.cache.put(key, Lists.newArrayList());
            }
            this.cache.put(key, approveIds);
        }
        return this.cache.get(key);
    }

    public String identifyKey(int taskId, int userId) {
        return taskId + "-" + userId;
    }

}
