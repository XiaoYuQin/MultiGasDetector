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
public class GeneralSetting extends Activity {

	protected static String tag = "GeneralSetting";
	private static void debug(String string){if(Debug.GeneralSettingDebug() == 1)Debug.debugi(tag,string);}
	
	Context appContext;
	Battery battery = new Battery();
	
	TextView SystemTime;
	Date date = new Date();
	Time time = new Time();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenHandler.setFullScreenAndPortrait(this);
		debug("----onCreate----");
		setContentView(R.layout.general_setting);
		
		
		appContext = getApplicationContext();
		setAllViews();
		handleButton();
		
	}

	private void setAllViews()
	{
		TextView GenSatuseekText_SystemBattery= (TextView)findViewById(R.id.generalsetting_testview_battery);
		battery.reciveBatteryLevel(appContext, GenSatuseekText_SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.generalsetting_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}

	
	private void handleButton()
	{
		handleReturnButton();
		handleCalibrationButton();
		handleAlarmPointSettingButton(); 
		handleDataSaveFrequenceSettingButton();
		handleDataUploadSettingButton();
		handleNetManageButton();
		handleDeviceLogButton();
		handleDeviceImformationButton();
		
	}
	
	private void handleReturnButton()
	{
		debug("handleReturnButton");
		final Button button_return = (Button)findViewById(R.id.generalsetting_button_reutrn);
		button_return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("handleReturnButton");
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSetting.this,GeneralSatuation.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button_return);
	}
	private void handleCalibrationButton()
	{
		debug("handleCalibrationButton");
		final Button button_calibration = (Button)findViewById(R.id.generalsetting_button_calibration);
		button_calibration.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("handleCalibrationButton");
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSetting.this,GeneralCalibration.class);
				startActivity(intent);
				finish(); 
			} 
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button_calibration);
	}
	private void handleAlarmPointSettingButton()
	{
		debug("handleAlarmPointSettingButton");
		final Button button_alarmpoint = (Button)findViewById(R.id.generalsetting_button_alarmpoint_setting);
		button_alarmpoint.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSetting.this,GeneralAlarmPoint.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button_alarmpoint);
	}
	private void handleDataSaveFrequenceSettingButton()
	{
		debug("handleDataSaveFrequenceSettingButton");
		Button button = (Button)findViewById(R.id.generalsetting_button_datasava_frequence);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSetting.this,SetDataSaveFreq.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleDataUploadSettingButton()
	{
		debug("handleDataUploadSettingButton");
		Button button = (Button)findViewById(R.id.generalsetting_button_data_upload);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSetting.this,DataUpload.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	private void handleNetManageButton()
	{
		debug("handleNetManageButton");
		Button button = (Button)findViewById(R.id.generalsetting_button_net_manage);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSetting.this,NetManager.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	public void handleDeviceLogButton()
	{
		debug("handleDeviceLogButton");
		Button button = (Button)findViewById(R.id.generalsetting_button_device_log);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSetting.this,DeviceLog.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
	public void handleDeviceImformationButton()
	{
		debug("handleDeviceImformationButton");
		Button button = (Button)findViewById(R.id.generalsetting_button_device_information);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSetting.this,DeviceInformation.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
	}
		
}
