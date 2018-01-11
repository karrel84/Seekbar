package com.karrel.mylibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Rell on 2018. 1. 10..
 */
public class RLogTest {
    @Test
    public void setEnableLog() throws Exception {
        String path = "bodyfriend.com.intergrationremote.presenter.MainPresenterImpl.test(MainPresenterImpl.java:34)";
        String[] arrStr = path.split("\\.");
        if (arrStr.length <= 2) return ;

        String str = "";
        for (int i = arrStr.length - 2; i < arrStr.length; i++) {
            str += arrStr[i] + ".";
        }
        System.out.println(str.substring(0, str.length() - 1));
    };

}