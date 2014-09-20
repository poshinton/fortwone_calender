package com.fortwone.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
		R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with };

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

		layout = (RelativeLayout)findViewById(R.id.scheduleInfolayout);

		textTop.setTextColor(Color.WHITE); 
		textTop.setBackgroundColor(0xff009944);
		textTop.setText("日程详情");
		textTop.setHeight(57);

		
		Intent intent = getIntent();
		
		//一个日期可能对应多个标记日程(scheduleID)
		String[] scheduleIDs = intent.getStringArrayExtra("scheduleID");
		//显示日程详细信息
		for(int i = 0; i< scheduleIDs.length; i++){
			handlerInfo(Integer.parseInt(scheduleIDs[i]));
		}
		RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
        final int itemCount = ITEM_DRAWABLES.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(ITEM_DRAWABLES[i]);

			final int position = i;
			rayMenu.addItem(item, new OnClickListener(){
				@Override
				public void onClick(View v) {
					Toast.makeText(ScheduleInfoView.this, "position:" + position, Toast.LENGTH_SHORT).show();
				}
			});
//			rayMenu.addItem(item, new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
//				}
//			});// Add a menu item
		}
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
		intent2.putStringArrayListExtra("scheduleDate", scheduleDate);
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
		System.out.println("hell+hello"+scheduleVO.getScheduleTypeID());
		if(scheduleVO.getScheduleTypeID()==3){
			scheduleinfotype.setText(CalendarConstant.sch_type[scheduleVO.getScheduleTypeID()]+"\t"+scheduleVO.getScheduleContent());
		}
		else{
			scheduleinfotype.setText(CalendarConstant.sch_type[scheduleVO.getScheduleTypeID()]+"\t\t\t\t"+scheduleVO.getScheduleContent());
		}
		
		scheduleinfotime.setText(scheduleVO.getScheduletime());
		scheduleinfonotes.setText(scheduleVO.getScheduleContent());
		scheduleinfonotes.setVisibility(8);
				
		
		//长时间按住日程类型textview就提示是否删除日程信息
//		scheduleinfotype.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//
//				final String scheduleID = String.valueOf(v.getTag());
//				
//				new AlertDialog.Builder(ScheduleInfoView.this).setTitle("删除日程").setMessage("确认删除").setPositiveButton("确认", new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//
//						dao.delete(Integer.parseInt(scheduleID));
//						Intent intent1 = new Intent();
//						intent1.setClass(ScheduleInfoView.this, ScheduleAll.class);
//						startActivity(intent1);
//					}
//				}).setNegativeButton("取消", null).show();
//				
//				return true;
//			}
//		});
		
		
	}
	public void back(View view)
			
	{
		Intent back = new Intent();
		back.setClass(ScheduleInfoView.this, CalendarActivity.class);
		startActivity(back);
	}
}
