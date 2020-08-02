package com.bonc.tianjin.guotou.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    /**
     * @param date 获取前一天的开始和结束时间
     * @return
     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
     * 1 返回yyyy-MM-dd 23:59:59日期
     */
    public static Date getYestesdayTime(Date date, int addFlag,int startFlag) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - addFlag);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        cal.set(Calendar.MILLISECOND, 000);
        //时分秒（毫秒数）
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        //凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);
        if (startFlag == 0) {
            return cal.getTime();
        } else if (startFlag == 1) {
            cal.set(Calendar.MILLISECOND, 999);
            //凌晨23:59:59
            cal.setTimeInMillis(cal.getTimeInMillis() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000);
        }
        return cal.getTime();
    }
    /**
     * 将毫秒转化为 yyyy-MM-dd hh:mm:ss格式
     *
     * @param millis
     * @return 2015-3-11 下午5:01:13
     */
    public static String fromatMillisToDate(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }
    /**
     * 字符串时间转化为日期
     *
     * @param time
     * @param pattern
     * @return
     */
    public static Date getStringToDate(String time, String pattern) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            date = sdf.parse(time);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }
    /**
     * long转date
     * @param dateLong
     * @return
     */
    public static Date longToDate(long dateLong){
        Date date = new Date(dateLong);
        return date;
    }
    public static void main(String[] args) {
        Date singleDate=new Date();
        //昨天开始的日期
        long startTimeStamps = DateUtil.getYestesdayTime(singleDate, 1,0).getTime();
        System.out.println("startTimeStamps:"+startTimeStamps);
        //今天开始的日期
        long endTimeStamps = DateUtil.getYestesdayTime(singleDate, 1,1).getTime();
        System.out.println("endTimeStamps:"+endTimeStamps);
        String startTimeStr = DateUtil.fromatMillisToDate(startTimeStamps);
        String endTimeStr = DateUtil.fromatMillisToDate(endTimeStamps);
        System.out.println( " 统计订单的开始日期:" + startTimeStr + " 统计订单的结束日期:" + endTimeStr);
        String today01Time=endTimeStr.split(" ")[0]+" 01:00:00";
        long time=getStringToDate(today01Time,"yyyy-MM-dd HH:mm:ss").getTime();
        System.out.println("time:"+time);
       Date d= longToDate(startTimeStamps);
       System.out.println("d:"+d);
    }

}
