package com.bohua.base;

import com.qinxiaoyu.multgas.R;

import android.content.Context;

public class GlobalData {
	
	static String tag = "GlobalData";
	private static void debug(String string){if(Debug.GlobalDataDebug() == 1) Debug.debugi(tag,string);}
	
	static int 		O2ZeroPoint = 0;
	static double 	O2Ratio = 0;
	static int 		COZeroPoint = 0;
	static double 	CORatio = 0;
	static int 		NO2ZeroPoint=0;
	static double 	NO2Ratio = 0;
	static int 		CH4ZeroPoint=0;
	static double 	CH4Ratio = 0;
	
	/**
	 * 氧气全局参数 的方法
	 * */
	public static int getO2ZeroPoint()				{return  O2ZeroPoint;}
	public static void setO2ZeroPoint(int zeroPoint){O2ZeroPoint = zeroPoint;}
	public static double getO2Ratio()				{return  O2Ratio;}
	public static void setO2Ratio(double ratio)		{O2Ratio = ratio;}
	

	/**
	 * 一氧化碳全局参数 的方法
	 * */
	public static int getCOZeroPoint()				{return  COZeroPoint;}
	public static void setCOZeroPoint(int zeroPoint){COZeroPoint = zeroPoint;}
	public static double getCORatio()				{return  CORatio;}
	public static void setCORatio(double ratio)		{CORatio = ratio;}

	/**
	 * 二氧化氮全局参数 的方法
	 * */
	public static int getNO2ZeroPoint()					{return  NO2ZeroPoint;}
	public static void setNO2ZeroPoint(int zeroPoint)	{NO2ZeroPoint = zeroPoint;}
	public static double getNO2Ratio()					{return  NO2Ratio;}
	public static void setNO2Ratio(double ratio)		{NO2Ratio = ratio;}
	
	/**
	 * 甲烷全局参数 的方法
	 * */
	public static int getCH4ZeroPoint()					{return  CH4ZeroPoint;}
	public static void setCH4ZeroPoint(int zeroPoint)	{CH4ZeroPoint = zeroPoint;}
	public static double getCH4Ratio()					{return  CH4Ratio;}
	public static void setCH4Ratio(double ratio)		{CH4Ratio = ratio;}
	
	
	
	/****************************************************************************************************/
	/****************************************************************************************************/
	static double 	CH4AlarmPoint = 0;
	static double 	COAlarmPoint = 0;
	static double 	O2AlarmPoint = 0;
	static double 	NO2AlarmPoint = 0;
	
	/**
	 * 获取/设置气体报警点的方法
	 * */
	public static void setCH4AlarmPoint(double alarmPoint)	
	{
		CH4AlarmPoint = alarmPoint;
		FileProperties file_cfg = new FileProperties();
		file_cfg.writeProperties("CH4AlarmPoint", CH4AlarmPoint+"");
	}
	public static void setCOAlarmPoint(double alarmPoint)	
	{
		COAlarmPoint = alarmPoint;
		FileProperties file_cfg = new FileProperties();
		file_cfg.writeProperties("COAlarmPoint", COAlarmPoint+"");
	}
	public static void setO2AlarmPoint(double alarmPoint)	
	{
		O2AlarmPoint = alarmPoint;
		FileProperties file_cfg = new FileProperties();
		file_cfg.writeProperties("O2AlarmPoint", O2AlarmPoint+"");
	}
	public static void setNO2AlarmPoint(double alarmPoint)	
	{
		NO2AlarmPoint = alarmPoint;
		FileProperties file_cfg = new FileProperties();
		file_cfg.writeProperties("NO2AlarmPoint", NO2AlarmPoint+"");
	}
	
	public static double getCH4AlarmPoint()	{return CH4AlarmPoint;}
	public static double getCOAlarmPoint()	{return COAlarmPoint;}
	public static double getO2AlarmPoint()	{return O2AlarmPoint;}
	public static double getNO2AlarmPoint()	{return NO2AlarmPoint;}
	
	
	/****************************************************************************************************/
	/****************************************************************************************************/
	static EnumDataSaveFreq DataSaveFreqence ;
	
