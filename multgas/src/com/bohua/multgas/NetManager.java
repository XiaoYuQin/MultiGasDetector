package com.bohua.multgas;

import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Time;
import com.bohua.graphics.ButtonImageReplace;
import com.bohua.graphics.ScreenHandler;
import com.qinxiaoyu.multgas.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NetManager extends Activity {

	Battery battery = new Battery();
	Context appContext;
	
	TextView SystemTime;
	Date date = new Date();
	Time time = new Time();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//»´∆¡œ‘ æ
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.net_manager);
		appContext = getApplicationContext();
		
		setAllViews();
		handleButton();
	}
	private void setAllViews()
	{
		TextView SystemBattery= (TextView)findViewById(R.id.netmanager_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.netmanager_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}
	private void handleButton()
	{
		handleReturnButton();
		handleWifiSettingButton();
	}
	
	private void handleReturnButton()
	{
		Button button = (Button)findViewById(R.id.netmanager_button_return);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(NetManager.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button);
	}

	private void handleWifiSettingButton()
	{
		Button button = (Button)findViewById(R.id.generalsetting_button_wifi_setting);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(NetManager.this,SetWifi.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	
}
