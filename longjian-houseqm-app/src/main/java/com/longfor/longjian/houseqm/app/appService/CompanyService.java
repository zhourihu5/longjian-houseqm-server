package com.longfor.longjian.houseqm.app.appService;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.app.vo.CompanyVo;
import com.longfor.longjian.houseqm.domain.externalService.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *  Company表即Team表，参考longhu2\zhijian_server_ucenter\app\models\org_models.py
 *
 * Created by lipeishuai on 2018/11/11.
 */

@Repository
@Service
@Slf4j
public class CompanyService {

    @Resource
    private TeamService teamService;

    /**
     * 得到公司列表
     *
     * @param companyId
     * @return
     */
    public List<CompanyVo> getAllCompany(Integer companyId){

        List<CompanyVo> companyVoList = Lists.newArrayList();
        List<Team> teamList = teamService.searchAllCompany(companyId);
        if(CollectionUtils.isEmpty(teamList)){
            return companyVoList;
        }

        log.debug("team list size:" + teamList.size());


        for(Team team: teamList){
            CompanyVo vo = new CompanyVo();
            //BeanUtils.copyProperties(team,vo);
            vo.setId(team.getTeamId());
            vo.setName(team.getTeamName());
            vo.setDisplay_index(team.getDisplayIndex());
            vo.setLevel(team.getLevel());
            vo.setParent_id(team.getParentTeamId());
            vo.setPath(team.getPath());

            if(team.getParentTeamId()>0){
                vo.setIs_group(1);
            }else{
                vo.setIs_group(0);
            }
            companyVoList.add(vo);
        }
        return companyVoList;
    }
}
