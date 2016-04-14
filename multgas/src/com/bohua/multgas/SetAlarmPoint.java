package com.bohua.multgas;

import com.bohua.algorithm.AlarmPointAlgorithm;
import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Debug;
import com.bohua.base.EnumAlarmPoinError;
import com.bohua.base.GlobalData;
import com.bohua.base.LogFile;
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
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetAlarmPoint extends Activity {
	
	String gasType;
	EditText alarmPointEditText;

	static String tag = "SetAlarmPoint";
	private static void debug(String string){if(Debug.SetAlarmPointDebug() == 1) Debug.debugi(tag,string);}
	
	Battery battery = new Battery();
	Context appContext;
	
	TextView SystemTime;
	Date date = new Date();
	Time time = new Time();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏显示
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.set_alarmpoint);
		
		appContext = getApplicationContext();
		getBundleData();
		setAllViews();
		handleButtons();
	}
	
	
		
	
	private void getBundleData()
	{
		Intent intent = getIntent();
		gasType = (String)intent.getSerializableExtra("GAS_TYPE");
		debug("bundle data = "+gasType);
	}
	private void setAllViews()
	{
		debug("setAllViews");
		setReturnButtonText();
		setEditTextInputStyle();
		setTpisText();
		
		TextView SystemBattery= (TextView)findViewById(R.id.setalarmpoint_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.setalarmpoint_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}
	private void handleButtons()
	{
		handleReturnButton();
		handleCompleteButton();
	}
	private void setTpisText()
	{
		debug("setTpisText");
		TextView tips = (TextView)findViewById(R.id.setalarmpoint_textView_tips);
		TextView introduction = (TextView)findViewById(R.id.setalarmpoint_textView_introduction);
		TextView old_set = (TextView)findViewById(R.id.setalarmpoint_textView_oldset);
		String tips_array[] = this.getResources().getStringArray(R.array.SetAlarmPointTipsText);
		String multGasType[] = this.getResources().getStringArray(R.array.GAS_TYPE);
		String introduction_text[] = this.getResources().getStringArray(R.array.SetAlarmPointIntroduction);
		for(int i=0 ; i<multGasType.length;i++)
		{
			if(gasType.equals(multGasType[i]))
			{
				tips.setText(tips_array[i]);
				debug("setalarmpoint_textView_tips = "+tips_array[i]);
				
				introduction.setText(introduction_text[i]);
				debug("setalarmpoint_textView_introduction = "+introduction_text[i]);
				
				
			}
			if(gasType.equals("NO2"))
			{
				old_set.setText("当前的报警点为"+GlobalData.getNO2AlarmPoint()+"PPM");
				alarmPointEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
			}
			else if(gasType.equals("O2"))
			{
				old_set.setText("当前的报警点为"+GlobalData.getO2AlarmPoint()+"%");
				alarmPointEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
			}
			else if(gasType.equals("CO"))
			{
				old_set.setText("当前的报警点为"+GlobalData.getCOAlarmPoint()+"PPM");
				alarmPointEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
			}
			else if(gasType.equals("CH4"))
			{
				old_set.setText("当前的报警点为"+GlobalData.getCH4AlarmPoint()+"%");
				alarmPointEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
			}
		}
		
				
	}
	private void setEditTextInputStyle()
	{
		debug("setEditTextInputStyle");
		alarmPointEditText = (EditText)findViewById(R.id.setalarmpoint_editText_alarmPoint);

//		if(gasType!=null)
//		{
//			if(gasType.equals("O2")||gasType.equals("CH4"))	
//			{
//				debug("setInputType O2 CH4");
//				alarmPointEditText.setKeyListener(new NumberKeyListener()
//				{
//					@Override
//					protected char[] getAcceptedChars() {
//						// TODO Auto-generated method stub
//						char[] numberChars={'0','1','2','3','4','5','6','7','8','9','0','.'};
//						return numberChars;
//					}
//
//					@Override
//					public int getInputType() {
//						// TODO Auto-generated method stub
//						return InputType.TYPE_CLASS_NUMBER;
//					}
//				});
//			}
//			else if(gasType.equals("NO2")||gasType.equals("CO"))	
//			{
//				debug("setInputType NO2 CO");
//				alarmPointEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
//			}
//
//		}
		
	}
	private void setReturnButtonText()
	{
//		debug("setReturnButtonText");
//		TextView textview_retuen = (TextView)findViewById(R.id.setalarmpoint_textView_return);		
//		String multGasType[] = this.getResources().getStringArray(R.array.GAS_TYPE);
//		String returnText[] = this.getResources().getStringArray(R.array.setAlarmPointArrayReturnText);
//		for(int i=0 ; i<multGasType.length;i++)
//		{
//			if(gasType.equals(multGasType[i]))
//			{
//				textview_retuen.setText(returnText[i]);
//			}
//		}
	}
	private void handleReturnButton()
	{
	
		Button button = (Button)findViewById(R.id.setalarmpoint_button_return);
		
		String multGasType[] = this.getResources().getStringArray(R.array.GAS_TYPE);
		String returnText[] = this.getResources().getStringArray(R.array.setAlarmPointArrayReturnText);
		for(int i=0 ; i<multGasType.length;i++)
		{
			if(gasType.equals(multGasType[i]))
			{
				button.setText(returnText[i]);
			}
		}

		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("setReturnButtonText");
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(SetAlarmPoint.this,GeneralAlarmPoint.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button);
	}
	private void handleCompleteButton()
	{
		Button button = (Button)findViewById(R.id.setalarmpoint_button_ok);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				debug("handleOkButton");
				
				EnumAlarmPoinError result;
				result = AlarmPointAlgorithm.checkoutEditTextError(alarmPointEditText, gasType);
				
				switch(result)
				{
					case EDIT_TEXT_INPUT_OK:
						debug("EDIT_TEXT_INPUT_OK");
						
	            		new AlertDialog.Builder(SetAlarmPoint.this)   
							.setCancelable(false)
							.setPositiveButton("完成",new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog,int which) 
								{
									// TODO Auto-generated method stub

									
									battery.unregisterBatteryReciver(appContext);
									time.unregisterTimeTick(appContext);
									String string = alarmPointEditText.getText().toString();
									AlarmPointAlgorithm.setAlarmPoint(gasType,string);
									
									LogFile log =  new LogFile();
									log.writeLogToFile("SetAlarmPoint","handleCompleteButton",
										 "设置报警点"
										+"\n\t\t\t\t设置报警点的气体类型："+gasType
										+"\n\t\t\t\t设置报警点的报警值："+string);
									
									Intent intent = new Intent(SetAlarmPoint.this,GeneralSetting.class);
									startActivity(intent);
									finish(); 
								}
							})
							.setMessage("设置成功\n点击返回")  
							.show();
						break;
					case EDIT_TEXT_INPUT_NOT_NUMBER:
						debug("EDIT_TEXT_INPUT_NOT_NUMBER");
						new AlertDialog.Builder(SetAlarmPoint.this)
							.setMessage("输入框不能为空")  
							.show();  
						break;
					case EDIT_TEXT_INPUT_BIGER_THAN_MAX:
						debug("EDIT_TEXT_INPUT_BIGER_THAN_MAX");
						new AlertDialog.Builder(SetAlarmPoint.this)
							.setMessage("输入数据太长")
							.show();  
						break;
					case EDIT_TEXT_INPUT_NEGATIVE:
						debug("EDIT_TEXT_INPUT_NEGATIVE");
							new AlertDialog.Builder(SetAlarmPoint.this)
							.setMessage("输入不能为负数")
							.show(); 
						break;
					case EDIT_TEXT_INPUT_OUT_OF_RANG:
						debug("EDIT_TEXT_INPUT_OUT_OF_RANG");
						new AlertDialog.Builder(SetAlarmPoint.this)
							.setMessage("输入超出范围")
							.show(); 
						break;
					case EDIT_TEXT_INPUT_NULL:
						debug("EDIT_TEXT_INPUT_NULL");
						new AlertDialog.Builder(SetAlarmPoint.this)
							.setMessage("输入不能为空")
							.show(); 
						break;
					default:
						break;
				}
			}
		});
		ButtonImageReplace.replaceCopmleteButtonImageOnTouch(button);
	}
}
