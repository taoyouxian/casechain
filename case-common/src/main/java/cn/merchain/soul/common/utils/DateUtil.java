package cn.merchain.soul.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class DateUtil {
    private static AtomicInteger count = new AtomicInteger(0);

    public static String formatTime(Long time) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String formatTime(Date time) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }

    public static String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//set the style
        return df.format(new Date()) + count.getAndIncrement();
    }

}
