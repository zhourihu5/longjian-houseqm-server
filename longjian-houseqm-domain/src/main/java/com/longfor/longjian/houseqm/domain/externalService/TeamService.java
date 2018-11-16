package com.longfor.longjian.houseqm.domain.externalService;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lipeishuai on 2018/11/11.
 */
@Service
@Slf4j
public class TeamService {

    @Resource
    private TeamMapper teamMapper;

    /**
     * 得到当前的Company
     *
     * @param companyId
     * @return
     */
    public Team getCompany(Integer companyId) {
        return teamMapper.selectByPrimaryKey(companyId);
    }

    /**
     *
     * @param companyId
     * @return
     */
    public List<Team> searchAllCompany(Integer companyId) {

        Team team = this.getCompany(companyId);
        if (team == null) {
            return Lists.newArrayList();
        }

        String childPath = team.getPath() + team.getTeamId()+"/";
        List<Team> subTeams = this.getAllSubs(childPath);

        if(CollectionUtils.isEmpty(subTeams)){
            subTeams= Lists.newArrayList();
        }
        subTeams.add(team);
        return subTeams;
    }

    /**
     * 获取所有层级的子公司:
     *
     * @param childPath
     * @return
     */
    private List<Team> getAllSubs(String childPath) {
        return teamMapper.selectSubTeamBasics(childPath);
    }


}
