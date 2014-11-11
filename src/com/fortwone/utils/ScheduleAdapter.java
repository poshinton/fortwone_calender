package com.fortwone.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fortwone.activity.R;
import com.fortwone.constant.CalendarConstant;
import com.fortwone.vo.ScheduleVO;

public class ScheduleAdapter extends BaseAdapter {

	
	Context context;
	LayoutInflater li;
	List<ScheduleVO> list;
	public ScheduleAdapter(Context context,List<ScheduleVO> list)
	{
		this.context=context;
		this.li=LayoutInflater.from(context);
		this.list=list;
	}	
	public ScheduleAdapter()
	{
		this.list=new ArrayList<ScheduleVO>();
	}	
	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int i) {
		
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		
		return i;
	}
	
	public void updateSchedule(List<ScheduleVO> list){
		this.list=list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View view, ViewGroup group) {
		ViewHolder holder=null;
		if(view!=null)
		{
			holder=(ViewHolder) view.getTag();
		}else{
			view=li.inflate(R.layout.list_item_schedule, null);
			holder=new ViewHolder();
			holder.time=(TextView)view.findViewById(R.id.list_item_tv_schedule_time);
			holder.remind=(TextView)view.findViewById(R.id.list_item_tv_schedule_remind);
			holder.type=(TextView)view.findViewById(R.id.list_item_tv_schedule_type);
			holder.note=(TextView)view.findViewById(R.id.list_item_tv_schedule_note);
			view.setTag(holder);
		}
		
		ScheduleVO schedule=list.get(position);
		holder.time.setText(schedule.getScheduleTime());
		
		int remind=schedule.getRemindID();
		holder.remind.setText(CalendarConstant.remind_type[remind]);
		
		int type=schedule.getScheduleTypeID();
		holder.type.setText(CalendarConstant.schedule_type[type]);
		
		holder.note.setText(schedule.getScheduleContent());
		
		return view;
	}
	
	static class ViewHolder{
		TextView time;
		TextView remind;
		TextView type;
		TextView note;
	}

}
