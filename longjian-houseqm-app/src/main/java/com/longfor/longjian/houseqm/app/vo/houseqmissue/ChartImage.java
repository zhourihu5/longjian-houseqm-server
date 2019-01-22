package com.longfor.longjian.houseqm.app.vo.houseqmissue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ChartImage implements Serializable {
    private String url;
    private String name;

    public ChartImage(String url, String name) {
        this.url = url;
        this.name = name;
    }
}
