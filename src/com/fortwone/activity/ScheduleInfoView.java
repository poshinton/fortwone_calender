package com.fortwone.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fortwone.borderText.BorderTextView;
import com.fortwone.constant.CalendarConstant;
import com.fortwone.dao.ScheduleDAO;
import com.fortwone.utils.ScheduleAdapter;
import com.fortwone.utils.ScheduleUtils;
import com.fortwone.vo.ScheduleVO;

public class ScheduleInfoView extends Activity {

	private RelativeLayout layout = null;
	private TextView textTop = null;
	//private TextView scheduleinfodate,scheduleinfotime,scheduleinfotype,scheduleinfonotes;
	private TextView scheduleinfodate;
	private BorderTextView info = null;
	private BorderTextView date = null;
	private BorderTextView type = null;
	private EditText editInfo = null;
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO = null;
	private Button  scheduleback;
	private static final int[] ITEM_DRAWABLES = {  R.drawable.composer_music,
		R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.back };

	private String scheduleInfo = "";    //日程信息被修改前的内容
	private String scheduleChangeInfo = "";  //日程信息被修改之后的内容
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	
	ListView lv_schedule;
	ScheduleAdapter sa;
	List<ScheduleVO> list;
	
	int year,month,day;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_info);
		Intent intent = getIntent();
		//一个日期可能对应多个标记日程(scheduleID)
		
		year=intent.getIntExtra("year", 1970);
		month=intent.getIntExtra("month", 1);
		day=intent.getIntExtra("day", 1);
		init();
		
	}
	
	public void init()
	{
		dao = new ScheduleDAO(this);
		layout = (RelativeLayout)findViewById(R.id.scheduleInfolayout);


		Log.i("date : ","year : "+year +" month : "+ month +" day : "+day);
		
		
		scheduleinfodate =(TextView)findViewById(R.id.scheduleDatetext);
		scheduleinfodate.setText(ScheduleUtils.getScheduleDateString(year, month, day));
		
		list=getAllSchedule(year, month, day);
		lv_schedule=(ListView)findViewById(R.id.lv_schedule);
		sa=new ScheduleAdapter(this, list);
		lv_schedule.setAdapter(sa);
		lv_schedule.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent=new Intent(ScheduleInfoView.this,ScheduleView.class);
				intent.putExtra("mode", CalendarConstant.MODE_EDIT_SCHEDULE);
				intent.putExtra("schedule_id", ((ScheduleVO)sa.getItem(position)).getScheduleID());
				startActivityForResult(intent, CalendarConstant.CODE_EDIT_SCHEDULE);
			}
		});
		
		lv_schedule.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				final int i=position;
				new AlertDialog.Builder(ScheduleInfoView.this).setTitle("删除日程").setMessage("确认删除").setPositiveButton("确认", 		new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						delete(i);
						list=getAllSchedule(year, month, day);
						sa.updateSchedule(list);
					}
				}).setNegativeButton("取消", null).show();
				return true;
			}
		});

		
		
		
		RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
        final int itemCount = ITEM_DRAWABLES.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(ITEM_DRAWABLES[i]);
			
			final int position = i;
			rayMenu.addItem(item, new OnClickListener(){
				@Override
				public void onClick(View v) {
					switch(position){
			        case 0:
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
			    		startActivityForResult(intent2, CalendarConstant.CODE_ADD_SCHEDULE);
			    		Toast.makeText(ScheduleInfoView.this, "添加日程", Toast.LENGTH_SHORT).show();
			        	break;
			        case 1:
			        	Intent intent = new Intent();
						intent.setClass(ScheduleInfoView.this, ScheduleAll.class);
						startActivity(intent);
						Toast.makeText(ScheduleInfoView.this, "打开日程列表", Toast.LENGTH_SHORT).show();
						break;
			        case 2:
			        	Toast.makeText(ScheduleInfoView.this, "编辑日程", Toast.LENGTH_SHORT).show();
			        	break;
			        case 3:
//			        	Intent intent21 = getIntent();
//			    		
//			    		//一个日期可能对应多个标记日程(scheduleID)
//			    		final String[] scheduleIDs = intent21.getStringArrayExtra("scheduleID");
//			        	for(int m = 0; m< scheduleIDs.length; m++){
//							handlerInfo(Integer.parseInt(scheduleIDs[m]));
//							int scheduleID=Integer.parseInt(scheduleIDs[m]);
//			        	scheduleinfotype.setTag(scheduleID);
//				    		scheduleVO = dao.getScheduleByID(scheduleID);
//				    		if(scheduleVO.getScheduleTypeID()==3){
//				    			scheduleinfotype.setText(CalendarConstant.sch_type[scheduleVO.getScheduleTypeID()]+"\t"+scheduleVO.getScheduleContent());
//				    		}
//				    		else{
//				    			scheduleinfotype.setText(CalendarConstant.sch_type[scheduleVO.getScheduleTypeID()]+"\t\t\t\t"+scheduleVO.getScheduleContent());
//				    		}
//							dao.delete(scheduleID);
//						}
////			        	finish();
//						Intent intent1 = new Intent();
//						intent1.setClass(ScheduleInfoView.this, ScheduleAll.class);
//						startActivity(intent1);
			        	Toast.makeText(ScheduleInfoView.this, "删除当前日程", Toast.LENGTH_SHORT).show();
						break;
			        case 4:
			        	Intent back = new Intent();
			    		back.setClass(ScheduleInfoView.this, CalendarActivity.class);
			    		startActivity(back);
			    		Toast.makeText(ScheduleInfoView.this, "返回主界面", Toast.LENGTH_SHORT).show();
			    		break;
			        default:
			        	Intent back1 = new Intent();
			    		back1.setClass(ScheduleInfoView.this, CalendarActivity.class);
			    		startActivity(back1);
			    		Toast.makeText(ScheduleInfoView.this, "返回主界面", Toast.LENGTH_SHORT).show();
			    		break;
			        }
				}
			});
		}
	}
	
	public List<ScheduleVO> getAllSchedule(int year,int month,int day){
		final String[] scheduleIDs = dao.getScheduleByTagDate(year, month, day);
		list =	new ArrayList<ScheduleVO>();
		//显示日程详细信息
		for(int i = 0; i< scheduleIDs.length; i++){
			int scheduleID=Integer.parseInt(scheduleIDs[i]);
			scheduleVO = dao.getScheduleByID(scheduleID);
			Log.i("scheduleVO id", scheduleVO.getScheduleID()+"");
			Log.i("scheduleVO type", scheduleVO.getScheduleTypeID()+"");
			Log.i("scheduleVO date", scheduleVO.getScheduleDate());
			Log.i("scheduleVO time", scheduleVO.getScheduleTime());
			Log.i("scheduleVO remind", scheduleVO.getRemindID()+"");
    		list.add(scheduleVO);
		}
		return list;
	}
	
	/**
	 * 显示日程所有信息
	 */
