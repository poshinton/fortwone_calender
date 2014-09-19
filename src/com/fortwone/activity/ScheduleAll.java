package com.fortwone.activity;

import java.util.ArrayList;

import com.fortwone.constant.CalendarConstant;
import com.fortwone.dao.ScheduleDAO;
import com.fortwone.borderText.BorderTextView;
import com.fortwone.vo.ScheduleVO;
import com.fortwone.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

/**
 * 显示/修改所有日程的activity
 * @author jack_peng
 *
 */
public class ScheduleAll extends Activity {

	private ScrollView sv = null;
	private LinearLayout layout = null;
	private BorderTextView textTop;
	private BorderTextView info;
	private Button add;
	private Button back;
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO = null;
	private ArrayList<ScheduleVO> schList = new ArrayList<ScheduleVO>();
	private String scheduleInfo = "";
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	
	private int scheduleID = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dao = new ScheduleDAO(this);
		sv = new ScrollView(this);
		setContentView(R.layout.scheduleall);
		textTop=(BorderTextView)findViewById(R.id.toptext1);
		params.setMargins(0, 5, 0, 0);
		textTop.setTextColor(Color.BLACK); 
		textTop.setText("所有日程");
		textTop.setHeight(57);
		textTop.setGravity(Gravity.CENTER);
		getScheduleAll();
		back=(Button)findViewById(R.id.back);
		add=(Button)findViewById(R.id.add);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent();
				intent1.setClass(ScheduleAll.this, CalendarActivity.class);
				startActivity(intent1);
			}
		});
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent add_intent = new Intent();
				add_intent.setClass(ScheduleAll.this, ScheduleView.class);
				startActivity(add_intent);
			}
		});
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
				scheduleInfo = CalendarConstant.sch_type[vo.getScheduleTypeID()]+"\n"+vo.getScheduleDate()+"\n"+content;
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
		info=(BorderTextView)findViewById(R.id.info);
		info.setText(scheduleInfo);
		info.setTag(scheduleID);
		
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
			intent1.setClass(ScheduleAll.this, ScheduleView.class);
			startActivity(intent1);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
