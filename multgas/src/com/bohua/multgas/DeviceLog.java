package com.bohua.multgas;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Debug;
import com.bohua.base.FileHandler;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceLog extends Activity {

	
	private Battery battery = new Battery();
	private Context appContext;
	
	
	private TextView SystemTime;
	private Date date = new Date();
	private Time time = new Time();
	
	private ArrayList<HashMap<String, String>> data;  
	private NormalListAdapter logAdapter;  
	private ListView listView;
	
	static String tag = "DeviceLog";
	private static void debug(String string){if(Debug.DeviceLogDebug() == 1) Debug.debugi(tag,string);}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏显示
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.device_log);
		appContext = getApplicationContext();
		
		setAllViews();
		handleButton();
		getLogFiles();
	}
	private void setAllViews()
	{
		TextView SystemBattery= (TextView)findViewById(R.id.devicelog_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.devicelog_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}
	private void handleButton()
	{
		handleReturnButton();
	}
	
	private void handleReturnButton()
	{
		Button button = (Button)findViewById(R.id.devicelog_button_return);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(DeviceLog.this,GeneralSetting.class);
				startActivity(intent);
				finish();
			}
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button);
	}
	private void getLogFiles()
	{
		FileHandler fileHandler = new FileHandler();
		String folderPath = fileHandler.getSdcardPath()+"/MultGasDetecter/logs/";
		File folder =  new File(folderPath);
		File[] fileLists = folder.listFiles();
		debug("文件数量为"+fileLists.length);
		
		data = new ArrayList<HashMap<String,String>>();  
		
		if (null != fileLists && fileLists.length > 0) 
		{
			for(File file : fileLists)
			{	
				HashMap<String, String> map = new HashMap<String, String>();  
	            String fileName = file.getName();
	            String fileSize = fileHandler.getFileSize(file);
	            debug("文件后缀为--"+fileHandler.getFileSuffix(file));
	            map.put("fileName", fileName);  
	            map.put("fileSize", fileSize);  
	            data.add(map);  
			}
		}
	      // 创建适配器，并把数据交给适配器  
		logAdapter = new NormalListAdapter(this, data, R.layout.listview_item,   
                new String[]{"fileName","fileSize"},   
                new int[]{R.id.listview_item_textView01,R.id.listview_item_textView02});  
		
		listView = (ListView)findViewById(R.id.devicelog_listView);
        // 为listView添加适配器  
        listView.setAdapter(this.logAdapter);  
		
        
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(arg2); 
				String fileName = map.get("fileName");
				debug("onItemClick fileName = "+fileName);
				
				Intent intent = new Intent(DeviceLog.this,DeviceLogReadPage.class);
				Bundle bundleData = new Bundle();
				bundleData.putSerializable("fileName", fileName);
				intent.putExtras(bundleData);
				startActivity(intent);
				finish();
			}});
	}
	
	

}
