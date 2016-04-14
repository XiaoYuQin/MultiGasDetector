package com.bohua.algorithm;

import static com.bohua.base.EnumAlarmPoinError.*;
import android.widget.EditText;

import com.bohua.base.Debug;
import com.bohua.base.EnumAlarmPoinError;
import com.bohua.base.GlobalData;
import com.bohua.base.LogFile;

public class AlarmPointAlgorithm {
	
	static String tag = "AlarmPointAlgorithm";
	private static void debug(String string){if(Debug.AlarmPointAlgorithmDebug() == 1) Debug.debugi(tag,string);}
	

	public static EnumAlarmPoinError checkoutEditTextError(EditText alarmPointEditText , String gasType)
	{
		String string = null;
		double _value;
		
		try
		{
			string = alarmPointEditText.getText().toString();
		}
		catch(Exception e)
		{
			LogFile log =  new LogFile();
			log.writeLogToFile("AlarmPointAlgorithm","EnumAlarmPoinError","报警点输入框输入了空值，这里已经做了处理"
					+"\n\t气体类型为："+gasType);
			
			return EDIT_TEXT_INPUT_NULL;
		}
		
		if("".equals(string.trim()))
		{
			debug("getEditTextText in put null");
			return EDIT_TEXT_INPUT_NULL;
		}
		
		try 
		{
			_value = Double.parseDouble(string);
			if(_value<0)
			{
				return EDIT_TEXT_INPUT_NEGATIVE;
			}
			else if(gasType.equals("O2"))
			{
				if(_value > 30 || _value < 0) 	return EDIT_TEXT_INPUT_OUT_OF_RANG;
			}
			else if(gasType.equals("CH4"))
			{
				if(_value > 4 || _value < 0) 	return EDIT_TEXT_INPUT_OUT_OF_RANG;
			}
			else if(gasType.equals("CO"))
			{
				if(_value > 500 || _value < 0) 	return EDIT_TEXT_INPUT_OUT_OF_RANG;
			}
			else if(gasType.equals("NO2"))
			{
				if(_value > 20 || _value < 0) 	return EDIT_TEXT_INPUT_OUT_OF_RANG;
			}
		}
		catch(Exception e)
		{
			debug("getEditTextText in put error");
			LogFile log =  new LogFile();
			log.writeLogToFile("AlarmPointAlgorithm","EnumAlarmPoinError","报警点输入框输入的不是数字，这里一般运行不到");
			return EDIT_TEXT_INPUT_NOT_NUMBER;
		}
		
		debug("getEditTextText"+_value);
		return EDIT_TEXT_INPUT_OK;		
	}
	
	public static void setAlarmPoint(String gasType ,String value)
	{
		double _value = Double.parseDouble(value);
		
		debug("setAlarmPoint gas is "+gasType+"   value is "+_value);
		if(gasType.equals("O2"))
		{
			GlobalData.setO2AlarmPoint(_value);
		}
		else if(gasType.equals("CH4"))
		{
			GlobalData.setCH4AlarmPoint(_value);
		}
		else if(gasType.equals("CO"))
		{
			GlobalData.setCOAlarmPoint(_value);
		}
		else if(gasType.equals("NO2"))
		{
			GlobalData.setNO2AlarmPoint(_value);
		}
	}
	public static int alarmingHandler(String gasType, double value)
	{
		debug("alarmingHandler :: gas is "+gasType+"   value is "+value);
		if(gasType.equals("O2"))
		{
			if((value < GlobalData.getO2AlarmPoint())||(value > 30))
			{
				debug("!!!!!!!!氧气含量低于报警点");
				return -1;
			}
		}
		else if(gasType.equals("CH4"))
		{
			if((value > GlobalData.getCH4AlarmPoint())||(value > 4))
			{
				debug("!!!!!!!!甲烷含量高于报警点");
				return -1;
			}
		}
		else if(gasType.equals("CO"))
		{
			if(value > GlobalData.getCOAlarmPoint()||(value > 500))
			{
				debug("!!!!!!!!一氧化碳含量高于报警点");
				return -1;
			}
		}
		else if(gasType.equals("NO2"))
		{
			if(value > GlobalData.getNO2AlarmPoint()||(value > 20))
			{
				debug("!!!!!!!!二氧化氮含量高于报警点");
				return -1;
			}	
		}
		return 0;
	}
	
	
	
	
	
}
