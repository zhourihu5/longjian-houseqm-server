package com.longfor.longjian.houseqm.app.vo.url;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.url
 * @ClassName: Userinfo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 20:48
 */
@Data
@NoArgsConstructor
public class Userinfo implements Serializable {
    //username    string
    //	password    string
    //	passwordSet bool
    private String username;
    private String password;
    private boolean passwordSet;

}
