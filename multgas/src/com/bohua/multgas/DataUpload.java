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

public class DataUpload extends Activity {

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
		setContentView(R.layout.data_upload);
		
		appContext = getApplicationContext();
		setAllViews();
		handleButtons();
	}
	
	private void handleButtons()
	{
		handleReturnButton();
		handleOkButton();
	}
	public void setAllViews()
	{
		TextView SystemBattery= (TextView)findViewById(R.id.dataupload_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.dataupload_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
		
	}
	
	
	private void handleReturnButton()
	{
		final Button button_return = (Button)findViewById(R.id.dataupload_button_return);
		button_return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(DataUpload.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button_return);
	}
	private void handleOkButton()
	{
		Button button = (Button)findViewById(R.id.dataupload_button_ok);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(DataUpload.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceCopmleteButtonImageOnTouch(button);
	}
	
	
}
