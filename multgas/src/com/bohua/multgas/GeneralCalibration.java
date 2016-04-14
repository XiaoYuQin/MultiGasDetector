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
import com.bohua.base.Time;
import com.bohua.graphics.*;
import com.qinxiaoyu.multgas.R;
public class GeneralCalibration extends Activity {

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
		setContentView(R.layout.general_calibration);
		appContext = getApplicationContext();
		
		setAllViews();
		handleCalibrationCOButton();
		handleCalibrationCH4Button();
		handleCalibrationNO2Button();
		handleCalibrationO2Button();
		handleReturnButton();
	}
	private void setAllViews()
	{
		TextView SystemBattery= (TextView)findViewById(R.id.generalcalibration_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.generalcalibration_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}
	
	private void handleCalibrationCOButton()
	{
		final Button button = (Button)findViewById(R.id.generalcalibration_button_set_co);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Bundle gasType = new Bundle();
				gasType.putSerializable("GAS_TYPE", "CO");	
				Intent intent = new Intent(GeneralCalibration.this,CalibrationChoose.class);
				intent.putExtras(gasType);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleCalibrationCH4Button()
	{
		final Button button = (Button)findViewById(R.id.generalcalibration_button_set_ch4);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Bundle gasType = new Bundle();
				gasType.putSerializable("GAS_TYPE", "CH4");	
				Intent intent = new Intent(GeneralCalibration.this,CalibrationChoose.class);
				intent.putExtras(gasType);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleCalibrationNO2Button()
	{
		final Button button = (Button)findViewById(R.id.generalcalibration_button_set_no2);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Bundle gasType = new Bundle();
				gasType.putSerializable("GAS_TYPE", "NO2");		
				Intent intent = new Intent(GeneralCalibration.this,CalibrationChoose.class);
				intent.putExtras(gasType);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleCalibrationO2Button()
	{
		final Button button = (Button)findViewById(R.id.generalcalibration_button_set_o2);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Bundle gasType = new Bundle();
				gasType.putSerializable("GAS_TYPE", "O2");		
				Intent intent = new Intent(GeneralCalibration.this,CalibrationChoose.class);
				intent.putExtras(gasType);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleReturnButton()
	{
		final Button button = (Button)findViewById(R.id.generalcalibration_button_return);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralCalibration.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button);
	}
	
	
	
	
	
	
}
