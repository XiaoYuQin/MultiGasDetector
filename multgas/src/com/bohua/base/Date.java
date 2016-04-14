package com.bohua.base;

import java.text.SimpleDateFormat;

public class Date {

	private static String tag = "Date";
	private static void debug(String string)	{if(Debug.DateDebug() == 1)Debug.debugi(tag,string);}
	
	public String getSystemDateToString()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = simpleDateFormat.format(new java.util.Date());
		debug("getSystemForString = "+date);
		return date;
	}
	/**
	 * 返回不包含时分秒的系统时间
	 * */
	public String getSystemDateToStringWithoutHMS()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new java.util.Date());
		debug("getSystemForString = "+date);
		return date;
	}
	public String getSystemDateToStringWithoutS()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String date = simpleDateFormat.format(new java.util.Date());
		debug("getSystemForString = "+date);
		return date;
	}
	
}
