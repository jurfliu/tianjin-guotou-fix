package com.bonc.tianjin.guotou.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    /**
     * 格式化Date时间
     * @param time Date类型时间
     * @param timeFromat String类型格式
     * @return 格式化后的字符串
     */
    public static String parseDateToStr(Date time, String timeFromat) {
        String backTime="";
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFromat);
            backTime= dateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backTime;
    }
}
