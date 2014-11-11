package com.fortwone.utils;

import java.util.List;

import com.fortwone.calendar.LunarCalendar;
import com.fortwone.vo.ScheduleVO;

public class ScheduleUtils {
	
	public static LunarCalendar lc=new LunarCalendar(); 
	public static List<ScheduleVO> getAllScheduleByDate(){
		return null;
	}
	
	
	/*
	 * 重写日历的日期选择，增加日期可选
	 */
	public static  String getScheduleDateString(int year, int monthOfYear, int dayOfMonth) {
		// 得到年月日和星期
		String scheduleYear = ""+year;
		int month=monthOfYear;
		String scheduleMonth = ""+month;
		if (Integer.parseInt(scheduleMonth) < 10) {
			scheduleMonth = "0" + scheduleMonth;
		}
		String scheduleDay =""+dayOfMonth;
		if (Integer.parseInt(scheduleDay) < 10) {
			scheduleDay = "0" + scheduleDay;
		}
		
		int c=20;//代表20世纪
		String week=CaculateWeekDay(year,monthOfYear+1,dayOfMonth);
		// 得到对应的阴历日期
		String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
		String scheduleLunarMonth = lc.getLunarMonth(); // 得到阴历的月份
		StringBuffer scheduleDateStr = new StringBuffer();
		scheduleDateStr.append(scheduleYear).append("年").append(scheduleMonth)
		.append("月").append(scheduleDay).append("日 ").append(" ").append(week).append(" 农历").append(
				scheduleLunarMonth).append(scheduleLunarDay)
		;
		// dateText.setText(scheduleDateStr);
		return scheduleDateStr.toString();
	}
	
	
	/*
	 * 通过蔡勒公式计算周星期几
	 * 
	 */
	public static String CaculateWeekDay(int y,int m,int d){
        if(m==1){m=13;y--;}
        if(m==2){m=14;y--;}
        int c=y/100;
        y%=100;
        int week=(c/4-2*c+y+y/4+13*(m+1)/5+d-1)%7;
        if(week<0){week=7-(-week)%7;}
        switch(week){
        case 1:return "星期一";
        case 2:return "星期二";
        case 3:return "星期三";
        case 4:return "星期四";
        case 5:return "星期五";
        case 6:return "星期六";
        default:return "星期日";
        }
	}
		
	/**
	 * 根据日期的年月日返回阴历日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static  String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		// {由于在取得阳历对应的阴历日期时，如果阳历日期对应的阴历日期为"初一"，就被设置成了月份(如:四月，五月。。。等)},所以在此就要判断得到的阴历日期是否为月份，如果是月份就设置为"初一"
		if (lunarDay.substring(1, 2).equals("月")) {
			lunarDay = "初一";
		}
		return lunarDay;
	}
}
