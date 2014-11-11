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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import com.fortwone.utils.ScheduleUtils;
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
	private TextView tv_scheduleType = null;
	private TextView tv_remindType = null;
	private TextView dateText = null;
	private TextView scheduleTop = null,seletedtime;
	private EditText scheduleText = null;
	private Button scheduleSave = null;  //保存按钮图片
	private static ArrayList<String> scheduleDate = null;
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	private String scheduleYear = "";
	private String scheduleMonth = "";
	private String scheduleDay = "";
	private String week = "";
	private String remindDate;
	private String remindTime;
	private String scheduleContent;
	
	//临时日期时间变量，
	private int tempYear;
	private int tempMonth;
	private int tempDay;
	private int tempHour;
	private int tempMinute;

	private int scheduleType = 0;   //日程类型
	private int tempRemindType = 0;     //提醒类型
	private int remindType=0;
	private int scheduleId;
	ScheduleVO schedule;
	
	private static String schText = "";
    int schTypeID = 0;
    
    private static int mode;
    
	public ScheduleView() {
		lc = new LunarCalendar();
		dao = new ScheduleDAO(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		Intent intent=getIntent();
		mode=intent.getIntExtra("mode", CalendarConstant.MODE_ADD_SCHEDULE);
		scheduleId=intent.getIntExtra("schedule_id", 0);
		Log.i("schedule_id", ""+scheduleId);
		initView();
		
		
	}
	public void initView(){
		if(mode==CalendarConstant.MODE_EDIT_SCHEDULE)
		{
			schedule=dao.getScheduleByID(scheduleId);
			
			scheduleType=schedule.getScheduleTypeID();
			remindType=schedule.getRemindID();
			tempRemindType=remindType;
			
			remindDate=schedule.getScheduleDate();
			
			Calendar c=dao.getScheduleFromTagDateByID(scheduleId);
			tempYear=c.get(Calendar.YEAR);
			tempMonth=c.get(Calendar.MONTH);
			tempDay=c.get(Calendar.DAY_OF_MONTH);
			remindTime=schedule.getScheduleTime();
			
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(sdf.parse(remindTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			tempHour=cal.get(Calendar.HOUR_OF_DAY);
			tempMinute=cal.get(Calendar.MINUTE);
			
			
			scheduleContent=schedule.getScheduleContent();
		}else{
			scheduleType=0;
			remindType=0;
			tempRemindType=remindType;
			
			Calendar c = Calendar.getInstance();
			tempYear=c.get(Calendar.YEAR);
			tempMonth=c.get(Calendar.MONTH)+1;
			tempDay=c.get(Calendar.DAY_OF_MONTH);
			remindDate=ScheduleUtils.getScheduleDateString(tempYear, tempMonth, tempDay);
			
			SimpleDateFormat sdf =new  SimpleDateFormat("HH:mm");
			tempHour=c.get(Calendar.HOUR_OF_DAY);
			tempMinute=c.get(Calendar.MINUTE);
			remindTime=sdf.format(new Date());
			
			scheduleContent="";
		}

		//日程类型
		tv_scheduleType = (TextView) findViewById(R.id.scheduleType);
		tv_scheduleType.setText(CalendarConstant.schedule_type[scheduleType]);
		tv_scheduleType.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				new AlertDialog.Builder(ScheduleView.this).setTitle(getString(R.string.schedule_type))
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(
						CalendarConstant.schedule_type, 
						scheduleType,
						new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog,int which) 
							{
								scheduleType = which;
								tv_scheduleType.setText(CalendarConstant.schedule_type[scheduleType]);
							}
						}).setPositiveButton(getString(R.string.ensure), null).setNegativeButton(getString(R.string.cancel), null).show();
			}
		});
		
		tv_remindType=(TextView)findViewById(R.id.tv_remindType);
		tv_remindType.setText(CalendarConstant.remind_type[tempRemindType]);
		tv_remindType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				new AlertDialog.Builder(ScheduleView.this).setTitle(getString(R.string.remind_type))
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(
						CalendarConstant.remind_type, 
						tempRemindType,
						new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog,int which) 
							{
								tempRemindType = which;
								tv_remindType.setText(CalendarConstant.remind_type[tempRemindType]);
							}
						}).setPositiveButton(getString(R.string.ensure), null).setNegativeButton(getString(R.string.cancel), null).show();
				}
		});
		
		//提醒日期
		dateText = (TextView) findViewById(R.id.scheduleDate);
		dateText.setText(remindDate);
		dateText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();  
				new DatePickerDialog(ScheduleView.this, new DatePickerDialog.OnDateSetListener() {
					
					@Override  
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
						//getScheduleDatetime(year, monthOfYear,dayOfMonth);
						tempYear=year;
						tempMonth=monthOfYear+1;
						Log.i("select month " ,tempMonth+"");
						tempDay=dayOfMonth;
						dateText.setText(getScheduleDatetime(year, monthOfYear,dayOfMonth));
                    }  
                }, tempYear, tempMonth-1, tempDay).show();  
           }
		});
		scheduleText = (EditText) findViewById(R.id.scheduleText);
		scheduleText.setText(scheduleContent);
		
		//提醒时间
		seletedtime=(TextView)findViewById(R.id.seletedtime);
		seletedtime.setText(remindTime);
		seletedtime.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				new TimePickerDialog(ScheduleView.this, new OnTimeSetListener() 
				{
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int min) 
					{
						tempHour = hourOfDay;
						String mhour;
						if(tempHour<10){
							mhour="0"+tempHour;
						}else{ 
							mhour=""+tempHour;
						}
						
						tempMinute = min;
						String mminute;
						if(tempMinute<10){
							mminute="0"+tempMinute;
						}else {
							mminute=""+tempMinute;
						}
						//dateText.setText(getScheduleDate());
						remindTime=mhour+":"+mminute;
						seletedtime.setText(remindTime);
					}
				}, tempHour, tempMinute, true).show();
				
			}
		});
		
		//保存日程信息
		scheduleSave = (Button) findViewById(R.id.save);
		scheduleSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(TextUtils.isEmpty(scheduleText.getText().toString()))
				{
					//判断输入框是否为空
					new AlertDialog.Builder(ScheduleView.this).setTitle("输入日程").setMessage("日程信息不能为空").setPositiveButton("确认", null).show();
				}else
				{
					//将日程信息保存
					String date=dateText.getText().toString();
					//String showDate = handleInfo(Integer.parseInt(scheduleYear), Integer.parseInt(tempMonth), Integer.parseInt(tempDay), hour, minute, week, remindID);
	                ScheduleVO schedulevo = new ScheduleVO();
	                schedulevo.setScheduleID(scheduleId);
	                schedulevo.setScheduleTypeID(scheduleType);
	                schedulevo.setRemindID(tempRemindType);
	                schedulevo.setScheduleDate(date);
	                schedulevo.setScheduletime(remindTime);
	                schedulevo.setScheduleContent(scheduleText.getText().toString());
	                
	                
	                if(mode==CalendarConstant.MODE_EDIT_SCHEDULE)
	                {
	                	Log.i("save mode", "edit");
	                	dao.update(schedulevo);
	                	//如果修改了提醒方式 那么 删除以前所有提醒的日期
	                	if(remindType!=tempRemindType)
	                	{
	                		Log.i("remindType!=tempRemindType", "delete scheduletag");
	                		deleteScheduleTag(scheduleId);
	                	}
	                	setScheduleDateTag(tempRemindType, tempYear, tempMonth, tempDay, scheduleId);
	                }else{
	                	Log.i("save mode", "add");
	                	int id = dao.save(schedulevo);
	                	//设置日程标记日期(将所有日程标记日期封装到list中)
						setScheduleDateTag(tempRemindType, tempYear, tempMonth, tempDay, id);
	                }
				
	                Intent intent=new Intent();
	                intent.putExtra("year", tempYear);
	                intent.putExtra("month", tempMonth);
	                intent.putExtra("month", tempDay);
					setResult(RESULT_OK,intent);
					finish();
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
	public void setScheduleDateTag(int remindID, int year, int month, int day,int scheduleID){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
		String d = year+"-"+month+"-"+day;
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//封装要标记的日期
		if(remindID >= 0 && remindID <= 3){
			//"提醒一次","隔10分钟","隔30分钟","隔一小时"（只需标记当前这一天）
			ScheduleDateTag dateTag = new ScheduleDateTag();
			dateTag.setYear(year);
			dateTag.setMonth(month);
			dateTag.setDay(day);
			dateTag.setScheduleID(scheduleID);
			dateTagList.add(dateTag);
		}else if(remindID == 4){
			//每天重复(从设置的日程的开始的之后每一天多要标记)
			for(int i =0; i <= (2049-year)*12*4*7; i++){
				if( i==0 )
				{
					cal.add(Calendar.DATE, 0);
				}else{
				    cal.add(Calendar.DATE, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 5){
			for(int i =0; i <= (2049-year)*12*4; i++){
				if( i==0 ){
					cal.add(Calendar.WEEK_OF_MONTH, 0);
				}else{
				    cal.add(Calendar.WEEK_OF_MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 6){
			//每月重复(从设置日程的这天(几月几号)，接下来的每月的这一天多要标记)
			for(int i =0; i <= (2049-year)*12; i++){
				if( i==0 ){
					cal.add(Calendar.MONTH, 0);
				}else{
				    cal.add(Calendar.MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 7){
			//每年重复(从设置日程的这天(哪一年几月几号)，接下来的每年的这一天多要标记)
			for(int i =0; i <= 2049-year; i++){
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
	
	public void deleteScheduleTag(int schedule){
		dao.deleteScheduleTag(schedule);
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
		String remindType = CalendarConstant.remind_type[remindID];     //提醒类型
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
		else {
			mminute=""+minute;
		}
/*		if(0 <= remindID && remindID <= 4){
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
		}*/
		show = mhour+":"+mminute;
		return show;
	}
	
	/**
	 * 点击item之后，显示的日期信息
	 * 
	 * @return
	 */
	/*public String getScheduleDate() {
		Intent intent = getIntent();
		// intent.getp
		if(intent.getStringArrayListExtra("scheduleDate") != null){
			//从CalendarActivity中传来的值（包含年与日信息）
			scheduleDate = intent.getStringArrayListExtra("scheduleDate");
		}
		int [] schType_remind = intent.getIntArrayExtra("schType_remind");  //从ScheduleTypeView中传来的值(包含日程类型和提醒次数信息)
		
		if(schType_remind != null){
			scheduleType = schType_remind[0];
			remindType = schType_remind[1];
			tv_scheduleType.setText(CalendarConstant.schedule_type[scheduleType]+"\t\t\t\t"+CalendarConstant.remind_type[remindType]);
		}
		// 得到年月日和星期
		//增加一个临时值
		if(null==scheduleDate.get(0)){
			scheduleYear="2014";
			scheduleMonth="04";
			scheduleDay="28";
			week="星期一";
		}else {
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
	}*/
/*
 * 重写日历的日期选择，增加日期可选
 */
	public String getScheduleDatetime(int year, int monthOfYear, int dayOfMonth) {
		// 得到年月日和星期
		scheduleYear = ""+year;
		int month=monthOfYear+1;
		
		scheduleMonth = ""+month;
		if (Integer.parseInt(scheduleMonth) < 10) {
			scheduleMonth = "0" + scheduleMonth;
		}
		scheduleDay =""+dayOfMonth;
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
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
