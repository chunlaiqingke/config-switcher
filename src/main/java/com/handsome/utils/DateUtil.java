package com.handsome.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    static ThreadLocal<SimpleDateFormat> formatLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat());

    public static String toString(Date date){
        return toFormatString(date, "YYYY-MM-dd HH:mm:ss");
    }

    public static String toFormatString(Date date, String format){
        SimpleDateFormat simpleDateFormat = formatLocal.get();
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(date);
    }
}
