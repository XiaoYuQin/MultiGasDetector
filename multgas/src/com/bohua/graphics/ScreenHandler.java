package com.bohua.graphics;

import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;



import android.app.Activity;;



public class ScreenHandler {

	public static void setFullScreenAndPortrait(Object object)
	{
		/**
		 * 设置设备全屏
		 * */
		((Activity) object).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		((Activity) object).requestWindowFeature(Window.FEATURE_NO_TITLE);
		/**
		 * 设置设备竖屏
		 * */
		((Activity) object).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		/**
		 * 设置设备屏幕一直亮
		 * */
		((Activity) object).getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
}
