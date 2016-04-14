package com.bohua.multgas;

import java.util.ArrayList;
import java.util.HashMap;

import com.bohua.base.Battery;
import com.bohua.base.Date;
import com.bohua.base.Debug;
import com.bohua.base.LogFile;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceLogReadPage extends Activity {

	
	private Battery battery = new Battery();
	private Context appContext;

	private TextView SystemTime;
	private Date date = new Date();
	private Time time = new Time();
	
	
	private ArrayList<HashMap<String, String>> data;  
	private ListView listView;
	private NormalListAdapter adapter;
	
	
	static String tag = "DeviceLogReadPage";
	private static void debug(String string){if(Debug.DeviceLogReadPageDebug() == 1) Debug.debugi(tag,string);}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏显示
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.device_log_read_page);
		appContext = getApplicationContext();
		
		setAllViews();
		showLog();
		handleReturnButton();
			
	}
	private void setAllViews()
	{
		TextView SystemBattery= (TextView)findViewById(R.id.DeviceLogReadPage_testview_battery);
		battery.reciveBatteryLevel(appContext, SystemBattery);
		
		SystemTime = (TextView)findViewById(R.id.DeviceLogReadPage_textView_time);
		SystemTime.setText(date.getSystemDateToStringWithoutS());
		time.reciveTimeTick(appContext , SystemTime);
	}
	
	private void handleReturnButton()
	{
		Button button = (Button) findViewById(R.id.DeviceLogReadPage_button_return);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				battery.unregisterBatteryReciver(appContext);
				time.unregisterTimeTick(appContext);
				Intent intent = new Intent(DeviceLogReadPage.this,DeviceLog.class);
				startActivity(intent);
				finish();
			}
			
		});
		ButtonImageReplace.replaceReturnButtonImageOnTouch(button);
	}
	private void showLog()
	{
		Intent intent = getIntent();
		String fileName = (String)intent.getSerializableExtra("fileName");
		debug("bundle fileName = "+fileName);
		
		LogFile logfile = new LogFile();
		String file_content = logfile.ReadlogByListViewItemFileName(fileName);
		
        data = new ArrayList<HashMap<String,String>>();  
        HashMap<String, String> map = new HashMap<String, String>();  
        map.put("file_content", file_content); 
        map.put("xxx", "");  
        data.add(map);  

        // 创建适配器，并把数据交给适配器  
            adapter = new NormalListAdapter(this, data, R.layout.listview_item,   
                new String[]{"file_content","xxx"},   
                new int[]{R.id.listview_item_textView01,R.id.listview_item_textView02});  
        // 为listView添加适配器  
        listView = (ListView)findViewById(R.id.DeviceLogReadPage_listView);
        listView.setAdapter(adapter);  
	}
}
