package com.bohua.multgas;

import static com.bohua.base.Debug.debugi;

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
import android.widget.EditText;
import android.widget.TextView;

public class CalibrationChooseSetSen extends Activity {

	static String tag = "CalibrationChooseSetSen";
	private static void debug(String string){if(Debug.CalibrationChooseSetSenDebug() == 1) Debug.debugi(tag,string);}
	
	protected static final int EDIT_TEXT_INPUT_NOT_NUMBER = -1;
	protected static final int EDIT_TEXT_INPUT_TOO_LONG = -2;
	protected static final int EDIT_TEXT_INPUT_NEGATIVE = -3;
	protected static final int EDIT_TEXT_INPUT_NULL = -4;
	private static final int TIME = 1000;
	
	String gasType;
	EditText SensitivityEditText;
	TextView textViewValue;
	int sensitivity;
	float _voltage;
	Timer timer = new Timer();
	Context appContext;
	float editTextInput;
	
	Battery battery = new Battery();
	
	TextView SystemTime;
	Date date = new Date();
	Time time = new Time();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**全屏显示**/
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.calibration_choose_set_sen);
		appContext = getApplicationContext();
		
		getBundleData();
		setAllView();
		handleButton();
		timer.schedule(task, TIME,TIME);	
		
		
	}
	private void setAllView()
	{
		SensitivityEditText = (EditText)findViewById(R.id.CalibrationChooseSetSen_EditText_Sen);
		textViewValue = (TextView)findViewById(R.id.CalibrationChooseSetSen_TextView_Value);
		
		TextView SystemBattery= (TextView)findViewById(R.id.CalibrationChooseSetSen_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		
		SystemTime = (TextView)findViewById(R.id.CalibrationChooseSetSen_textView_time);
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
		final Button button_return = (Button)findViewById(R.id.CalibrationChooseSetSen_Button_Return);
		button_return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("handleReturnButton");
				time.unregisterTimeTick(appContext);
				battery.unregisterBatteryReciver(appContext);
				timer.cancel();
				Intent intent = new Intent(CalibrationChooseSetSen.this,CalibrationChoose.class);
				
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
		final Button button = (Button)findViewById(R.id.CalibrationChooseSetSen_Button_Complete);
		button.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				debug("handleSetZeroPointCompleteButton");
				
				editTextInput = getEditTextText();
				if(editTextInput == EDIT_TEXT_INPUT_NOT_NUMBER)
				{
					new AlertDialog.Builder(CalibrationChooseSetSen.this)   
					.setMessage("输入格式错")  
					.show();  
				}
				else if(editTextInput == EDIT_TEXT_INPUT_TOO_LONG)
				{
					new AlertDialog.Builder(CalibrationChooseSetSen.this)   
					.setMessage("输入超长")  
					.show();  
				}
				else if(editTextInput == EDIT_TEXT_INPUT_NEGATIVE)
				{
					new AlertDialog.Builder(CalibrationChooseSetSen.this)   
					.setMessage("不能输入负数")  
					.show();  
				}
				else if(editTextInput == EDIT_TEXT_INPUT_NULL)
				{
					new AlertDialog.Builder(CalibrationChooseSetSen.this)   
					.setMessage("输入框不能为空")  
					.show(); 
				}
				else
				{
					battery.unregisterBatteryReciver(appContext);
					time.unregisterTimeTick(appContext);
					new AlertDialog.Builder(CalibrationChooseSetSen.this)   
					.setCancelable(false)
					.setPositiveButton("确定",new DialogInterface.OnClickListener()
					{
	
						@Override
						public void onClick(DialogInterface dialog,int which) 
						{
							timer.cancel();
							GasAlgorithm.setGasCalibration(gasType,sensitivity,editTextInput,20);

							Intent intent = new Intent(CalibrationChooseSetSen.this,CalibrationChoose.class);
							Bundle bundleData = new Bundle();
							bundleData.putSerializable("GAS_TYPE", gasType);
							intent.putExtras(bundleData);
							startActivity(intent);
		            		finish(); 
						}
					})
					.setMessage("成功调整灵敏度\n灵敏度电压为 "+_voltage+"伏")  
					.show(); 
				}
			} 
		});
		ButtonImageReplace.replaceCopmleteButtonImageOnTouch(button);
		
	}

	private float getEditTextText()
	{
		String string = null;
		float ret = -1;
		
		
		try
		{
			string = SensitivityEditText.getText().toString();
		}catch(Exception e)
		{
			
			return EDIT_TEXT_INPUT_NULL;
		}
		
		if("".equals(string.trim()))
		{
			debugi("CalibrationSetSensitivity","getEditTextText in put null");
			return EDIT_TEXT_INPUT_NULL;
		}
		
		try
		{
			ret = Float.parseFloat(string);
			if(ret<0)
			{
				ret = EDIT_TEXT_INPUT_NEGATIVE;
			}
			else if(ret >R.integer.SensitivityInputMax)
			{
				ret = EDIT_TEXT_INPUT_TOO_LONG;
			}
		}catch(Exception e)
		{
			debugi("CalibrationSetSensitivity","getEditTextText in put error");
			ret = EDIT_TEXT_INPUT_NOT_NUMBER;
		}
		debugi("CalibrationSetSensitivity","getEditTextText"+ret);
		
		return ret;
	}
	
	Handler gas_handler = new Handler() 
	{  
	  	public void handleMessage(Message msg) 
        {  
            if (msg.what == 1) 
            {  
        		GasAlgorithm gasAlgorithm = new GasAlgorithm();
        		sensitivity = gasAlgorithm.getGasData(appContext);
        		_voltage = gasAlgorithm.ConvertNumberToVoltage(sensitivity);
        		textViewValue.setText(sensitivity+"\n"+_voltage+"v");
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
	        gas_handler.sendMessage(message);
	    }  
	}; 	
	
}
