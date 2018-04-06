package com.keruyun.fintech.commons;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    /**
     * 获取当前天的前后天数
     *
     * @param i
     * @return
     */
    public static Date getOneDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, i);
        Date oneDay = calendar.getTime();
        return oneDay;
    }
}