	public static void setDataSaveFreqence(EnumDataSaveFreq selectFreq)	
	{
		DataSaveFreqence = selectFreq;
		FileProperties file_cfg = new FileProperties();
		file_cfg.writeProperties("DataSaveFreqence", DataSaveFreqence+"");
	}
	public static EnumDataSaveFreq getDataSaveFreqence()	{return DataSaveFreqence;}
	
	
	/****************************************************************************************************/
	/****************************************************************************************************/
	/***
	 *系统状态全局变量 
	 ***/
	public static EnumState deviceState;
	
	public static void setDeviceState(EnumState state)	
	{
		deviceState = state;
	}
	public static EnumState getDeviceState()	{return deviceState ;}
	
	

	/****************************************************************************************************/
	/****************************************************************************************************/
	/**
	 * 只在系统启动的时刻调用该函数
	 * 当各参数发生变化的时刻，即时赋值给全局的变量，不调用该函数
	 * **/
	public static void readGlobalDataFromFileProperties(Context context)  
	{
		FileProperties file_cfg = new FileProperties();
		
		/*从文件中获取---氧气零点*/
		debug("从文件中获取---氧气零点");
		O2ZeroPoint=Integer.parseInt(file_cfg.readProperties(context.getResources().getString(R.string.SenserParamO2ZeroPiont)));
		debug("零点为"+O2ZeroPoint);
		
		/*从文件中获取---氧气斜率*/
		debug("从文件中获取---氧气斜率");
		O2Ratio = Double.parseDouble(file_cfg.readProperties(context.getResources().getString(R.string.SenserParamO2Ratio)));
		debug("斜率为"+O2Ratio);
		
		/*从文件中获取---一氧化碳零点*/
		debug("从文件中获取---一氧化碳零点");
		COZeroPoint=Integer.parseInt(file_cfg.readProperties("COZeroPoint"));
		debug("零点为"+COZeroPoint);
		
		/*从文件中获取---一氧化碳斜率*/
		debug("从文件中获取---一氧化碳斜率");
		CORatio =  Double.parseDouble(file_cfg.readProperties("CORatio"));
		debug("斜率为"+CORatio);
		
		/*从文件中获取---二氧化氮零点*/
		debug("从文件中获取---二氧化氮零点");
		NO2ZeroPoint=Integer.parseInt(file_cfg.readProperties("NO2ZeroPoint"));
		debug("零点为"+NO2ZeroPoint);
		
		/*从文件中获取---二氧化氮斜率*/
		debug("从文件中获取---二氧化氮斜率");
		NO2Ratio =  Double.parseDouble(file_cfg.readProperties("NO2Ratio"));
		debug("斜率为"+NO2Ratio);
		
		/*从文件中获取---甲烷零点*/
		debug("从文件中获取---甲烷零点");
		CH4ZeroPoint=Integer.parseInt(file_cfg.readProperties("CH4ZeroPoint"));
		debug("零点为"+CH4ZeroPoint);
		
		/*从文件中获取---甲烷斜率*/
		debug("从文件中获取---甲烷斜率");
		CH4Ratio = Double.parseDouble(file_cfg.readProperties("CH4Ratio"));
		debug("斜率为"+CH4Ratio);
		 
		/**
		 * 获取报警点
		 * */
		/**从文件中获取---甲烷报警点*/
		debug("从文件中获取---甲烷报警点");
		CH4AlarmPoint = Double.parseDouble(file_cfg.readProperties("CH4AlarmPoint"));
		debug("报警点"+CH4AlarmPoint);
		
		/**从文件中获取---一氧化碳报警点*/
		debug("从文件中获取---一氧化碳报警点");
		COAlarmPoint = Double.parseDouble(file_cfg.readProperties("COAlarmPoint"));
		debug("报警点"+COAlarmPoint);
		
		/**从文件中获取---二氧化氮报警点*/
		debug("从文件中获取---二氧化氮报警点");
		NO2AlarmPoint = Double.parseDouble(file_cfg.readProperties("NO2AlarmPoint"));
		debug("报警点"+NO2AlarmPoint);
		
		/**从文件中获取---氧气报警点*/
		debug("从文件中获取---氧气报警点");
		O2AlarmPoint = Double.parseDouble(file_cfg.readProperties("O2AlarmPoint"));
		debug("报警点"+O2AlarmPoint);
		
		/**从文件中获取---数据保存频次*/
		debug("文件中获取---数据保存频次");
		DataSaveFreqence = EnumDataSaveFreq.valueOf(file_cfg.readProperties("DataSaveFreqence"));
		
		debug("数据保存频次"+DataSaveFreqence);
		

	}
	
	
}
