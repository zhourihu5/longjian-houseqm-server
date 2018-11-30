package com.longfor.longjian.houseqm.app.req;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class StatGroupReq implements Serializable {

    private String query;
    private VariableVo variables;


    @Data
    @NoArgsConstructor
    public class VariableVo implements Serializable{

        private Map<String, Object> map;
        private String categoryKey;
        private int timeFrameMax;
        private String timeFrameType;

        public Map<String, Object> toMap(){

            if(map == null){
                map = Maps.newHashMap();
                map.put("categoryKey",this.getCategoryKey());
                map.put("timeFrameMax",this.getTimeFrameMax());
                map.put("timeFrameType",this.getTimeFrameType());
            }
            return map;
        }
    }

}
