package com.bohua.multgas;

import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Debug;
import com.bohua.base.EnumDataSaveFreq;
import com.bohua.base.GlobalData;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SetDataSaveFreq extends Activity {

	RadioGroup selectRadioGroup;
	RadioButton SetDataSaveFreq_Radio_5s;
	RadioButton SetDataSaveFreq_Radio_10s;
	RadioButton SetDataSaveFreq_Radio_20s;
	RadioButton SetDataSaveFreq_Radio_1m;
	RadioButton SetDataSaveFreq_Radio_3m;
	RadioButton SetDataSaveFreq_Radio_5m;
	
	EnumDataSaveFreq selectFreq;

	Battery battery = new Battery();
	Context appContext;
	
	TextView SystemTime;
	Date date = new Date();
	Time time = new Time();
	
	static String tag = "SetDataSaveFreq";
	private static void debug(String string){if(Debug.SetDataSaveFreqDebug() == 1)Debug.debugi(tag,string);}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏显示
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.set_data_save_freq);
		appContext = getApplicationContext();
		
		handleButtons();
		setAllViews();
		getDataSvaeFreq();
	}
	private void setAllViews()
	{
		selectRadioGroup = (RadioGroup)findViewById(R.id.SetDataSaveFreq_RadioGroup);
		
		SetDataSaveFreq_Radio_5s 	= (RadioButton)findViewById(R.id.SetDataSaveFreq_Radio_5s);
		SetDataSaveFreq_Radio_10s 	= (RadioButton)findViewById(R.id.SetDataSaveFreq_Radio_10s);
		SetDataSaveFreq_Radio_20s 	= (RadioButton)findViewById(R.id.SetDataSaveFreq_Radio_20s);
		SetDataSaveFreq_Radio_1m 	= (RadioButton)findViewById(R.id.SetDataSaveFreq_Radio_1m);
		SetDataSaveFreq_Radio_3m 	= (RadioButton)findViewById(R.id.SetDataSaveFreq_Radio_3m);
		SetDataSaveFreq_Radio_5m 	= (RadioButton)findViewById(R.id.SetDataSaveFreq_Radio_5m);
		
		String array[] = this.getResources().getStringArray(R.array.SetDataSaveFreq);
		SetDataSaveFreq_Radio_5s.setText(array[0]);
		SetDataSaveFreq_Radio_10s.setText(array[1]);
		SetDataSaveFreq_Radio_20s.setText(array[2]);
		SetDataSaveFreq_Radio_1m.setText(array[3]);
		SetDataSaveFreq_Radio_3m.setText(array[4]);
		SetDataSaveFreq_Radio_5m.setText(array[5]);
		
		switch(GlobalData.getDataSaveFreqence())
		{
			case DATA_SAVE_FREQ_5S:
				debug("设置数据保存间隔为5S");
				SetDataSaveFreq_Radio_5s.setChecked(true);
				break;
			case DATA_SAVE_FREQ_10S:
				debug("设置数据保存间隔为10S");
				SetDataSaveFreq_Radio_10s.setChecked(true);
				break;
			case DATA_SAVE_FREQ_20S:
				SetDataSaveFreq_Radio_20s.setChecked(true);
				debug("设置数据保存间隔为20S");
				break;
			case DATA_SAVE_FREQ_1M:
				SetDataSaveFreq_Radio_1m.setChecked(true);
				debug("设置数据保存间隔为1m");
				break;
			case DATA_SAVE_FREQ_3M:
				SetDataSaveFreq_Radio_3m.setChecked(true);
				debug("设置数据保存间隔为3m");
				break;
			case DATA_SAVE_FREQ_5M:
				SetDataSaveFreq_Radio_5m.setChecked(true);
				debug("设置数据保存间隔为5m");
				break;
		}
		
		TextView SystemBattery= (TextView)findViewById(R.id.SetDataSaveFreq_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.SetDataSaveFreq_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}
	private void getDataSvaeFreq()
	{
		selectRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				debug("设置数据保存间隔的id为"+checkedId);
				switch(checkedId)
				{
					case R.id.SetDataSaveFreq_Radio_5s:
							selectFreq = EnumDataSaveFreq.DATA_SAVE_FREQ_5S;
						break;
					case R.id.SetDataSaveFreq_Radio_10s:
							selectFreq = EnumDataSaveFreq.DATA_SAVE_FREQ_10S;
						break;
					case R.id.SetDataSaveFreq_Radio_20s:
							selectFreq = EnumDataSaveFreq.DATA_SAVE_FREQ_20S;
						break;
					case R.id.SetDataSaveFreq_Radio_1m:
							selectFreq = EnumDataSaveFreq.DATA_SAVE_FREQ_1M;
						break;
					case R.id.SetDataSaveFreq_Radio_3m:
							selectFreq = EnumDataSaveFreq.DATA_SAVE_FREQ_3M;
						break;
					case R.id.SetDataSaveFreq_Radio_5m:
							selectFreq = EnumDataSaveFreq.DATA_SAVE_FREQ_5M;
						break;
				}
				
				
				
			}
		});
	}
	
	private void handleButtons()
	{
		handleReturnButton();
		handleCompleteButton();
	}
	private void handleReturnButton()
	{
		final Button button_return = (Button)findViewById(R.id.setdatasavefreq_button_return);
		button_return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(SetDataSaveFreq.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button_return);
	}
	private void handleCompleteButton()
	{
		Button button = (Button)findViewById(R.id.setdatasavefreq_button_ok);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("完成设置 - 当前设置值为"+selectFreq);
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				GlobalData.setDataSaveFreqence(selectFreq);
				Intent intent = new Intent(SetDataSaveFreq.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceCopmleteButtonImageOnTouch(button);
	}
	
	
	
	
}
