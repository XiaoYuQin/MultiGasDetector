package com.bohua.multgas;

import com.bohua.algorithm.GasAlgorithm;
import com.bohua.base.Debug;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class ServiceData extends Service{

	static String tag = "ServiceData";
	private static void debug(String string){if(Debug.ServiceDataDebug() == 1) Debug.debugi(tag,string);}
	

	private static final int dataCount = 100;
	private int originalData[] = new int[dataCount];
	GasAlgorithm gasAlgorithm = new GasAlgorithm();
		
	int _i = 0;
	@SuppressWarnings("unused")
	private boolean threadFlag = false;
	private Handler handler;
	
	private Runnable getDataRunnable; 
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		debug("onBind");
		
		return null;
	}
	
	@Override  
    public void onCreate() {  
		debug("onCreate");
        super.onCreate();  
        earsBuffer();
        createHandler();
        
        getDataRunnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				originalDataHandle();
			}};
		handler.post(getDataRunnable);
    }  
      
	private void createHandler()
	{
		handler=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0x123:
					
					
					break;

				default:
					break;
				}
			}
		};
	}
    
    
	public void originalDataHandle()
	{
		debug("originalDataHandle");
		while(true)
		{
			threadSleep(100);
			writeDataIntoBuffer();
			debugBuffer();
		}
	}
	public void threadSleep(int time)
	{
		try 
		{
			Thread.sleep(200);
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	public void writeDataIntoBuffer()
	{
		debug("writeDataIntoBuffer");
		int _value;
		_value = gasAlgorithm.getGasData(getApplicationContext());
		originalData[_i] = _value;
		_i++;
		if(_i == dataCount) _i=0;
	}
    public void debugBuffer()
    {
    	debug("debugBuffer");
    	for(int i=0;i<dataCount;i++)
    		System.out.printf("%c ", originalData[i]);
    	
    	System.out.printf("\n");
    }
    public void earsBuffer()
    {
    	debug("earsBuffer");
    	for(int i=0;i<dataCount;i++)
    		originalData[i] = 0;
    }
    
    
    
    
    
    
    
    
    
    
    
	
	
}
