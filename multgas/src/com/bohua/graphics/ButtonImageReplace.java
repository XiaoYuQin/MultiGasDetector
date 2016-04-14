package com.bohua.graphics;

import com.bohua.base.Debug;
import com.qinxiaoyu.multgas.R;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class ButtonImageReplace {
	
	private static final String tag = "graphics ButtonImageReplace";
	private static void debug(String string)	{if(Debug.ButtonImageReplaceDebug()== 1)Debug.debugi(tag,string);}
	
	
	public static void replaceNormalButtonImageOnTouch(final Button button){
		
			button.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					debug("replaceNormalButtonImageOnTouch");
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
						button.setBackgroundResource(R.drawable.chosebutton_press); 
					}
					else if(event.getAction() == MotionEvent.ACTION_UP)
					{
						button.setBackgroundResource(R.drawable.chosebutton);    
					}
					return false;
				}
			});
	}
	public static void replaceSpecialButtonImageOnTouch(final Button button,final int not_press_pic ,final int press_pic)
	{
		button.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				debug("replaceSpecialButtonImageOnTouch");
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					button.setBackgroundResource(press_pic); 
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					button.setBackgroundResource(not_press_pic);    
				}
				return false;
			}
		});
	}
	public static void replaceReturnButtonImageOnTouch(final Button button)
	{
		
		button.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				debug("replaceNormalButtonImageOnTouch");
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					button.setBackgroundResource(R.drawable.returnbutton_press); 
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					button.setBackgroundResource(R.drawable.returnbutton);    
				}
				return false;
			}
		});
	}
	public static void replaceSettingButtonImageOnTouch(final Button button)
	{
		button.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				debug("replaceNormalButtonImageOnTouch");
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					button.setBackgroundResource(R.drawable.generalactivity_setting_press); 
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					button.setBackgroundResource(R.drawable.generalactivity_setting);    
				}
				return false;
			}
		});
	}
	public static void replaceCopmleteButtonImageOnTouch(final Button button)
	{
		button.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				debug("replaceNormalButtonImageOnTouch");
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					button.setBackgroundResource(R.drawable.ok_long_button_press); 
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					button.setBackgroundResource(R.drawable.ok_long_button);    
				}
				return false;
			}
		});
	}
	
	
	
	
	
	
	
	
}
