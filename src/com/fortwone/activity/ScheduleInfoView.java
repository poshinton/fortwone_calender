package com.fortwone.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fortwone.borderText.BorderTextView;
import com.fortwone.constant.CalendarConstant;
import com.fortwone.dao.ScheduleDAO;
import com.fortwone.vo.ScheduleVO;

public class ScheduleInfoView extends Activity {

	private RelativeLayout layout = null;
	private TextView textTop = null,scheduleinfodate,scheduleinfotime,scheduleinfotype,scheduleinfonotes;
	private BorderTextView info = null;
	private BorderTextView date = null;
	private BorderTextView type = null;
	private EditText editInfo = null;
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO = null;
	private Button  scheduleback;
	
	private String scheduleInfo = "";    //日程信息被修改前的内容
	private String scheduleChangeInfo = "";  //日程信息被修改之后的内容
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_info);
		dao = new ScheduleDAO(this);
	    textTop=(TextView)findViewById(R.id.scheduleInfoTop);
	    
//        //final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//        params.setMargins(0, 5, 0, 0);
		layout = (RelativeLayout)findViewById(R.id.scheduleInfolayout);
//		layout.setOrientation(LinearLayout.VERTICAL);
//		layout.setLayoutParams(params);
//		
//		textTop = new BorderTextView(this, null);
		textTop.setTextColor(Color.WHITE); 
		textTop.setBackgroundColor(0xff009944);
		textTop.setText("日程列表");
		textTop.setHeight(57);
//		textTop.setGravity(Gravity.CENTER);
		
//		editInfo = (EditText)findViewById(R.id.scheduleDatetext);
//		editInfo.setTextColor(Color.BLACK); 
//		editInfo.setBackgroundColor(Color.WHITE);
//		editInfo.setHeight(200);
//		editInfo.setGravity(Gravity.TOP);
////		editInfo.setLayoutParams(params);
//		editInfo.setPadding(10, 5, 10, 5);		
	//	layout.addView(textTop);
		
		Intent intent = getIntent();
		
		//一个日期可能对应多个标记日程(scheduleID)
		String[] scheduleIDs = intent.getStringArrayExtra("scheduleID");
		//显示日程详细信息
		for(int i = 0; i< scheduleIDs.length; i++){
			handlerInfo(Integer.parseInt(scheduleIDs[i]));
		}
//		setContentView(layout);
		
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1, menu.FIRST, menu.FIRST, "所有日程");
		menu.add(1, menu.FIRST+1, menu.FIRST+1,"添加日程");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case Menu.FIRST:
			Intent intent = new Intent();
			intent.setClass(ScheduleInfoView.this, ScheduleAll.class);
			startActivity(intent);
			break;
        case Menu.FIRST+1:
        	Intent intent2 = new Intent();
        final ArrayList<String> scheduleDate = new ArrayList<String>();
//        scheduleDate.add(scheduleYear);
//        scheduleDate.add(scheduleMonth);
//        scheduleDate.add(scheduleDay);
//        scheduleDate.add(week);
        intent2.putStringArrayListExtra("scheduleDate", scheduleDate);
		intent2.setClass(ScheduleInfoView.this, ScheduleView.class);
		startActivity(intent2);
		break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	/**
	 * 显示日程所有信息
	 */
	public void handlerInfo(int scheduleID){
		scheduleinfodate =(TextView)findViewById(R.id.scheduleDatetext);
		scheduleinfotime =(TextView)findViewById(R.id.scheduletime);
		scheduleinfotype =(TextView)findViewById(R.id.scheduletype);
		scheduleinfonotes=(TextView)findViewById(R.id.schedulenotes);
		scheduleinfotype.setTag(scheduleID);
		scheduleVO = dao.getScheduleByID(scheduleID);
		scheduleinfodate.setText(scheduleVO.getScheduleDate());
		scheduleinfotype.setText(CalendarConstant.sch_type[scheduleVO.getScheduleTypeID()]);
		scheduleinfotime.setText(scheduleVO.getScheduletime());
		scheduleinfonotes.setText(scheduleVO.getScheduleContent());
		
		
		
		//长时间按住日程类型textview就提示是否删除日程信息
		scheduleinfotype.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {

				final String scheduleID = String.valueOf(v.getTag());
				
				new AlertDialog.Builder(ScheduleInfoView.this).setTitle("删除日程").setMessage("确认删除").setPositiveButton("确认", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dao.delete(Integer.parseInt(scheduleID));
						Intent intent1 = new Intent();
						intent1.setClass(ScheduleInfoView.this, ScheduleAll.class);
						startActivity(intent1);
					}
				}).setNegativeButton("取消", null).show();
				
				return true;
			}
		});
		
		
	}
	public void back(View view)
			
	{
		Intent back = new Intent();
		back.setClass(ScheduleInfoView.this, CalendarActivity.class);
		startActivity(back);
	}
}
