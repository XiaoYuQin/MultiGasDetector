package com.bohua.multgas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Debug;
import com.bohua.base.Time;
import com.bohua.graphics.ButtonImageReplace;
import com.bohua.graphics.ScreenHandler;
import com.qinxiaoyu.multgas.R;
public class GeneralAlarmPoint extends Activity {

	static String tag = "GeneralAlarmPoint";
	@SuppressWarnings("unused")
	private static void debug(String string)	{if(Debug.CalibrationSetZeroDebug() == 1)Debug.debugi(tag,string);}
	
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
		setContentView(R.layout.general_alarmpoint);
		
		appContext = getApplicationContext();
		setAllViews();
		handleButtons();
		
	}
	private void setAllViews()
	{
		TextView SystemBattery= (TextView)findViewById(R.id.generalalarmpoint_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.generalalarmpoint_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}
	private void handleButtons()
	{
		handleReturnButton();
		handleSetCOAlarmPointButton();
		handleSetCh4AlarmPointButton();
		handleSetNO2AlarmPointButton();
		handleSetO2AlarmPointButton();
	}
	
	
	private void handleReturnButton()
	{
		Button button = (Button)findViewById(R.id.generalalarmpoint_button_return);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralAlarmPoint.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button);
	}
	private void handleSetCOAlarmPointButton()
	{
		Button button = (Button)findViewById(R.id.generalalarmpoint_button_setalarmC0);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Bundle gasType = new Bundle();
				gasType.putSerializable("GAS_TYPE", "CO");
				Intent intent = new Intent(GeneralAlarmPoint.this,SetAlarmPoint.class);
				intent.putExtras(gasType);
				startActivity(intent);
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleSetCh4AlarmPointButton()
	{
		Button button = (Button)findViewById(R.id.generalalarmpoint_button_setalarmCH4);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Bundle gasType = new Bundle();
				gasType.putSerializable("GAS_TYPE", "CH4");
				Intent intent = new Intent(GeneralAlarmPoint.this,SetAlarmPoint.class);
				intent.putExtras(gasType);
				startActivity(intent);
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleSetNO2AlarmPointButton()
	{
		Button button = (Button)findViewById(R.id.generalalarmpoint_button_setalarmNO2);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Bundle gasType = new Bundle();
				gasType.putSerializable("GAS_TYPE", "NO2");
				Intent intent = new Intent(GeneralAlarmPoint.this,SetAlarmPoint.class);
				intent.putExtras(gasType);
				startActivity(intent);
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleSetO2AlarmPointButton()
	{
		Button button = (Button)findViewById(R.id.generalalarmpoint_button_setalarmO2);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Bundle gasType = new Bundle();
				gasType.putSerializable("GAS_TYPE", "O2");
				Intent intent = new Intent(GeneralAlarmPoint.this,SetAlarmPoint.class);
				intent.putExtras(gasType);
				startActivity(intent);
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
}
