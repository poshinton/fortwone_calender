package com.fortwone.activity;

import com.fortwone.calendar.LunarCalendar;
import com.fortwone.activity.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * 日期转换
 * @author jack_peng
 *
 */
public class CalendarConvert extends Activity {

	private LunarCalendar lc = null;
	private TextView lunarDate = null;
	private Button check;
	private Button yun_date;
	
	
	
	private int year_c;
	private int month_c;
	private int day_c;

	
	public CalendarConvert(){
		lc = new LunarCalendar();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convert);

		lunarDate = (TextView) findViewById(R.id.convertResult);
		check = (Button)findViewById(R.id.check);
		yun_date = (Button)findViewById(R.id.yun_Date);
		
		Intent intent = getIntent();
		int[] date = intent.getIntArrayExtra("date");
		year_c = date[0];
		month_c = date[1];
		day_c = date[2];
		
		yun_date.setText(year_c+"年"+month_c+"月"+day_c);
		
		yun_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new DatePickerDialog(CalendarConvert.this, new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {

						if(year < 1901 || year > 2049){
							//不在查询范围内
							new AlertDialog.Builder(CalendarConvert.this).setTitle("错误日期").setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
						}else{
							year_c = year;
							month_c = monthOfYear+1;
							day_c = dayOfMonth;
							yun_date.setText(year_c+"年"+month_c+"月"+day_c);
						}
					}
				}, year_c, month_c-1, day_c).show();
			}
		});
		
		
		check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String lunarDay = getLunarDay(year_c,month_c,day_c);
				String lunarYear = String.valueOf(lc.getYear());
				String lunarMonth = lc.getLunarMonth();
				
				lunarDate.setText(year_c+"年"+month_c+"月"+day_c+"日\n"+
				"-----> \n"+lunarYear+"年"+lunarMonth+lunarDay);

			}

		});
		

	}
	
	/**
	 * 根据日期的年月日返回阴历日期
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
}
