package com.longfor.longjian.houseqm.app.vo.houseqmissue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class StatExportPic implements Serializable {
    private String remote_path;
    private String local_path;

    public String tplPath(Boolean debug) {
        if (debug) {
            return remote_path;
        }
        return local_path;
    }
}
