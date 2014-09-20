package com.fortwone.activity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fortwone.borderText.BorderTextView;
import com.fortwone.constant.CalendarConstant;
import com.fortwone.dao.ScheduleDAO;
import com.fortwone.vo.ScheduleVO;


/**
 * 显示/修改所有日程的activity
 * @author jack_peng
 *
 */
public class ScheduleAll extends Activity {

	private ScrollView sv = null;
	private LinearLayout layout = null;
	private TextView textTop = null;
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO = null;
	private ArrayList<ScheduleVO> schList = new ArrayList<ScheduleVO>();
	private String scheduleInfo = "";
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	
	private int scheduleID = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		dao = new ScheduleDAO(this);
		sv = new ScrollView(this);
		
		params.setMargins(0, 5, 0, 0);
		layout = new LinearLayout(this); // 实例化布局对象
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(params);
		
		textTop = new TextView(this, null);
		textTop.setTextColor(Color.WHITE); 
		textTop.setBackgroundColor(0xff009944);
		textTop.setText("日程列表");
		textTop.setHeight(87);
		textTop.setGravity(Gravity.CENTER);
		
		layout.addView(textTop);
		sv.addView(layout);
		
		setContentView(sv);
		
		getScheduleAll();
	}
	
	/**
	 * 得到所有的日程信息
	 */
	public void getScheduleAll(){
		schList = dao.getAllSchedule();
		if(schList != null){
			for (ScheduleVO vo : schList) {
				String content = vo.getScheduleContent();
				int startLine = content.indexOf("\n");
				if(startLine > 0){
					content = content.substring(0, startLine)+"...";
				}else if(content.length() > 30){
					content = content.substring(0, 30)+"...";
				}
				scheduleInfo = vo.getScheduleDate()+"\n"+ vo.getScheduletime()+"\n"+CalendarConstant.sch_type[vo.getScheduleTypeID()]+"\t\t\t\t"+content;
				scheduleID = vo.getScheduleID();
				createInfotext(scheduleInfo, scheduleID);
			}
		}else{
			scheduleInfo = "没有日程";
			createInfotext(scheduleInfo,-1);
		}
	}
	
	/**
	 * 创建放日程信息的textview
	 */
	public void createInfotext(String scheduleInfo, int scheduleID){
		final BorderTextView info = new BorderTextView(this, null);
		info.setText(scheduleInfo);
		
		info.setTextColor(Color.BLACK); 
		info.setTextSize(16);
		info.setBackgroundColor(Color.WHITE);
		info.setLayoutParams(params);
		info.setGravity(Gravity.CENTER_VERTICAL);
		info.setPadding(10, 20, 10,20);
		info.setTag(scheduleID);
		layout.addView(info);
		
		//点击每一个textview就跳转到shceduleInfoView中显示详细信息
		info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String schID = String.valueOf(v.getTag());
				String scheduleIDs[] = new String[]{schID};
				Intent intent = new Intent();
				intent.setClass(ScheduleAll.this, ScheduleInfoView.class);
				intent.putExtra("scheduleID", scheduleIDs);
				startActivity(intent);
			}
		});
		
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1, menu.FIRST, menu.FIRST, "返回日历");
		menu.add(1, menu.FIRST+1, menu.FIRST+1, "添加日程");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case Menu.FIRST:
			Intent intent = new Intent();
			intent.setClass(ScheduleAll.this, CalendarActivity.class);
			startActivity(intent);
			break;
		case Menu.FIRST+1:
			Intent intent1 = new Intent();
			final ArrayList<String> scheduleDate = new ArrayList<String>();
					Date date = new Date();
			    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
			    	String currentDate = sdf.format(date);  //当期日期
			    	int year_c = Integer.parseInt(currentDate.split("-")[0]);
			    	int month_c = Integer.parseInt(currentDate.split("-")[1]);
			    	int day_c = Integer.parseInt(currentDate.split("-")[2]);
			scheduleDate.add(""+year_c);
			scheduleDate.add(""+month_c);
			scheduleDate.add(""+day_c);
			scheduleDate.add("");
			intent1.putStringArrayListExtra("scheduleDate", scheduleDate);
			intent1.setClass(ScheduleAll.this, ScheduleView.class);
			startActivity(intent1);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AbsListView.LayoutParams;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.fortwone.constant.CalendarConstant;
