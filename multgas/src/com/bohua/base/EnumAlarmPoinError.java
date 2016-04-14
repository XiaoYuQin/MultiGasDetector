package com.bohua.base;

public enum EnumAlarmPoinError {

	EDIT_TEXT_INPUT_OK(0),
	EDIT_TEXT_INPUT_NOT_NUMBER(-1),
	EDIT_TEXT_INPUT_BIGER_THAN_MAX(-2),
	EDIT_TEXT_INPUT_NEGATIVE(-3),
	EDIT_TEXT_INPUT_OUT_OF_RANG(-5), 
	EDIT_TEXT_INPUT_NULL(-4);
	
	private int _value;
	
	private EnumAlarmPoinError(int value)
	{
		_value = value;
	}
	public int value()
	{
		return _value;
	}
}
