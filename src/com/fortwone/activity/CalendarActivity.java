package com.fortwone.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.fortwone.borderText.BorderText;
import com.fortwone.constant.CalendarConstant;
import com.fortwone.dao.ScheduleDAO;

/**
 * 日历显示activity
 * @author jack_peng
 *
 */
public class CalendarActivity extends Activity implements OnGestureListener {

	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null;
	private GridView gridView = null;
	private BorderText topText = null;
	private Drawable draw = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private Button orrang_day;
	private Button lunar_transport,addbutton;
	private TextView hinttext;
	private ScheduleDAO dao = null;


	public CalendarActivity() {

		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	
    	dao = new ScheduleDAO(this);
    	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gestureDetector = new GestureDetector(this);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        orrang_day=(Button)findViewById(R.id.orrang_day);
        lunar_transport=(Button)findViewById(R.id.lunar_transport);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView,0);
		topText = (BorderText) findViewById(R.id.toptext);
		addTextToTopTextView(topText);
		lunar_transport.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent1 = new Intent();
		        	intent1.setClass(CalendarActivity.this, CalendarConvert.class);
		        	intent1.putExtra("date", new int[]{year_c,month_c,day_c});
		        	startActivity(intent1);
				}
		});
		orrang_day.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	        	Intent intent = new Intent();
				intent.setClass(CalendarActivity.this, ScheduleAll.class);
				startActivity(intent);
			}
		});
	}
	//添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.top_day);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		textDate.append("(").append(calV.getCyclical()).append(")").append(calV.getAnimalsYear()).append("年");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("\t").append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		view.setText(textDate);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 120) {
            //像左滑动
			//addGridView();   //添加一个gridView
			jumpMonth++;     //下一个月
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
			//addGridView();   //添加一个gridView
			jumpMonth--;     //上一个月
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        gvFlag++;
	        addTextToTopTextView(topText);
	        flipper.addView(gridView,gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}
	
	//添加gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		//取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
//	        gridView.setBackgroundResource(R.color.bg);
		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return CalendarActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position  && position <= endPosition)
				  {
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
	                  String week = "";
	                  
	                  //通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
	                  int year=Integer.parseInt(scheduleYear);
	                  int month=Integer.parseInt(scheduleMonth);
	                  int day=Integer.parseInt(scheduleDay);
	                 
	                  if( dao.hasScheduleByDate(year, month, day))
	                  {
	                	  //跳转到显示这一天的所有日程信息界面
	                	  Intent intent = new Intent(CalendarActivity.this, ScheduleInfoView.class);
		  				  intent.putExtra("year", year);
		  				  intent.putExtra("month", month);
		  				  intent.putExtra("day", day);
		  				  startActivity(intent);
	                  }else{
	                  //直接跳转到需要添加日程的界面
	                	  
		                  //得到这一天是星期几
		                  switch(position%7){
		                  case 0:
		                	  week = "星期日";
		                	  break;
		                  case 1:
		                	  week = "星期一";
		                	  break;
		                  case 2:
		                	  week = "星期二";
		                	  break;
		                  case 3:
		                	  week = "星期三";
		                	  break;
		                  case 4:
		                	  week = "星期四";
		                	  break;
		                  case 5:
		                	  week = "星期五";
		                	  break;
		                  case 6:
		                	  week = "星期六";
		                	  break;
		                  }	
		                  final ArrayList<String> scheduleDate = new ArrayList<String>();
		                  scheduleDate.add(scheduleYear);
		                  scheduleDate.add(scheduleMonth);
		                  scheduleDate.add(scheduleDay);
		                  scheduleDate.add(week);
		                  //scheduleDate.add(scheduleLunarDay);
		                  
		                  addbutton=(Button)findViewById(R.id.addarrange);
	                	  hinttext=(TextView)findViewById(R.id.hittentext);
	                	  addbutton.setVisibility(0);
	                	  hinttext.setVisibility(0);
	                	  hinttext.setText(scheduleYear+"年"+scheduleMonth+"月"+scheduleDay+"日  无日程安排");
	                	  addbutton.setOnClickListener(new OnClickListener() {
	          				@Override
	        				public void onClick(View v) {
	          					Intent intent = new Intent();
		  		                intent.putStringArrayListExtra("scheduleDate", scheduleDate);
		  		                intent.setClass(CalendarActivity.this, ScheduleView.class);
		  		                startActivityForResult(intent, CalendarConstant.REQUEST_CODE_ADD_SCHEDULE);
	          				}
	                	  });
	                  }
				  }
			}
		});
		gridView.setLayoutParams(params);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			if(requestCode==CalendarConstant.REQUEST_CODE_ADD_SCHEDULE)
			{
				int year=data.getIntExtra("year", 1970);
				int month=data.getIntExtra("month", 1);
				int day=data.getIntExtra("day", 1);
				calV.updateCarlendar(year, month, day);
			}
		}
		
	}	
	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu.FIRST, menu.FIRST, "今天");
		menu.add(0, menu.FIRST+1, menu.FIRST+1, "跳转");
		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * 选择菜单
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
        case Menu.FIRST:
        	//跳转到今天
        	int xMonth = jumpMonth;
        	int xYear = jumpYear;
        	int gvFlag =0;
        	jumpMonth = 0;
        	jumpYear = 0;
        	addGridView();   //添加一个gridView
        	year_c = Integer.parseInt(currentDate.split("-")[0]);
        	month_c = Integer.parseInt(currentDate.split("-")[1]);
        	day_c = Integer.parseInt(currentDate.split("-")[2]);
        	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView,gvFlag);
	        if(xMonth == 0 && xYear == 0){
	        	//nothing to do
	        }else if((xYear == 0 && xMonth >0) || xYear >0){
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
				this.flipper.showNext();
	        }else{
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
				this.flipper.showPrevious();
	        }
			flipper.removeViewAt(0);
        	break;
        case Menu.FIRST+1:
        	new DatePickerDialog(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					//1901-1-1 ----> 2049-12-31
					if(year < 1901 || year > 2049){
						//不在查询范围内
						new AlertDialog.Builder(CalendarActivity.this).setTitle("错误日期").setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
					}else{
						int gvFlag = 0;
						addGridView();   //添加一个gridView
			        	calV = new CalendarView(CalendarActivity.this, CalendarActivity.this.getResources(),year,monthOfYear+1,dayOfMonth);
				        gridView.setAdapter(calV);
				        addTextToTopTextView(topText);
				        gvFlag++;
				        flipper.addView(gridView,gvFlag);
				        if(year == year_c && monthOfYear+1 == month_c){
				        	//nothing to do
				        }
				        if((year == year_c && monthOfYear+1 > month_c) || year > year_c ){
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_out));
				        	CalendarActivity.this.flipper.showNext();
				        }else{
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_out));
				        	CalendarActivity.this.flipper.showPrevious();
				        }
				        flipper.removeViewAt(0);
				        //跳转之后将跳转之后的日期设置为当期日期
				        year_c = year;
						month_c = monthOfYear+1;
						day_c = dayOfMonth;
						jumpMonth = 0;
						jumpYear = 0;
					}
				}
			},year_c, month_c-1, day_c).show();
        	break;
        }
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}