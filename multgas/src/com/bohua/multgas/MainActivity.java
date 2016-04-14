package com.bohua.multgas;

import java.util.Timer;
import java.util.TimerTask;

import com.bohua.base.FileHandler;
import com.bohua.base.FileProperties;
import com.bohua.base.GlobalData;
import com.bohua.base.LogFile;
import com.bohua.graphics.ScreenHandler;
import com.qinxiaoyu.multgas.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainActivity extends Activity {

	private static final int TIME = 100;
	FileHandler file = new FileHandler();
	FileProperties file_cfg = new FileProperties();
	Context appContext;
	Timer timer = new Timer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏显示
		ScreenHandler.setFullScreenAndPortrait(this);
		setContentView(R.layout.activity_main);

		appInit();
		/*启动定时器，1秒后进入下个界面*/
		timer.schedule(task, TIME,TIME);

	} 
	private void appInit(){
		appContext = getApplicationContext();
		
		/**
		 * 创建所有文件夹，若文件夹存在则不再次创建
		 * */
		file.creatAllAppFiles(getApplicationContext());
		/**
		 * 载入参数，若参数为空则创建并且赋值为0
		 * */
		file_cfg.checkPropertiesKey(appContext);
		/**
		 * 从文本中获取全局变量
		 * */
		GlobalData.readGlobalDataFromFileProperties(appContext);
		
		Log.i("主界面","初始化完成");
		
		LogFile log =  new LogFile();
		log.writeLogToFile("MainActivity","onCreate","软件启动");
	}
	Handler main_wnd_handler = new Handler() 
	{  
	  	public void handleMessage(Message msg) 
        {  
            if (msg.what == 1) 
            { 
            	Log.i("主界面","定时1秒达成");
            	timer.cancel();
            	Intent intent = new Intent(MainActivity.this,GeneralSatuation.class);
            	startActivity(intent);
            	finish();
            }
            super.handleMessage(msg);  
        }
	};
	
	TimerTask task = new TimerTask() 
	{   
	    @Override  
	    public void run() 
	    {  
	        // 需要做的事:发送消息  
	        Message message = new Message();  
	        message.what = 1;  
	        main_wnd_handler.sendMessage(message);  
	    }  
	}; 
}
