package com.espressif.iot.esptouch.util;

public class Log {
    public static void v(String tag, String log) {
        System.out.println(tag + " | " + log);
    }

    public static void d(String tag, String log) {
        System.out.println(tag + " | " + log);
    }

    public static void i(String tag, String log) {
        System.out.println(tag + " | " + log);
    }

    public static void w(String tag, String log) {
        System.err.println(tag + " | " + log);
    }

    public static void e(String tag, String log) {
        System.err.println(tag + " | " + log);
    }
}
