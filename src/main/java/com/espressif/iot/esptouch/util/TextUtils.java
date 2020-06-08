package com.espressif.iot.esptouch.util;

public final class TextUtils {
    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence text) {
        return text != null && text.length() > 0;
    }
}
