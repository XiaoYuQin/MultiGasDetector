package com.bohua.algorithm;

import static com.bohua.base.Debug.debugi;
import static com.bohua.base.EnumAlarmPoinError.*;
import android.widget.EditText;

import com.bohua.base.EnumAlarmPoinError;

public class AlarmPointInputErrorCheckout {

	public static EnumAlarmPoinError checkoutEditTextError(EditText alarmPointEditText , String gasTpye)
	{
		String string = null;
		double _value;
		
		try
		{
			string = alarmPointEditText.getText().toString();
		}
		catch(Exception e)
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
			_value = Double.parseDouble(string);
			if(_value<0)
			{
				return EDIT_TEXT_INPUT_NEGATIVE;
			}
			else if(gasTpye.equals("O2"))
			{
				if(_value > 30 || _value < 0) 	return EDIT_TEXT_INPUT_OUT_OF_RANG;
			}
			else if(gasTpye.equals("CH4"))
			{
				if(_value > 4 || _value < 0) 	return EDIT_TEXT_INPUT_OUT_OF_RANG;
			}
			else if(gasTpye.equals("CO"))
			{
				if(_value > 500 || _value < 0) 	return EDIT_TEXT_INPUT_OUT_OF_RANG;
			}
			else if(gasTpye.equals("NO2"))
			{
				if(_value > 20 || _value < 0) 	return EDIT_TEXT_INPUT_OUT_OF_RANG;
			}
		}
		catch(Exception e)
		{
			debugi("AlarmPointInputErrorCheckout","getEditTextText in put error");
			return EDIT_TEXT_INPUT_NOT_NUMBER;
		}
		
		debugi("AlarmPointInputErrorCheckout","getEditTextText"+_value);
		return EDIT_TEXT_INPUT_OK;		
	}
	
	
	
}