//import com.fortwone.dao.ScheduleDAO;
//import com.fortwone.vo.ScheduleVO;
//
///**
// * 显示/修改所有日程的activity
// * @author jack_peng
// *
// */
//public class ScheduleAll extends Activity {
//
//	private ScrollView sv = null;
//	private FrameLayout layout = null;
//	private FrameLayout textlist=null;
//	private TextView textTop;
//	private TextView info,infolist;
//	private Button add;
//	private Button back;
//	private ScheduleDAO dao = null;
//	private ScheduleVO scheduleVO = null;
//	private ArrayList<ScheduleVO> schList = new ArrayList<ScheduleVO>();
//	private String scheduleInfo = "";
//	private ListView list=null;
//	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//	
//	private int scheduleID = -1;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		dao = new ScheduleDAO(this);
//		sv = new ScrollView(this);
//		params.setMargins(0, 5, 0, 0);
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//				FrameLayout.LayoutParams.WRAP_CONTENT,     
//                FrameLayout.LayoutParams.WRAP_CONTENT );
//		layout = (FrameLayout)findViewById(R.id.schedulelist);
////		layout.setOrientation(LinearLayout.VERTICAL);
////		layout.setBackgroundResource(R.drawable.schedule_bk);
//		layout.setLayoutParams(params);
//		setContentView(R.layout.scheduleall);
//		textTop=(TextView)findViewById(R.id.toptext1);
//		getScheduleAll();
//		back=(Button)findViewById(R.id.back);
//		add=(Button)findViewById(R.id.add);
//		back.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent1 = new Intent();
//				intent1.setClass(ScheduleAll.this, CalendarActivity.class);
//				startActivity(intent1);
//			}
//		});
//		add.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent add_intent = new Intent();
//				add_intent.setClass(ScheduleAll.this, ScheduleView.class);
//				startActivity(add_intent);
//			}
//		});
//	}
//	
//	/**
//	 * 得到所有的日程信息
//	 */
//	public void getScheduleAll(){
//		schList = dao.getAllSchedule();
//		if(schList != null){
//			textTop.setText("日程列表");
//			
//			for (ScheduleVO vo : schList) {
//				String content = vo.getScheduleContent();
//				int startLine = content.indexOf("\n");
//				if(startLine > 0){
//					content = content.substring(0, startLine)+"...";
//				}else if(content.length() > 30){
//					content = content.substring(0, 30)+"...";
//				}
//				scheduleInfo = CalendarConstant.sch_type[vo.getScheduleTypeID()]+"\n"+vo.getScheduleDate()+"\n"+content;
//				scheduleID = vo.getScheduleID();
//				createInfotext(scheduleInfo, scheduleID);
//				Log.e("tips", scheduleInfo);
//				Log.e("tips", scheduleID+"");
//				
//			}
//		}else{
//			scheduleInfo = "没有日程";
//			createInfotext(scheduleInfo,-1);
//		}
//	}
//	
//	/**
//	 * 创建放日程信息的textview
//	 */
//	public void createInfotext(String scheduleInfo, int scheduleID){
////		info=(TextView)findViewById(R.id.info);
////		
////		info.setText(scheduleInfo);
////		info.setTag(scheduleID);
////		this.list=new ListView(this);
////		this.list.setAdapter(new A);
////		
////		
//		final TextView info = new TextView(this, null);
////		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
////				FrameLayout.LayoutParams.WRAP_CONTENT,     
////                FrameLayout.LayoutParams.WRAP_CONTENT );
////		textlist=(FrameLayout)findViewById(R.id.schedulelist);
//		info.setText(scheduleInfo);
//		info.setTextColor(Color.BLACK); 
//		info.setBackgroundColor(Color.WHITE);
//		info.setLayoutParams(params);
//		info.setGravity(Gravity.CENTER_VERTICAL);
//		info.setPadding(10, 5, 10, 5);
//		info.setTag(scheduleID);
//		layout.addView(info);
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
////		textlist=(FrameLayout)findViewById(R.id.schedulelist);
////		
////		for(ScheduleVO vo : schList){
////			int i=0;
////			infolist=new TextView(this);
////			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
////					FrameLayout.LayoutParams.WRAP_CONTENT,     
////	                FrameLayout.LayoutParams.WRAP_CONTENT );
////			infolist.setPadding(80, 100+i, 5, 5);
////			infolist.setId(i);
////	        infolist.setText(scheduleInfo);
////	        infolist.setTag(scheduleID);
//////	        infolist.set = 5;
////	        infolist.setTag(scheduleID);
////	        textlist.addView(infolist,params);
////	        i=i+60;
////	        }
//		
//		//点击每一个textview就跳转到shceduleInfoView中显示详细信息
//		info.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String schID = String.valueOf(v.getTag());
//				String scheduleIDs[] = new String[]{schID};
//				Intent intent = new Intent();
//				intent.setClass(ScheduleAll.this, ScheduleInfoView.class);
//				intent.putExtra("scheduleID", scheduleIDs);
//				startActivity(intent);
//			}
//		}); 
//		
//		
//	}
//	
//	
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		menu.add(1, menu.FIRST, menu.FIRST, "返回日历");
//		menu.add(1, menu.FIRST+1, menu.FIRST+1, "添加日程");
//		return super.onCreateOptionsMenu(menu);
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		switch(item.getItemId()){
//		case Menu.FIRST:
//			Intent intent = new Intent();
//			intent.setClass(ScheduleAll.this, CalendarActivity.class);
//			startActivity(intent);
//			break;
//		case Menu.FIRST+1:
//			Intent intent1 = new Intent();
//			intent1.setClass(ScheduleAll.this, ScheduleView.class);
//			startActivity(intent1);
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//}
