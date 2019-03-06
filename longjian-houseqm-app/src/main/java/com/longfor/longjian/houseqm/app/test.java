package com.longfor.longjian.houseqm.app;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by Dongshun on 2019/3/6.
 */
public class test {
    public static void main(String[] args) {
        ArrayList<Object> objects = Lists.newArrayList();
        objects.add(1);
        objects.add(1);
        objects.add(1);
        objects.add(1);
        objects.add(1);
        System.out.println(objects);
        System.out.println(StringUtils.join(objects,","));
    }
}
