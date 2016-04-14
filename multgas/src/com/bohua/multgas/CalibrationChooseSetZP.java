package com.bohua.multgas;

import java.util.Timer;
import java.util.TimerTask;

import com.bohua.algorithm.GasAlgorithm;
import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Debug;
import com.bohua.base.Time;
import com.bohua.graphics.ButtonImageReplace;
import com.bohua.graphics.ScreenHandler;
import com.qinxiaoyu.multgas.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalibrationChooseSetZP extends Activity {

	static String tag = "CalibrationChooseSetZP";
	private static void debug(String string){if(Debug.CalibrationChooseSetZPDebug() == 1) Debug.debugi(tag,string);}
	
	private static final int TIME = 1000;
	
	Timer timer = new Timer();
	Battery battery = new Battery();

	Context appContext;
	TextView textViewValue;
	String gasType;
	int zeroPoint;
	float _voltage;
	
	TextView SystemTime;
	Date date = new Date();
	Time time = new Time();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**全屏显示**/
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.calibration_choose_set_zero_point);
		appContext = getApplicationContext();

		getBundleData();
		setAllViews();
		handleButton();
		timer.schedule(task, TIME,TIME);	
	
		
	}
	private void setAllViews()
	{
		textViewValue = (TextView)findViewById(R.id.CalibrationChooseSetZP_TextView_Value);
		
		TextView SystemBattery= (TextView)findViewById(R.id.CalibrationChooseSetZP_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		
		SystemTime = (TextView)findViewById(R.id.CalibrationChooseSetZP_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
		
	}
	private void handleButton()
	{
		handleReturnButton();
		handleSetZeroPointCompleteButton();
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
		final Button button_return = (Button)findViewById(R.id.CalibrationChooseSetZeroPoint_Button_Return);
		button_return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("handleReturnButton");
				timer.cancel();
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(CalibrationChooseSetZP.this,CalibrationChoose.class);
				Bundle bundleData = new Bundle();
				bundleData.putSerializable("GAS_TYPE", gasType);
				intent.putExtras(bundleData);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button_return);
	}
	
	private void handleSetZeroPointCompleteButton()
	{
		debug("handleSetZeroPointCompleteButton");
		final Button button = (Button)findViewById(R.id.CalibrationChooseSetZeroPoint_Button_Complete);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				debug("handleSetZeroPointCompleteButton");
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				final Intent intent = new Intent(CalibrationChooseSetZP.this,CalibrationChoose.class);
				Bundle bundleData = new Bundle();
				bundleData.putSerializable("GAS_TYPE", gasType);
				intent.putExtras(bundleData);
				
				new AlertDialog.Builder(CalibrationChooseSetZP.this)   
					.setCancelable(false)
					.setPositiveButton("确定",new DialogInterface.OnClickListener()
					{
	
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							timer.cancel();
							GasAlgorithm.setGasZeroPoint(appContext,gasType,zeroPoint);
							startActivity(intent);
		            		finish(); 
						}
					})
					.setMessage("成功调整零点\n零点电压为 "+_voltage+"伏")  
					.show(); 
			}
		});
		ButtonImageReplace.replaceCopmleteButtonImageOnTouch(button);
		
	}
	Handler O2_handler = new Handler() 
	{  
	  	public void handleMessage(Message msg) 
        {  
            if (msg.what == 1) 
            {  
        		GasAlgorithm gasAlgorithm = new GasAlgorithm();
        		zeroPoint = gasAlgorithm.getGasData(appContext);
        		_voltage = gasAlgorithm.ConvertNumberToVoltage(zeroPoint);
        		textViewValue.setText(zeroPoint+"\n"+_voltage+"v");
        		debug("_voltage = "+_voltage);
            }  
            super.handleMessage(msg);  
        };  
	};  
	
	TimerTask task = new TimerTask() 
	{  
	    @Override  
	    public void run() 
	    {  
	        // 需要做的事:发送消息  
	        Message message = new Message();  
	        message.what = 1;  
	        O2_handler.sendMessage(message);  
	    }  
	}; 	
	
}