/*	public void handlerInfo(int scheduleID){
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
				
		
//		长时间按住日程类型textview就提示是否删除日程信息
//		scheduleinfotype.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//
//				final String scheduleID = String.valueOf(v.getTag());
//				
//				new AlertDialog.Builder(ScheduleInfoView.this).setTitle("删除日程").setMessage("确认删除").setPositiveButton("确认", new OnClickListener() {
//					
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
//		
		
	}*/
	
	public void delete(int position){
		int id=list.get(position).getScheduleID();
		dao.delete(id);
		Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
		
	}
	public void back(View view)
	{
		finish();
	}
	
//	public void delete(){
//		final String scheduleID = String.valueOf(v.getTag());
//		
//		
//
//				dao.delete(Integer.parseInt(scheduleID));
//				Intent intent1 = new Intent();
//				intent1.setClass(ScheduleInfoView.this, ScheduleAll.class);
//				startActivity(intent1);
//			
//	
//		
//		
//	}
//	protected void dialog() {
//		  AlertDialog.Builder builder = new Builder(ScheduleInfoView.this);
//		  builder.setMessage("确认退出吗？");
//
//		  builder.setTitle("提示");
//
//		  builder.setPositiveButton("确认", new OnClickListener() {
//
//		   @Override
//		   public void onClick(DialogInterface dialog, int which) {
//		    dialog.dismiss();
//
//		    Main.this.finish();
//		   }
//		  });
//
//		  builder.setNegativeButton("取消", new OnClickListener() {
//
//		   @Override
//		   public void onClick(DialogInterface dialog, int which) {
//		    dialog.dismiss();
//		   }
//		  });
//
//		  builder.create().show();
//		 }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			List<ScheduleVO> list=getAllSchedule(year,month,day);
			sa.updateSchedule(list);
		}
		
	}
		 
}
