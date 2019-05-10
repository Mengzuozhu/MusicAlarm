package com.example.randomalarm.common;

import java.util.Locale;

/**
 * author : Mzz
 * date : 2019 2019/5/10 20:28
 * description :
 */
public class StringHelper {

    private final static Locale CHINA = Locale.CHINA;

    public static String getLocalFormat(String format, Object... args) {
        return String.format(CHINA, format, args);
    }

}
