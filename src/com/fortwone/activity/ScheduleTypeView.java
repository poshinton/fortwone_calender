package com.fortwone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fortwone.borderText.BorderTextView;
import com.fortwone.constant.CalendarConstant;

/**
 * 日程类型选择
 * @author jack_peng
 *
 */
public class ScheduleTypeView extends Activity {

	private CalendarConstant cc = null;
	private int sch_typeID = 0;
	private int remindID = 0;
	private RelativeLayout layout; // 布局 ， 可以在xml布局中获得
	private LinearLayout layButton;
	private RadioGroup group; // 点选按钮组
	private TextView textTop = null;
	private RadioButton radio = null;
	private Button btCancel,btSave ;
	private int schType_temp = 0;
	private int remind_temp = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picktype);
		cc = new CalendarConstant();
		layout =(RelativeLayout) findViewById(R.id.pickuplayout); // 实例化布局对象
		group = (RadioGroup)findViewById(R.id.pickupRadio);
		btSave = (Button)findViewById(R.id.typeok);
		btCancel = (Button)findViewById(R.id.typecancle);
		
		Intent intent = getIntent();
		int sch_remind[] = intent.getIntArrayExtra("sch_remind");  //从ScheduleView传来的值
		if(sch_remind != null){
			sch_typeID = sch_remind[0];
			remindID = sch_remind[1];
		}
		for(int i = 0 ; i < cc.schedule_type.length ; i++){
			radio = new RadioButton(this);
			if(i == sch_typeID){
				radio.setChecked(true);
			}
	        radio.setText(cc.schedule_type[i]);
	        radio.setId(i);
	        group.addView(radio);
	        }
		layButton = new LinearLayout(this);
		layButton.setOrientation(LinearLayout.HORIZONTAL);
		btSave.setClickable(true);
		btCancel.setClickable(true);
		layout.addView(layButton);
		this.setContentView(layout);

		//触发radioButton
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				schType_temp = checkedId;
				
			}
		});
		

		//触发确定按钮
		btSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				sch_typeID = schType_temp;
				remindID = remind_temp;
				Intent intent = new Intent();
				intent.setClass(ScheduleTypeView.this, ScheduleView.class);
				intent.putExtra("schType_remind", new int[]{sch_typeID,remindID});
				startActivity(intent);
			}
		});
		
		//触发取消按钮
		btCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(ScheduleTypeView.this, ScheduleView.class);
				intent.putExtra("schType_remind", new int[]{sch_typeID,remindID});
				startActivity(intent);
			}
		});
	}
	public void picktype(View view){
		new AlertDialog.Builder(ScheduleTypeView.this).setTitle("日程类型")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setSingleChoiceItems(
				new String[] { cc.remind_type[0], cc.remind_type[1], cc.remind_type[2], cc.remind_type[3], cc.remind_type[4], cc.remind_type[5], cc.remind_type[6], cc.remind_type[7] }, remindID,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						remind_temp = which;
					}
				}).setPositiveButton("确认", null).setNegativeButton("取消", null).show();
	}

}
