package com.fortwone.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fortwone.borderText.BorderTextView;
import com.fortwone.calendar.LunarCalendar;
import com.fortwone.constant.CalendarConstant;
import com.fortwone.dao.ScheduleDAO;
import com.fortwone.vo.ScheduleDateTag;
import com.fortwone.vo.ScheduleVO;

/**
 * 添加日程主界面
 * @author jack_peng
 *
 */
public class ScheduleView extends Activity {

	private LunarCalendar lc = null;
	private ScheduleDAO dao = null;
	private TextView scheduleType = null;
	private TextView dateText = null;
	private TextView scheduleTop = null,seletedtime;
	private EditText scheduleText = null;
	private Button scheduleSave = null;  //保存按钮图片
	private static int hour = -1;
	private static int minute = -1;
	private static ArrayList<String> scheduleDate = null;
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	private String scheduleYear = "";
	private String scheduleMonth = "";
	private String scheduleDay = "";
	private String week = "";
	
	//临时日期时间变量，
	private String tempMonth;
	private String tempDay;

	private String[] sch_type = CalendarConstant.sch_type;
	private String[] remind = CalendarConstant.remind;
	private int sch_typeID = 0;   //日程类型
	private int remindID = 0;     //提醒类型
	
	private static String schText = "";
    int schTypeID = 0;
    
