package com.keruyun.fintech.commons;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>格式化工具
 *
 * @author LiangBin
 * @since 2012-9-19 下午01:30:32
 * @version 1.0
 */
@Slf4j
public class FormatUtils {
	private static final DecimalFormat DECIMAL_FORMAT = new  DecimalFormat("#####0.00");
	public static final String LONG_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	//SimpleDateFormat 的parse, format不是线程安全的。
	public static final ThreadLocal<SimpleDateFormat> LONG_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat dateFormat = new SimpleDateFormat(LONG_DATE_FORMAT_STR);
			dateFormat.setTimeZone(Constant.TIME_ZONE);
			return dateFormat;
		}
	};

	/**
	 * <p>格式化金额  1000000.11
	 *  2012-9-19 下午01:32:52 edit by LiangBin 
	 *
	 * @param money
	 * @return
	 */
	public static String formatMoney(double money){
		return DECIMAL_FORMAT.format(money);
	}
	
	/**
	 * <p>格式化小数  1000000.11
	 * 2013-9-17 下午3:14:43 edit by LiangBin 
	 *
	 * @param num
	 * @param style #####0.00
	 * @return
	 */
	public static String formatNum(double num,String style){
		DecimalFormat df = new DecimalFormat(style);
		return df.format(num);
	}
	
	/**
	 * <p>格式化日期时间
	 * 2013-9-17 下午3:15:24 edit by LiangBin 
	 *
	 * @param date
	 * @param dateStyle
	 * @return
	 */
	public static String formatDataTime(Date date, String dateStyle){
		if(date == null){
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateStyle);
		dateFormat.setTimeZone(Constant.TIME_ZONE);
		return dateFormat.format(date);
	}

	/**
	 * <p>格式化日期时间
	 *  2012-9-19 下午01:34:31 edit by LiangBin 
	 *
	 * @param date
	 * @return
	 */
	public static String formatLongDataTime(Date date){
		if(date == null){
			return "";
		}
		return LONG_DATE_FORMAT.get().format(date);
	}
	
	/**
	 * <p>字符串转日期
	 * 2013-9-17 下午3:15:59 edit by LiangBin
	 *
	 * @param dateStr
	 * @param dateStyle
	 * @return
	 */
	public static Date parseDate(String dateStr, String dateStyle){
		if(StringUtils.isBlank(dateStr)){
			return null;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateStyle);
			dateFormat.setTimeZone(Constant.TIME_ZONE);
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			log.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * <p>字符串转timestamp
	 * 2016-7-11 下午3:15:59 edit by LiangBin
	 *
	 * @param dateStr
	 * @param dateStyle
	 * @return
	 */
	public static Timestamp parseTimestamp(String dateStr, String dateStyle){
		if(StringUtils.isBlank(dateStr)){
			return null;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateStyle);
			dateFormat.setTimeZone(Constant.TIME_ZONE);
			return new Timestamp(dateFormat.parse(dateStr).getTime());
		} catch (ParseException e) {
			log.error(e.getLocalizedMessage());
		}
		return null;
	}
	
	/**
	 * <p>字符串转化  把字符串中{0}、{1}...按顺序替换
	 * 2013-3-22 下午2:10:26 edit by LiangBin 
	 *
	 * @param str
	 * @param obj
	 * @return
	 */
	public static String replaceData4String(String str, Object...obj){
		for (int i = 0; i < obj.length; i++) {
			str = StringUtils.replace(str, "{" + i + "}", String.valueOf(obj[i]));
		}
		return str;
	}
	
	/**
	 * <p>返回每个月的天数
	 * 2014-1-9 下午3:50:40 edit by LiangBin 
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDayMonth(int year,int month){
		int num=0;
		switch(month){
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			num=31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			num=30;
			break;
		case 2:
			num = isLeapYear(year)?29:28;
			break;
		default:
			System.out.println("非法月份");
			break;
		}
		return num;
	}
	
	//定义方法isLeapYear()方法判断指定的年份是否为闰年
	public static boolean isLeapYear(int year){  
		if((year%4==0)&&(year%100!=0)||(year%400==0))
			return true;
		else
			return false;
	}
	
	public static int toWeekIndex(String weekStr){
		return Week.commentOf(weekStr).index;
	}
	
	public static Time toTime(String timeStr){
		return Time.valueOf(timeStr);
	}
	
	public enum Week{
		MONDAY(1,"星期一"),
		TUESDAY(2,"星期二"),
		WEDNESDAY(3,"星期三"),
		THURSDAY(4,"星期四"),
		FRIDAY(5,"星期五"),
		SATURDAY(6,"星期六"),
		SUNDAY(7,"星期日");
		
		private int index;
		private String comment;
		private Week(int index,String comment){
			this.index = index;
			this.comment = comment;
		}
		public static Week commentOf(String comment){
			Week[] weeks = Week.values();
			for (Week week : weeks) {
				if(week.comment.equals(comment)){
					return week;
				}
			}
			return null;
		}
		public static Week indexOf(int index){
			Week[] weeks = Week.values();
			for (Week week : weeks) {
				if(week.index == index){
					return week;
				}
			}
			return null;
		}
		public int getIndex() {
			return index;
		}
		public String getComment() {
			return comment;
		}
	}

	public static Long formatCheckoutPrice(String price){
		if(StringUtils.isBlank(price)){
			return Constant.LONG_ZERO;
		}
		return formatCheckoutPrice(new BigDecimal(price));
	}
    public static Long formatCheckoutPrice(BigDecimal price){
        if (null == price) {
            return Constant.LONG_ZERO;
        }
        BigDecimal checkoutPrice = price.multiply(Constant.BigDecimal_HUNDRED);
        return Long.valueOf(checkoutPrice.longValue());
    }

	/**
	 * 将特定格式的日期时间字符串转换为另一种格式的字符串
	 * @param dateSource
	 * @param sourceStyle
	 * @param targetStyle
     * @return
     */
	public static String dateStrFormat(String dateSource,String sourceStyle,String targetStyle) {
		try {
			SimpleDateFormat sourceDateFomat = new SimpleDateFormat(sourceStyle);
			SimpleDateFormat targetDateFomat = new SimpleDateFormat(targetStyle);
			sourceDateFomat.setTimeZone(Constant.TIME_ZONE);
			targetDateFomat.setTimeZone(Constant.TIME_ZONE);
			Date date = sourceDateFomat.parse(dateSource);
			return targetDateFomat.format(date);
		} catch (ParseException e) {
			log.error(e.getLocalizedMessage());
		}
		return null;
	}
}

