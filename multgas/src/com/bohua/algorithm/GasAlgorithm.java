package com.bohua.algorithm;

import java.io.File;

import android.content.Context;
import android.os.MultGas;

import com.bohua.base.AD7790;
import com.bohua.base.Debug;
import com.bohua.base.FileProperties;
import com.bohua.base.GlobalData;
import com.qinxiaoyu.multgas.R;

public class GasAlgorithm {
	static FileProperties file_cfg = new FileProperties();
	protected static final int _ret = -1;
	
	
	static String tag = "GasAlgorithm";
	private static void debug(String string)	{if(Debug.GasAlgorithmDebug() == 1)Debug.debugi(tag,string);}
	
	
	/*获取各种气体的浓度值*/
	public static double getO2precent(Context context)
	{
		double O2_precent;
		int O2_zero_point=0;
		double O2_ratio = 0;
		
		if(detectFirmwareClassFileReady(context)==-1)
			return _ret;
		
		O2_zero_point = GlobalData.getO2ZeroPoint();
		O2_ratio = GlobalData.getO2Ratio();

		int ret=MultGas.readMultGasData(context.getResources().getString(R.string.ReadAd7790Path), 1);
		
		/*计算氧气百分百*/
		O2_precent = (1-(1/Math.pow(Math.E, ((ret - O2_zero_point)/(O2_ratio)))))*100;
//		debug("计算气体浓度值 = "+O2_precent);
		
//		/*氧气温度补偿*/
//		O2_precent = O2_precent/temperCompen.calculationCoefficient();
//		debug("main","温度补偿后的K值= "+O2_precent);

		
		return O2_precent;
	}
	public static double getCOppm(Context context)
	{
		int co_zero_point=0;
		double co_ratio = 0;
		double co_ppm;
		
		if(detectFirmwareClassFileReady(context)==-1)
			return _ret;
		
		co_zero_point = GlobalData.getCOZeroPoint();
		co_ratio = GlobalData.getCORatio();
		
		AD7790 Ad7790Handler= new AD7790();
		int ret=MultGas.readMultGasData(Ad7790Handler.GetReadAd7790Path(), 1);
		
		/*一氧化碳计算PPM*/
		co_ppm = (double)(ret - co_zero_point)*co_ratio;
		
		/*一氧化碳温度补偿算式*/
		
		return co_ppm;
	}
	public static double getNO2ppm(Context context)
	{
		 double no2_ppm;
		 int no2_zero_point=0;
		 double no2_ratio = 0;
		
		 if(detectFirmwareClassFileReady(context)==-1)
			 return _ret;
	 
		 no2_zero_point = GlobalData.getNO2ZeroPoint();
		 no2_ratio = GlobalData.getNO2Ratio();
		 
		 debug("no2_zero_point = "+no2_zero_point);
		 debug("no2_ratio = "+no2_ratio);
		 
		 int ret=MultGas.readMultGasData(context.getResources().getString(R.string.ReadAd7790Path), 1);
		 
		 debug("原始数据为："+ret);
		 
		 no2_ppm = (double)(ret - no2_zero_point)*no2_ratio;
		 
		 debug("no2_ppm = "+no2_ppm);
		
		 return no2_ppm;
	 }
	public static double getCH4precent(Context context)
	{
		 double ch4_precent = 0;
		 int CH4_zero_point=0;
		 double CH4_ratio = 0;
		
		 if(detectFirmwareClassFileReady(context)==-1)
			return _ret;
		 
		 CH4_zero_point = GlobalData.getCH4ZeroPoint();
		 CH4_ratio = GlobalData.getCH4Ratio();
			
		 int ret=MultGas.readMultGasData(context.getResources().getString(R.string.ReadAd7790Path), 1);
		 ch4_precent = (double)(CH4_zero_point-ret)/CH4_ratio;
		 return ch4_precent;
	}
	
	
	
