package com.bohua.multgas;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bohua.algorithm.DatabaseAlgorithm;
import com.bohua.algorithm.GasAlgorithm;
import com.bohua.base.AD7790;
import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Debug;
import com.bohua.base.GlobalData;
import com.bohua.base.LogFile;
import com.bohua.base.Time;
import com.bohua.graphics.ButtonImageReplace;
import com.bohua.graphics.GenerSatuTextView;
import com.bohua.graphics.ScreenHandler;
import com.qinxiaoyu.multgas.R;

public class GeneralSatuation extends Activity {

	ButtonImageReplace rep = new ButtonImageReplace();
	static DatabaseAlgorithm database = new DatabaseAlgorithm();
	protected static int TIME = 1000;
	static TextView textViewCoValue;
	static TextView textViewCh4Value;
	static TextView textViewNo2Value;
	static TextView textViewO2Value;
	static TextView textViewTempValue;
	
	static TextView textViewCoUnit; 
	static TextView textViewCh4Unit;
	static TextView textViewNo2Unit;
	static TextView textViewO2Unit;
	
	static TextView textViewO2Alarmpoint;
	static TextView textViewCH4Alarmpoint;
	static TextView textViewNO2Alarmpoint;
	static TextView textViewCOAlarmpoint;
	
	static TextView test1;
	static TextView test2;
	
	private static TextView SystemBattery;
	Battery battery = new Battery();
	
	
	private static TextView SystemTime;
	Date date = new Date();
	Time time = new Time();

	static Context appContext;
	private static Timer mTimer = null;
	private static TimerTask mTimerTask = null;
	
	
	static int dataSaveTime = 0;

	static String tag = "GeneralSatuation";
	private static void debug(String string){if(Debug.GeneralSatuationDebug() == 1)Debug.debugi(tag,string);}
	


	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		debug("----onCreate----");
		super.onCreate(savedInstanceState);
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.general_satuation);

		appContext = getApplicationContext();
		intAllViews();
		handleSettingButton();
		startTimer();
		


	}
	
	private void intAllViews() { 
		textViewCoValue 	= (TextView) findViewById(R.id.generalsatuation_textview_co_value);
		textViewCh4Value 	= (TextView) findViewById(R.id.generalsatuation_textview_ch4_value);
		textViewNo2Value 	= (TextView) findViewById(R.id.generalsatuation_textview_no2_value);
		textViewO2Value 	= (TextView) findViewById(R.id.generalsatuation_textview_o2_value);
		textViewTempValue 	= (TextView) findViewById(R.id.generalsatuation_textview_Temperature);
		
		textViewCoUnit 		= (TextView) findViewById(R.id.generalsatuation_textview_co_unit);
		textViewCh4Unit 	= (TextView) findViewById(R.id.generalsatuation_textview_ch4_unit);
		textViewNo2Unit 	= (TextView) findViewById(R.id.generalsatuation_textview_no2_unit);
		textViewO2Unit 		= (TextView) findViewById(R.id.generalsatuation_textview_o2_unit);		
		
		textViewO2Alarmpoint	= (TextView) findViewById(R.id.generalsatuation_textview_o2_alarmpoint);
		textViewCH4Alarmpoint	= (TextView) findViewById(R.id.generalsatuation_textview_ch4_alarmpoint);
		textViewNO2Alarmpoint	= (TextView) findViewById(R.id.generalsatuation_textview_no2_alarmpoint);
		textViewCOAlarmpoint	= (TextView) findViewById(R.id.generalsatuation_textview_co_alarmpoint);
		
		textViewO2Alarmpoint.setText("报警：<= "+GlobalData.getO2AlarmPoint()+"%");
		textViewCH4Alarmpoint.setText("报警：>= "+GlobalData.getCH4AlarmPoint()+"%");
		textViewNO2Alarmpoint.setText("报警：>= "+GlobalData.getNO2AlarmPoint()+"PPM");
		textViewCOAlarmpoint.setText("报警：>= "+GlobalData.getCOAlarmPoint()+"PPM");
		
		test1 = (TextView) findViewById(R.id.generalsatuation_testview_test1);
		test2 = (TextView) findViewById(R.id.generalsatuation_testview_test2);
		
		SystemBattery= (TextView)findViewById(R.id.generalsatuation_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.generalsatuation_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
		
	}

	private static void handleAllViews() 
	{
		GasAlgorithm txxx = new GasAlgorithm();
		AD7790 ad = new AD7790();
		int _val = txxx.getGasData(appContext);
		test1.setText(""+_val);
		test2.setText(ad.ConvertNumberToVoltage(_val)+"v");
		
		/*设置各气体和温度的参数*/
		GenerSatuTextView generalSatuationTextView = new GenerSatuTextView();
		generalSatuationTextView.setCOTextView(appContext, textViewCoValue, textViewCoUnit);
		generalSatuationTextView.setCH4TextView(appContext, textViewCh4Value, textViewCh4Unit);
		generalSatuationTextView.setNO2TextView(appContext, textViewNo2Value, textViewNo2Unit);
		generalSatuationTextView.setO2TextView(appContext, textViewO2Value, textViewO2Unit);
		generalSatuationTextView.setTemperatureTextView(textViewTempValue);
		
		debug("获取NO2,CH4,CO,O2,温度值");	
	}

	private void handleSettingButton() {
		final Button button_setting = (Button) findViewById(R.id.generalactivity_button_setting);
		button_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				debug("设置按键按下");
				/*停止气体数据刷型定时器*/
				stopTimer();
				/*取消绑定电池广播接收*/
				battery.unregisterBatteryReciver(appContext);
				/*取消绑定系统时间广播接收*/
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(GeneralSatuation.this,GeneralSetting.class);
				startActivity(intent);
				// System.exit(0);
				finish();
			}
		});
		ButtonImageReplace.replaceSettingButtonImageOnTouch(button_setting);
	}
	
	private static void startTimer() 
	{
		if (mTimer == null)		mTimer = new Timer();

		if (mTimerTask == null) 
		{
			mTimerTask = new TimerTask() 
			{
				@Override
				public void run() 
				{
					Message message = new Message();
					message.what = 1;
					gasCollectionHandler.sendMessage(message);
				}
			};
		}
		if (mTimer != null && mTimerTask != null)	mTimer.schedule(mTimerTask, TIME, TIME);
	}

	private static void stopTimer() {

		if (mTimer != null) 
		{
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) 
		{
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	static Handler gasCollectionHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) 
			{
				dataSaveTime++;
				handleAllViews();
			}
			super.handleMessage(msg);
		}
	};

}
