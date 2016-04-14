package com.bohua.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;


public class Battery {

	
	static String tag = "Battery";
	private static void debug(String string){if(Debug.BatteryDebug() == 1)Debug.debugi(tag,string);}
	
	int intLevel;
	int intScale;
	TextView text_View;
	String battery_level = null;
	/*false : 表示没有注册过
	  true  : 表示注册过*/
	boolean register_state = false;
	BroadcastReceiver mBatInfoReceive = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if(Intent.ACTION_BATTERY_CHANGED.equals(action))
			{
				intLevel = intent.getIntExtra("level", 0);
				intScale = intent.getIntExtra("scale", 10);
				text_View.setText("电量"+String.valueOf(intLevel*100/intScale)+"%");
				battery_level = String.valueOf(intLevel*100/intScale);
			}
		}
	};
	
	/**
	 * 注册电池电量变化广播接收
	 * 并且将一个TextView绑定在上面,当电池电量发送变化的时候，TextView会自动刷型
	 * **/
	public void reciveBatteryLevel(Context context , TextView textView) 
	{
		debug("registerReceiver ACTION_BATTERY_CHANGED");
		context.registerReceiver(mBatInfoReceive,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		text_View = textView;
		register_state = true;
	}
	/**
	 * 注销电池电量变化广播接收
	 * ：在注册了电池电量变化后必须注销
	 * **/
	public void unregisterBatteryReciver(Context context)
	{
		debug("unregisterBatteryReciver ACTION_BATTERY_CHANGED");
		if(register_state == true)
		{
			debug("注销电池电量变化广播接收");
			context.unregisterReceiver(mBatInfoReceive);
		}
		else
		{
			debug("注销电池电量变化广播接收失败，没有注册过该广播");
		}
		
	}
	public String getBatteryLevel()
	{
		return battery_level;
	}
	
	
}