	/**
	 * 调整灵敏度
	 * */	
	public static double setO2calibration(int o2_zeropoint , int O2_sensitivity,int temperature ,float gas_strength)
	{
		String zero_point_str;
		int o2_zero_point = 0;
		
		/**保存氧气灵敏度*/
		debug("保存氧气灵敏度");
//		file_cfg.writeProperties("O2ZeroPoint",o2_zeropoint+"");
		file_cfg.writeProperties("O2Sensitivity",O2_sensitivity+"");
		file_cfg.writeProperties("O2Temperature",temperature+"");
		
		/**从文件中获取---零点*/
		zero_point_str=file_cfg.readProperties("O2ZeroPoint");
		o2_zero_point=Integer.parseInt(zero_point_str);
		
		double o2_ratio = Math.log((double) (1/(1-gas_strength/100)));
		o2_ratio =  ((O2_sensitivity-o2_zero_point))/o2_ratio;
		debug("计算K值 = "+o2_ratio);
		
		/**保存比率*/
		file_cfg.writeProperties("O2Ratio", o2_ratio+"");
		/**应用参数到整个应用*/
		GlobalData.setO2Ratio(o2_ratio);
//		GlobalData.setO2ZeroPoint(o2_zeropoint);
		debug("ratio = "+o2_ratio);
	
		return o2_ratio;
	}
	public static double setCOcalibration(int co_zeropoint,int co_sensitivity,int temperature ,float gas_strength)
	{
		double ratio;
		String zero_point_str;
		int co_zero_point;
		/**保存一氧化碳(CO)灵敏度*/
		debug("保存一氧化碳(CO)灵敏度");
//		file_cfg.writeProperties("COZeroPoint",co_zeropoint+"");
		file_cfg.writeProperties("COSensitivity",co_sensitivity+"");
		file_cfg.writeProperties("COTemperature",temperature+"");
		/**从文件中获取---零点*/
		zero_point_str=file_cfg.readProperties("COZeroPoint");;
		co_zero_point=Integer.parseInt(zero_point_str);
		
		/**计算比率*/
		if(((co_sensitivity-co_zero_point)<=0))
			ratio=0;
		else
			ratio=(double)gas_strength/(co_sensitivity-co_zero_point);
		/**保存比率*/
		file_cfg.writeProperties("CORatio", ratio+"");
		/**应用参数到整个应用*/
//		setCOZeroPoint(co_zeropoint);
		GlobalData.setCORatio(ratio);
		debug("ratio = "+ratio);

		return ratio;
	}
	public static double setNO2calibration(int no2_zeropoint,int no2_sensitivity,int temperature ,float gas_strength)
	{
		double ratio;
		String zero_point_str;
		int no2_zero_point;
		
		/**保存二氧化氮(NO2)灵敏度*/
		debug("保存二氧化氮(NO2)灵敏度");
//		file_cfg.writeProperties("NO2ZeroPoint",no2_zeropoint+"");
		file_cfg.writeProperties("NO2Sensitivity",no2_sensitivity+"");
		file_cfg.writeProperties("NO2Temperature",temperature+"");
		
		
		/**从文件中获取---零点*/
		zero_point_str=file_cfg.readProperties("NO2ZeroPoint");;
		no2_zero_point=Integer.parseInt(zero_point_str);
		
		/**计算比率*/
		if(((no2_sensitivity-no2_zero_point)<=0))
			ratio=0;
		else
			ratio=(double)gas_strength/(no2_sensitivity-no2_zero_point);
		
		/**保存比率*/
		file_cfg.writeProperties("NO2Ratio", ratio+"");
		/**应用参数到整个应用*/
		GlobalData.setNO2Ratio(ratio); 
//		setNO2ZeroPoint(no2_zeropoint);
		debug("ratio = "+ratio);

		return ratio;
	}
	public static double setCH4calibration(int ch4_zeropoint,int ch4_sensitivity,int temperature ,float gas_strength)
	{
		double ratio;
		String zero_point_str;
		int ch4_zero_point;
		
		/**保存甲烷(CH4)灵敏度*/
		debug("保存甲烷(CH4)灵敏度");
//		file_cfg.writeProperties("CH4ZeroPoint",ch4_zeropoint+"");
		file_cfg.writeProperties("CH4Sensitivity",ch4_sensitivity+"");
		file_cfg.writeProperties("CH4Temperature",temperature+"");
		
		/**从文件中获取---零点*/
		zero_point_str=file_cfg.readProperties("CH4ZeroPoint");;
		ch4_zero_point=Integer.parseInt(zero_point_str);
		
		/**计算比率*/
		if(gas_strength == 0)
			ratio = 0;
		else
			ratio = (double)(ch4_zero_point - ch4_sensitivity)/gas_strength;
		
		/**保存比率*/
		file_cfg.writeProperties("CH4Ratio", ratio+"");
		/**应用参数到整个应用*/
		GlobalData.setCH4Ratio(ratio);
//		setCH4ZeroPoint(ch4_zeropoint);
		debug("ratio = "+ratio);
		
		return ratio;
	}
	/**
	 * 
	 * **/
	public static void setGasCalibration(String gasType , int gas_sensitivity ,float gas_strength,int temperature)
	{
		debug("setGasCalibration----"+gasType+" in put ----");
		
		if("O2".equals(gasType))
		{
			setO2calibration(0,gas_sensitivity,temperature,gas_strength);
			debug("调整氧气灵敏度");
		}
		else if("CH4".equals(gasType))
		{
			debug("调整甲烷灵敏度");
			setCH4calibration(0,gas_sensitivity,temperature,gas_strength);
		}
		else if("NO2".equals(gasType))
		{
			debug("调整二氧化氮灵敏度");
			setNO2calibration(0,gas_sensitivity,temperature,gas_strength);
		}
		else if("CO".equals(gasType))
		{
			debug("调整一氧化碳灵敏度");
			setCOcalibration(0,gas_sensitivity,temperature,gas_strength);
		}
	}
	
	
	public static int  setGasZeroPoint(Context context , String gasType ,int zeroPoint)
	{
		
		debug("setGasZeroPoint----"+gasType+" in put ----");
		
		if("O2".equals(gasType))
		{
			GlobalData.setO2ZeroPoint(zeroPoint);
			file_cfg.writeProperties("O2ZeroPoint", zeroPoint+"");
			debug("调整氧气零点");
		}
		else if("CH4".equals(gasType))
		{
			GlobalData.setCH4ZeroPoint(zeroPoint);
			file_cfg.writeProperties("CH4ZeroPoint", zeroPoint+"");
			debug("调整甲烷零点");
		}
		else if("NO2".equals(gasType))
		{
			GlobalData.setNO2ZeroPoint(zeroPoint);
			file_cfg.writeProperties("NO2ZeroPoint", zeroPoint+"");
			debug("调整二氧化氮零点");
		}
		else if("CO".equals(gasType))
		{
			GlobalData.setCOZeroPoint(zeroPoint);
			file_cfg.writeProperties("COZeroPoint", zeroPoint+"");
			debug("调整一氧化碳零点");
		}
		return 0;
	}
	
	public  float ConvertNumberToVoltage(int number)
	{
		int volteage_val=0;
		float c=0; 
		if(number<=0) return 0;
		volteage_val=(int) ((number-32768)*2.5);
		c=(float)volteage_val/32768;
		return c;
	} 
	
	public int getGasData(Context context)
	{ 
		if(detectFirmwareClassFileReady(context)==-1)
			return -1;
		
		AD7790 Ad7790Handler= new AD7790();
		int ret=MultGas.readMultGasData(Ad7790Handler.GetReadAd7790Path(), 1);
		
		return ret;
	}
	
	public static int detectFirmwareClassFileReady(Context context)
	{
		File classFile = new File(context.getResources().getString(R.string.ReadAd7790Path));
		if(classFile.exists())
		{
			return 0;
		}
		else
		{
//			Log.w("GasAlgorithm","未在firmwork层接口文件");
			return -1;
		}
	}
	
}
