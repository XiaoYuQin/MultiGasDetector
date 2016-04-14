package com.bohua.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

public class Time {

	
	static String tag = "Time";
	private static void debug(String string){if(Debug.TimeDebug() == 1)Debug.debugi(tag,string);}
	
	
	TextView text_View;
	
	/*false : 表示没有注册过
	  true  : 表示注册过*/
	boolean register_state = false;
	
	BroadcastReceiver timeTickReceive = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if(Intent.ACTION_TIME_TICK.equals(action))
			{
				Date data = new Date();
				text_View.setText(data.getSystemDateToStringWithoutS());
				debug(data.getSystemDateToStringWithoutS());
			}
		}
	};
	/**
	 * 注册电池电量变化广播接收
	 * 并且将一个TextView绑定在上面,当电池电量发送变化的时候，TextView会自动刷型
	 * **/
	public void reciveTimeTick(Context context , TextView textView) 
	{
		debug("registerReceiver ACTION_TIME_TICK");
		context.registerReceiver(timeTickReceive,new IntentFilter(Intent.ACTION_TIME_TICK));
		text_View = textView;
		register_state = true;
	}
	public void unregisterTimeTick(Context context)
	{
		debug("unregisterBatteryReciver ACTION_BATTERY_CHANGED");
		if(register_state == true)
		{
			debug("注销时间变化广播接收");
			context.unregisterReceiver(timeTickReceive);
		}
		else 
		{
			debug("注销时间变化广播接收失败，没有注册过该广播");
		}
		
	}
	
}
