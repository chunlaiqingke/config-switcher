package com.handsome.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    static ThreadLocal<SimpleDateFormat> formatLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat());

    public static String toString(Date date){
        return toFormatString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toFormatString(Date date, String format){
        SimpleDateFormat simpleDateFormat = formatLocal.get();
        simpleDateFormat.applyLocalizedPattern(format);
        return simpleDateFormat.format(date);
    }
}
