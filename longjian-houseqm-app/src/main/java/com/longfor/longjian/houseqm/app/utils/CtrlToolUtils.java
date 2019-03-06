package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.common.base.LjBaseResponse;


public class CtrlToolUtils {

    public static <T> LjBaseResponse<T> errorReturn(LjBaseResponse<T> response, Exception e){
        response.setMessage(e.getMessage());
        response.setResult(1);
        response.setCode(1);
        return response;
    }

}