	public ScheduleView() {
		lc = new LunarCalendar();
		dao = new ScheduleDAO(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		scheduleTop = (TextView) findViewById(R.id.scheduleTop);
		scheduleType = (TextView) findViewById(R.id.scheduleType);
		scheduleSave = (Button) findViewById(R.id.save);
		scheduleType.setBackgroundColor(Color.WHITE);
		scheduleType.setText(sch_type[0]+"\t\t\t\t"+remind[remindID]);
		dateText = (TextView) findViewById(R.id.scheduleDate);
		dateText.setBackgroundColor(Color.WHITE);
		scheduleText = (EditText) findViewById(R.id.scheduleText);
		scheduleText.setBackgroundColor(Color.WHITE);
		seletedtime=(TextView)findViewById(R.id.seletedtime);
		if(schText != null){
			//在选择日程类型之前已经输入了日程的信息，则在跳转到选择日程类型之前应当将日程信息保存到schText中，当返回时再次可以取得。
			scheduleText.setText(schText);
			//一旦设置完成之后就应该将此静态变量设置为空，
			schText = "";  
		}

		Date date = new Date();
		if(hour == -1 && minute == -1){
			hour = date.getHours();
			minute = date.getMinutes();
		}
		dateText.setText(getScheduleDate());
		seletedtime.setText("提醒时间："+hour+":"+minute);

		//获得日程类型
		scheduleType.setOnClickListener(new OnClickListener() {
			  
			@Override
			public void onClick(View v) {
				schText = scheduleText.getText().toString();
				Intent intent = new Intent();
				intent.setClass(ScheduleView.this, ScheduleTypeView.class);
				intent.putExtra("sch_remind", new int[]{sch_typeID,remindID});
				startActivity(intent);
			}
		});
		dateText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();  
				new DatePickerDialog(ScheduleView.this, new DatePickerDialog.OnDateSetListener() {
					
					@Override  
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
//						getScheduleDatetime(year, monthOfYear,dayOfMonth);
						
						dateText.setText(getScheduleDatetime(year, monthOfYear,dayOfMonth));
                    }  
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();  
             
                 }
		});
		//获得时间
		seletedtime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new TimePickerDialog(ScheduleView.this, new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int min) {

						hour = hourOfDay;
						String mhour;
						if(hour<10){
							mhour="0"+hour;
						}
						else mhour=""+hour;
						
						minute = min;
						String mminute;
						if(minute<10){
							mminute="0"+minute;
						}
						else mminute=""+minute;
//						dateText.setText(getScheduleDate());
						seletedtime.setText("提醒时间："+mhour+":"+mminute);
					}
				}, hour, minute, true).show();
				
			}
		});
		
		//保存日程信息
		scheduleSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(scheduleText.getText().toString())){
					//判断输入框是否为空
					new AlertDialog.Builder(ScheduleView.this).setTitle("输入日程").setMessage("日程信息不能为空").setPositiveButton("确认", null).show();
				}else{
					//将日程信息保存
				//	String showDate = handleInfo(Integer.parseInt(scheduleYear), Integer.parseInt(tempMonth), Integer.parseInt(tempDay), hour, minute, week, remindID);
	                ScheduleVO schedulevo = new ScheduleVO();
	                schedulevo.setScheduleTypeID(sch_typeID);
	                schedulevo.setRemindID(remindID);
//	                String showDate1 = (getScheduleDatetime(year, monthOfYear,dayOfMonth));
	                String showDate1=dateText.getText().toString();
	                schedulevo.setScheduleDate(showDate1);
	                String showtime = handleInfotext(Integer.parseInt(scheduleYear), Integer.parseInt(tempMonth), Integer.parseInt(tempDay), hour, minute, week, remindID);
	                schedulevo.setScheduletime(showtime);
	                schedulevo.setScheduleContent(scheduleText.getText().toString());
					int scheduleID = dao.save(schedulevo);
					//将scheduleID保存到数据中(因为在CalendarActivity中点击gridView中的一个Item可能会对应多个标记日程(scheduleID))
					String [] scheduleIDs = new String[]{String.valueOf(scheduleID)};
					Intent intent = new Intent();
					intent.setClass(ScheduleView.this, ScheduleInfoView.class);
                    intent.putExtra("scheduleID", scheduleIDs);
					startActivity(intent);
					
					//设置日程标记日期(将所有日程标记日期封装到list中)
					setScheduleDateTag(remindID, scheduleYear, tempMonth, tempDay, scheduleID);
				}
			}
		});
		
	}

	/**
	 * 设置日程标记日期
	 * @param remindID
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setScheduleDateTag(int remindID, String year, String month, String day,int scheduleID){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
		String d = year+"-"+month+"-"+day;
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(d));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//封装要标记的日期
		if(remindID >= 0 && remindID <= 3){
			//"提醒一次","隔10分钟","隔30分钟","隔一小时"（只需标记当前这一天）
			ScheduleDateTag dateTag = new ScheduleDateTag();
			dateTag.setYear(Integer.parseInt(year));
			dateTag.setMonth(Integer.parseInt(month));
			dateTag.setDay(Integer.parseInt(day));
			dateTag.setScheduleID(scheduleID);
			dateTagList.add(dateTag);
		}else if(remindID == 4){
			//每天重复(从设置的日程的开始的之后每一天多要标记)
			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4*7; i++){
				if( i==0 ){
					cal.add(Calendar.DATE, 0);
				}else{
				    cal.add(Calendar.DATE, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 5){
			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4; i++){
				if( i==0 ){
					cal.add(Calendar.WEEK_OF_MONTH, 0);
				}else{
				    cal.add(Calendar.WEEK_OF_MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 6){
			//每月重复(从设置日程的这天(几月几号)，接下来的每月的这一天多要标记)
			for(int i =0; i <= (2049-Integer.parseInt(year))*12; i++){
				if( i==0 ){
					cal.add(Calendar.MONTH, 0);
				}else{
				    cal.add(Calendar.MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 7){
			//每年重复(从设置日程的这天(哪一年几月几号)，接下来的每年的这一天多要标记)
			for(int i =0; i <= 2049-Integer.parseInt(year); i++){
				if( i==0 ){
					cal.add(Calendar.YEAR, 0);
				}else{
				    cal.add(Calendar.YEAR, 1);
				}
				handleDate(cal,scheduleID);
			}
		}
		//将标记日期存入数据库中
		dao.saveTagDate(dateTagList);
	}
	
	/**
	 * 日程标记日期的处理
	 * @param cal
	 */
	public void handleDate(Calendar cal, int scheduleID){
		ScheduleDateTag dateTag = new ScheduleDateTag();
		dateTag.setYear(cal.get(Calendar.YEAR));
		dateTag.setMonth(cal.get(Calendar.MONTH)+1);
		dateTag.setDay(cal.get(Calendar.DATE));
		dateTag.setScheduleID(scheduleID);
		dateTagList.add(dateTag);
	}
	
	/**
	 * 通过选择提醒次数来处理最后的显示结果
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param week
	 * @param remindID
	 */

	public String handleInfotext(int year, int month, int day, int hour, int minute, String week, int remindID){
		String remindType = remind[remindID];     //提醒类型
		String show = "";
		String mhour;
		if(hour<10){
			mhour="0"+hour;
		}
		else mhour=""+hour;
		
		String mminute;
		if(minute<10){
			mminute="0"+minute;
		}
		else mminute=""+minute;
		if(0 <= remindID && remindID <= 4){
			//提醒一次,隔10分钟,隔30分钟,隔一小时
			show = mhour+":"+mminute+"\t\t\t"+remindType;
		}else if(remindID == 5){
			//每周
			show =mhour+":"+mminute+"\t\t\t"+ "每周"+week;
		}else if(remindID == 6){
			//每月
			show = mhour+":"+mminute+"\t\t\t"+"每月"+day+"号";
		}else if(remindID == 7){
			//每年
			show =mhour+":"+mminute+"\t\t\t"+"每年"+month+"月"+day+"日";
		}
		return show;
	}
	
	/**
	 * 点击item之后，显示的日期信息
	 * 
	 * @return
	 */
	public String getScheduleDate() {
		Intent intent = getIntent();
		// intent.getp
		if(intent.getStringArrayListExtra("scheduleDate") != null){
			//从CalendarActivity中传来的值（包含年与日信息）
			scheduleDate = intent.getStringArrayListExtra("scheduleDate");
		}
		int [] schType_remind = intent.getIntArrayExtra("schType_remind");  //从ScheduleTypeView中传来的值(包含日程类型和提醒次数信息)
		
		if(schType_remind != null){
			sch_typeID = schType_remind[0];
			remindID = schType_remind[1];
			scheduleType.setText(sch_type[sch_typeID]+"\t\t\t\t"+remind[remindID]);
		}
		// 得到年月日和星期
		//增加一个临时值
		if(null==scheduleDate.get(0)){
			
	
		scheduleYear="2014";
		scheduleMonth="04";
		scheduleDay="28";
		week="星期一";
		}
		else {
			scheduleYear = scheduleDate.get(0);
			scheduleMonth = scheduleDate.get(1);
			scheduleDay = scheduleDate.get(2);
			week = scheduleDate.get(3);
		}
		//
		
		tempMonth = scheduleMonth;
		if (Integer.parseInt(scheduleMonth) < 10) {
			scheduleMonth = "0" + scheduleMonth;
		}
		
		tempDay = scheduleDay;
		if (Integer.parseInt(scheduleDay) < 10) {
			scheduleDay = "0" + scheduleDay;
		}
		
		String hour_c = String.valueOf(hour);
		String minute_c = String.valueOf(minute);
		if(hour < 10){
			hour_c = "0"+hour_c;
		}
		if(minute < 10){
			minute_c = "0"+minute_c;
		}
		// 得到对应的阴历日期
		String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
		String scheduleLunarMonth = lc.getLunarMonth(); // 得到阴历的月份
		StringBuffer scheduleDateStr = new StringBuffer();
//		scheduleDateStr.append(scheduleYear).append("-").append(scheduleMonth)
//				.append("-").append(scheduleDay).append(" ").append(hour_c).append(":").append(minute_c).append("\n").append(
//						scheduleLunarMonth).append(scheduleLunarDay)
//				.append(" ").append(week);
		scheduleDateStr.append(scheduleYear).append("年").append(scheduleMonth)
		.append("月").append(scheduleDay).append("日 ").append(" ").append(week).append(" 农历").append(
				scheduleLunarMonth).append(scheduleLunarDay)
		;
		// dateText.setText(scheduleDateStr);
		return scheduleDateStr.toString();
	}
/*
 * 重写日历的日期选择，增加日期可选
 */
	public String getScheduleDatetime(int year, int monthOfYear, int dayOfMonth) {
		// 得到年月日和星期
		scheduleYear = ""+year;
		int month=monthOfYear+1;
		scheduleMonth = ""+month;
		tempMonth = scheduleMonth;
		if (Integer.parseInt(scheduleMonth) < 10) {
			scheduleMonth = "0" + scheduleMonth;
		}
		scheduleDay =""+dayOfMonth;
		tempDay = scheduleDay;
		if (Integer.parseInt(scheduleDay) < 10) {
			scheduleDay = "0" + scheduleDay;
		}
		
		int c=20;//代表20世纪
		week=CaculateWeekDay(year,monthOfYear+1,dayOfMonth);
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
	/**
	 * 根据日期的年月日返回阴历日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		// {由于在取得阳历对应的阴历日期时，如果阳历日期对应的阴历日期为"初一"，就被设置成了月份(如:四月，五月。。。等)},所以在此就要判断得到的阴历日期是否为月份，如果是月份就设置为"初一"
		if (lunarDay.substring(1, 2).equals("月")) {
			lunarDay = "初一";
		}
		return lunarDay;
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
}
