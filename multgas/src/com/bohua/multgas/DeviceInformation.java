package com.bohua.multgas;

import java.util.ArrayList;
import java.util.HashMap;

import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Time;
import com.bohua.graphics.ButtonImageReplace;
import com.bohua.graphics.NormalListAdapter;
import com.bohua.graphics.ScreenHandler;
import com.qinxiaoyu.multgas.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceInformation extends Activity {

	Battery battery = new Battery();
	Context appContext;
	
	TextView SystemTime;
	Date date = new Date();
	Time time = new Time();
	
	private ListView listView;
	private ArrayList<HashMap<String, String>> data;  
	
	private NormalListAdapter myAdapter;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏显示
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.device_information);
		appContext = getApplicationContext();
		
		setAllViews();
		handleButton();
	}
	
	
	private void setAllViews()
	{
		TextView SystemBattery= (TextView)findViewById(R.id.deviceinformation_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		
		SystemTime = (TextView)findViewById(R.id.deviceinformation_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
		
		listView = (ListView)findViewById(R.id.deviceinformation_listView);
		
//		ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,R.layout.listview_item1,device_inf);
		
		initData();
	}
	
    private void initData(){  
        
    	String[] device_inf = this.getResources().getStringArray(R.array.DeviceInformationList);
    	String[] device_inf_val = this.getResources().getStringArray(R.array.DeviceInformationValue);
        data = new ArrayList<HashMap<String,String>>();  
        for(int i=0; i<device_inf.length; i++)
        {  
            HashMap<String, String> map = new HashMap<String, String>();  
            String _inf = device_inf[i];  
            String _val = device_inf_val[i];
            map.put("inf", _inf);  
            map.put("val", _val);  
            data.add(map);  
        }  
        // 创建适配器，并把数据交给适配器  
        myAdapter = new NormalListAdapter(this, data, R.layout.listview_item,   
                new String[]{"inf","val"},   
                new int[]{R.id.listview_item_textView01,R.id.listview_item_textView02});  
        // 为listView添加适配器  
        listView.setAdapter(this.myAdapter);  
    }
	
	

	private void handleButton()
	{
		handleReturnButton();
		handleCompleteButton();
		
		
//		handleB1();
//		handleB2();
//		handleB3();
//		handleB4();
	}
	
	private void handleReturnButton()
	{
		Button button = (Button)findViewById(R.id.deviceinformation_button_return);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(DeviceInformation.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button);
	}
	private void handleCompleteButton()
	{
		Button button = (Button)findViewById(R.id.deviceinformation_button_ok);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(DeviceInformation.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceCopmleteButtonImageOnTouch(button);
	}
	
//	private void handleB1()
//	{
//		Button button = (Button)findViewById(R.id.deviceinformation_button1);
//		button.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(DeviceInformation.this,ServiceData.class);
//				startService(intent);
//			}
//		});
//	}
//	
//	private void handleB2()
//	{
//		Button button = (Button)findViewById(R.id.deviceinformation_button2);
//		button.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(DeviceInformation.this,ServiceData.class);
//				stopService(intent);
//			}
//		});
//	}
//	private void handleB3()
//	{
//		Button button = (Button)findViewById(R.id.deviceinformation_button3);
//		button.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
//	private void handleB4()
//	{
//		Button button = (Button)findViewById(R.id.deviceinformation_button4);
//		button.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
	
	
	

}
