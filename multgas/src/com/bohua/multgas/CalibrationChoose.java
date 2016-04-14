package com.bohua.multgas;

import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Debug;
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

public class CalibrationChoose extends Activity {

	
	static String tag = "CalibrationChoose";
	private static void debug(String string){if(Debug.CalibrationChooseDebug() == 1) Debug.debugi(tag,string);}
	
	String gasType;
	Battery battery = new Battery();

	Context appContext;

	TextView SystemTime;
	Date date = new Date();
	Time time = new Time();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** »´∆¡œ‘ æ**/
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.calibration_choose);
		
		appContext = getApplicationContext();
		getBundleData();
		setAllViews();
		handleButton();
	}
	private void setAllViews()
	{
		TextView SystemBattery= (TextView)findViewById(R.id.CalibrationChoose_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.CalibrationChoose_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}
	private void handleButton()
	{
		handleReturnButton();
		handleSetZeroPointButton();
		handleSetSensitivityButton();
	}
	private void getBundleData()
	{
		Intent intent = getIntent();
		gasType = (String)intent.getSerializableExtra("GAS_TYPE");
		debug("bundle data = "+gasType);
	}
	private void handleReturnButton()
	{
		debug("handleReturnButton");
		final Button button_return = (Button)findViewById(R.id.CalibrationChoose_Button_Return);
		button_return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("handleReturnButton");
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(CalibrationChoose.this,GeneralCalibration.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button_return);
	}
	
	private void handleSetZeroPointButton()
	{
		debug("handleSetZeroPointButton");
		final Button button = (Button)findViewById(R.id.CalibrationChoose_Button_SetZeroPoint);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("handleCalibrationButton");
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(CalibrationChoose.this,CalibrationChooseSetZP.class);
				
				Bundle bundleData = new Bundle();
				bundleData.putSerializable("GAS_TYPE", gasType);
				intent.putExtras(bundleData);
				
				startActivity(intent);
				finish(); 
			} 
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
		
	}

	private void handleSetSensitivityButton()
	{
		debug("handleSetZeroPointButton");
		final Button button = (Button)findViewById(R.id.CalibrationChoose_Button_SetSensitivity);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("handleCalibrationButton");
				battery.unregisterBatteryReciver(appContext);
				Intent intent = new Intent(CalibrationChoose.this,CalibrationChooseSetSen.class);
				
				Bundle bundleData = new Bundle();
				bundleData.putSerializable("GAS_TYPE", gasType);
				intent.putExtras(bundleData);
				
				startActivity(intent);
				finish(); 
			} 
		});
		ButtonImageReplace.replaceNormalButtonImageOnTouch(button);
		
	}
	
	
 

}
