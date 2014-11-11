package com.fortwone.constant;

public class CalendarConstant {

	public final static String[] schedule_type = { "会议", "约会", "电话", "纪念日", "生日", "课程", "其他" }; // 日程类型
	public final static String[] remind_type = {"提醒一次","隔10分钟","隔30分钟","隔一小时","每天重复","每周重复","每月重复","每年重复"};
	public final static int  CODE_ADD_SCHEDULE=101;
	public final static int  CODE_DELETE_SCHEDULE=102;
	public final static int  CODE_SEARCH_SCHEDULE=103;
	public final static int  CODE_EDIT_SCHEDULE=104;
	
	public final static int  MODE_ADD_SCHEDULE=0;
	public final static int  MODE_EDIT_SCHEDULE=1;
	
	public final static int REQUEST_CODE_ADD_SCHEDULE=301;
}
