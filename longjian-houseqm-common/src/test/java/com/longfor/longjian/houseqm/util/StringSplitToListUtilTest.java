package com.longfor.longjian.houseqm.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StringSplitToListUtilTest {

    @Test
    public void splitToStringComma() {
        List<String> result = StringSplitToListUtil.splitToStringComma("1,2,3",",");
        List<String> expected = Arrays.asList("1","2","3");
        assertEquals(expected,result);
    }

    @Test
    public void dataToString() {
        String result = StringSplitToListUtil.dataToString(Arrays.asList(1,2,3),",");
        String expected = "1,2,3";
        assertEquals(expected,result);
    }

    @Test
    public void isInteger() {
        boolean result = StringSplitToListUtil.isInteger("2-");
        boolean expected = false;
        assertEquals(expected,result);
    }

    @Test
    public void removeStartAndEndStrAndSplit() {
        List<String> result = StringSplitToListUtil.removeStartAndEndStrAndSplit("1,2,3","2",",");
        List<String> expected = Arrays.asList("1","2","3");
        assertEquals(expected,result);
    }

    @Test
    public void strToInts() {
        List<Integer> result = StringSplitToListUtil.strToInts("1,2,3",",");
        List<Integer> expected = Arrays.asList(1,2,3);
        assertEquals(expected,result);
    }

    @Test
    public void strToFloats() {
        List<Float> result = StringSplitToListUtil.strToFloats("1,2,3",",");
        List<Float> expected = new ArrayList<>();
        expected.add(1.0f);
        expected.add(2.0f);
        expected.add(3.0f);
        assertEquals(expected,result);
    }

    @Test
    public void strToStrs() {
        List<String> result = StringSplitToListUtil.strToStrs("1,2,3",",");
        List<String> expected = Arrays.asList("1","2","3");
        assertEquals(expected,result);
    }

    @Test
    public void trimFirstAndLastChar() {
        String result = StringSplitToListUtil.trimFirstAndLastChar("-54r4-",'-');
        String expected = "54r4";
        assertEquals(expected,result);
    }
}
