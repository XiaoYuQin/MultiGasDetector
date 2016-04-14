package com.bohua.base;

import android.util.Log;

public class Debug {
	/**
	/*****************************
	 * qinxiaoyu.algorithm debug选项
	 * */
	static int GasAlgorithm = 1;
	static int AlarmPointAlgorithm = 0;
	static int DatabaseAlgorithm = 1;
	
	/*****************************
	 * qinxiaoyu.graphics debug选项
	 * */
	static int ButtonImageReplace = 0;
	static int GlobalData = 1;
	static int GenerSatuTextView = 1;
	
	/*****************************
	 * qinxiaoyu.multgas debug选项
	 * **************************/
	static int GeneralSatuation = 1;
	static int GeneralSetting = 1;
	static int CalibrationSetZero = 1;
	static int SetAlarmPoint = 1;
	static int CalibrationSetSensitivity = 1 ;
	static int CalibrationSetRatio = 1 ;
	static int SetDataSaveFreq = 1;
	static int SetWifi = 1;
	static int CalibrationChoose = 1;
	static int CalibrationChooseSetZP = 1;
	static int CalibrationChooseSetSen = 1;
	static int ServiceData = 1;
	static int DeviceLog = 1;
	static int DeviceLogReadPage = 1;
	
	/*****************************
	 * qinxiaoyu.base debug选项
	 * */
	static int Database = 1;
	static int Date = 1;
	static int Battery = 1;
	static int FileHandler = 1;
	static int Time = 1;
	/****************************************************************************************************/
	/****************************************************************************************************/
	/**
	 * qinxiaoyu.algorithm debug选项
	 * */
	public static int GasAlgorithmDebug()				{return GasAlgorithm ;}
	public static int AlarmPointAlgorithmDebug()		{return AlarmPointAlgorithm ;}
	public static int DatabaseAlgorithmDebug()			{return DatabaseAlgorithm ;}
	
	
	/**
	 * qinxiaoyu.base debug选项
	 * */
	public static int GlobalDataDebug()					{return GlobalData ;} 
	public static int DatabaseDebug()					{return Database ;}
	public static int BatteryDebug()					{return Battery ;}
	public static int FileHandlerDebug()				{return FileHandler ;}
	public static int TimeDebug()						{return Time ;}
	/**
	 * qinxiaoyu.multgas debug选项
	 * */
	public static int GeneralSatuationDebug()			{return GeneralSatuation ;}
	public static int GeneralSettingDebug()				{return GeneralSetting ;}
	public static int CalibrationSetZeroDebug()			{return CalibrationSetZero ;}
	public static int SetAlarmPointDebug()				{return SetAlarmPoint ;}
	public static int CalibrationSetSensitivityDebug()	{return CalibrationSetSensitivity ;}
	public static int CalibrationSetRatioDebug()		{return CalibrationSetRatio ;}
	public static int SetDataSaveFreqDebug()			{return SetDataSaveFreq ;}
	public static int SetWifiDebug()					{return SetWifi ;}
	public static int CalibrationChooseDebug()			{return CalibrationChoose ;}
	public static int CalibrationChooseSetZPDebug()		{return CalibrationChooseSetZP ;}
	public static int CalibrationChooseSetSenDebug()	{return CalibrationChooseSetSen ;}
	public static int ServiceDataDebug()				{return ServiceData ;}
	public static int DeviceLogDebug()					{return DeviceLog ;}
	public static int DeviceLogReadPageDebug()			{return DeviceLogReadPage ;}
	
	
	/**
	 * qinxiaoyu.graphics debug选项
	 * */
	public static int ButtonImageReplaceDebug()			{return ButtonImageReplace ;}
	public static int DateDebug()						{return Date ;}
	public static int GenerSatuTextViewDebug()			{return GenerSatuTextView ;}
	
	/****************************************************************************************************/
	public static void debugi(String tag,String text)	{Log.i(tag,text);}
	public static void debugd(String tag,String text)	{Log.d(tag,text);}
	public static void debugw(String tag,String text)	{Log.w(tag,text);}
	public static void debuge(String tag,String text)	{Log.e(tag,text);}
	public static void debugv(String tag,String text)	{Log.v(tag,text);}
	public static void debugln(String text)				{System.out.println(text);}




}
